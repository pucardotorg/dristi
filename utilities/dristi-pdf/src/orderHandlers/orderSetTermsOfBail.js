const cheerio = require("cheerio");
const config = require("../config");
const {
  search_case,
  search_order,
  search_message,
  search_sunbirdrc_credential_service,
  search_application,
  create_pdf,
} = require("../api");
const { renderError } = require("../utils/renderError");
const { formatDate } = require("./formatDate");

async function orderSetTermsOfBail(req, res, qrCode) {
  const cnrNumber = req.query.cnrNumber;
  const orderId = req.query.orderId;
  const tenantId = req.query.tenantId;
  const entityId = req.query.entityId;
  const code = req.query.code;
  const requestInfo = req.body.RequestInfo;

  const missingFields = [];
  if (!cnrNumber) missingFields.push("cnrNumber");
  if (!orderId) missingFields.push("orderId");
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
    const resMessage = await handleApiCall(
      () =>
        search_message(tenantId, "rainmaker-submissions", "en_IN", requestInfo),
      "Failed to query Localized messages"
    );
    const messages = resMessage?.data?.messages;
    const messagesMap = messages.reduce((acc, curr) => {
      acc[curr.code] = curr.message;
      return acc;
    }, {});

    const resCase = await handleApiCall(
      () => search_case(cnrNumber, tenantId, requestInfo),
      "Failed to query case service"
    );
    const courtCase = resCase?.data?.criteria[0]?.responseList[0];
    if (!courtCase) {
      return renderError(res, "Court case not found", 404);
    }

    const mdmsCourtRoom = config.constants.mdmsCourtRoom;
    const judgeDetails = config.constants.judgeDetails;

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

    const applicationDocuments =
      application?.applicationDetails?.applicationDocuments;
    const documentList =
      applicationDocuments?.length > 0
        ? applicationDocuments.map((item) => ({
            ...item,
            documentType:
              messagesMap?.[item?.documentType] || item?.documentType,
          }))
        : [{ documentType: "" }];

    const partyName = application?.additionalDetails?.onBehalOfName || "";
    const complainantName =
      courtCase?.litigants?.find(
        (litigant) => litigant.partyType === "complainant.primary"
      )?.additionalDetails?.fullName || "";
    const applicationDate = formatDate(
      new Date(application?.createdDate),
      "DD-MM-YYYY"
    );

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

    const currentDate = new Date();

    const formattedToday = formatDate(currentDate, "DD-MM-YYYY");

    const caseNumber = courtCase?.courtCaseNumber || courtCase?.cmpNumber || "";
    const data = {
      Data: [
        {
          orderHeading: mdmsCourtRoom.orderHeading,
          cmpNumber: courtCase.cmpNumber,
          caseNumber: caseNumber,
          caseYear: caseYear,
          caseName: courtCase.caseTitle,
          date: formattedToday,
          applicantName: complainantName,
          partyName: partyName,
          dateOfApplication: applicationDate,
          briefSummaryOfBail:
            order?.additionalDetails?.formdata?.bailSummaryCircumstances
              ?.text || "",
          documentList: documentList,
          additionalConditionsOfBail:
            order?.additionalDetails?.formdata?.additionalComments?.text || "",
          judgeSignature: judgeDetails.judgeSignature,
          judgeName: judgeDetails.name,
          judgeDesignation: judgeDetails.judgeDesignation,
          qrCodeUrl: base64Url,
        },
      ],
    };
    const pdfKey =
      qrCode === "true"
        ? config.pdf.order_set_terms_of_bail_qr
        : config.pdf.order_set_terms_of_bail;
    const pdfResponse = await handleApiCall(
      () => create_pdf(tenantId, pdfKey, data, req.body),
      "Failed to generate PDF of Order for Set Terms of Bail"
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
      "Failed to generate PDF for Acceptance of Bail",
      500,
      ex
    );
  }
}

module.exports = orderSetTermsOfBail;
