const axios = require("axios");
const { PDFDocument } = require("pdf-lib");
const imageToPdf = require("image-to-pdf");
const fs = require("fs");
const path = require("path");
const config = require("../config/config");
const { DocumentError } = require("../util/errorUtils");

/**
 * Fetches a document from the file store based on the provided file store ID.
 *
 * @param {string} fileStoreId - The ID of the file to fetch.
 * @returns {Buffer} The fetched document as a buffer.
 */
async function fetchDocument(fileStoreId) {
  const url = `${config.fileStoreHost}/filestore/v1/files/id?tenantId=kl&fileStoreId=${fileStoreId}`;

  try {
    const response = await axios.get(url, { responseType: "arraybuffer" });

    const contentType = response.headers["content-type"];

    if (contentType === "application/pdf") {
      console.log("PDF file detected");
      return response.data;
    } else if (contentType.startsWith("image/")) {
      console.log("Image file detected");

      const imageBytes = Buffer.from(response.data);
      const pdfDoc = await PDFDocument.create();

      let image;
      if (contentType === "image/jpeg") {
        image = await pdfDoc.embedJpg(imageBytes);
      } else if (contentType === "image/png") {
        image = await pdfDoc.embedPng(imageBytes);
      } else {
        throw new Error(`Unsupported image format: ${contentType}`);
      }

      const page = pdfDoc.addPage();
      const { width: pageWidth, height: pageHeight } = page.getSize();
      const { width: imageWidth, height: imageHeight } = image;
      const scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);
      const xOffset = (pageWidth - imageWidth * scale) / 2;
      const yOffset = (pageHeight - imageHeight * scale) / 2;
      page.drawImage(image, {
        x: xOffset,
        y: yOffset,
        width: imageWidth * scale,
        height: imageHeight * scale,
      });

      const pdfBytes = await pdfDoc.save();
      return pdfBytes;
    } else {
      throw new Error(`Unsupported content type: ${contentType}`);
    }
  } catch (error) {
    console.error("Error fetching document:", error.message);
    throw error;
  }
}

/**
 * Appends PDF pages with a header to an existing PDF document.
 *
 * @param {PDFDocument} existingPdfDoc - The existing PDF document.
 * @param {string} fileStoreId - The ID of the file to append.
 * @param {string} header - The header to add to the page.
 * @returns {Promise<Buffer>} The updated PDF document as a buffer.
 */
async function appendPdfPagesWithHeader(existingPdfDoc, fileStoreId, header) {
  const helveticaFont = await existingPdfDoc.embedStandardFont("Helvetica");

  const headerPage = existingPdfDoc.addPage();
  const { width: existingWidth, height: existingHeight } = headerPage.getSize();
  headerPage.drawText(header, {
    x: 50,
    y: existingHeight - 50,
    size: 24,
    font: helveticaFont,
  });

  let documentBytes = null;
  try {
    documentBytes = await fetchDocument(fileStoreId);
  } catch (error) {
    throw new DocumentError("DOCUMENT_CURRUPTED");
  }

  const fetchedPdfDoc = await PDFDocument.load(documentBytes);
  if (!fetchedPdfDoc) {
    console.error("Failed to load PDF document.");
    return;
  }

  const fetchedPages = fetchedPdfDoc.getPages();
  if (fetchedPages.length === 0) {
    console.error("No pages found in the fetched PDF document.");
    return;
  }

  for (const pageIndex of fetchedPages.map((_, i) => i)) {
    const [copiedPage] = await existingPdfDoc.copyPages(fetchedPdfDoc, [
      pageIndex,
    ]);
    const { width: fetchedWidth, height: fetchedHeight } = copiedPage.getSize();
    const scale = existingWidth / fetchedWidth;
    copiedPage.scale(scale, scale);
    existingPdfDoc.addPage(copiedPage);
  }
}

