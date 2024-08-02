const express = require("express");
const router = express.Router();
const cheerio = require('cheerio');
const config = require("../config");

const { search_case, search_order, search_mdms_order, search_hrms, search_sunbirdrc_credential_service, create_pdf } = require("../api");

const { asyncMiddleware } = require("../utils/asyncMiddleware");
const { logger } = require("../logger");

function renderError(res, errorMessage, errorCode, errorObject) {
    if (errorCode == undefined) errorCode = 500;
    logger.error(`${errorMessage}: ${errorObject ? errorObject.stack || errorObject : ''}`);
    res.status(errorCode).send({ errorMessage });
}

router.post(
    "",
    asyncMiddleware(async function (req, res, next) {
        const cnrNumber = req.query.cnrNumber;
        const orderId = req.query.orderId;
        const qrCode = req.query.qrCode;
        const taskId = req.query.taskId;
        const code = req.query.code;
        const tenantId = req.query.tenantId;
        const requestInfo = req.body;

        if (!cnrNumber) {
            return renderError(res, "cnrNumber is mandatory to generate the PDF", 400);
        }
        if (!orderId) {
            return renderError(res, "orderId is mandatory to generate the PDF", 400);
        }
        if (!tenantId) {
            return renderError(res, "tenantId is mandatory to generate the PDF", 400);
        }
        if(qrCode) {
            if (!taskId) {
                return renderError(res, "taskId is mandatory to generate the PDF", 400);
            }
            if (!code) {
                return renderError(res, "code is mandatory to generate the PDF", 400);
            }
        }
        if (requestInfo == undefined) {
            return renderError(res, "requestInfo cannot be null", 400);
        }

        try {
            var resCase;
            try {
                resCase = await search_case(cnrNumber, tenantId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the case", 500, ex);
            }
            var courtCase = resCase.data.criteria[0].responseList[0];

            var resHrms;
            try {
                resHrms = await search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of HRMS", 500, ex);
            }
            var employee = resHrms.data.Employees[0];

            var resMdms;
            try {
                resMdms = await search_mdms_order(courtCase.courtId, "common-masters.Court_Rooms", tenantId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the court room mdms", 500, ex);
            }
            var mdmsCourtRoom = resMdms.data.mdms[0].data;

            var resMdms1;
            try {
                resMdms1 = await search_mdms_order(mdmsCourtRoom.courtEstablishmentId, "case.CourtEstablishment", tenantId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the court establishment mdms", 500, ex);
            }
            var mdmsCourtEstablishment = resMdms1.data.mdms[0].data;
            
            var resOrder;
            try {
                resOrder = await search_order(tenantId, orderId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the order", 500, ex);
            }
            var order = resOrder.data.list[0];

            let base64Url = "";
            if (qrCode) {
                var resCredential;
                try {
                    resCredential = await search_sunbirdrc_credential_service(tenantId, code, taskId, requestInfo);
                } catch (ex) {
                    return renderError(res, "Failed to query details of the sunbirdrc credential service", 500, ex);
                }
                // Load the response HTML into cheerio
                const $ = cheerio.load(resCredential.data);
    
                // Extract the base64 URL from the img tag
                base64Url = $('img').attr('src');
            }

            let year;
            if (typeof courtCase.filingDate === 'string') {
                year = courtCase.filingDate.slice(-4);
            } else if (courtCase.filingDate instanceof Date) {
                year = courtCase.filingDate.getFullYear();
            }else if (typeof courtCase.filingDate === 'number') {
             // Assuming the number is in milliseconds (epoch time)
                year = new Date(courtCase.filingDate).getFullYear();
            }  else {
                return renderError(res, "Invalid filingDate format", 500);
            }

            const data = {
                "Data": [
                    {
                        "courtName": mdmsCourtRoom.name,
                        "place": mdmsCourtEstablishment.boundaryName,
                        "state": mdmsCourtEstablishment.rootBoundaryName,
                        "caseNumber": courtCase.cnrNumber,
                        "year": year,
                        "caseName": courtCase.caseTitle,
                        "date": order.createdDate,
                        "adrMode": "Mode of ADR from UI",
                        "additionalComments": order.comments,
                        "judgeSignature": "Judge's Signature",
                        "judgeName": employee.user.name,
                        "courtSeal": "Court Seal",
                        "qrCodeUrl": base64Url
                    }
                ]
            };

            var pdfResponse;
            const pdfKey = qrCode === 'true' ? config.pdf.adr_case_referral_qr : config.pdf.adr_case_referral;
            try {
                pdfResponse = await create_pdf(
                    tenantId,
                    pdfKey,
                    data,
                    requestInfo
                );
            } catch (ex) {
                return renderError(res, "Failed to generate PDF", 500, ex);
            }
            const filename = `${pdfKey}_${new Date().getTime()}`;
            res.writeHead(200, {
                "Content-Type": "application/pdf",
                "Content-Disposition": `attachment; filename=${filename}.pdf`,
            });
            pdfResponse.data.pipe(res);

        } catch (ex) {
            return renderError(res, "Failed to query details for issue of summons", 500, ex);
        }
    })
);

module.exports = router;