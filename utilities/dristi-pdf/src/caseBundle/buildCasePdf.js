const { search_pdf, create_file, search_mdms } = require("../api");
const { PDFDocument, rgb } = require("pdf-lib");
const axios = require("axios");
const fs = require("fs");
const path = require("path");

/**
 * @typedef CaseBundleMaster
 * @type {object}
 * @property {string} Items
 * @property {string} docketpagerequired
 * @property {string} doctype
 * @property {string} id
 * @property {string} isactive
 * @property {string} name
 * @property {string} section
 * @property {string} sorton
 */

async function buildCasePdf(caseNumber, index, requestInfo, tenantId) {
  try {
    /**
     * @type {CaseBundleMaster[]}
     */
    const caseBundleDesign = await search_mdms(
      null,
      "CaseManagement.case_bundle_master",
      tenantId,
      requestInfo
    ).then((mdmsRes) => {
      return mdmsRes.data.mdms.filter((x) => x.isActive).map((x) => x.data);
    });

    if (!caseBundleDesign || caseBundleDesign.length === 0) {
      throw new Error("No case bundle design found in MDMS.");
    }

    // Create a new PDF document to merge all sections
    const mergedPdf = await PDFDocument.create();

    // Iterate through sections in the index
    for (const section of index.sections) {
      if (!section || !section.name) {
        console.warn("Skipping section with no name");
        continue;
      }

      const sectionConfig = caseBundleDesign.find(
        (design) => design.name === section.name && design.isactive
      );

      if (!sectionConfig) {
        console.warn(`Section '${section.name}' is not enabled in the design`);
        continue;
      }

      if (!section.lineItems || section.lineItems.length === 0) {
        console.warn(`Section '${section.name}' has no line items.`);
        continue;
      }

      // Process each line item
      for (const item of section.lineItems) {
        if (!item || !item.fileStoreId || !item.content) {
          console.warn("Skipping invalid line item");
          continue;
        }

        try {
          // Fetch PDF from fileStoreId
          const pdfResponse = await search_pdf(
            tenantId,
            item.fileStoreId,
            requestInfo
          );
          if (
            pdfResponse.status === 200 &&
            pdfResponse.data[item.fileStoreId]
          ) {
            const pdfUrl = pdfResponse.data[item.fileStoreId];
            const pdfFetchResponse = await axios.get(pdfUrl, {
              responseType: "arraybuffer",
            });
            const pdfData = pdfFetchResponse.data;

            const itemPdf = await PDFDocument.load(pdfData);

            // Add case number to each page
            const pages = itemPdf.getPages();
            for (const page of pages) {
              const { width, height } = page.getSize();
              page.drawText(`Case Number: ${caseNumber}`, {
                x: width / 2 - 50,
                y: height - 30,
                size: 12,
                color: rgb(0, 0, 0),
              });
            }

            // Merge the fetched PDF pages
            const copiedPages = await mergedPdf.copyPages(
              itemPdf,
              itemPdf.getPageIndices()
            );
            copiedPages.forEach((page) => mergedPdf.addPage(page));

            console.log(
              `Successfully appended pages from fileStoreId: ${item.fileStoreId}`
            );
          } else {
            console.error(
              `Failed to fetch PDF for fileStoreId: ${item.fileStoreId}`
            );
          }
        } catch (error) {
          console.error(
            `Error processing fileStoreId '${item.fileStoreId}': ${error.message}`
          );
        }
      }
    }

    // Add page numbers to the final merged PDF
    const mergedPages = mergedPdf.getPages();
    mergedPages.forEach((page, index) => {
      const { width } = page.getSize();
      const pageNumber = index + 1;
      page.drawText(`Page ${pageNumber}`, {
        x: width - 100,
        y: 30,
        size: 10,
        color: rgb(0, 0, 0),
      });
    });

    // Create a temporary directory and unique file name
    const directoryPath =
      process.env.TEMP_FILES_DIR || path.join(__dirname, "../case-bundles");
    if (!fs.existsSync(directoryPath)) {
      fs.mkdirSync(directoryPath, { recursive: true });
    }

    const tempFileName = `bundle-${Date.now()}-${Math.random()
      .toString(36)
      .slice(2)}.pdf`;
    const filePath = path.join(directoryPath, tempFileName);

    try {
      // Save the merged PDF
      const pdfBytes = await mergedPdf.save();
      fs.writeFileSync(filePath, pdfBytes);

      // Upload the merged PDF and update the index
      const fileStoreResponse = await create_file(
        filePath,
        tenantId,
        "case-bundle",
        "application/pdf"
      );
      const fileStoreId = fileStoreResponse?.data?.files?.[0].fileStoreId;

      index.fileStoreId = fileStoreId;
      index.pdfCreatedDate = Date.now();

      console.log(`PDF created and stored with fileStoreId: ${fileStoreId}`);

      return { ...index, pageCount: mergedPages.length };
    } finally {
      // Ensure cleanup of the temporary file
      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath);
      }
    }
  } catch (error) {
    console.error("Error processing case bundle:", error.message);
    throw error;
  }
}

module.exports = buildCasePdf;
