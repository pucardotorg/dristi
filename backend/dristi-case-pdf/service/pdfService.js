const axios = require('axios');
const config = require('../config/config');

exports.generatePDF = async (data) => {
    const response = await axios.post(`${config.pdfServiceUrl}/pdf-service/v1/_createnosave?key=case&tenantId=kl`, data, {
        responseType: 'arraybuffer'
    });
    return response.data;
};