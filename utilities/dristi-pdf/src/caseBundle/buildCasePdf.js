const { search_pdf, create_file } = require("../api");
const { PDFDocument, rgb } = require("pdf-lib");
const axios = require("axios");
const fs = require("fs");
const path = require("path");

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
      section_name: "Objections",
      isEnabled: true,
      title: "Objections against Applications",
      hasHeader: true,
      hasFooter: true,
      name: "pendingapplications",
      doctype: "With associated application",
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
      name: "complainant",
      doctype: null,
      docketpagerequired: "yes",
      sorton: null,
      isactive: "yes",
    },
  ];
  

async function buildCasePdf(caseNumber, index, requestInfo) {
  try {

    // Todo- fetch this data from mdms.
    const caseBundleDesign = caseBundleDesignMock;

    if (!caseBundleDesign || caseBundleDesign.length === 0) {
      throw new Error("No case bundle design found in MDMS.");
    }

    const mergedPdf = await PDFDocument.create();

    //Todo- create complaint pdf based on case details. It is already available in the api. include in the params for buildCasePdf above and merge to mergePdf


    //Todo- create cover page pdf function to be implemented.


    for (const section of index.sections) {
      if (!section || !section.name) {
        continue;
      }

      const sectionConfig = caseBundleDesign.find(
        (design) => design.name === section.name && design.isEnabled
      );

      if (!sectionConfig) {
        continue;
      }

      if (!section.lineItems || section.lineItems.length === 0) {
        continue;
      }

      for (const item of section.lineItems) {
        if (!item || !item.fileStoreId || !item.content) {
          continue;
        }

        if (sectionConfig.isEnabled && !item.createPDF) {
          const pdfResponse = await search_pdf(index.tenantId, item.fileStoreId);

          if (pdfResponse.status === 200 && pdfResponse.data[item.fileStoreId]) {
            const pdfUrl = pdfResponse.data[item.fileStoreId];

            try {
              const pdfFetchResponse = await axios.get(pdfUrl, {
                responseType: "arraybuffer",
              });
              const pdfData = pdfFetchResponse.data;

              const itemPdf = await PDFDocument.load(pdfData);

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

              const modifiedPages = await mergedPdf.copyPages(itemPdf, itemPdf.getPageIndices());
              modifiedPages.forEach((page) => mergedPdf.addPage(page));
            } catch (pdfFetchError) {
                console.error(`Failed to fetch PDF for fileStoreId: ${item.fileStoreId}`);
            }
          } else {
            console.error(`Failed to fetch PDF for fileStoreId: ${item.fileStoreId}`);
          }
        }
      }
    }

    const directoryPath = path.join(__dirname, "../case-bundles");
    if (!fs.existsSync(directoryPath)) {
      fs.mkdirSync(directoryPath, { recursive: true });
    }

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

    const pdfBytes = await mergedPdf.save();
    const filePath = path.join(directoryPath, `bundle.pdf`);
    fs.writeFileSync(filePath, pdfBytes);

    const fileContent = fs.readFileSync(filePath);
    const currentDate = Math.floor(Date.now() / 1000);

    const tenantId = index.tenantId;
    const fileStoreResponse = await create_file(filePath, tenantId, "test", "gotcha");
    const fileStoreId = fileStoreResponse?.data?.files?.[0].fileStoreId;

    index.fileStoreId = fileStoreId;
    index.contentLastModified = currentDate;
    const totalPageCount = mergedPdf.getPageCount();

    fs.unlinkSync(filePath);
    return {
      ...index,
      pageCount: totalPageCount,
    };
  } catch (error) {
    console.error("Error processing case bundle:", error.message);
    throw error;
  }
}

module.exports = buildCasePdf;
