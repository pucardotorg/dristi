import { useCallback } from "react";
import { Urls } from "../index";

const useDownloadCasePdf = () => {
  const downloadPdf = useCallback((tenantId, fileStoreId) => {
    if (!fileStoreId) {
      return;
    }

    const url = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId}`;
    const link = document.createElement("a");
    link.href = url;
    link.download = true;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }, []);

  return { downloadPdf };
};

export default useDownloadCasePdf;
