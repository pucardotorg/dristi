const {
  search_pdf_v2,
  search_case_v2,
  create_pdf_v2,
  create_file_v2,
} = require("../api"); // Removed create_pdf import
const fs = require("fs");
const path = require("path");
const { PDFDocument } = require("pdf-lib");
const config = require("../config");
const cloneDeep = require("lodash.clonedeep");
const sharp = require("sharp");

const TEMP_FILES_DIR = path.join(__dirname, "../temp");

if (!fs.existsSync(TEMP_FILES_DIR)) {
  fs.mkdirSync(TEMP_FILES_DIR);
}

const A4_WIDTH = 595.28; // A4 width in points
const A4_HEIGHT = 841.89; // A4 height in points

// Master Data
const MASTER_DATA = [
  {
    name: "Case Cover Page",
    section: "titlepage",
    docType: null,
    isActive: true,
    order: 1,
  },
  {
    name: "Complaint",
    section: "complaint",
    docType: null,
    isActive: true,
    docketPageRequired: false,
    order: 1,
  },
  {
    name: "Cheque",
    section: "filings",
    docketPageRequired: true,
    docType: "case.cheque",
    isActive: true,
    order: 1,
  },
  {
    name: "Cheque Deposit Slip",
    section: "filings",
    docketPageRequired: true,
    docType: "case.cheque.depositslip",
    isActive: true,
    order: 2,
  },
  {
    name: "Cheque Return Memo",
    section: "filings",
    docketPageRequired: true,
    docType: "case.cheque.returnmemo",
    isActive: true,
    order: 3,
  },
  {
    name: "Demand Notice",
    section: "filings",
    docketPageRequired: true,
    docType: "case.demandnotice",
    isActive: true,
    order: 4,
  },
  {
    name: "Proof of Dispatch of Demand Notice",
    section: "filings",
    docketPageRequired: true,
    docType: "case.demandnotice.proof",
    isActive: true,
    order: 5,
  },
  {
    name: "Proof of Service of Demand Notice",
    section: "filings",
    docketPageRequired: true,
    docType: "case.demandnotice.serviceproof",
    isActive: true,
    order: 6,
  },
  {
    name: "Reply Notice",
    section: "filings",
    docketPageRequired: true,
    docType: "case.replynotice",
    isActive: true,
    order: 7,
  },
  {
    name: "Proof of Liability",
    section: "filings",
    docketPageRequired: true,
    docType: "case.liabilityproof",
    isActive: true,
    order: 8,
  },
  {
    name: "Proof of Authorization",
    section: "filings",
    docketPageRequired: true,
    docType: "case.authorizationproof",
    isActive: true,
    order: 9,
  },
  {
    name: "Affidavit under Section 223 BNSS",
    section: "affidavit",
    docType: "case.affidavit.223bnss",
    isActive: true,
    order: 1,
  },
  {
    name: "Affidavit of Proof under Section 225 BNSS",
    section: "affidavit",
    docType: "case.affidavit.225bnss",
    isActive: true,
    order: 2,
  },
  { name: "Vakalatnama", section: "vakalat", docType: null, isActive: true },
];

// // Function to retrieve documents from the master data
// function getDocumentsForSection(sectionName) {
//   return MASTER_DATA.filter(
//     (doc) => doc.section === sectionName && doc.isActive
//   );
// }

// // Function to process documents for a section
// async function processSectionDocuments(tenantId, section, requestInfo) {
//   const fileStoreIds = [];

//   console.log(`Processing section: ${section.name}`);

//   const documents = getDocumentsForSection(section.name);
//   if (documents.length === 0) {
//     console.log(`No active documents found for section: ${section.name}`);
//     return fileStoreIds;
//   }

//   for (const doc of documents) {
//     // Simulate retrieving the fileStoreId (in a real scenario, fetch from the database or service)
//     const fileStoreId = `fileStore-${doc.docType}-${Date.now()}`;
//     console.log(
//       `Validating fileStoreId: ${fileStoreId} for document: ${doc.name}`
//     );

