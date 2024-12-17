import axios from "axios";

const downloadPdfWithLink = async (url, fileName) => {
  if (!Boolean(url)) {
    return;
  }

  try {
    const baseUrl = window?.globalConfigs?.getConfig("PUCAR_FILESTORE_BLOB") || "https://pucarfilestore.blob.core.windows.net";
    const response = await axios.get(baseUrl + url, {
      responseType: "blob",
    });
    const mimeType = response.data.type;
    const extension = mimeType.split("/")[1];

    const blob = new Blob([response.data], { type: mimeType });
    const blobUrl = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = blobUrl;
    link.download = `${fileName}.${extension}`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(blobUrl);
  } catch (error) {
    console.error("Error downloading the PDF:", error);
  }
};

export default downloadPdfWithLink;
