const cheerio = require("cheerio");
const config = require("../config");
const {
  search_case,
  search_order,
  search_mdms,
  search_hrms,
  search_sunbirdrc_credential_service,
  create_pdf,
  search_individual_uuid,
  search_application,
  search_message,
} = require("../api");
const { renderError } = require("../utils/renderError");
const { formatDate } = require("./formatDate");

async function orderWithdrawalAccept(req, res, qrCode) {
  const cnrNumber = req.query.cnrNumber;
  const orderId = req.query.orderId;
  const entityId = req.query.entityId;
  const code = req.query.code;
  const tenantId = req.query.tenantId;
  const requestInfo = req.body.RequestInfo;

  const missingFields = [];
  if (!cnrNumber) missingFields.push("cnrNumber");
  if (!orderId) missingFields.push("orderId");
  if (!tenantId) missingFields.push("tenantId");
  if (qrCode === "true" && (!entityId || !code))
    missingFields.push("entityId and code");
  if (requestInfo === undefined) missingFields.push("requestInfo");

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
    const resMessage = await handleApiCall(
      () =>
        search_message(tenantId, "rainmaker-submissions", "en_IN", requestInfo),
      "Failed to query Localized messages"
    );
    const messages = resMessage?.data?.messages || [];
    const messagesMap =
      messages?.length > 0
        ? Object.fromEntries(
            messages.map(({ code, message }) => [code, message])
          )
        : {};

    // Search for case details
    const resCase = await handleApiCall(
      () => search_case(cnrNumber, tenantId, requestInfo),
      "Failed to query case service"
    );
    const courtCase = resCase?.data?.criteria[0]?.responseList[0];
    if (!courtCase) {
      renderError(res, "Court case not found", 404);
    }

    // Search for HRMS details
    const resHrms = await handleApiCall(
      () => search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo),
      "Failed to query HRMS service"
    );
    const employee = resHrms?.data?.Employees[0];
    // if (!employee) {
    //     renderError(res, "Employee not found", 404);
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
      renderError(res, "Court room MDMS master not found", 404);
    }

    // Search for order details
    const resOrder = await handleApiCall(
      () => search_order(tenantId, orderId, requestInfo),
      "Failed to query order service"
    );
    const order = resOrder?.data?.list[0];
    if (!order) {
      renderError(res, "Order not found", 404);
    }

    const resApplication = await handleApiCall(
      () =>
        search_application(
          tenantId,
          order?.additionalDetails?.formdata?.refApplicationId,
          requestInfo
        ),
      "Failed to query application service"
    );
    const application = resApplication?.data?.applicationList[0];
    if (!application) {
      return renderError(res, "Application not found", 404);
    }

    const behalfOfIndividual = await handleApiCall(
      () =>
        search_individual_uuid(
          tenantId,
          application.onBehalfOf[0],
          requestInfo
        ),
      "Failed to query individual service using id"
    );
    const onbehalfOfIndividual = behalfOfIndividual?.data?.Individual[0];
    if (!onbehalfOfIndividual) {
      renderError(res, "Individual not found", 404);
    }
    // Search for individual details

    // Handle QR code if enabled
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

    const onBehalfOfuuid = application?.onBehalfOf?.[0];
    const onBehalfOfLitigent = courtCase?.litigants?.find(
      (item) => item.additionalDetails.uuid === onBehalfOfuuid
    );
    let partyType = "COURT";
    if (onBehalfOfLitigent?.partyType?.toLowerCase()?.includes("complainant")) {
      partyType = "COMPLAINANT";
    }
    if (onBehalfOfLitigent?.partyType?.toLowerCase()?.includes("respondent")) {
      partyType = "ACCUSED";
    }

    const partyName = [
      onbehalfOfIndividual.name.givenName,
      onbehalfOfIndividual.name.otherNames,
      onbehalfOfIndividual.name.familyName,
    ]
      .filter(Boolean)
      .join(" ");
    const currentDate = new Date();
    const formattedToday = formatDate(currentDate, "DD-MM-YYYY");

    const additionalComments = order?.comments || "";
    const localreasonForWithdrawal =
      application?.applicationDetails?.reasonForWithdrawal || "";
    const summaryReasonForWithdrawal =
      messagesMap?.[localreasonForWithdrawal] || localreasonForWithdrawal;
    const data = {
      Data: [
        {
          courtName: mdmsCourtRoom.name,
          place: "Kollam",
          state: "Kerala",
          caseName: courtCase.caseTitle,
          caseNumber: courtCase.caseNumber,
          caseYear: caseYear,
          partyName: partyName,
          partyType: partyType,
          date: formattedToday,
          dateOfMotion: formattedToday,
          additionalComments: additionalComments,
          summaryReasonForWithdrawal: summaryReasonForWithdrawal,
          judgeSignature: "Judge Signature",
          judgeName: "John Doe",
          courtSeal: "Court Seal",
          qrCodeUrl: base64Url,
        },
      ],
    };

    // Generate the PDF
    const pdfKey =
      qrCode === "true"
        ? config.pdf.order_case_withdrawal_acceptance_qr
        : config.pdf.order_case_withdrawal_acceptance;
    const pdfResponse = await handleApiCall(
      () => create_pdf(tenantId, pdfKey, data, req.body),
      "Failed to generate PDF of order to Settle a Case - Acceptance"
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
      "Failed to query details of order to Settle a Case - Acceptance",
      500,
      ex
    );
  }
}

module.exports = orderWithdrawalAccept;
