const { search_pdf, create_file } = require("../api");
const { PDFDocument, rgb } = require("pdf-lib");
const axios = require("axios");
const fs = require("fs");
const path = require("path");

// Mock Case Bundle Design
const caseBundleDesignMock = [
  {
    section_name: "Case Cover Page",
    isEnabled: true,
    title: "Case Cover Page",
    hasHeader: false,
    hasFooter: true,
    name: "titlepage",
    doctype: null,
    docketpagerequired: "no",
    sorton: null,
    isactive: "yes",
  },
  {
    section_name: "Case History",
    isEnabled: true,
    title: "Case History",
    hasHeader: false,
    hasFooter: false,
    name: "adiary",
    doctype: null,
    docketpagerequired: "no",
    sorton: null,
    isactive: "no",
  },
  {
    section_name: "Pending Applications",
    isEnabled: true,
    title: "Pending Applications",
    hasHeader: true,
    hasFooter: true,
    name: "pendingapplications",
    doctype: "applicationNumber",
    docketpagerequired: "yes",
    sorton: "applicationNumber",
    isactive: "yes",
  },
  {
    section_name: "Complaint",
    isEnabled: true,
    title: "Complaint",
    hasHeader: false,
    hasFooter: true,
    name: "complaint",
    doctype: null,
    docketpagerequired: "yes",
    sorton: null,
    isactive: "yes",
  },
  {
    section_name: "Affidavit",
    isEnabled: true,
    title: "Affidavit",
    hasHeader: false,
    hasFooter: true,
    name: "affidavit",
    doctype: null,
    docketpagerequired: "yes",
    sorton: null,
    isactive: "yes",
  },
];

async function buildCasePdf(caseNumber, index, requestInfo) {
  try {
    const caseBundleDesign = caseBundleDesignMock;

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
        (design) => design.name === section.name && design.isEnabled
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
          const pdfResponse = await search_pdf(index.tenantId, item.fileStoreId);
          if (pdfResponse.status === 200 && pdfResponse.data[item.fileStoreId]) {
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

            console.log(`Successfully appended pages from fileStoreId: ${item.fileStoreId}`);
          } else {
            console.error(`Failed to fetch PDF for fileStoreId: ${item.fileStoreId}`);
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
    const directoryPath = process.env.TEMP_FILES_DIR || path.join(__dirname, "../case-bundles");
    if (!fs.existsSync(directoryPath)) {
      fs.mkdirSync(directoryPath, { recursive: true });
    }

    const tempFileName = `bundle-${Date.now()}-${Math.random().toString(36).slice(2)}.pdf`;
    const filePath = path.join(directoryPath, tempFileName);

    try {
      // Save the merged PDF
      const pdfBytes = await mergedPdf.save();
      fs.writeFileSync(filePath, pdfBytes);

      // Upload the merged PDF and update the index
      const tenantId = index.tenantId;
      const fileStoreResponse = await create_file(
        filePath,
        tenantId,
        "case-bundle",
        "application/pdf"
      );
      const fileStoreId = fileStoreResponse?.data?.files?.[0].fileStoreId;

      index.fileStoreId = fileStoreId;
      index.contentLastModified = Math.floor(Date.now() / 1000);

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
