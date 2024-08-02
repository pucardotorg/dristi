const express = require("express");
const router = express.Router();
const url = require("url");
const config = require("../config");

const {  search_case, search_order, search_hearing, search_mdms_order, search_hrms, search_individual, create_pdf } = require("../api");

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
    "/new-date-after-reschedule",
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
            if (!courtCase) {
                return renderError(res, "case not found", 404);
            }

            var resHrms;
            try {
                resHrms = await search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of HRMS", 500, ex);
            }
            var employee = resHrms.data.Employees[0];
            if (!employee) {
                return renderError(res, "employee not found", 404);
            }


            var resMdms;
            try {
                resMdms = await search_mdms_order(courtCase.courtId, "common-masters.Court_Rooms", tenantId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the court room mdms", 500, ex);
            }
            var mdmsCourtRoom = resMdms.data.mdms[0].data;
            if (!mdmsCourtRoom) {
                return renderError(res, "court room not found", 404);
            }
            

            var resOrder;
            try {
                resOrder = await search_order(tenantId,  orderId, requestInfo);
            } catch (ex) {
                return renderError(res, "Failed to query details of the order", 500, ex);
            }
            var order = resOrder.data.list[0];
            if (!order) {
                return renderError(res, "Order not found", 404);
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
                        "caseName": courtCase.caseTitle,
                        "caseNumber": courtCase.cnrNumber,
                        "date": stringDate,
                        "newHearingDate": orderDate,
                        "additionalComments": order.comments,
                        "judgeSignature": "Judge's Signature",
                        "judgeName": employee.user.name,
                        "courtSeal": "Seal of Superior Court"
                    }
             
                ]
            };

            var pdfResponse;
            const pdfKey = config.pdf.new_hearing_date_after_reschedule;
            try {
                pdfResponse = await create_pdf(
                    tenantId,
                    pdfKey,
                    data,
                    requestInfo
                );
            } catch (ex) {
                return renderError(res, "Failed to generate PDF for new hearing date after rescheduling", 500, ex);
            }
            const filename = `${pdfKey}_${new Date().getTime()}`;
            res.writeHead(200, {
                "Content-Type": "application/pdf",
                "Content-Disposition": `attachment; filename=${filename}.pdf`,
            });
            pdfResponse.data.pipe(res);

        } catch (ex) {
            return renderError(res, "Failed to query details of new hearing date after rescheduling", 500, ex);
        }
    })
);

module.exports = router;