//     // Validate the fileStoreId
//     const pdfResponse = await search_pdf(tenantId, fileStoreId, requestInfo);
//     if (pdfResponse.status !== 200) {
//       console.error(
//         `Invalid fileStoreId: ${fileStoreId} for document: ${doc.name}`
//       );
//       continue;
//     }

//     fileStoreIds.push(fileStoreId);
//   }

//   return fileStoreIds;
// }

// // Function to merge PDFs
// async function mergePdfs(fileStoreIds, tenantId) {
//   const mergedPdf = await PDFDocument.create();

//   for (const fileStoreId of fileStoreIds) {
//     const pdfResponse = await search_pdf(tenantId, fileStoreId);
//     if (pdfResponse.status === 200) {
//       const pdfUrl = pdfResponse.data[fileStoreId];
//       const pdfFetchResponse = await axios.get(pdfUrl, {
//         responseType: "arraybuffer",
//       });
//       const pdfData = pdfFetchResponse.data;

//       const pdfDoc = await PDFDocument.load(pdfData);
//       const copiedPages = await mergedPdf.copyPages(
//         pdfDoc,
//         pdfDoc.getPageIndices()
//       );
//       copiedPages.forEach((page) => mergedPdf.addPage(page));
//     }
//   }

//   return mergedPdf;
// }

/**
 *
 * @param  {...PDFDocument} pdfDocuments
 * @returns
 */
async function mergePDFDocuments(...pdfDocuments) {
  const pdfDocument = await PDFDocument.create();
  for (const pdfDoc of pdfDocuments) {
    const copiedPages = await pdfDocument.copyPages(
      pdfDoc,
      pdfDoc.getPageIndices()
    );
    copiedPages.forEach((page) => pdfDocument.addPage(page));
  }
  return pdfDocument;
}

/**
 *
 * @param {PDFDocument} pdfDoc
 * @param {string} tenantId
 * @param {*}
 * @returns {Promise<string>}
 */
async function persistPDF(pdfDoc, tenantId, requestInfo) {
  const pdfBytes = await pdfDoc.save();

  const mergedFilePath = path.join(
    TEMP_FILES_DIR,
    `merged-bundle-${Date.now()}.pdf`
  );
  fs.writeFileSync(mergedFilePath, pdfBytes);

  const fileStoreResponse = await create_file_v2({
    filePath: mergedFilePath,
    tenantId,
    requestInfo,
    module: "case-bundle-case-index",
  });
  fs.unlinkSync(mergedFilePath);

  return fileStoreResponse?.data?.files?.[0].fileStoreId;
}

/**
 *
 * @param {MASTER_DATA} caseBundleMasterData
 * @param {string} sectionName
 * @returns
 */
function filterCaseBundleBySection(caseBundleMasterData, sectionName) {
  return caseBundleMasterData.filter(
    (indexItem) => indexItem.section === sectionName && indexItem.isActive
  );
}

/**
 *
 * @param {*} jpgBuffer
 * @returns
 */
async function fixJpg(jpgBuffer) {
  return await sharp(jpgBuffer).jpeg().toBuffer();
}

/**
 *
 * @param {string} documentFileStoreId
 * @param {string} docketApplicationType
 * @param {string} tenantId
 * @param {*} requestInfo
 * @returns {Promise<string>} document [with docket] filestore id
 */
