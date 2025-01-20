const axios = require("axios");
const config = require("../config/config");

/**
 * Generates a PDF document by calling Egov PDF service.
 *
 * @param {Object} data - The data to use to generate the PDF.
 * @returns {Promise<Buffer>} The generated PDF document.
 */
exports.generatePDF = async (data) => {
  const response = await axios.post(
    `${config.pdfServiceUrl}/pdf-service/v1/_createnosave?key=case&tenantId=kl`,
    data,
    {
      responseType: "arraybuffer",
    }
  );
  return response.data;
};

exports.generateComplaintPDF = async (data) => {
  const response = await axios.post(
    `${config.pdfServiceUrl}/pdf-service/v1/_createnosave?key=complainant-case-efiling&tenantId=kl`,
    data,
    {
      responseType: "arraybuffer",
    }
  );
  return response.data;
};
