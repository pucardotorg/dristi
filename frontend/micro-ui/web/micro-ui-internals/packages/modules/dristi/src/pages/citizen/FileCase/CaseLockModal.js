import { CloseSvg, Modal, CheckBox } from "@egovernments/digit-ui-react-components";

import React, { useMemo, useState } from "react";
import { useHistory } from "react-router-dom";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import { useToast } from "../../../components/Toast/useToast";

const caseLockingMainDiv = {
  padding: "24px",
  display: "flex",
  flexDirection: "column",
  gap: "16px",
};

const caseSubmissionWarningText = {
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 400,
  lineHeight: "21.6px",
  color: "#3D3C3C",
};

const caseLockModalStyle = {
  border: "1px solid #007E7E",
  backgroundColor: "white",
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 700,
  lineHeight: "18.75px",
  textAlign: "center",
  width: "190px",
};

const Heading = (props) => {
  return (
    <h1 className="heading-m" style={{ marginLeft: "47px" }}>
      {props.label}
    </h1>
  );
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

function CaseLockModal({
  t,
  path,
  setShowCaseLockingModal,
  setShowConfirmCaseDetailsModal,
  isAdvocateFilingCase,
  onSubmit,
  createPendingTask,
  setPrevSelected,
  selected,
  caseDetails,
  state,
}) {
  const [submitConfirmed, setSubmitConfirmed] = useState(false);
  const history = useHistory();
  const toast = useToast();
  const userInfo = Digit?.UserService?.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);

  const filingNumber = useMemo(() => {
    return caseDetails?.filingNumber;
  }, [caseDetails]);

  const handleSaveOnSubmit = async () => {
    setShowCaseLockingModal(false);

    const isCaseReassigned = state === CaseWorkflowState.CASE_REASSIGNED;

    // uncomment if confirm caseDetails modal needed and don't want to call update case api
    // if (isAdvocateFilingCase && !isCaseReassigned) {
    //   setShowConfirmCaseDetailsModal(true);
    //   return;
    // }

    const actionType = isCaseReassigned ? "EDIT_CASE" : "SUBMIT_CASE";

    const result = await onSubmit(actionType, true);
    if (result?.error) return;

    try {
      const taskName = isCaseReassigned ? t("PENDING_RE_E_SIGN_FOR_CASE") : t("PENDING_E_SIGN_FOR_CASE");
      const taskStatus = isCaseReassigned ? "PENDING_RE_E-SIGN" : "PENDING_E-SIGN";
      const promises = [...(caseDetails?.litigants || []), ...(caseDetails?.representatives || [])].map((party) =>
        createPendingTask({
          name: taskName,
          status: taskStatus,
          assignee: party?.additionalDetails?.uuid,
        })
      );
      await Promise.all(promises);
      if (isAdvocateFilingCase) {
        history.replace(`/${window?.contextPath}/${userInfoType}/dristi/landing-page`);
      } else {
        history.replace(`${path}/sign-complaint?filingNumber=${filingNumber}`);
      }
    } catch (error) {
      console.error("An error occurred:", error);
      toast.error(t("SOMETHING_WENT_WRONG"));
    }
  };

  const handleCancelOnSubmit = async () => {
    setShowCaseLockingModal(false);

    if (isAdvocateFilingCase) {
      const assignees = Array.isArray(caseDetails?.representatives)
        ? caseDetails?.representatives?.map((advocate) => ({
            uuid: advocate?.additionalDetails?.uuid,
          }))
        : [];

      const isCaseReassigned = state === CaseWorkflowState.CASE_REASSIGNED;
      const actionType = isCaseReassigned ? "EDIT_CASE_ADVOCATE" : "SUBMIT_CASE_ADVOCATE";

      const result = await onSubmit(actionType, true);
      if (result?.error) return;

      const taskName = isCaseReassigned ? t("PENDING_RE_UPLOAD_SIGNATURE_FOR_CASE") : t("PENDING_UPLOAD_SIGNATURE_FOR_CASE");
      const taskStatus = isCaseReassigned ? "PENDING_RE_SIGN" : "PENDING_SIGN";

      try {
        await createPendingTask({
          name: taskName,
          status: taskStatus,
          assignees: assignees,
        });
        history.replace(`${path}/sign-complaint?filingNumber=${filingNumber}`);
      } catch (error) {
        console.error("An error occurred:", error);
        toast.error(t("SOMETHING_WENT_WRONG"));
      }
    }
  };

  return (
    <Modal
      headerBarEnd={
        <CloseBtn
          onClick={() => {
            setPrevSelected(selected);
            setShowCaseLockingModal(false);
          }}
        />
      }
      actionSaveLabel={isAdvocateFilingCase ? t("LITIGANT_WILL_ESIGN") : t("CONFIRM_AND_SIGN")}
      actionSaveOnSubmit={handleSaveOnSubmit}
      actionCancelLabel={isAdvocateFilingCase ? t("UPLOAD_SIGNED_COPY") : t("DOWNLOAD_CS_BACK")}
      actionCancelOnSubmit={handleCancelOnSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={isAdvocateFilingCase ? t("CONFIRM_COMPLAINT_DETAILS") : t("CONFIRM_CASE_DETAILS")} />}
      popmoduleClassName={"case-lock-confirm-modal"}
      style={{ width: "50%", height: "40px" }}
      // textStyle={{ margin: "0px", color: "" }}
      // popupStyles={{ maxWidth: "60%" }}
      popUpStyleMain={{ zIndex: "1000" }}
      isDisabled={!isAdvocateFilingCase && !submitConfirmed}
    >
      <div className="case-locking-main-div" style={caseLockingMainDiv}>
        {isAdvocateFilingCase && (
          <div>
            <p className="case-submission-warning" style={{ ...caseSubmissionWarningText, margin: "10px 0px" }}>
              {t("NO_EDITS_WILL_BE_ALLOWED")}
            </p>
            <p className="case-submission-warning" style={{ ...caseSubmissionWarningText, margin: "10px 0px" }}>
              {t("CONFIRM_HOW_COMPLAINT_WILL_BE_SIGNED")}
            </p>
            <p className="case-submission-warning" style={{ ...caseSubmissionWarningText, margin: "10px 0px" }}>
              {t("E_SIGN_INFORMATION_FOR_BOTH_TEXT")}
            </p>
            <p className="case-submission-warning" style={{ ...caseSubmissionWarningText, margin: "10px 0px" }}>
              {t("IF_YOU WANT_TO_UPLOAD_DOCUMENT_TEXT")}
            </p>
          </div>
        )}
        {!isAdvocateFilingCase && (
          <div>
            <p className="case-submission-warning" style={{ ...caseSubmissionWarningText, margin: "10px 0px" }}>
              {t("CASE_SUBMISSION_WARNING")}
            </p>
            <p className="case-submission-warning" style={{ ...caseSubmissionWarningText, margin: "10px 0px" }}>
              {t("CASE_SUBMISSION_PROCESS_SUBMISSION")} <span style={{ fontWeight: "700" }}>{t("CASE_SUBMISSION_PROCESS_SIGNED")}</span>{" "}
              {t("CASE_SUBMISSION_PROCESS_MOVED")} <span style={{ fontWeight: "700" }}>{t("CASE_SUBMISSION_PROCESS_SCRUTINY")}</span>{" "}
              {t("CASE_SUBMISSION_PROCESS_COMPLETED")}
            </p>
            <CheckBox
              value={submitConfirmed}
              label={t("CASE_SUBMISSION_CONFIRMATION")}
              wrkflwStyle={{}}
              style={{ ...caseSubmissionWarningText, lineHeight: "18.75px", fontStyle: "italic" }}
              onChange={() => setSubmitConfirmed(!submitConfirmed)}
            />
          </div>
        )}
      </div>
    </Modal>
  );
}

export default CaseLockModal;
