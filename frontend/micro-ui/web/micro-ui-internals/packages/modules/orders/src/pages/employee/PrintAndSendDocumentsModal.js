import React, { useMemo, useState } from "react";
import { Modal, CardLabel } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { printAndSendDocumentsConfig } from "./../../configs/EpostFormConfigs";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { Urls } from "../../hooks/services/Urls";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";
import DocumentPrintComponent from "../../components/DocumentPrintComponent";

const PrintAndSendDocumentsModal = ({ onClose, stepper, setStepper, rowData, form, setForm, fileStoreId }) => {
  const [tempForm, setTempForm] = useState(form);
  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const config = printAndSendDocumentsConfig;

  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&&fileStoreId=${fileStoreId}`;

  const Close = () => (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFFFF">
      <path d="M0 0h24v24H0V0z" fill="none" />
      <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
    </svg>
  );

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={props?.isMobileView ? { padding: 5 } : null}>
        <div className={"icon-bg-secondary"} style={{ backgroundColor: "#505A5F" }}>
          {" "}
          <Close />{" "}
        </div>
      </div>
    );
  };
  const clickableTextStyle = {
    color: "#007E7E",
    textDecoration: "underline",
    cursor: "pointer",
    paddingRight: "1rem",
  };

  const onFormValueChange = (setValue, formData, formState) => {
    if (JSON.stringify(tempForm) !== JSON.stringify(formData)) {
      setTempForm(formData);
    }
  };

  const infos = useMemo(() => {
    return [
      { key: "E-post fees", value: "Rs.575" },
      { key: "Received on", value: "04/07/2024, 12:56" },
    ];
  }, []);

  const links = useMemo(() => {
    return [{ text: "View Details", link: "" }];
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
        fileName: "Receiver’s Address",
        fileStoreId: "a236b4e0-5ddd-4ece-9ba3-4d02edf15adc",
        documentName: "file_example_JPG_100kB.jpg",
        documentType: "image/jpeg",
      },
    ];
  }, []);

  return (
    <Modal
      popupStyles={{
        height: "auto",
        maxHeight: "700px",
        width: "700px",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        padding: "20px",
      }}
      headerBarMain={<h1 className="heading-m">{t("Print & Send Documents")}</h1>}
      headerBarEnd={<CloseBtn onClick={onClose} />}
      actionCancelLabel={t("Skip")}
      actionCancelOnSubmit={() => setStepper(stepper + 1)}
      actionSaveLabel={t("Save & Proceed")}
      actionSaveOnSubmit={() => {
        setForm(tempForm);
        setStepper(stepper + 1);
      }}
      isDisabled={rowData.original.deliveryStatus === "DELIVERED" || rowData.original.deliveryStatus === "NOT_DELIVERED" ? true : false}
    >
      <div className="epost-print-and-send">
        <ApplicationInfoComponent infos={infos} links={links} />
        <DocumentPrintComponent documents={documents} />
        <hr className="horizontal-line" />
        <h3 className="speed-post-header">{t("Speed Post Details")}</h3>
        <FormComposerV2
          className="form-print-and-summon"
          key={"printAndSendDocuments"}
          config={config}
          onFormValueChange={onFormValueChange}
          defaultValues={{
            barCode: `${rowData?.original?.trackingNumber || ""}`,
            dateofBooking: `${rowData?.original?.bookingDate}`,
          }}
        />
      </div>
    </Modal>
  );
};
export default PrintAndSendDocumentsModal;
