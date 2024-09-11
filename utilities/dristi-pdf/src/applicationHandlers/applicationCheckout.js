const cheerio = require("cheerio");
const config = require("../config");
const {
  search_case,
  search_mdms,
  search_hrms,
  search_sunbirdrc_credential_service,
  search_application,
  create_pdf,
} = require("../api");
const { renderError } = require("../utils/renderError");
const { getAdvocates } = require("./getAdvocates");

function getOrdinalSuffix(day) {
  if (day > 3 && day < 21) return "th"; // 11th, 12th, 13th, etc.
  switch (day % 10) {
    case 1:
      return "st"; // 1st, 21st, 31st
    case 2:
      return "nd"; // 2nd, 22nd
    case 3:
      return "rd"; // 3rd, 23rd
    default:
      return "th"; // 4th, 5th, 6th, etc.
  }
}

async function applicationCheckout(req, res, qrCode) {
  const cnrNumber = req.query.cnrNumber;
  const applicationNumber = req.query.applicationNumber;
  const tenantId = req.query.tenantId;
  const entityId = req.query.entityId;
  const code = req.query.code;
  const requestInfo = req.body.RequestInfo;

  const missingFields = [];
  if (!cnrNumber) missingFields.push("cnrNumber");
  if (!applicationNumber) missingFields.push("applicationNumber");
  if (!tenantId) missingFields.push("tenantId");
  if (requestInfo === undefined) missingFields.push("requestInfo");
  if (qrCode === "true" && (!entityId || !code))
    missingFields.push("entityId and code");

  if (missingFields.length > 0) {
    return renderError(
      res,
      `${missingFields.join(", ")} are mandatory to generate the PDF`,
      400
    );
  }

  // Function to handle API calls
  const handleApiCall = async (apiCall, errorMessage) => {
    try {
      return await apiCall();
    } catch (ex) {
      renderError(res, `${errorMessage}`, 500, ex);
      throw ex; // Ensure the function stops on error
    }
  };

  try {
    // Search for case details
    const resCase = await handleApiCall(
      () => search_case(cnrNumber, tenantId, requestInfo),
      "Failed to query case service"
    );
    const courtCase = resCase?.data?.criteria[0]?.responseList[0];
    if (!courtCase) {
      return renderError(res, "Court case not found", 404);
    }
    const allAdvocates = getAdvocates(courtCase);
    // Search for HRMS details
    // const resHrms = await handleApiCall(
    //   () => search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo),
    //   "Failed to query HRMS service"
    // );
    // const employee = resHrms?.data?.Employees[0];
    // if (!employee) {
    //   renderError(res, "Employee not found", 404);
    // }

    // Search for MDMS court room details
    const resMdms = await handleApiCall(
      () =>
        search_mdms(
          courtCase.courtId,
          "common-masters.Court_Rooms",
          tenantId,
          requestInfo
        ),
      "Failed to query MDMS service for court room"
    );
    const mdmsCourtRoom = resMdms?.data?.mdms[0]?.data;
    if (!mdmsCourtRoom) {
      return renderError(res, "Court room MDMS master not found", 404);
    }

    // Search for MDMS designation details
    // const resMdms1 = await handleApiCall(
    //   () =>
    //     search_mdms(
    //       employee.assignments[0].designation,
    //       "common-masters.Designation",
    //       tenantId,
    //       requestInfo
    //     ),
    //   "Failed to query MDMS service for court room"
    // );
    // const mdmsDesignation = resMdms1?.data?.mdms[0]?.data;
    // if (!mdmsDesignation) {
    //   renderError(res, "Court room MDMS master not found", 404);
    // }

    // Search for application details
    const resApplication = await handleApiCall(
      () => search_application(tenantId, applicationNumber, requestInfo),
      "Failed to query application service"
    );
    const application = resApplication?.data?.applicationList[0];
    if (!application) {
      return renderError(res, "Application not found", 404);
    }
    console.debug(application);
    const onBehalfOfuuid = application?.onBehalfOf?.[0];
    const advocate = allAdvocates[onBehalfOfuuid]?.[0]?.additionalDetails
      ?.advocateName
      ? allAdvocates[onBehalfOfuuid]?.[0]
      : {};
    const advocateName = advocate?.additionalDetails?.advocateName || "";
    const partyName = application?.additionalDetails?.onBehalOfName || "";
    const onBehalfOfLitigent = courtCase?.litigants?.find(
      (item) => item.additionalDetails.uuid === onBehalfOfuuid
    );
    const sourceType = onBehalfOfLitigent?.partyType
      ?.toLowerCase()
      ?.includes("complainant")
      ? "COMPLAINANT"
      : !isCitizen
      ? "COURT"
      : "ACCUSED";
    let base64Url = "";
    if (qrCode === "true") {
      const resCredential = await handleApiCall(
        () =>
          search_sunbirdrc_credential_service(
            tenantId,
            code,
            entityId,
            requestInfo
          ),
        "Failed to query sunbirdrc credential service"
      );
      const $ = cheerio.load(resCredential.data);
      const imgTag = $("img");
      if (imgTag.length === 0) {
        return renderError(
          res,
          "No img tag found in the sunbirdrc response",
          500
        );
      }
      base64Url = imgTag.attr("src");
    }

    let caseYear;
    if (typeof courtCase.filingDate === "string") {
      caseYear = courtCase.filingDate.slice(-4);
    } else if (courtCase.filingDate instanceof Date) {
      caseYear = courtCase.filingDate.getFullYear();
    } else if (typeof courtCase.filingDate === "number") {
      // Assuming the number is in milliseconds (epoch time)
      caseYear = new Date(courtCase.filingDate).getFullYear();
    } else {
      return renderError(res, "Invalid filingDate format", 500);
    }

    const months = [
      "January",
      "February",
      "March",
      "April",
      "May",
      "June",
      "July",
      "August",
      "September",
      "October",
      "November",
      "December",
    ];

    const currentDate = new Date();

    const day = currentDate.getDate();
    const month = months[currentDate.getMonth()];
    const year = currentDate.getFullYear();

    const ordinalSuffix = getOrdinalSuffix(day);

    const data = {
      Data: [
        {
          courtComplex: mdmsCourtRoom.name,
          caseType: "Negotiable Instruments Act 138 A",
          caseNumber: courtCase.cnrNumber,
          caseYear: caseYear,
          caseName: courtCase.caseTitle,
          judgeName: "John Doe", // FIXME: employee.user.name
          courtDesignation: "HIGHT COURRT", //FIXME: mdmsDesignation.name,
          addressOfTheCourt: "Kerala", //FIXME: mdmsCourtRoom.address,
          date: currentDate,
          partyName: partyName,
          partyType: sourceType,
          reasonForReschedule:
            application?.additionalDetails?.formdata?.reschedulingReason
              ?.code || "",
          reasonForApplication: "Reason", //FIXME : Currently this field is document inside the application, it should be a textbox
          complainantName: partyName, //FIXME: REMOVE it from both pdf configs and here,
          additionalComments:
            application?.additionalDetails?.formdata?.comments | " ",
          initialHearingDate: "11-11-1111",
          proposedHearingDate: "16-11-1111",
          prayerOptional: "",
          advocateSignature: "Advocate_Signature", //FIXME: It should also come from the application
          advocateName: advocateName,
          nameOfDocument: "Aadhar card", //FIXME: It should come from the application, currently there is not field present inside of it
          barRegistrationNumber: "sdf",
          day: day + ordinalSuffix,
          month: month,
          year: year,
          qrCodeUrl: base64Url,
        },
      ],
    };

    // Generate the PDF
    const pdfKey =
      qrCode === "true"
        ? config.pdf.application_checkout_qr
        : config.pdf.application_checkout;
    const pdfResponse = await handleApiCall(
      () => create_pdf(tenantId, pdfKey, data, req.body),
      "Failed to generate PDF of Application for checkout"
    );
    const filename = `${pdfKey}_${new Date().getTime()}`;
    res.writeHead(200, {
      "Content-Type": "application/pdf",
      "Content-Disposition": `attachment; filename=${filename}.pdf`,
    });
    pdfResponse.data
      .pipe(res)
      .on("finish", () => {
        res.end();
      })
      .on("error", (err) => {
        return renderError(res, "Failed to send PDF response", 500, err);
      });
  } catch (ex) {
    return renderError(
      res,
      "Failed to query details of APPLICATION FOR CHECKOUT",
      500,
      ex
    );
  }
}

module.exports = applicationCheckout;
