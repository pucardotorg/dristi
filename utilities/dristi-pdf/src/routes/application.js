const express = require("express");
const router = express.Router();

// Import application handlers
const applicationSubmissionExtension = require("../applicationHandlers/applicationSubmissionExtension");


const asyncMiddleware  = require("../utils/asyncMiddleware");
const { logger } = require("../logger");
const { clear } = require("winston");

function renderError(res, errorMessage, errorCode, errorObject) {
    if (errorCode == undefined) errorCode = 500;
    logger.error(`${errorMessage}: ${errorObject ? errorObject.stack || errorObject : ''}`);
    res.status(errorCode).send({ errorMessage });
}

router.post(
    "",
    asyncMiddleware(async function (req, res, next) {
        const applicationType = req.query.applicationType;
        let qrCode = req.query.qrCode;

        // Set qrCode to false if it is undefined, null, or empty
        if (!qrCode) {
            qrCode = 'false';
        }else {
            // Convert qrCode to lowercase
            qrCode = qrCode.toString().toLowerCase();
        }

        if (!applicationType) {
            return renderError(res, "Application type is mandatory to generate the PDF", 400);
        }

        try {
            switch (applicationType.toLowerCase()) {
                case 'application-submission-extension':
                    await applicationSubmissionExtension(req, res, qrCode);
                    break;
                default:
                    renderError(res, "Invalid application type", 400);
                    break;
            }
        } catch (error) {
            renderError(res, "An error occurred while processing the request", 500, error);
        }
    })
);

module.exports = router;