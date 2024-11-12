const express = require("express");
const router = express.Router();
const multer = require("multer");
const upload = multer({ dest: "uploads/" });
const { PDFDocument } = require("pdf-lib");
const fs = require("fs");
const asyncMiddleware = require("../utils/asyncMiddleware");
const buildCasePdf = require("../caseBundle/buildCasePdf");

const A4_WIDTH = 595.28; // A4 width in points
const A4_HEIGHT = 841.89; // A4 height in points


router.post(
  "/case-bundle",
  asyncMiddleware(async (req, res) => {
    const { index, caseNumber, RequestInfo, caseDetails} = req.body;

    // Validate required parameters
    if (!index || !caseNumber || !RequestInfo) {
      return renderError(
        res,
        "Missing required fields: 'index', 'caseNumber', or 'RequestInfo'.",
        400
      );
    }

    try {
      // Call buildCasePdf and get updated index with pageCount
      const result = await buildCasePdf(caseNumber, index, RequestInfo);

      // Extract pageCount and remove it from updatedIndex
      const { pageCount, ...updatedIndex } = result;

      // Send success response with pageCount included but removed from updatedIndex
      res.status(200).json({
        ResponseInfo: RequestInfo,
        index: updatedIndex, // Updated index without pageCount
        pageCount: pageCount, // Page count sent separately
      });
    } catch (error) {
      renderError(
        res,
        "An error occurred while creating the case bundle PDF.",
        500,
        error
      );
    }
  })
);





router.post(
  "/combine-documents",
  upload.array("documents"),
  asyncMiddleware(async function (req, res, next) {
    try {
      const mergedPdf = await PDFDocument.create();

      for (const file of req.files) {
        const mimeType = file.mimetype;
        const fileData = await fs.promises.readFile(file.path);

        if (mimeType === "application/pdf") {
          const pdfDoc = await PDFDocument.load(fileData);
          const pages = await mergedPdf.copyPages(
            pdfDoc,
            pdfDoc.getPageIndices()
          );
          pages.forEach((page) => mergedPdf.addPage(page));
        } else if (
          ["image/jpeg", "image/png", "image/jpg"].includes(mimeType)
        ) {
          const img =
            mimeType === "image/png"
              ? await mergedPdf.embedPng(fileData)
              : await mergedPdf.embedJpg(fileData);
          const { width, height } = img.scale(1);
          const scale = Math.min(A4_WIDTH / width, A4_HEIGHT / height);
          const xOffset = (A4_WIDTH - width * scale) / 2;
          const yOffset = (A4_HEIGHT - height * scale) / 2;
          const page = mergedPdf.addPage([A4_WIDTH, A4_HEIGHT]);
          page.drawImage(img, {
            x: xOffset,
            y: yOffset,
            width: width * scale,
            height: height * scale,
          });
        }
        try {
          await fs.promises.unlink(file.path);
        } catch (err) {
          console.error(`Failed to delete file ${file.path}:`, err);
        }
      }
      const mergedPdfBytes = await mergedPdf.save();
      const finalPdfBuffer = Buffer.from(mergedPdfBytes);
      res.set({
        "Content-Type": "application/pdf",
        "Content-Disposition": "attachment; filename=merged.pdf",
      });
      res.send(finalPdfBuffer);
    } catch (error) {
      console.error("Error during PDF merging:", error?.message);

      res.status(500).json({
        message: "Error creating merged PDF",
        error: error.message,
      });
    }
  })
);

module.exports = router;