async function applyDocketToDocument(
  documentFileStoreId,
  {
    docketApplicationType,
    docketCounselFor,
    docketNameOfFiling,
    docketNameOfAdvocate,
    docketDateOfSubmission,
  },
  courtCase,
  tenantId,
  requestInfo
) {
  if (!documentFileStoreId) {
    return null;
  }
  const { data: stream, headers } = await search_pdf_v2(
    tenantId,
    documentFileStoreId,
    requestInfo
  );
  const mimeType = headers["content-type"];
  let filingPDFDocument;
  if (mimeType === "application/pdf") {
    filingPDFDocument = await PDFDocument.load(stream);
  } else if (["image/jpeg", "image/png", "image/jpg"].includes(mimeType)) {
    filingPDFDocument = await PDFDocument.create();
    let img;
    if (mimeType === "image/png") {
      img = await filingPDFDocument.embedPng(stream);
    } else {
      const repairedImage = await fixJpg(stream);
      img = await filingPDFDocument.embedJpg(repairedImage);
    }

    const { width, height } = img.scale(1);
    const scale = Math.min(A4_WIDTH / width, A4_HEIGHT / height);
    const xOffset = (A4_WIDTH - width * scale) / 2;
    const yOffset = (A4_HEIGHT - height * scale) / 2;
    const page = filingPDFDocument.addPage([A4_WIDTH, A4_HEIGHT]);
    page.drawImage(img, {
      x: xOffset,
      y: yOffset,
      width: width * scale,
      height: height * scale,
    });
  }

  const complainant =
    courtCase?.additionalDetails?.complainantDetails?.formdata?.[0]?.data;
  const respondent =
    courtCase?.additionalDetails?.respondentDetails?.formdata?.[0]?.data;
  const docketComplainantName = [
    complainant?.firstName,
    complainant?.middleName,
    complainant?.lastName,
  ]
    .filter(Boolean)
    .join(" ");
  const data = {
    Data: [
      {
        docketDateOfSubmission: docketDateOfSubmission,
        docketCourtName: config.constants.mdmsCourtRoom.name,
        docketComplainantName,
        docketAccusedName: [
          respondent.firstName,
          respondent.middleName,
          respondent.lastName,
        ]
          .filter(Boolean)
          .join(" "),
        docketApplicationType,
        docketNameOfAdvocate,
        docketCounselFor,
        docketNameOfFiling,
      },
    ],
  };
  const filingDocketPdfResponse = await create_pdf_v2(
    tenantId,
    "docket-page",
    data,
    { RequestInfo: requestInfo }
  );
  const filingDocketPDFDocument = await PDFDocument.load(
    filingDocketPdfResponse.data
  );

  const mergedDocumentWithDocket = await mergePDFDocuments(
    filingDocketPDFDocument,
    filingPDFDocument
  );
  const mergedDocWithDocketFileStoreId = await persistPDF(
    mergedDocumentWithDocket,
    tenantId,
    requestInfo
  );
  return mergedDocWithDocketFileStoreId;
}

