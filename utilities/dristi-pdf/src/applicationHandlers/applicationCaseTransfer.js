const cheerio = require("cheerio");
const config = require("../config");
const {
  search_case,
  search_mdms,
  search_hrms,
  search_sunbirdrc_credential_service,
  search_application,
  create_pdf,
  search_advocate,
} = require("../api");
const { renderError } = require("../utils/renderError");
const { getAdvocates } = require("./getAdvocates");
const { formatDate } = require("./formatDate");

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

const applicationCaseTransfer = async (req, res, qrCode) => {
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
  // Search for case details
  try {
    const resCase = await handleApiCall(
      () => search_case(cnrNumber, tenantId, requestInfo),
      "Failed to query case service"
    );
    const courtCase = resCase?.data?.criteria[0]?.responseList[0];
    if (!courtCase) {
      return renderError(res, "Court case not found", 404);
    }

    // Search for MDMS court room details
    // const resMdms = await handleApiCall(
    //   () =>
    //     search_mdms(
    //       courtCase.courtId,
    //       "common-masters.Court_Rooms",
    //       tenantId,
    //       requestInfo
    //     ),
    //   "Failed to query MDMS service for court room"
    // );
    // const mdmsCourtRoom = resMdms?.data?.mdms[0]?.data;
    // if (!mdmsCourtRoom) {
    //   return renderError(res, "Court room MDMS master not found", 404);
    // }

    const mdmsCourtRoom = config.constants.mdmsCourtRoom;
    const judgeDetails = config.constants.judgeDetails;

    // Search for application details
    const resApplication = await handleApiCall(
      () => search_application(tenantId, applicationNumber, requestInfo),
      "Failed to query application service"
    );
    const application = resApplication?.data?.applicationList[0];
    if (!application) {
      return renderError(res, "Application not found", 404);
    }
    let barRegistrationNumber = "";
    const advocateIndividualId =
      application?.applicationDetails?.advocateIndividualId;
    if (advocateIndividualId) {
      const resAdvocate = await handleApiCall(
        () => search_advocate(tenantId, advocateIndividualId, requestInfo),
        "Failed to query Advocate Details"
      );
      const advocateData = resAdvocate?.data?.advocates?.[0];
      const advocateDetails = advocateData?.responseList?.find(
        (item) => item.isActive === true
      );
      barRegistrationNumber = advocateDetails?.barRegistrationNumber || "";
    }

    const onBehalfOfuuid = application?.onBehalfOf?.[0];
    const allAdvocates = getAdvocates(courtCase);
    const advocate = allAdvocates[onBehalfOfuuid]?.[0]?.additionalDetails
      ?.advocateName
      ? allAdvocates[onBehalfOfuuid]?.[0]
      : {};
    const advocateName = advocate?.additionalDetails?.advocateName || "";
    const partyName = application?.additionalDetails?.onBehalOfName || "";
    const additionalComments =
      application?.applicationDetails?.additionalComments || "";
    const grounds =
      application?.applicationDetails?.groundsForSeekingTransfer || "";
    const selectRequestedCourt =
      application?.applicationDetails?.selectRequestedCourt || "";
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
    const formattedToday = formatDate(currentDate, "DD-MM-YYYY");
    const day = currentDate.getDate();
    const month = months[currentDate.getMonth()];
    const year = currentDate.getFullYear();

    const ordinalSuffix = getOrdinalSuffix(day);

    const data = {
      Data: [
        {
          courtComplex: mdmsCourtRoom.name,
          caseType: "Negotiable Instruments Act 138 A",
          caseNumber: courtCase.caseNumber,
          caseYear: caseYear,
          caseName: courtCase.caseTitle,
          caseNo: courtCase.caseNumber,
          originalCourt: "Supreme Court of India", // FIXME:Take current court
          newCourt: selectRequestedCourt,
          judgeName: judgeDetails.name, // FIXME: employee.user.name
          courtDesignation: judgeDetails.designation, //FIXME: mdmsDesignation.name,
          addressOfTheCourt: mdmsCourtRoom.state, //FIXME: mdmsCourtRoom.address,
          date: formattedToday,
          partyName: partyName,
          additionalComments,
          grounds,
          reliefSought: "",
          day: day + ordinalSuffix,
          month: month,
          year: year,
          advocateSignature: "Advocate Signature",
          advocateName,
          barRegistrationNumber,
          qrCodeUrl: base64Url,
        },
      ],
    };
    const pdfKey =
      qrCode === "true"
        ? config.pdf.application_case_transfer_qr
        : config.pdf.application_case_transfer;
    const pdfResponse = await handleApiCall(
      () => create_pdf(tenantId, pdfKey, data, req.body),
      "Failed to generate PDF of Application for Case Transfer"
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
      "Failed to query details of APPLICATION FOR EXTENSION OF SUBMISSION DEADLINE",
      500,
      ex
    );
  }
};

module.exports = applicationCaseTransfer;
