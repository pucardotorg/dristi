const express = require('express');
const bodyParser = require('body-parser');
const axios = require('axios'); 
const path = require('path');
const qs = require('qs');

const app = express();
const port = 8080;
const backendUrl = process.env.EXTERNAL_HOST || "http://localhost:8088/sbi-backend/v1/_decryptBrowserResponse";
const redirectUrl = process.env.REDIRECT_URL || 'https://dristi-kerala-dev.pucar.org/digit-ui/citizen/home/sbi-payment-screen';
const pushResponseContextPath = "/sbi-payments";
const successUrlContextPath = "/sbi-payments/success.jsp";
const failUrlContextPath = "/sbi-payments/fail.jsp";

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

  async function callBackendService(backendUrl, data) {
    try {
      const requestInfo = await getRequestInfo();

      const encryptedPayload = typeof data === 'string' ? data : JSON.stringify(data);
  
      const dataToSend = {
        RequestInfo: requestInfo,
        encryptedPayload
      };

      console.log("Data to send:", JSON.stringify(dataToSend, null, 2));

      const backendResponse = await axios.post(`${backendUrl}`, dataToSend, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Basic ZWdvdi11c2VyLWNsaWVudDo="
        }
      });

      console.log("Backend response:", backendResponse.data);
      return backendResponse.data;
    } catch (error) {
      console.error('Error forwarding to backend:', error);
    return {
        TransactionDetails: {
          TransactionStatus: 'error'
        }
      };
    }
  }

  function forwardJspPage(res, redirectUrl, transactionDetails) {
    const queryParams = new URLSearchParams();

    if (transactionDetails.TransactionStatus) {
      queryParams.append('status', transactionDetails.TransactionStatus);
    }
    if (transactionDetails.billId) {
      queryParams.append('billId', transactionDetails.billId);
    }
    if (transactionDetails.businessService) {
      queryParams.append('businessService', transactionDetails.businessService);
    }
    if (transactionDetails.serviceNumber) {
      queryParams.append('serviceNumber', transactionDetails.serviceNumber);
    }

    res.redirect(`${redirectUrl}?${queryParams}`);
  }

  async function getRequestInfo() {
    const url = process.env.DRISTI_URL || "https://dristi-kerala-dev.pucar.org/user/oauth/token?_=1713357247536";
    const data = qs.stringify({
      username: process.env.USERNAME || "sbi-payment-collector",
      password: process.env.PASSWORD || "Dristi@123",
      tenantId: "kl",
      userType: "EMPLOYEE",
      scope: "read",
      grant_type: "password"
    });
  
    const headers = {
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive',
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ZWdvdi11c2VyLWNsaWVudDo='
    };
  
    try {
      const response = await axios.post(url, data, { headers });
      console.log("Response data:", response.data);
  
      const accessToken = response.data.access_token;
      const userInfo = response.data.UserRequest;
  
      const requestInfo = {
        apiId: "Rainmaker",
        msgId: "1723548200333|en_IN",
        authToken: accessToken,
        userInfo: userInfo,
        tenantId: "kl"
      };
  
      return requestInfo;
    } catch (error) {
      console.error('Error fetching Auth token:', error.response ? error.response.data : error.message);
      throw error;
    }
  }

app.post(`${successUrlContextPath}`, async (req, res) => {
  console.log('Request body:', JSON.stringify(req.body));
  try {
    const backendResponse = await callBackendService(backendUrl, req.body.encData);
    const transactionDetails = backendResponse.TransactionDetails;
    forwardJspPage(res, redirectUrl, transactionDetails);
  } catch (error) {
    res.status(500).send('Failed to process payment');
  }
});

app.post(`${failUrlContextPath}`, async (req, res) => {
  console.log('Request body:', JSON.stringify(req.body));
  try {
    const backendResponse = await callBackendService(backendUrl, req.body.encData);
    const transactionDetails = backendResponse.TransactionDetails;
    forwardJspPage(res, redirectUrl, transactionDetails);
  } catch (error) {
    res.status(500).send('Failed to process payment');
  }
});

app.post(`${pushResponseContextPath}`, async (req, res) => {
  console.log('Request body:', JSON.stringify(req.body));
  try {
    await callBackendService(backendUrl, req.body.pushRespData);
    res.status(200).send('Response processed successfully');
  } catch (error) {
    res.status(500).send('Failed to process response');
  }
});


app.listen(port, () => {
  console.log(`sbi payments app listening on port ${port}`);
});
