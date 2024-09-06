import React, { useEffect, useMemo, useState } from "react";
import { Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { SummonsTabsConfig } from "../../configs/SuumonsConfig";
import { useTranslation } from "react-i18next";
import DocumentModal from "../../components/DocumentModal";
import PrintAndSendDocumentComponent from "../../components/Print&SendDocuments";
import DocumentViewerWithComment from "../../components/DocumentViewerWithComment";
import AddSignatureComponent from "../../components/AddSignatureComponent";
import CustomStepperSuccess from "../../components/CustomStepperSuccess";
import UpdateDeliveryStatusComponent from "../../components/UpdateDeliveryStatusComponent";

const defaultSearchValues = {
  eprocess: "",
  caseId: "",
};

const ReviewSummonsNoticeAndWarrant = () => {
  const { t } = useTranslation();
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues);
  const [config, setConfig] = useState(SummonsTabsConfig?.SummonsTabsConfig?.[0]);
  const [showActionModal, setShowActionModal] = useState(false);
  const [isSigned, setIsSigned] = useState(false);
  const [actionModalType, setActionModalType] = useState("");
  const [isDisabled, setIsDisabled] = useState(true);

  const [tabData, setTabData] = useState(
    SummonsTabsConfig?.SummonsTabsConfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  );

  const handleOpen = (props) => {
    //change status to signed or unsigned
  };

  const handleSubmitButtonDisable = (disable) => {
    console.log("disable :>> ", disable);
    setIsDisabled(disable);
  };

  const handleClose = () => {
    setShowActionModal(false);
  };
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(SummonsTabsConfig?.SummonsTabsConfig?.[n]); // as per tab number filtering the config
  };

  const infos = useMemo(() => {
    return [
      { key: "Issued to", value: "Vikram Singh" },
      { key: "Issued Date", value: "23/04/2024" },
      { key: "Next Hearing Date", value: "04/07/2024" },
      { key: "Amount Paid", value: "Rs. 15" },
      { key: "Channel Details", value: "Physical Post" },
    ];
  }, []);

  const links = useMemo(() => {
    return [{ text: "View order", link: "" }];
  }, []);

  const documents = useMemo(() => {
    return [
      {
        fileName: "Summons Document",
        fileStoreId: "a236b4e0-5ddd-4ece-9ba3-4d02edf15adc",
        documentName: "file_example_JPG_100kB.jpg",
        documentType: "image/jpeg",
      },
      {
        fileName: "Vakalatnama Document",
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

  const unsignedModalConfig = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: "Review Document: Summons Document" },
      actionSaveLabel: "E-sign",
      isStepperModal: true,
      actionSaveOnSubmit: () => {},
      steps: [
        {
          type: "document",
          modalBody: <DocumentViewerWithComment infos={infos} documents={documents} links={links} />,
          // modalBody: <UpdateDeliveryStatusComponent t={t} infos={infos} />,
          actionSaveOnSubmit: () => {},
        },
        {
          heading: { label: "Add Signature (1)" },
          actionSaveLabel: "Send Email",
          actionCancelLabel: "Back",
          modalBody: <AddSignatureComponent t={t} isSigned={isSigned} handleSigned={() => setIsSigned(true)} />,
          isDisabled: isSigned ? false : true,
        },
        {
          type: "success",
          hideSubmit: true,
          modalBody: <CustomStepperSuccess closeButtonAction={handleClose} t={t} submissionData={submissionData} documents={documents} />,
        },
      ],
    };
  }, [documents, infos, isSigned, links, submissionData, t]);

  const signedModalConfig = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: "Print & Send Documents" },
      actionSaveLabel: "Mark As Sent",
      isStepperModal: false,
      modalBody: <PrintAndSendDocumentComponent infos={infos} documents={documents} links={links} t={t} />,
      actionSaveOnSubmit: handleClose,
    };
  }, [documents, infos, links, t]);

  const sentModalConfig = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: "Print & Send Documents" },
      actionSaveLabel: "Mark As Sent",
      isStepperModal: false,
      modalBody: <UpdateDeliveryStatusComponent infos={infos} links={links} t={t} handleSubmitButtonDisable={handleSubmitButtonDisable} />,
      actionSaveOnSubmit: handleClose,
      isDisabled: isDisabled,
    };
  }, [infos, isDisabled, links, t]);

  return (
    <div className="review-summon-warrant">
      <div className="header-wraper">
        <Header>{t("REVIEW_SUMMON_NOTICE_WARRANTS_TEXT")}</Header>
      </div>

      <div className="inbox-search-wrapper pucar-home home-view">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          configs={config}
          defaultValues={defaultValues}
          showTab={true}
          tabData={tabData}
          onTabChange={onTabChange}
          additionalConfig={{
            resultsTable: {
              onClickRow: (props) => {
                setActionModalType("signed");
                setShowActionModal(true);
              },
            },
          }}
        ></InboxSearchComposer>
        {showActionModal && (
          <DocumentModal
            config={config?.label === "Pending" ? (actionModalType !== "signed" ? signedModalConfig : unsignedModalConfig) : sentModalConfig}
          />
        )}
      </div>
    </div>
  );
};

export default ReviewSummonsNoticeAndWarrant;
