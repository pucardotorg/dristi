const cheerio = require('cheerio');
const config = require("../config");

const { search_case, search_order, search_mdms_order, search_hrms, search_individual, search_sunbirdrc_credential_service, create_pdf } = require("../api");

const { renderError } = require("../utils/renderError");

async function caseTransfer(req, res, qrCode) {
    const cnrNumber = req.query.cnrNumber;
    const orderId = req.query.orderId;
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
    if (qrCode === 'true' && (!taskId || !code)) {
        return renderError(res, "taskId and code are mandatory when qrCode is enabled", 400);
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

        // Filter litigants to find the respondent.primary
        const respondentParty = courtCase.litigants.find(party => party.partyType === 'respondent.primary');
        if (!respondentParty) {
            return renderError(res, "No respondent with partyType 'respondent.primary' found", 400);
        }

        var resIndividual;
        try {
            resIndividual = await search_individual(tenantId, respondentParty.individualId, requestInfo);
        } catch (ex) {
            return renderError(res, "Failed to query details of the individual", 500, ex);
        }
        var individual = resIndividual.data.Individual[0];

        let base64Url = "";
        if (qrCode === 'true') {
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
        } else if (typeof courtCase.filingDate === 'number') {
            // Assuming the number is in milliseconds (epoch time)
            year = new Date(courtCase.filingDate).getFullYear();
        } else {
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
                    "applicationId": order.applicationNumber[0],
                    "partyName": `${individual.name.givenName} ${individual.name.familyName}`,
                    "courtOrJurisdiction": "Court from UI",
                    "groundsForSeekingTransfer": "Grounds for seeking transfer from UI",
                    "status": "GRANTED/REJECTED from UI",
                    "transferredToCourtOrJurisdiction": "Transferred Court from UI",
                    "rejectionReason": "Rejection reason from UI",
                    "additionalComments": order.comments,
                    "judgeSignature": "Judge's Signature",
                    "judgeName": employee.user.name,
                    "judgeSeal": "Court Seal",
                    "qrCodeUrl": base64Url
                }
            ]

        };

        var pdfResponse;
        const pdfKey = qrCode === 'true' ? config.pdf.case_transfer_qr : config.pdf.case_transfer;
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
        return renderError(res, "Failed to query details for transfer of case", 500, ex);
    }
}

module.exports = caseTransfer;