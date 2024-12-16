const {
  search_pdf_v2,
  search_case_v2,
  create_pdf_v2,
  create_file_v2,
  search_mdms,
} = require("../api"); // Removed create_pdf import
const fs = require("fs");
const path = require("path");
const { PDFDocument } = require("pdf-lib");
const config = require("../config");
const cloneDeep = require("lodash.clonedeep");
const sharp = require("sharp");
const { logger } = require("../logger");

const TEMP_FILES_DIR = path.join(__dirname, "../temp");

if (!fs.existsSync(TEMP_FILES_DIR)) {
  fs.mkdirSync(TEMP_FILES_DIR);
}

const A4_WIDTH = 595.28; // A4 width in points
const A4_HEIGHT = 841.89; // A4 height in points

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
 * @param {import("./buildCasePdf").CaseBundleMaster[]} caseBundleMasterData
 * @param {string} sectionName
 * @returns
 */
function filterCaseBundleBySection(caseBundleMasterData, sectionName) {
  return caseBundleMasterData.filter(
    (indexItem) =>
      indexItem.name === sectionName && indexItem.isactive === "yes"
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
          respondent.respondentFirstName,
          respondent.respondentMiddleName,
          respondent.respondentLastName,
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
  /**
   * @type {import("./buildCasePdf").CaseBundleMaster[]}
   */
  const caseBundleMaster = await search_mdms(
    null,
    "CaseManagement.case_bundle_master",
    tenantId,
    requestInfo
  ).then((mdmsRes) => {
    return mdmsRes.data.mdms.filter((x) => x.isActive).map((x) => x.data);
  });

  const caseResponse = await search_case_v2(
    [
      {
        caseId,
      },
    ],
    tenantId,
    requestInfo
  );
  logger.info("recd case response", JSON.stringify(caseResponse?.data));
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
    const coverYear = (
      courtCase?.filingDate ? new Date(courtCase?.filingDate) : new Date()
    )?.getFullYear();
    const data = {
      Data: [{ coverCaseName, coverCaseType, coverCaseNumber, coverYear }],
    };
    const caseCoverPdfResponse = await create_pdf_v2(
      tenantId,
      "cover-page-pdf",
      data,
      { RequestInfo: requestInfo }
    );
    const caseCoverDoc = await PDFDocument.load(
      caseCoverPdfResponse.data
    ).catch((e) => {
      logger.error(JSON.stringify(e));
      throw e;
    });

    const titlepageFileStoreId = await persistPDF(
      caseCoverDoc,
      tenantId,
      requestInfo
    ).catch((e) => {
      logger.error(JSON.stringify(e));
      throw e;
    });

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

  console.debug(caseBundleMaster);
  const complaintSection = filterCaseBundleBySection(
    caseBundleMaster,
    "complaint"
  )[0];

  const complaintFileStoreId = courtCase.documents.find(
    (doc) => doc.documentType === "case.complaint.signed"
    //(doc) => doc.documentType === "case.cheque"
  )?.fileStore;
  if (!complaintFileStoreId) {
    throw new Error("no case complaint");
  }

  const { data: stream, headers } = await search_pdf_v2(
    tenantId,
    complaintFileStoreId,
    requestInfo
  ).catch((e) => {
    logger.error(JSON.stringify(e));
    throw e;
  });
  const mimeType = headers["content-type"];
  let pdfDoc;
  if (mimeType === "application/pdf") {
    pdfDoc = await PDFDocument.load(stream);
  } else if (["image/jpeg", "image/png", "image/jpg"].includes(mimeType)) {
    pdfDoc = await PDFDocument.create();
    let img;
    if (mimeType === "image/png") {
      img = await pdfDoc.embedPng(stream);
    } else {
      const repairedImage = await fixJpg(stream);
      img = await pdfDoc.embedJpg(repairedImage);
    }

    const { width, height } = img.scale(1);
    const scale = Math.min(A4_WIDTH / width, A4_HEIGHT / height);
    const xOffset = (A4_WIDTH - width * scale) / 2;
    const yOffset = (A4_HEIGHT - height * scale) / 2;
    const page = pdfDoc.addPage([A4_WIDTH, A4_HEIGHT]);
    page.drawImage(img, {
      x: xOffset,
      y: yOffset,
      width: width * scale,
      height: height * scale,
    });
  }

  let caseBundlePdfDoc = await PDFDocument.create();

  const copiedPages = await caseBundlePdfDoc.copyPages(
    pdfDoc,
    pdfDoc.getPageIndices()
  );
  copiedPages.forEach((page) => caseBundlePdfDoc.addPage(page));

  if (complaintSection.docketpagerequired === "yes") {
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
    ).catch((e) => {
      logger.error(JSON.stringify(e));
      throw e;
    });

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
  ).catch((e) => {
    logger.error(JSON.stringify(e));
    throw e;
  });

  // update index

  const complaintIndexSection = indexCopy.sections.find(
    (section) => section.name === "complaint"
  );
  complaintIndexSection.lineItems = complaintIndexSection.lineItems || [];
  complaintIndexSection.lineItems[0] = {
    ...complaintIndexSection.lineItems[0],
    createPDF: false,
    sourceId: complaintFileStoreId,
    fileStoreId: complaintNewFileStoreId,
    content: "complaint",
    sortParam: null,
  };

  // move to filings section of case complaint
  logger.info(caseBundleMaster);
  const filingsSection = filterCaseBundleBySection(caseBundleMaster, "filings");
  const sortedFilingSection = [...filingsSection].sort(
    (secA, secB) => secA.sorton - secB.sorton
  );
  const filingsLineItems = await Promise.all(
    sortedFilingSection.map(async (section, index) => {
      const documentFileStoreId = courtCase.documents.find(
        (doc) => doc.documentType === section.doctype
      )?.fileStore;
      if (!documentFileStoreId) {
        return null;
      }
      if (section.docketpagerequired === "yes") {
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
    (secA, secB) => secA.sorton - secB.sorton
  );

  const affidavitsLineItems = await Promise.all(
    sortedAffidavitsSection.map(async (section, index) => {
      const documentFileStoreId = courtCase.documents.find(
        (doc) => doc.documentType === section.doctype
      )?.fileStore;
      if (!documentFileStoreId) {
        return null;
      }
      if (section.docketpagerequired === "yes") {
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
          adv.representing?.some(
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
        if (section.docketpagerequired === "yes") {
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
  indexCopy.contentLastModified = Date.now();

  return indexCopy;
}

// Main Function
async function processCaseBundle(tenantId, caseId, index, state, requestInfo) {
  logger.info(`Processing caseId: ${caseId}, state: ${state}`);

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
      logger.error(`Unknown state: ${state}`);
      throw new Error(`Unknown state: ${state}`);
  }

  return updatedIndex;
}

module.exports = processCaseBundle;
