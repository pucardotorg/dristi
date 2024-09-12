const cheerio = require('cheerio');
const config = require("../config");
const { search_case, search_order, search_hearing, search_mdms, search_hrms, search_individual, search_sunbirdrc_credential_service, create_pdf } = require("../api");
const { renderError } = require("../utils/renderError");

async function summonsIssue(req, res, qrCode) {
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
        
        // FIXME: Commenting out HRMS calls is it not impl in solution
        // Search for HRMS details
        // const resHrms = await handleApiCall(
        //     () => search_hrms(tenantId, "JUDGE", courtCase.courtId, requestInfo),
        //     "Failed to query HRMS service"
        // );
        // const employee = resHrms?.data?.Employees[0];
        // if (!employee) {
        //     renderError(res, "Employee not found", 404);
        // }

        // Search for MDMS court room details
        const resMdms = await handleApiCall(
            () => search_mdms(courtCase.courtId, "common-masters.Court_Rooms", tenantId, requestInfo),
            "Failed to query MDMS service for court room"
        );
        const mdmsCourtRoom = resMdms?.data?.mdms[0]?.data;
        if (!mdmsCourtRoom) {
            renderError(res, "Court room MDMS master not found", 404);
        }

        // FIXME: Commenting out MDMS calls is it not impl in solution
        // Search for MDMS court establishment details
        // const resMdms1 = await handleApiCall(
        //     () => search_mdms(mdmsCourtRoom.courtEstablishmentId, "case.CourtEstablishment", tenantId, requestInfo),
        //     "Failed to query MDMS service for court establishment"
        // );
        // const mdmsCourtEstablishment = resMdms1?.data?.mdms[0]?.data;
        // if (!mdmsCourtEstablishment) {
        //     renderError(res, "Court establishment MDMS master not found", 404);
        // }
        
        // Search for order details
        const resOrder = await handleApiCall(
            () => search_order(tenantId, orderId, requestInfo),
            "Failed to query order service"
        );
        const order = resOrder?.data?.list[0];
        if (!order) {
            renderError(res, "Order not found", 404);
        }

        // Search for hearing details
        const resHearing = await handleApiCall(
            () => search_hearing(tenantId, cnrNumber, requestInfo),
            "Failed to query hearing service"
        );
        const hearing = resHearing?.data?.HearingList[0];
        if (!hearing) {
            renderError(res, "Hearing not found", 404);
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
                    "caseNumber": courtCase.caseNumber,
                    "year": year,
                    "caseName": courtCase.caseTitle,
                    "respondentName": order.orderDetails.respondentName,
                    "date": order.createdDate,
                    "hearingDate": hearing.startTime,
                    "additionalComments": order.comments,
                    "judgeSignature": "Judge Signature",
                    "courtSeal": "Court Seal",
                    "qrCodeUrl": base64Url,
                    "place": "Kollam", // FIXME: mdmsCourtEstablishment.boundaryName,
                    "state": "Kerala", //FIXME: mdmsCourtEstablishment.rootBoundaryName,
                    "judgeName": "John Watt", // FIXME: employee.user.name,
                }
            ]
        };

        // Generate the PDF
        const pdfKey = qrCode === 'true' ? config.pdf.summons_issue_qr : config.pdf.summons_issue;
        const pdfResponse = await handleApiCall(
            () => create_pdf(tenantId, pdfKey, data, req.body),
            "Failed to generate PDF of order for Issue of Summons"
        )
        const filename = `${pdfKey}_${new Date().getTime()}`;
        res.writeHead(200, {
            "Content-Type": "application/pdf",
            "Content-Disposition": `attachment; filename=${filename}.pdf`
        });
        pdfResponse.data.pipe(res).on('finish', () => {
            res.end();
        }).on('error', (err) => {
            return renderError(res, "Failed to send PDF response", 500, err);
        });
    } catch (ex) {
        return renderError(res, "Failed to query details of order for Issue of Summons", 500, ex);
    }
}

module.exports = summonsIssue;