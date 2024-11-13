const express = require("express");
const router = express.Router();

// Import order handlers
const acceptAdrApplication = require("../orderHandlers/acceptAdrApplication");
const acceptReschedulingRequest = require("../orderHandlers/acceptReschedulingRequest");
const adrCaseReferral = require("../orderHandlers/adrCaseReferral");
const caseSettlementAcceptance = require("../orderHandlers/caseSettlementAcceptance");
const caseSettlementRejection = require("../orderHandlers/caseSettlementRejection");
const caseTransfer = require("../orderHandlers/caseTransfer");
const mandatoryAsyncSubmissionsResponses = require("../orderHandlers/mandatoryAsyncSubmissionsResponses");
const newHearingDateAfterReschedule = require("../orderHandlers/newHearingDateAfterReschedule");
const orderGeneric = require("../orderHandlers/orderGeneric");
const rejectAdrApplication = require("../orderHandlers/rejectAdrApplication");
const rejectReschedulingRequest = require("../orderHandlers/rejectReschedulingRequest");
const rescheduleRequestJudge = require("../orderHandlers/rescheduleRequestJudge");
const scheduleHearingDate = require("../orderHandlers/scheduleHearingDate");
const summonsIssue = require("../orderHandlers/summonsIssue");

const asyncMiddleware = require("../utils/asyncMiddleware");
const { logger } = require("../logger");
const { clear } = require("winston");
const orderBailAcceptance = require("../orderHandlers/orderBailAcceptance");
const orderBailRejection = require("../orderHandlers/orderBailRejection");
const orderForAcceptReschedulingRequest = require("../orderHandlers/orderForAcceptReschedulingRequest");
const orderForRejectionReschedulingRequest = require("../orderHandlers/orderForRejectionReschedulingRequest");
const orderForMandatoryAsyncSubmissionsAndResponse = require("../orderHandlers/orderForMandatoryAsyncSubmissionsAndResponse");
const orderAcceptVoluntary = require("../orderHandlers/orderAcceptVoluntary");
const orderRejectVoluntary = require("../orderHandlers/orderRejectVoluntary");
const orderAcceptCheckout = require("../orderHandlers/orderAcceptCheckout");
const orderRejectCheckout = require("../orderHandlers/orderRejectCheckout");
const orderNotice = require("../orderHandlers/orderNotice");
const orderWarrant = require("../orderHandlers/orderWarrant");
const orderWithdrawalAccept = require("../orderHandlers/orderWithdrawalAccept");
const orderWithdrawalReject = require("../orderHandlers/orderWithdrawalReject");
const orderSection202Crpc = require("../orderHandlers/orderSection202crpc");
const orderAcceptExtension = require("../orderHandlers/orderAcceptExtension");
const orderRejectExtension = require("../orderHandlers/orderRejectExtension");

function renderError(res, errorMessage, errorCode, errorObject) {
  if (errorCode == undefined) errorCode = 500;
  logger.error(
    `${errorMessage}: ${errorObject ? errorObject.stack || errorObject : ""}`
  );
  res.status(errorCode).send({ errorMessage });
}

router.post(
  "",
  asyncMiddleware(async function (req, res, next) {
    const orderType = req.query.orderType;
    let qrCode = req.query.qrCode;

    // Set qrCode to false if it is undefined, null, or empty
    if (!qrCode) {
      qrCode = "false";
    } else {
      // Convert qrCode to lowercase
      qrCode = qrCode.toString().toLowerCase();
    }

    if (!orderType) {
      return renderError(
        res,
        "Order type is mandatory to generate the PDF",
        400
      );
    }

    try {
      switch (orderType.toLowerCase()) {
        case "new-hearing-date-after-rescheduling":
          await newHearingDateAfterReschedule(req, res, qrCode);
          break;
        case "schedule-hearing-date":
          await scheduleHearingDate(req, res, qrCode);
          break;
        case "accept-reschedule-request":
          await acceptReschedulingRequest(req, res, qrCode);
          break;
        case "mandatory-async-submissions-responses":
          await mandatoryAsyncSubmissionsResponses(req, res, qrCode);
          break;
        case "order-referral-case-adr":
          await adrCaseReferral(req, res, qrCode);
          break;
        case "order-case-settlement-rejected":
          await caseSettlementRejection(req, res, qrCode);
          break;
        case "order-for-extension-deadline":
          await orderAcceptExtension(req, res, qrCode);
          break;
        case "order-reject-application-submission-deadline":
          await orderRejectExtension(req, res, qrCode);
          break;
        case "order-case-settlement-acceptance":
          await caseSettlementAcceptance(req, res, qrCode);
          break;
        case "order-case-transfer":
          await caseTransfer(req, res, qrCode);
          break;
        case "summons-issue":
          await summonsIssue(req, res, qrCode);
          break;
        case "order-generic":
          await orderGeneric(req, res, qrCode);
          break;
        case "order-bail-acceptance":
          await orderBailAcceptance(req, res, qrCode);
          break;
        case "order-bail-rejection":
          await orderBailRejection(req, res, qrCode);
          break;
        case "order-for-rejection-rescheduling-request":
          await orderForRejectionReschedulingRequest(req, res, qrCode);
          break;
        case "order-accept-voluntary":
          await orderAcceptVoluntary(req, res, qrCode);
          break;
        case "order-reject-voluntary":
          await orderRejectVoluntary(req, res, qrCode);
          break;
        case "order-accept-checkout-request":
          await orderAcceptCheckout(req, res, qrCode);
          break;
        case "order-reject-checkout-request":
          await orderRejectCheckout(req, res, qrCode);
          break;
        case "order-notice":
          await orderNotice(req, res, qrCode);
          break;
        case "order-warrant":
          await orderWarrant(req, res, qrCode);
          break;
        case "order-case-withdrawal-acceptance":
          await orderWithdrawalAccept(req, res, qrCode);
          break;
        case "order-case-withdrawal-rejected":
          await orderWithdrawalReject(req, res, qrCode);
          break;
        case "order-202-crpc":
          await orderSection202Crpc(req, res, qrCode);
          break;
        default:
          await orderGeneric(req, res, qrCode);
          break;
      }
    } catch (error) {
      renderError(
        res,
        "An error occurred while processing the request",
        500,
        error
      );
    }
  })
);

module.exports = router;
