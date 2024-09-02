const axios = require('axios');
const config = require('../config/config');

exports.generatePDF = async (data) => {
    const response = await axios.post(`${config.pdfServiceUrl}/generatePDF`, data, {
        responseType: 'arraybuffer'
    });
    return response.data;
};
