const express = require("express");
const router = express.Router();
const caseController = require("../controller/caseController");

router.post("/v1/generateCasePdf", caseController.generateCasePdf);
router.post("/v1/fetchCaseComplaintPdf", caseController.caseComplaintPdf);

module.exports = router;