/**
 * Appends complainant files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} complainants - The complainants object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendComplainantFilesToPDF(pdf, complainants) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < complainants.length; i++) {
    const complainant = complainants[i];
    if (complainant.companyDetailsFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        complainant.companyDetailsFileStore,
        `Authoriastion of Representative Document ${i + 1}`
      );
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends respondent files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} respondents - The respondents object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendRespondentFilesToPDF(pdf, respondents) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < respondents.length; i++) {
    const respondent = respondents[i];
    if (respondent?.inquiryAffidavitFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        respondent?.inquiryAffidavitFileStore,
        `Inquiry Affidavit Document ${i + 1}`
      );
    }
    if (respondent?.companyDetailsUpload) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        respondent?.companyDetailsUpload,
        `Accused Company Document ${i + 1}`
      );
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends cheque details files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} chequeDetails - The cheque details object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendChequeDetailsToPDF(pdf, chequeDetails) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < chequeDetails.length; i++) {
    const chequeDetail = chequeDetails[i];

    if (chequeDetail.bouncedChequeFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        chequeDetail.bouncedChequeFileStore,
        `Bounced Cheque Document ${i + 1}`
      );
    }
    if (chequeDetail.depositChequeFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        chequeDetail.depositChequeFileStore,
        `Deposit Cheque Document ${i + 1}`
      );
    }
    if (chequeDetail.returnMemoFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        chequeDetail.returnMemoFileStore,
        `Return Memo Document ${i + 1}`
      );
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends debt liability details files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} debtLiabilityDetails - The debt liability details object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendDebtLiabilityFilesToPDF(pdf, debtLiabilityDetails) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < debtLiabilityDetails.length; i++) {
    const debtLiability = debtLiabilityDetails[i];
    if (debtLiability.proofOfLiabilityFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        debtLiability.proofOfLiabilityFileStore,
        `Debt Liability Document ${i + 1}`
      );
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends demand notice details files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} demandNoticeDetails - The demand notice details object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendDemandNoticeFilesToPDF(pdf, demandNoticeDetails) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < demandNoticeDetails.length; i++) {
    const demandNotice = demandNoticeDetails[i];

    if (demandNotice.legalDemandNoticeFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        demandNotice.legalDemandNoticeFileStore,
        `Demand Notice Document ${i + 1}`
      );
    }
    if (demandNotice.proofOfDispatchFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        demandNotice.proofOfDispatchFileStore,
        `Proof of Dispatch Document ${i + 1}`
      );
    }
    if (demandNotice.proofOfAcknowledgmentFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        demandNotice.proofOfAcknowledgmentFileStore,
        `Proof of Acknowledgment Document ${i + 1}`
      );
    }
    if (demandNotice.proofOfReplyFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        demandNotice.proofOfReplyFileStore,
        `Proof of Reply Document ${i + 1}`
      );
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends demand notice details files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} delayCondonationDetails - The delay condonation details object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendDelayCondonationFilesToPDF(pdf, delayCondonationDetails) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < delayCondonationDetails.length; i++) {
    const delayCondonation = delayCondonationDetails[i];
    if (delayCondonation.delayCondonationFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        delayCondonation.delayCondonationFileStore,
        `Delay Condonation Document ${i + 1}`
      );
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends demand notice details files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} prayerSwornStatementDetails - The prayer and sworn statement details object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendPrayerSwornFilesToPDF(pdf, prayerSwornStatementDetails) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  for (let i = 0; i < prayerSwornStatementDetails.length; i++) {
    const prayerSworn = prayerSwornStatementDetails[i];

    if (prayerSworn.memorandumOfComplaintFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        prayerSworn.memorandumOfComplaintFileStore,
        `Complaint ${i + 1}`
      );
    }
    if (prayerSworn.prayerForReliefFileStore) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        prayerSworn.prayerForReliefFileStore,
        `Prayer for Relief Document ${i + 1}`
      );
    }
    if (prayerSworn.swornStatement) {
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        prayerSworn.swornStatement,
        `Affidavit under section 223 of BNSS ${i + 1}`
      );
    }
    if (prayerSworn?.complaintAdditionalDocumentFileStore?.length > 0) {
      for (
        let j = 0;
        j < prayerSworn?.complaintAdditionalDocumentFileStore.length;
        j++
      ) {
        await appendPdfPagesWithHeader(
          existingPdfDoc,
          prayerSworn?.complaintAdditionalDocumentFileStore?.[j],
          `Complaint Additional Document ${j + 1}`
        );
      }
    }
  }

  return await existingPdfDoc.save();
}

/**
 * Appends advocate files to a PDF document.
 *
 * @param {Buffer} pdf - The PDF document to append to.
 * @param {Array} advocates - The advocates object to append files from.
 * @returns {Promise<Buffer>} The updated PDF document.
 */
async function appendAdvocateFilesToPDF(pdf, advocates) {
  const existingPdfDoc = await PDFDocument.load(pdf);

  let vakalatnamaCount = 0;
  let affidavitCount = 0;
  const uniqueFileStores = new Set();

  for (let i = 0; i < advocates.length; i++) {
    const advocate = advocates[i];

    if (
      advocate.vakalatnamaFileStore &&
      !uniqueFileStores.has(advocate.vakalatnamaFileStore)
    ) {
      vakalatnamaCount++;
      uniqueFileStores.add(advocate.vakalatnamaFileStore);
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        advocate.vakalatnamaFileStore,
        `Vakalatnama Document ${vakalatnamaCount}`
      );
    } else if (
      advocate.pipAffidavitFileStore &&
      !uniqueFileStores.has(advocate.pipAffidavitFileStore)
    ) {
      uniqueFileStores.add(advocate.pipAffidavitFileStore);
      affidavitCount++;
      await appendPdfPagesWithHeader(
        existingPdfDoc,
        advocate.pipAffidavitFileStore,
        `Affidavit Document ${affidavitCount}`
      );
    }
  }

  return await existingPdfDoc.save();
}

async function validateDocuments(docs) {
  for (const doc of docs) {
    try {
      const documentBytes = await fetchDocument(doc?.fileStore);
    } catch (error) {
      throw new DocumentError(doc?.documentType);
    }
  }
  return;
}

module.exports = {
  appendComplainantFilesToPDF,
  appendRespondentFilesToPDF,
  appendChequeDetailsToPDF,
  appendDebtLiabilityFilesToPDF,
  appendDemandNoticeFilesToPDF,
  appendDelayCondonationFilesToPDF,
  appendPrayerSwornFilesToPDF,
  appendAdvocateFilesToPDF,
  validateDocuments,
};
