const express = require("express");
const router = express.Router();
const cheerio = require('cheerio');
const config = require("../config");

const {  search_case, search_order, search_mdms_order, search_hrms, search_individual_uuid, search_sunbirdrc_credential_service, search_application, create_pdf } = require("../api");

const { asyncMiddleware } = require("../utils/asyncMiddleware");
const { logger } = require("../logger");


function renderError(res, errorMessage, errorCode, errorObject) {
    if (errorCode == undefined) errorCode = 500;
    logger.error(`${errorMessage}: ${errorObject ? errorObject.stack || errorObject : ''}`);
    res.status(errorCode).send({ errorMessage });
}

function formatDate(epochMillis) {
    // Convert epoch milliseconds to a Date object
    const date = new Date(epochMillis);

    // Ensure that the date is a valid Date object
    if (isNaN(date)) {
        throw new Error("Invalid date");
    }

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const year = date.getFullYear();

    return `${day}-${month}-${year}`;
}

router.post(
    "",
    asyncMiddleware(async function (req, res, next) {
        const cnrNumber = req.query.cnrNumber;
        const orderId = req.query.orderId;
        const tenantId = req.query.tenantId;
        const requestInfo = req.body;
        const orderDate = req.query.date;
        const qrCode = req.query.qrCode;
        const taskId = req.query.taskId;
        const code = req.query.code;

        if (!cnrNumber) {
            return renderError(res, "cnrNumber is mandatory to generate the receipt", 400);
        }
        if (!orderId) {
            return renderError(res, "orderId is mandatory to generate the receipt", 400);
        }
        if (!tenantId) {
            return renderError(res, "tenantId is mandatory to generate the receipt", 400);
        }
        if (requestInfo == undefined) {
            return renderError(res, "requestInfo cannot be null", 400);
        }
        if (!orderDate) {
            return renderError(res, "date is mandatory to generate the receipt", 400);
        }
        if (qrCode && (!taskId || !code)) {
            return renderError(res, "taskId and code are mandatory when qrCode is enabled", 400);
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

            var resApplication;
            try {
                resApplication = await search_application(tenantId, order.applicationNumber[0] , requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the application", 500, ex);
            }
            var application = resApplication.data.applicationList[0];
            if (!application) {
                return renderError(res, "application not found", 404);
            }

            var resIndividual;
            try {
                resIndividual = await search_individual_uuid(tenantId, application.onBehalfOf[0], requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the individual", 500, ex);
            }
            var individual = resIndividual.data.Individual[0];
            if (!individual) {
                return renderError(res, "individual not found", 404);
            }

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

            var stringDate 
            try {
                stringDate = formatDate(order.createdDate);
            } catch (error) {
                console.error("cannot convert epoch time to date");
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
                        "date": stringDate,
                        "applicationId": order.applicationNumber[0],
                        "partyName": `${individual.name.givenName} ${individual.name.familyName}`,
                        "advocateName": "Jane Doe",
                        "provideBriefReasoning": "the complexity of the case and the potential for significant legal disputes",
                        "reasonForRejection": "The application for ADR is rejected as the Court believes that the case involves complex issues that require a formal litigation process.",
                        "additionalComments": order.comments,
                        "judgeSignature": "John Doe",
                        "judgeName":employee.user.name,
                        "courtSeal": "Seal of the Superior Court",
                        "qrCodeUrl": base64Url
                    }
             
                ]
            };

            var pdfResponse;
            const pdfKey = qrCode === 'true' ? config.pdf.reject_adr_application_qr : config.pdf.reject_adr_application;
            try {
                pdfResponse = await create_pdf(
                    tenantId,
                    pdfKey,
                    data,
                    requestInfo
                );
            } catch (ex) {
                return renderError(res, "Failed to generate PDF for reject adr application", 500, ex);
            }
            const filename = `${pdfKey}_${new Date().getTime()}`;
            res.writeHead(200, {
                "Content-Type": "application/pdf",
                "Content-Disposition": `attachment; filename=${filename}.pdf`,
            });
            pdfResponse.data.pipe(res);

        } catch (ex) {
            return renderError(res, "Failed to query details of reject adr", 500, ex);
        }
    })
);

module.exports = router;