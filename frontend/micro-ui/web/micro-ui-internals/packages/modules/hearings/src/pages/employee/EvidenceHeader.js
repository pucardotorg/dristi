import { Button } from "@egovernments/digit-ui-components";
import { Header, Menu } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { VideoIcon } from "./CustomSVGs";

const EvidenceHearingHeader = ({ hearing, caseData, filingNumber, setActiveTab, activeTab, onAddParty }) => {
  const [showMenu, setShowMenu] = useState(false);
  const { t } = useTranslation();

  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const userType = Digit.UserService.getUser()?.info?.type === "CITIZEN" ? "citizen" : "employee";
  const handleTakeAction = () => {
    setShowMenu(!showMenu);
  };

  const handleSelect = (option) => {
    if (option === t("GENERATE_ORDER_HOME")) {
      const searchParams = new URLSearchParams({
        hearingId: hearing.hearingId,
        filingNumber,
      });
      window.open(`${window.location.origin}/${window.contextPath}/${userType}/${"orders/generate-orders"}?${searchParams.toString()}`, "_blank");
      return;
    }

    if (option === t("MAKE_SUBMISSION")) {
      const searchParams = new URLSearchParams({
        hearingId: hearing.hearingId,
        filingNumber,
      });
      window.open(
        `${window.location.origin}/${window.contextPath}/${userType}/${"submissions/submissions-create"}?${searchParams.toString()}`,
        "_blank"
      );
      return;
    }

    if (option === t("CASE_ADD_PARTY")) {
      onAddParty();
      return;
    }
  };

  return (
    <div className="evidence-header-wrapper">
      <div className="evidence-hearing-header">
        <div className="title-section">
          <Header className={"evidence-header"}>{t(`HEARING_TYPE_${hearing.hearingType}`)}</Header>
          <div className="case-details">
            <div className="text">Aparna vs. Subarna</div>
            <div className="breakline"></div>
            <div className="text">NIA 138</div>
            <div className="breakline"></div>
            <div className="text">Trial</div>
          </div>
        </div>
        <div className="evidence-actions">
          <Button variation={"teritiary"} label={"Share"} icon={"Share"} iconFill={"#007E7E"}></Button>
          {userRoles.includes("EMPLOYEE") ? (
            <React.Fragment>
              <Button
                variation={"primary"}
                label={t("TAKE_ACTION_LABEL")}
                icon={showMenu ? "ExpandLess" : "ExpandMore"}
                isSuffix={true}
                onClick={handleTakeAction}
              ></Button>
              {showMenu && (
                <Menu
                  options={
                    userRoles.includes("JUDGE_ROLE")
                      ? [t("GENERATE_ORDER_HOME"), t("CASE_ADD_PARTY")]
                      : [t("GENERATE_ORDER_HOME"), t("MAKE_SUBMISSION"), t("CASE_ADD_PARTY")]
                  }
                  onSelect={(option) => handleSelect(option)}
                ></Menu>
              )}
            </React.Fragment>
          ) : (
            <Button variation={"primary"} label={t("MAKE_SUBMISSION")} onClick={() => handleSelect(t("MAKE_SUBMISSION"))}></Button>
          )}
        </div>
      </div>
      <div className="join-video-conference">
        <div className="conference-info">
          <VideoIcon></VideoIcon>
          <span>You can join this hearing online if you are not present in court.</span>
        </div>
        <Button
          variation={"tertiary"}
          label={t("JOIN_VIDEO_CONFERENCE")}
          onButtonClick={() => {
            window.open(hearing.vcLink, "_blank");
          }}
        ></Button>
      </div>
      <div className="tabs-component">
        <div className="tab" onClick={() => setActiveTab("Transcript/Summary")}>
          <span className={activeTab === "Transcript/Summary" ? "active" : ""}>Transcript/Summary</span>
          {activeTab === "Transcript/Summary" && <div className="breakline"></div>}
        </div>
        <div className="tab" onClick={() => setActiveTab("Witness Deposition")}>
          <span className={activeTab === "Witness Deposition" ? "active" : ""}>Witness Deposition</span>
          {activeTab === "Witness Deposition" && <div className="breakline"></div>}
        </div>
      </div>
    </div>
  );
};
export default EvidenceHearingHeader;
