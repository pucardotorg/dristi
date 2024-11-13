import { Request } from "@egovernments/digit-ui-libraries";
import isEmpty from "lodash/isEmpty";
import axios from "axios";

export const ServiceRequest = async ({
  serviceName,
  method = "POST",
  url,
  data = {},
  headers = {},
  useCache = false,
  params = {},
  auth,
  reqTimestamp,
  userService,
}) => {
  const preHookName = `${serviceName}Pre`;
  const postHookName = `${serviceName}Post`;

  let reqParams = params;
  let reqData = data;
  if (window[preHookName] && typeof window[preHookName] === "function") {
    let preHookRes = await window[preHookName]({ params, data });
    reqParams = preHookRes.params;
    reqData = preHookRes.data;
  }
  const resData = await Request({ method, url, data: reqData, headers, useCache, params: reqParams, auth, userService, reqTimestamp });

  if (window[postHookName] && typeof window[postHookName] === "function") {
    return await window[postHookName](resData);
  }
  return resData;
};

export function generateUUID() {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0;
    const v = c === "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

const formatWithSuffix = (day) => {
  if (day > 3 && day < 21) return `${day}th`;
  switch (day % 10) {
    case 1:
      return `${day}st`;
    case 2:
      return `${day}nd`;
    case 3:
      return `${day}rd`;
    default:
      return `${day}th`;
  }
};

// Function to format a date object into "20th June 2024"
export const formatDateInMonth = (date) => {
  const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  const day = formatWithSuffix(date.getDate());
  const month = monthNames[date.getMonth()];
  const year = date.getFullYear();
  return `${day} ${month} ${year}`;
};

export function isEmptyObject(obj) {
  // Check if the object is empty
  if (isEmpty(obj)) {
    return true;
  }

  // Check if all properties are empty
  for (const key in obj) {
    if (Array.isArray(obj[key])) {
      return obj[key].length === 0;
    } else if (typeof obj[key] === "object" && isEmptyObject(obj[key])) {
      continue;
    } else {
      return false;
    }
  }

  return true;
}

export const formatDate = (date) => {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
};

export const getMDMSObj = (mdmsdata = [], codekey, code) => {
  if (!code || !mdmsdata || mdmsdata?.length == 0) {
    return {};
  }
  return mdmsdata?.find((item) => item[codekey] == code) || {};
};

export const getSuffixByBusinessCode = (paymentType = [], businessCode) => {
  return paymentType?.find((data) => data?.businessService?.some((businessService) => businessService?.businessCode === businessCode))?.suffix || "";
};

export const getTaxPeriodByBusinessService = (taxPeriod = [], businessService) => {
  return taxPeriod?.find((data) => data?.service === businessService) || {};
};
export const removeInvalidNameParts = (name) => {
  return name
    ?.split(" ")
    .filter((part) => part && !["undefined", "null"].includes(part.toLowerCase()))
    .join(" ");
};

export const getFilteredPaymentData = (paymentType, paymentData, bill) => {
  const processedPaymentType = paymentType?.toLowerCase()?.includes("application");
  return processedPaymentType ? [{ key: "Total Amount", value: bill?.totalAmount }] : paymentData;
};

export const getTaskType = (businessService) => {
  const normalizedBusinessService = businessService?.trim().toLowerCase();
  switch (normalizedBusinessService) {
    case "task-summons":
      return "SUMMONS";
    case "task-notice":
      return "NOTICE";
    default:
      return "WARRANT";
  }
};

export const extractFeeMedium = (feeName) => {
  const feeMediums = {
    post: "EPOST",
    email: "EMAIL",
    sms: "SMS",
    police: "POLICE",
    rpad: "RPAD",
  };
  return feeMediums?.[feeName?.toLowerCase()] || "";
};

export const getFileByFileStoreId = async (uri) => {
  try {
    const response = await axios.get(uri, {
      responseType: "blob", // To treat the response as a binary Blob
    });
    // Create a file object from the response Blob
    const file = new File([response.data], "downloaded-file.pdf", {
      type: response.data.type || "application/pdf",
    });
    return file;
  } catch (error) {
    console.error("Error fetching file:", error);
    throw error;
  }
};

export const combineMultipleFiles = async (pdfFilesArray, finalFileName = "combined-document.pdf") => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const formData = new FormData();

  const filePromises = pdfFilesArray.map(async (file) => {
    const { fileStore } = file;
    if (fileStore) {
      // ${Urls.FileFetchById} // check-Should use this but it is causing circular dependency, need to relocate Urls later
      const uri = `${window.location.origin}/filestore/v1/files/id?tenantId=${tenantId}&fileStoreId=${fileStore}`;
      const draftFile = await getFileByFileStoreId(uri);
      return draftFile;
    } else {
      return file;
    }
  });

  const allFiles = await Promise.all(filePromises);
  allFiles.forEach((file) => {
    formData.append("documents", file);
  });

  try {
    // ${Urls.CombineDocuments} // check- Should use this but it is causing circular dependency, need to relocate Urls
    const combineDocumentsUrl = `${window.location.origin}/egov-pdf/dristi-pdf/combine-documents`;
    const response = await axios.post(combineDocumentsUrl, formData, {
      headers: {
        // "Content-Type": "multipart/form-data",
      },
      responseType: "blob", // To handle the response as a Blob
    });
    const file = new File([response.data], finalFileName, { type: response.data.type });
    return [file];
  } catch (error) {
    console.error("Error:", error);
    throw error;
  }
};
