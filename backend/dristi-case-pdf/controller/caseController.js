const caseService = require('../service/caseService');
const pdfService = require('../service/pdfService');
const fileService = require('../service/fileService');
 
exports.generateCasePdf = async (req, res, next) => {
    try {
        const cases = req.body.cases;

        const filingNumber = cases.filingNumber;
        const courtName = cases.courtName;
        const caseYear = await extractCaseYear(filingNumber);
        const caseNumber = await extractCaseNumber(filingNumber);
        const sectionNumber = await caseService.getCaseSectionNumber(cases);
 
        const complainants = await caseService.getComplainantsDetails(cases);
        const respondents = await caseService.getRespondentsDetails(cases);
        const witnesses = await caseService.getWitnessDetails(cases);
        const advocates = await caseService.getAdvocateDetails(cases);
 
        const chequeDetails = await caseService.getChequeDetails(cases);
        const debtLiabilityDetails = await caseService.getDebtLiabilityDetails(cases);
        const demandNoticeDetails = await caseService.getDemandNoticeDetails(cases);
        const delayCondonationDetails = await caseService.getDelayCondonationDetails(cases);
 
        const prayerSwornStatementDetails = await caseService.getPrayerSwornStatementDetails(cases);
 
        const requestInfo = req.body.RequestInfo;
 
        const pdfRequest = {
            RequestInfo: requestInfo,
            caseDetails: {
                id: Date.now(),
                courtName: courtName,
                caseNumber: caseNumber,
                caseYear: caseYear,
                filingNumber: filingNumber,
                sectionNumber: sectionNumber,
                complainants: complainants,
                respondents: respondents,
                witnesses: witnesses,
                advocates: advocates,
                chequeDetails: chequeDetails,
                debtLiabilityDetails: debtLiabilityDetails,
                demandNoticeDetails: demandNoticeDetails,
                delayCondonationDetails: delayCondonationDetails,
                prayerSwornStatementDetails: prayerSwornStatementDetails
            }
        };

        console.log("Pdf Request: {}", pdfRequest);
        const pdf = await pdfService.generatePDF(pdfRequest);

        let finalPdf = await fileService.appendComplainantFilesToPDF(pdf, complainants);
        finalPdf = await fileService.appendRespondentFilesToPDF(finalPdf, respondents);
        finalPdf = await fileService.appendChequeDetailsToPDF(finalPdf, chequeDetails);
        finalPdf = await fileService.appendDebtLiabilityFilesToPDF(finalPdf, debtLiabilityDetails);
        finalPdf = await fileService.appendDemandNoticeFilesToPDF(finalPdf, demandNoticeDetails);
        finalPdf = await fileService.appendDelayCondonationFilesToPDF(finalPdf, delayCondonationDetails);
        finalPdf = await fileService.appendPrayerSwornFilesToPDF(finalPdf, prayerSwornStatementDetails);
        finalPdf = await fileService.appendAdvocateFilesToPDF(finalPdf, advocates);

        const finalPdfBuffer = Buffer.from(finalPdf);
        console.log("Pdf Generated Successfully");

        res.setHeader('Content-Type', 'application/pdf');
        res.setHeader('Content-Disposition', 'attachment; filename="caseDetails.pdf"');
        res.send(finalPdfBuffer);
    } catch (error) {
        next(error);
    }
};

function extractCaseYear(input) {
    const yearMatch = input.match(/-\d{4}-/);
    if (yearMatch) {
        return yearMatch[0].replace(/-/g, ''); 
    } else {
        return '';
    }
}

function extractCaseNumber(input) {
    const match = input.match(/-(\d+)$/);
    if (match) {
        return parseInt(match[1], 10);
    } else {
        return ''; 
    }
}
