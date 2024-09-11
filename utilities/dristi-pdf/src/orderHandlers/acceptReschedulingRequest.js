const cheerio = require('cheerio');
const config = require("../config");
const { search_individual_uuid, search_case, search_order, search_mdms, search_hrms, search_sunbirdrc_credential_service, create_pdf, search_application } = require("../api");
const { renderError } = require("../utils/renderError");

function formatDate(epochMillis) {
    // Convert epoch milliseconds to a Date object
    const date = new Date(epochMillis);

    // Ensure that the date is a valid Date object
    if (Number.isNaN(date.getTime())) {
        throw new Error("Invalid date");
    }

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
}

async function acceptReschedulingRequest(req, res, qrCode) {
    const cnrNumber = req.query.cnrNumber;
    const orderId = req.query.orderId;
    const tenantId = req.query.tenantId;
    const entityId = req.query.entityId;
    const code = req.query.code;
    const requestInfo = req.body.RequestInfo;

    const missingFields = [];
    if (!cnrNumber) missingFields.push("cnrNumber");
    if (!orderId) missingFields.push("orderId");
    if (!tenantId) missingFields.push("tenantId");
    if (requestInfo === undefined) missingFields.push("requestInfo");
    if (qrCode === 'true' && (!entityId || !code)) missingFields.push("entityId and code");

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

        // Search for order details
        const resOrder = await handleApiCall(
            () => search_order(tenantId, orderId, requestInfo),
            "Failed to query order service"
        );
        const order = resOrder?.data?.list[0];
        if (!order) {
            renderError(res, "Order not found", 404);
        }

        // Search for application details
        const resApplication = await handleApiCall(
            () => search_application(tenantId, order.applicationNumber[0], requestInfo),
            "Failed to query application service"
        );
        const application = resApplication?.data?.applicationList[0];
        if (!application) {
            renderError(res, "Application not found", 404);
        }

        // Search for individual details
        // const resIndividual = await handleApiCall(
        //     () => search_individual_uuid(tenantId, application.onBehalfOf[0], requestInfo),
        //     "Failed to query individual service using id"
        // );
        // const individual = resIndividual?.data?.Individual[0];
        // if (!individual) {
        //     renderError(res, "Individual not found", 404);
        // }

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

        var stringDate
        try {
            stringDate = formatDate(order.createdDate);
        } catch (error) {
            return renderError(res, "Cannot convert epoch time to date", 500, error);
        }

        const data = {
            "Data": [
                {
                    "courtName": mdmsCourtRoom.name,
                    "caseName": courtCase.caseTitle,
                    "caseNumber": courtCase.cnrNumber,
                    "date": stringDate,
                    "partyNames": application.additionalDetails.onBehalOfName,
                    "applicationId": order.orderDetails?.refApplicationId || "N/A",
                    "reasonForRescheduling": application?.additionalDetails?.formdata?.reschedulingReason?.name || "N/A",
                    "originalHearingDate": order.orderDetails?.originalHearingDate ? formatDate(order.orderDetails?.originalHearingDate) : "",
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
        const pdfKey = qrCode === 'true' ? config.pdf.accept_rescheduling_request_qr : config.pdf.accept_rescheduling_request;
        const pdfResponse = await handleApiCall(
            () => create_pdf(tenantId, pdfKey, data, req.body),
            "Failed to generate PDF of order to Accept Rescheduling Request (No New Date)"
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
        return renderError(res, "Failed to query details of order to Accept Rescheduling Request (No New Date)", 500, ex);
    }
}


module.exports = acceptReschedulingRequest;