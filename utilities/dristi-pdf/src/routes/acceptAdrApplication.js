const express = require("express");
const router = express.Router();
const url = require("url");
const config = require("../config");

const {  search_case, search_order, search_hearing, search_mdms_order, search_hrms, search_individual_uuid, create_pdf } = require("../api");

const { asyncMiddleware } = require("../utils/asyncMiddleware");
const { pdf } = require("../config");
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
    "/accept-adr-application",
    asyncMiddleware(async function (req, res, next) {
        const cnrNumber = req.query.cnrNumber;
        const orderId = req.query.orderId;
        const tenantId = req.query.tenantId;
        const requestInfo = req.body;
        const orderDate = req.query.date

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
                resOrder = await search_order(tenantId,  orderId, requestInfo);
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

            // const hearingDate = formatDate(new Date(hearing.startTime));


            // Filter litigants to find the respondent.primary
         

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
                        "specifyModeOfADR": "mediation",
                        "additionalComments": order.comments,
                        "judgeSignature": "Judge Alice Johnson",
                        "judgeName": employee.user.name,
                        "courtSeal": "Seal of Superior Court"
                    }
             
                ]
            };

            var pdfResponse;
            const pdfKey = config.pdf.accept_adr_application;
            try {
                pdfResponse = await create_pdf(
                    tenantId,
                    pdfKey,
                    data,
                    requestInfo
                );
            } catch (ex) {
                return renderError(res, "Failed to generate PDF for accept adr", 500, ex);
            }
            const filename = `${pdfKey}_${new Date().getTime()}`;
            res.writeHead(200, {
                "Content-Type": "application/pdf",
                "Content-Disposition": `attachment; filename=${filename}.pdf`,
            });
            pdfResponse.data.pipe(res);

        } catch (ex) {
            return renderError(res, "Failed to query details of accept adr", 500, ex);
        }
    })
);

module.exports = router;
