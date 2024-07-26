import React from "react";
import Modal from "../../../components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";

const ViewAllSubmissions = ({ t, setShow, submissionList, filingNumber, openEvidenceModal }) => {
  const user = localStorage.getItem("user-info");
  const userRoles = JSON.parse(user).roles.map((role) => role.code);
  const history = useHistory();
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const Heading = (props) => {
    return (
      <div className="evidence-title">
        <h1 className="heading-m">{props.label}</h1>
      </div>
    );
  };

  const handleMakeSubmission = () => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}`);
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={() => setShow(false)} />}
      actionSaveLabel={null}
      hideSubmit={true}
      actionCancelLabel={null}
      popupStyles={{ width: "800px" }}
      popupModuleMianStyles={{ maxHeight: "500px", overflow: "auto", display: "flex", flexDirection: "column", gap: "30px", marginTop: "15px" }}
      headerBarMain={
        <Heading
          label={`${userRoles.includes("CITIZEN") ? t("PENDING_SUBMISSIONS_HEADER") : t("REVIEW_SUBMISSIONS_HEADER")} (${submissionList?.length})`}
        />
      }
    >
      {submissionList.map((application) => (
        <div style={{ width: "100%", display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <div style={{ width: "75%", display: "flex", flexDirection: "column", gap: "10px" }}>
            <div style={{ fontWeight: 700, fontSize: "16px", lineHeight: "18.75px", color: "#101828" }}>
              {t(`${application.applicationType.toUpperCase()}`)}
            </div>
            <div style={{ display: "flex", gap: "5px" }}>
              <div style={{ fontWeight: 600, fontSize: "14px", lineHeight: "20px", color: "#101828" }}>Deadline: </div>
              <div style={{ fontWeight: 500, fontSize: "14px", lineHeight: "20px", color: "#101828" }}>24 Jul, 2024</div>
            </div>
          </div>
          <div
            onClick={() => {
              setShow(false);
              if (userRoles.includes("CITIZEN")) {
                handleMakeSubmission();
              } else {
                setShow(false);
                openEvidenceModal(application);
              }
            }}
            style={{ cursor: "pointer", fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#007E7E" }}
          >
            {t("VIEW_LINK")}
          </div>
        </div>
      ))}
    </Modal>
  );
};

export default ViewAllSubmissions;
