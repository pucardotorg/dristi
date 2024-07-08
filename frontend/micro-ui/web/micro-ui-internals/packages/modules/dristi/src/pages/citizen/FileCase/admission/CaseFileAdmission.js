import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";
import SendCaseBack from "./SendCaseBack";

function CaseFileAdmission({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const [showModal, setShowModal] = useState(false);
  const [modalData, setModalData] = useState(null);
  const [submitModalInfo, setSubmitModalInfo] = useState(null);
  const onSubmit = () => {
    setSubmitModalInfo({
      header: "The case file has been admitted successfully.",
      subHeader: "CASE_UPDATES_SENT_VIA_SMS_MESSAGE.",
      caseInfo: [
        {
          key: "Case Number",
          value: "FSM-2019-04-23-898898",
        },
        {
          key: "Case Category",
          value: "Criminal",
        },
        {
          key: "Case Type",
          value: "NIA S138",
        },
        {
          key: "Court Name",
          value: "Kerala City Criminal Court",
        },
        {
          key: "Submitted on",
          value: "23 Jan 2024",
        },
      ],
      backButtonText: "Back to Home",
      nextButtonText: "Schedule next hearing",
      isArrow: false,
      showTable: true,
    });

    // setModalInfo({ type: "schedule", page: "0" });
    setShowModal(true);
  };
  const onSaveDraft = () => {
    setSubmitModalInfo({
      header: "The case file has been sent back for correction",
      subHeader: "CASE_UPDATES_SENT_VIA_SMS_MESSAGE.",
      caseInfo: {
        key: "Case File Number",
        value: "KA08293928392",
      },
      backButtonText: "Back to Home",
      nextButtonText: "Next Case",
      isArrow: true,
    });
    setShowModal(true);
  };
  const onSendBack = () => {
    setSubmitModalInfo({
      header: "The case file has been sent back for correction",
      subHeader: "CASE_UPDATES_SENT_VIA_SMS_MESSAGE.",
      caseInfo: [{ key: "Case File Number", value: "KA08293928392" }],
      backButtonText: "Back to Home",
      nextButtonText: "Next Case",
      isArrow: true,
      showCopytext: true,
    });
    setShowModal(true);
  };
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata.data)) {
      setFormdata((prev) => {
        return { ...prev, data: formData };
      });
    }
  };
  const closeToast = () => {
    setShowErrorToast(false);
  };

  const handleCloseModal = () => {
    setModalData(null);
    setSubmitModalInfo(null);
  };
  // {
  //   modalInfo.type === "Schedule" && modalInfo.page === 0 && <Modal1 modalData={modalData} setModalData={setModalData} />;
  // }
  if (showModal) {
    return <SendCaseBack t={t} setShowModal={setShowModal} setSubmitModalInfo={setSubmitModalInfo} submitModalInfo={submitModalInfo}></SendCaseBack>;
  }

  return (
    <div className="file-case">
      <div className="file-case-side-stepper">
        <div className="file-case-select-form-section">
          <div className="accordion-wrapper">Litigent Details</div>
          <div className="accordion-wrapper">Case Specific Details</div>
          <div className="accordion-wrapper">Additional Details</div>
        </div>
      </div>
      <div className="file-case-form-section">
        <div className="employee-card-wrapper">
          <div className="header-content">
            <div className="header-details">
              <Header>{t("Review Case")}</Header>
              <div className="header-icon" onClick={() => {}}>
                <CustomArrowDownIcon />
              </div>
            </div>
          </div>
          <FormComposerV2
            label={t("CS_ADMIT_CASE")}
            config={reviewCaseFileFormConfig}
            onSubmit={onSubmit}
            onSecondayActionClick={onSaveDraft}
            defaultValues={{}}
            onFormValueChange={onFormValueChange}
            cardStyle={{ minWidth: "100%" }}
            isDisabled={isDisabled}
            cardClassName={`e-filing-card-form-style`}
            secondaryLabel={t("CS_SCHEDULE_ADMISSION_HEARING")}
            showSecondaryLabel={true}
            actionClassName="admission-action-buttons"
            showSkip={"FDSJKLDFSJL"}
            onSkip={onSendBack}
          />
          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
    </div>
  );
}

export default CaseFileAdmission;
