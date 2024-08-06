const cheerio = require('cheerio');
const config = require("../config");

const { search_case, search_order, search_mdms_order, search_hrms, search_sunbirdrc_credential_service, create_pdf } = require("../api");

const { renderError } = require("../utils/renderError");

async function orderGeneric(req, res, qrCode) {
        const cnrNumber = req.query.cnrNumber;
        const orderId = req.query.orderId;
        const entityId = req.query.entityId;
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
        if (qrCode === 'true' && (!entityId || !code)) {
            return renderError(res, "entityId and code are mandatory when qrCode is enabled", 400);
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
            if (qrCode === 'true') {
                var resCredential;
                try {
                    resCredential = await search_sunbirdrc_credential_service(tenantId, code, entityId, requestInfo);
                } catch (ex) {
                    return renderError(res, "Failed to query details of the sunbirdrc credential service", 500, ex);
                }
                // Load the response HTML into cheerio
                const $ = cheerio.load(resCredential.data);
    
                // Extract the base64 URL from the img tag
                base64Url = $('img').attr('src');
            }

            const data = {
                "Data": [
                    {
                        "courtName": mdmsCourtRoom.name,
                        "caseName": mdmsCourtEstablishment.boundaryName,
                        "caseNumber": mdmsCourtEstablishment.rootBoundaryName,
                        "orderName": order.orderNumber,
                        "date": order.createdDate,
                        "content": order.comments,
                        "judgeSignature": "[ Judge's Signature ]",
                        "judgeName": employee.user.name,
                        "courtSeal": "[ Court Seal ]",
                        "qrCodeUrl": base64Url
                    }
                ]

            };

            var pdfResponse;
            const pdfKey = qrCode === 'true' ? config.pdf.order_generic_qr : config.pdf.order_generic;
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
            return renderError(res, "Failed to query details for generic order", 500, ex);
        }
    }

module.exports = orderGeneric;