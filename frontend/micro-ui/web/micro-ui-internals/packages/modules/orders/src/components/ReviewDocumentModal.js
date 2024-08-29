import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import DocumentModal from "./DocumentModal";
import CustomStepperSuccess from "./CustomStepperSuccess";
import AddSignatureComponent from "./AddSignatureComponent";
import UpdateDeliveryStatusComponent from "./UpdateDeliveryStatusComponent";

const ReviewDocumentModal = ({ handleClose, setOpenSigned, setShowsignatureModal, showActions = true }) => {
  const { t } = useTranslation();

  const [isSigned, setIsSigned] = useState(false);

  const infos = useMemo(() => {
    return [
      { key: "Issued to", value: "Vikram Singh" },
      { key: "Issued Date", value: "23/04/2024" },
      { key: "Next Hearing Date", value: "04/07/2024" },
      { key: "Amount Paid", value: "Rs. 15" },
      { key: "Channel Details", value: "Physical Post" },
    ];
  }, []);

  const documents = useMemo(() => {
    return [
      {
        fileName: "CS_CHEQUE_RETURN_MEMO",
        fileStoreId: "a236b4e0-5ddd-4ece-9ba3-4d02edf15adc",
        documentName: "file_example_JPG_100kB.jpg",
        documentType: "image/jpeg",
      },
      {
        fileName: "CS_CHEQUE_RETURN_MEMO",
        fileStoreId: "a236b4e0-5ddd-4ece-9ba3-4d02edf15adc",
        documentName: "file_example_JPG_100kB.jpg",
        documentType: "image/jpeg",
      },
    ];
  }, []);

  const submissionData = useMemo(() => {
    return [
      { key: "SUBMISSION_DATE", value: "25-08-2001", copyData: false },
      { key: "SUBMISSION_ID", value: "875897348579453457", copyData: true },
    ];
  }, []);

  const handleSigned = () => {
    setIsSigned(true);
  };

  const config = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: "Review Document: Summons Document" },
      actionSaveLabel: "E-sign",
      isStepperModal: true,
      actionSaveOnSubmit: () => {},
      steps: [
        {
          // type: "document",
          // modalBody: <DocumentViewerWithComment infos={infos} documents={documents} />,
          modalBody: <UpdateDeliveryStatusComponent t={t} infos={infos} />,
          actionSaveOnSubmit: () => {},
        },
        {
          heading: { label: "Add Signature (1)" },
          actionSaveLabel: "Send Email",
          actionCancelLabel: "Back",
          modalBody: <AddSignatureComponent t={t} isSigned={isSigned} handleSigned={handleSigned} />,
          isDisabled: isSigned ? false : true,
        },
        {
          type: "success",
          hideSubmit: true,
          modalBody: <CustomStepperSuccess closeButtonAction={handleClose} t={t} submissionData={submissionData} />,
        },
      ],
    };
  }, [documents, handleClose, infos, isSigned, submissionData, t]);

  return <DocumentModal config={config} />;
};
export default ReviewDocumentModal;
