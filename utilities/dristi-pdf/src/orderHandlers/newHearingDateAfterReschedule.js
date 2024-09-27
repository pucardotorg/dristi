const cheerio = require("cheerio");
const config = require("../config");
const {
  search_case,
  search_order,
  search_mdms,
  search_hrms,
  search_hearing,
  search_sunbirdrc_credential_service,
  create_pdf,
} = require("../api");
const { renderError } = require("../utils/renderError");

function formatDate(epochMillis) {
  // Convert epoch milliseconds to a Date object
  const date = new Date(epochMillis);

  // Ensure that the date is a valid Date object
  if (Number.isNaN(date.getTime())) {
    throw new Error("Invalid date");
  }

  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
}

async function newHearingDateAfterReschedule(req, res, qrCode) {
  const cnrNumber = req.query.cnrNumber;
  const orderId = req.query.orderId;
  const tenantId = req.query.tenantId;
  const requestInfo = req.body.RequestInfo;
  const entityId = req.query.entityId;
  const code = req.query.code;

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

  try {
    // Search for case details
    const resCase = await handleApiCall(
      () => search_case(cnrNumber, tenantId, requestInfo),
      "Failed to query case service"
    );
    const courtCase = resCase?.data?.criteria[0]?.responseList[0];
    if (!courtCase) {
      renderError(res, "Court case not found", 404);
    }

    const resHearing = await handleApiCall(
      () => search_hearing(tenantId, cnrNumber, requestInfo),
      "Failed to query hearing service"
    );
    const hearing = resHearing?.data?.HearingList?.find(
      (item) => item.status == "OPTOUT"
    );
    const originalHearingDate = formatDate(hearing?.startTime);

    // FIXME: Commenting out HRMS calls is it not impl in solution
    // Search for HRMS details
    // const resHrms = await handleApiCall(
    //     () => search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo),
    //     "Failed to query HRMS service"
    // );
    // const employee = resHrms?.data?.Employees[0];
    // if (!employee) {
    //     renderError(res, "Employee not found", 404);
    // }

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
    //   renderError(res, "Court room MDMS master not found", 404);
    // }
    const mdmsCourtRoom = config?.constants.mdmsCourtRoom;
    const judgeDetails = config.constants.judgeDetails;

    // Search for order details
    const resOrder = await handleApiCall(
      () => search_order(tenantId, orderId, requestInfo),
      "Failed to query order service"
    );
    const order = resOrder?.data?.list[0];
    if (!order) {
      renderError(res, "Order not found", 404);
    }

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

    const formattedToday = formatDate(Date.now());
    const newHearingDate = order?.orderDetails?.newHearingDate
      ? formatDate(order?.orderDetails?.newHearingDate)
      : "";
    const caseNumber = courtCase?.courtCaseNumber || courtCase?.cmpNumber || "";
    const data = {
      Data: [
        {
          courtName: mdmsCourtRoom.name,
          place: mdmsCourtRoom.place,
          state: mdmsCourtRoom.state,
          caseName: courtCase.caseTitle,
          caseNumber: caseNumber,
          originalHearingDate,
          date: formattedToday,
          newHearingDate,
          additionalComments: order.comments,
          judgeSignature: judgeDetails.name,
          judgeName: judgeDetails.name,
          courtSeal: judgeDetails.courtSeal,
          qrCodeUrl: base64Url,
        },
      ],
    };

    // Generate the PDF
    const pdfKey =
      qrCode === "true"
        ? config.pdf.new_hearing_date_after_reschedule_qr
        : config.pdf.new_hearing_date_after_reschedule;
    const pdfResponse = await handleApiCall(
      () => create_pdf(tenantId, pdfKey, data, req.body),
      "Failed to generate PDF of order to Assign New Hearing Date after Rescheduling"
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
      "Failed to query details of order to Assign New Hearing Date after Rescheduling",
      500,
      ex
    );
  }
}

module.exports = newHearingDateAfterReschedule;