async function processPendingAdmissionCase({
  tenantId,
  caseId,
  index,
  requestInfo,
}) {
  const indexCopy = cloneDeep(index);

  // TODO: fetch case-bundle-master master data
  const caseBundleMaster = MASTER_DATA;

  const caseResponse = await search_case_v2(
    [
      {
        caseId,
      },
    ],
    tenantId,
    requestInfo
  );
  const courtCase = caseResponse?.data?.criteria[0]?.responseList[0];

  const titlepageSection = filterCaseBundleBySection(
    caseBundleMaster,
    "titlepage"
  )[0];

  if (titlepageSection) {
    const coverCaseName = courtCase.caseTitle;
    const coverCaseType = courtCase.caseType;
    const coverCaseNumber =
      courtCase.courtCaseNumber ||
      courtCase.cmpNumber ||
      courtCase.filingNumber;
    const data = { Data: [{ coverCaseName, coverCaseType, coverCaseNumber }] };
    const caseCoverPdfResponse = await create_pdf_v2(
      tenantId,
      "cover-page-pdf",
      data,
      { RequestInfo: requestInfo }
    );
    const caseCoverDoc = await PDFDocument.load(caseCoverPdfResponse.data);

    const titlepageFileStoreId = await persistPDF(
      caseCoverDoc,
      tenantId,
      requestInfo
    );

    // update index

    const titlepageIndexSection = indexCopy.sections.find(
      (section) => section.name === "titlepage"
    );

    titlepageIndexSection.lineItems = [
      {
        content: "cover",
        createPDF: false,
        sourceId: titlepageFileStoreId,
        fileStoreId: titlepageFileStoreId,
        sortParam: null,
      },
    ];
  }

  const complaintSection = filterCaseBundleBySection(
    caseBundleMaster,
    "complaint"
  )[0];


  const complaintFileStoreId = courtCase.documents.find(
    // (doc) => doc.documentType === "case.complaint.signed"
    (doc) => doc.documentType === "case.cheque"
  )?.fileStore;
  if (!complaintFileStoreId) {
    throw new Error("no case complaint");
  }
  const { data: stream } = await search_pdf_v2(
    tenantId,
    complaintFileStoreId,
    requestInfo
  );
  let caseBundlePdfDoc = await PDFDocument.create();
  let pdfDoc = await PDFDocument.load(stream);

  const copiedPages = await caseBundlePdfDoc.copyPages(
    pdfDoc,
    pdfDoc.getPageIndices()
  );
  copiedPages.forEach((page) => caseBundlePdfDoc.addPage(page));

  if (complaintSection.docketPageRequired) {
    const coverCaseName = courtCase.caseTitle;
    const coverCaseType = courtCase.caseType;
    const coverCaseNumber =
      courtCase.courtCaseNumber ||
      courtCase.cmpNumber ||
      courtCase.filingNumber;
    const data = { Data: [{ coverCaseName, coverCaseType, coverCaseNumber }] };
    const caseCoverPdfResponse = await create_pdf_v2(
      tenantId,
      "cover-page-pdf",
      data,
      { RequestInfo: requestInfo }
    );
    const caseCoverDoc = await PDFDocument.load(caseCoverPdfResponse.data);
    const copiedPages = await caseCoverDoc.copyPages(
      caseBundlePdfDoc,
      caseBundlePdfDoc.getPageIndices()
    );
    copiedPages.forEach((page) => caseCoverDoc.addPage(page));
    caseBundlePdfDoc = caseCoverDoc;
  }

  const complaintNewFileStoreId = await persistPDF(
    caseBundlePdfDoc,
    tenantId,
    requestInfo
  );

  // update index

  const complaintIndexSection = indexCopy.sections.find(
    (section) => section.name === "complaint"
  );
  complaintIndexSection.lineItems[0] = {
    ...complaintIndexSection.lineItems[0],
    createPDF: false,
    sourceId: complaintFileStoreId,
    fileStoreId: complaintNewFileStoreId,
    content: "complaint",
    sortParam: null,
  };

  // move to filings section of case complaint

  const filingsSection = filterCaseBundleBySection(caseBundleMaster, "filings");
  const sortedFilingSection = [...filingsSection].sort(
    (secA, secB) => secA.order - secB.order
  );
  const filingsLineItems = await Promise.all(
    sortedFilingSection.map(async (section, index) => {
      const documentFileStoreId = courtCase.documents.find(
        (doc) => doc.documentType === section.docType
      )?.fileStore;
      if (!documentFileStoreId) {
        return null;
      }
      if (section.docketPageRequired) {
        const complainant =
          courtCase?.additionalDetails?.complainantDetails?.formdata?.[0]?.data;
        const docketComplainantName = [
          complainant?.firstName,
          complainant?.middleName,
          complainant?.lastName,
        ]
          .filter(Boolean)
          .join(" ");
        const docketNameOfAdvocate = courtCase.representatives?.find((adv) =>
          adv.representing?.find(
            (party) => party.partyType === "complainant.primary"
          )
        )?.additionalDetails?.advocateName;

        const mergedFilingDocumentFileStoreId = await applyDocketToDocument(
          documentFileStoreId,
          {
            docketApplicationType: section.name,
            docketCounselFor: "COMPLAINANT",
            docketNameOfFiling: docketComplainantName,
            docketNameOfAdvocate: docketNameOfAdvocate || docketComplainantName,
            docketDateOfSubmission: new Date(
              courtCase.registrationDate
            ).toLocaleDateString("en-IN"),
          },
          courtCase,
          tenantId,
          requestInfo
        );

        return {
          sourceId: documentFileStoreId,
          fileStoreId: mergedFilingDocumentFileStoreId,
          sortParam: index + 1,
          createPDF: false,
          content: "initialFiling",
        };
      } else {
        return {
          sourceId: documentFileStoreId,
          fileStoreId: documentFileStoreId,
          sortParam: index + 1,
          createPDF: false,
          content: "initialFiling",
        };
      }
    })
  );

  // update index

  const filingsIndexSection = indexCopy.sections.find(
    (section) => section.name === "filings"
  );
  filingsIndexSection.lineItems = filingsLineItems.filter(Boolean);

  // update affidavits
  const affidavitsSection = filterCaseBundleBySection(
    caseBundleMaster,
    "affidavit"
  );
  const sortedAffidavitsSection = [...affidavitsSection].sort(
    (secA, secB) => secA.order - secB.order
  );

  const affidavitsLineItems = await Promise.all(
    sortedAffidavitsSection.map(async (section, index) => {
      const documentFileStoreId = courtCase.documents.find(
        (doc) => doc.documentType === section.docType
      )?.fileStore;
      if (!documentFileStoreId) {
        return null;
      }
      if (section.docketPageRequired) {
        const complainant =
          courtCase?.additionalDetails?.complainantDetails?.formdata?.[0]?.data;
        const docketComplainantName = [
          complainant?.firstName,
          complainant?.middleName,
          complainant?.lastName,
        ]
          .filter(Boolean)
          .join(" ");
        const docketNameOfAdvocate = courtCase.representatives?.find((adv) =>
          adv.representing?.any(
            (party) => party.partyType === "complainant.primary"
          )
        )?.additionalDetails?.advocateName;

        const mergedFilingDocumentFileStoreId = await applyDocketToDocument(
          documentFileStoreId,
          {
            docketApplicationType: section.name,
            docketCounselFor: "COMPLAINANT",
            docketNameOfFiling: docketComplainantName,
            docketNameOfAdvocate: docketNameOfAdvocate || docketComplainantName,
            docketDateOfSubmission: new Date(
              courtCase.registrationDate
            ).toLocaleDateString("en-IN"),
          },
          courtCase,
          tenantId,
          requestInfo
        );

        return {
          sourceId: documentFileStoreId,
          fileStoreId: mergedFilingDocumentFileStoreId,
          sortParam: index + 1,
          createPDF: false,
          content: "initialFiling",
        };
      } else {
        return {
          sourceId: documentFileStoreId,
          fileStoreId: documentFileStoreId,
          sortParam: index + 1,
          createPDF: false,
          content: "initialFiling",
        };
      }
    })
  );

  // update index

  const affidavitsIndexSection = indexCopy.sections.find(
    (section) => section.name === "affidavit"
  );
  affidavitsIndexSection.lineItems = affidavitsLineItems.filter(Boolean);

  // update vakalatnamas
  const vakalatnamaSection = filterCaseBundleBySection(
    caseBundleMaster,
    "affidavit"
  );

  if (vakalatnamaSection && Array.isArray(courtCase.representatives)) {
    const vakalats = courtCase.representatives
      .map((representative) => {
        const representation = representative.representing[0];
        const fileStoreId =
          representative?.additionalDetails?.document?.[0]
            ?.vakalatnamaFileUpload?.fileStore;
        if (!fileStoreId) {
          return null;
        }
        return {
          isActive: representation.isActive,
          partyType: representation.partyType,
          fileStoreId: fileStoreId,
          representingFullName: representation.additionalDetails.fullName,
          advocateFullName: representative.additionalDetails.advocateName,
          dateOfAddition: representative.auditDetails.createdTime,
        };
      })
      .filter(Boolean);

    vakalats.sort((vak1, vak2) => {
      if (vak1.isActive && vak2.isActive) {
        if (vak1.partyType.includes("complainant")) {
          return -1;
        } else {
          return 1;
        }
      }
      return vak2.isActive - vak1.isActive;
    });

    const section = vakalatnamaSection[0];

    const vakalatLineItems = await Promise.all(
      vakalats.map(async (vakalat) => {
        if (section.docketPageRequired) {
          const mergedVakalatDocumentFileStoreId = await applyDocketToDocument(
            vakalat.fileStoreId,
            {
              docketApplicationType: section.name,
              docketCounselFor: vakalat.partyType.includes("complainant")
                ? "COMPLAINANT"
                : "RESPONDENT",
              docketNameOfFiling: vakalat.representingFullName,
              docketNameOfAdvocate: vakalat.advocateFullName,
              docketDateOfSubmission: new Date(
                vakalat.dateOfAddition
              ).toLocaleDateString("en-IN"),
            },
            courtCase,
            tenantId,
            requestInfo
          );
          return {
            sourceId: vakalat.fileStoreId,
            fileStoreId: mergedVakalatDocumentFileStoreId,
            createPDF: false,
            sortParam: null,
            content: "vakalat",
          };
        } else {
          return {
            sourceId: vakalat.fileStoreId,
            fileStoreId: vakalat.fileStoreId,
            createPDF: false,
            sortParam: null,
            content: "vakalat",
          };
        }
      })
    );

    // update index

    const vakalatsIndexSection = indexCopy.sections.find(
      (section) => section.name === "vakalat"
    );
    vakalatsIndexSection.lineItems = vakalatLineItems.filter(Boolean);
  }

  indexCopy.isRegistered = true;

  return indexCopy;
}

