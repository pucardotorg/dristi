const cheerio = require('cheerio');
const config = require("../config");
const { search_case, search_order, search_mdms, search_hrms, search_individual, search_sunbirdrc_credential_service, create_pdf } = require("../api");
const { renderError } = require("../utils/renderError");

async function caseSettlementRejection(req, res, qrCode) {
    const cnrNumber = req.query.cnrNumber;
    const orderId = req.query.orderId;
    const entityId = req.query.entityId;
    const code = req.query.code;
    const tenantId = req.query.tenantId;
    const requestInfo = req.body.RequestInfo;

    const missingFields = [];
    if (!cnrNumber) missingFields.push("cnrNumber");
    if (!orderId) missingFields.push("orderId");
    if (!tenantId) missingFields.push("tenantId");
    if (qrCode === 'true' && (!entityId || !code)) missingFields.push("entityId and code");
    if (requestInfo === undefined) missingFields.push("requestInfo");

    if (missingFields.length > 0) {
        return renderError(res, `${missingFields.join(", ")} are mandatory to generate the PDF`, 400);
    }

    // Function to handle API calls
    const handleApiCall = async (apiCall, errorMessage) => {
        try {
            return await apiCall();
        } catch (ex) {
            renderError(res, `${errorMessage}`, 500, ex);
            throw ex;  // Ensure the function stops on error
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

        // Search for HRMS details
        const resHrms = await handleApiCall(
            () => search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo),
            "Failed to query HRMS service"
        );
        const employee = resHrms?.data?.Employees[0];
        if (!employee) {
            renderError(res, "Employee not found", 404);
        }

        // Search for MDMS court room details
        const resMdms = await handleApiCall(
            () => search_mdms(courtCase.courtId, "common-masters.Court_Rooms", tenantId, requestInfo),
            "Failed to query MDMS service for court room"
        );
        const mdmsCourtRoom = resMdms?.data?.mdms[0]?.data;
        if (!mdmsCourtRoom) {
            renderError(res, "Court room MDMS master not found", 404);
        }

        // Search for order details
        const resOrder = await handleApiCall(
            () => search_order(tenantId, orderId, requestInfo),
            "Failed to query order service"
        );
        const order = resOrder?.data?.list[0];
        if (!order) {
            renderError(res, "Order not found", 404);
        }

        // Filter litigants to find the respondent.primary
        const respondentParty = courtCase.litigants.find(party => party.partyType === 'respondent.primary');
        if (!respondentParty) {
            return renderError(res, "No party with partyType 'respondent.primary' found", 400);
        }
        // Search for individual details
        const resIndividual = await handleApiCall(
            () => search_individual(tenantId, respondentParty.individualId, requestInfo),
            "Failed to query individual service using individualId"
        );
        const respondentIndividual = resIndividual?.data?.Individual[0];
        if (!respondentIndividual) {
            renderError(res, "Respondent individual not found", 404);
        }

        // Filter litigants to find the complaint.primary
        const complaintParty = courtCase.litigants.find(party => party.partyType === 'complaint.primary');
        if (!complaintParty) {
            return renderError(res, "No party with partyType 'complaint.primary' found", 400);
        }

        // Search for individual details
        const resIndividual1 = await handleApiCall(
            () => search_individual(tenantId, complaintParty.individualId, requestInfo),
            "Failed to query individual service using individualId"
        );
        const complaintIndividual = resIndividual1?.data?.Individual[0];
        if (!complaintIndividual) {
            renderError(res, "Complaint individual not found", 404);
        }

        // Handle QR code if enabled
        let base64Url = "";
        if (qrCode === 'true') {
            const resCredential = await handleApiCall(
                () => search_sunbirdrc_credential_service(tenantId, code, entityId, requestInfo),
                "Failed to query sunbirdrc credential service"
            );
            const $ = cheerio.load(resCredential.data);
            const imgTag = $('img');
            if (imgTag.length === 0) {
                return renderError(res, "No img tag found in the sunbirdrc response", 500);
            }
            base64Url = imgTag.attr('src');
        }

        const data = {
            "Data": [
                {
                    "courtName": mdmsCourtRoom.name,
                    "caseName": courtCase.caseTitle,
                    "caseNumber": courtCase.cnrNumber,
                    "partyName": `${complaintIndividual.name.givenName} ${complaintIndividual.name.familyName}`,
                    "otherPartyName": `${respondentIndividual.name.givenName} ${respondentIndividual.name.familyName}`,
                    "date": order.createdDate,
                    "settlementAgreementDate": "Settlement agreement date from UI",
                    "mechanism": "mechanism from UI",
                    "implemented": "Implementation status from UI",
                    "rejectionReason": "Rejection reason from UI",
                    "additionalComments": order.comments,
                    "judgeSignature": "Judge Signature",
                    "judgeName": employee.user.name,
                    "courtSeal": "Court Seal",
                    "qrCodeUrl": base64Url
                }
            ]
        };

        // Generate the PDF
        const pdfKey = qrCode === 'true' ? config.pdf.case_settlement_rejection_qr : config.pdf.case_settlement_rejection;
        const pdfResponse = await handleApiCall(
            () => create_pdf(tenantId, pdfKey, data, req.body),
            "Failed to generate PDF of order to Settle a Case - Rejection"
        )
        res.writeHead(200, {
            "Content-Type": "application/json",
        });
        pdfResponse.data.pipe(res).on('finish', () => {
            res.end();
        }).on('error', (err) => {
            return renderError(res, "Failed to send PDF response", 500, err);
        });
    } catch (ex) {
        return renderError(res, "Failed to query details of order to Settle a Case - Rejection", 500, ex);
    }
}

module.exports = caseSettlementRejection;