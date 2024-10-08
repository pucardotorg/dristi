const caseService = require('../service/caseService');
const pdfService = require('../service/pdfService');
const fileService = require('../service/fileService');
const config = require('../config/config');

 /**
  * Generates a PDF document for a case.
  *
  * @param {Object} req - The request object.
  * @param {Object} res - The response object.
  * @param {Function} next - The next middleware function.
  */
exports.generateCasePdf = async (req, res, next) => {
    try {
        const cases = req.body.cases;

        const filingNumber = cases.filingNumber || null;
        const courtName = cases.courtName || `${config.courtName}`;
        
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

// Function to extract the year (2025)
function extractCaseYear(input) {
    const yearMatch = input.match(/-(\d{4})$/);
    if (yearMatch) {
        return yearMatch[1]; // Return the captured year
    } else {
        return '';
    }
}

// Function to extract the case number (000053)
function extractCaseNumber(input) {
    const match = input.match(/-(\d{6})-/);
    if (match) {
        return match[1]; // Return the captured 6-digit case number
    } else {
        return '';
    }
}