// Main Function
async function processCaseBundle(tenantId, caseId, index, state, requestInfo) {
  console.log(`Processing caseId: ${caseId}, state: ${state}`);

  // let fileStoreIds = [];

  let updatedIndex;

  switch (state.toUpperCase()) {
    case "PENDING_ADMISSION_HEARING": {
      updatedIndex = await processPendingAdmissionCase({
        tenantId,
        caseId,
        index,
        requestInfo,
        state,
      });

      //for (const section of index.sections) {
      //  if (["filings", "affidavit", "vakalat"].includes(section.name)) {
      //    const sectionFileStoreIds = await processSectionDocuments(
      //      tenantId,
      //      section,
      //      requestInfo
      //    );
      //    fileStoreIds.push(...sectionFileStoreIds);
      //  }
      //}
      break;
    }

    case "CASE_ADMITTED": {
      updatedIndex = cloneDeep(index);
      updatedIndex.contentLastModified = Date.now();
      break;
    }

    case "CASE_REASSIGNED": {
      updatedIndex = cloneDeep(index);
      updatedIndex.isRegistered = false;
      break;
    }

    default:
      console.error(`Unknown state: ${state}`);
      throw new Error(`Unknown state: ${state}`);
  }

  return updatedIndex;

  // console.log("Merging PDFs...");
  // const mergedPdf = await mergePdfs(fileStoreIds, tenantId);

  // console.log("Uploading merged PDF...");
  // const mergedPdfBytes = await mergedPdf.save();
  // const mergedFilePath = path.join(
  //   TEMP_FILES_DIR,
  //   `merged-bundle-${Date.now()}.pdf`
  // );
  // fs.writeFileSync(mergedFilePath, mergedPdfBytes);

  // const finalFileUpload = await create_file(
  //   mergedFilePath,
  //   tenantId,
  //   "case-bundle",
  //   "application/pdf"
  // );
  // fs.unlinkSync(mergedFilePath);

  // index.fileStoreId = finalFileUpload.data.files[0].fileStoreId;
  // index.contentLastModified = Math.floor(Date.now() / 1000);

  // return index;
}

module.exports = processCaseBundle;
