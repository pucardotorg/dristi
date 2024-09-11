const express = require("express");
const router = express.Router();

// Import application handlers
const applicationSubmissionExtension = require("../applicationHandlers/applicationSubmissionExtension");

const asyncMiddleware = require("../utils/asyncMiddleware");
const { logger } = require("../logger");
const { clear } = require("winston");
const applicationGeneric = require("../applicationHandlers/applicationGeneric");
const applicationProductionOfDocuments = require("../applicationHandlers/applicationProductionOfDocuments");
const applicationBailBond = require("../applicationHandlers/applicationBailBond");
const applicationCaseTransfer = require("../applicationHandlers/applicationCaseTransfer");
const applicationCaseWithdrawal = require("../applicationHandlers/applicationCaseWithdrawal");
const applicationRescheduleRequest = require("../applicationHandlers/applicationRescheduleRequest");
const applicationCheckout = require("../applicationHandlers/applicationCheckout");

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
    const applicationType = req.query.applicationType;
    let qrCode = req.query.qrCode;

    // Set qrCode to false if it is undefined, null, or empty
    if (!qrCode) {
      qrCode = "false";
    } else {
      // Convert qrCode to lowercase
      qrCode = qrCode.toString().toLowerCase();
    }

    if (!applicationType) {
      return renderError(
        res,
        "Application type is mandatory to generate the PDF",
        400
      );
    }

    try {
      switch (applicationType.toLowerCase()) {
        // case "application-submission-extension":
        //   await applicationSubmissionExtension(req, res, qrCode);
        //   break;
        case "application-generic":
          await applicationGeneric(req, res, qrCode);
          break;
        case "application-production-of-documents":
          await applicationProductionOfDocuments(req, res, qrCode);
          break;
        case "application-reschedule-request":
          await applicationRescheduleRequest(req, res, qrCode);
          break;
        // case "application-bail-bond":
        //   await applicationBailBond(req, res, qrCode);
        //   break;
        // case "application-case-transfer":
        //   await applicationCaseTransfer(req, res, qrCode);
        //   break;
        // case "application-case-withdrawal":
        //   await applicationCaseWithdrawal(req, res, qrCode);
        //   break;
        // case "application-for-checkout-request":
        //   await applicationCheckout(req, res, qrCode);
        //   break;
        default:
          await applicationGeneric(req, res, qrCode);
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
