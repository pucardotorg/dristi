import React, { useState, useCallback } from "react";

const useDocumentUpload = () => {
  const onDocumentUpload = useCallback(async (fileData, filename, tenantId) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  }, []);

  const uploadDocuments = useCallback(
    async (file, tenantId) => {
      const documentRes = await Promise.all([onDocumentUpload(file?.[0], file?.[0]?.name, tenantId)]);

      return documentRes.map((doc) => doc?.file?.files?.[0]);
    },
    [onDocumentUpload]
  );

  return { uploadDocuments };
};

export default useDocumentUpload;
