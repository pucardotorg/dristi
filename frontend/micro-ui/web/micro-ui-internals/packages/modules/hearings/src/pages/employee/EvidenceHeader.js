import { Button } from "@egovernments/digit-ui-components";

import { Header, Menu } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { VideoIcon } from "./CustomSVGs";

const EvidenceHearingHeader = ({ hearing, caseData, filingNumber, setActiveTab, activeTab, onAddParty, hearingLink }) => {
  const [showMenu, setShowMenu] = useState(false);
  const { t } = useTranslation();

  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const userType = Digit.UserService.getUser()?.info?.type === "CITIZEN" ? "citizen" : "employee";
  const handleTakeAction = () => {
    setShowMenu(!showMenu);
  };

  const handleSelect = (option) => {
    setShowMenu(false);
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

  const allAdvocates = useMemo(
    () => Digit?.Customizations?.DristiCaseUtils?.getAllCaseRepresentativesUUID?.(caseData)[Digit.UserService.getUser()?.info?.uuid] || [],
    [caseData]
  );

  const isAdvocatePresent = useMemo(
    () => (userRoles?.includes("ADVOCATE_ROLE") ? true : allAdvocates.includes(Digit.UserService.getUser()?.info?.uuid)),
    [allAdvocates, userRoles]
  );

  const showMakeSubmission = useMemo(() => {
    return isAdvocatePresent && userRoles?.includes("APPLICATION_CREATOR");
  }, [userRoles, isAdvocatePresent]);

  return (
    <div className="admitted-case-header" style={{ padding: "0px", border: "none" }}>
      <div className="admitted-case-details" style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <div className="case-details-title" style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          {/* <Header>{t(`HEARING_TYPE_${hearing.hearingType}`)}</Header> */}
          <Header>
            {t(hearing.hearingType)} {t("HEARING")}
          </Header>
          <div className="sub-details-text">{caseData?.caseTitle || " "}</div>
          <div className="vertical-line"></div>
          <div className="sub-details-text">{caseData?.caseType || "Section 138 of Negotiable Instruments Act"}</div>
          <div className="vertical-line"></div>
          <div className="sub-details-text">{caseData?.substage || "Trial"}</div>
        </div>
        <div className="judge-action-block">
          <div className="evidence-header-wrapper">
            <div className="evidence-hearing-header" style={{ background: "transparent" }}>
              {/* <div className="evidence-actions">
                <Button
                  style={{
                    backgroundColor: "#fff",
                  }}
                  textStyles={{
                    // fontFamily: "Roboto",
                    // fontSize: "16px",
                    // fontWeight: 700,
                    // lineHeight: "18.75px",
                    // textAlign: "center",
                    // color: "#007E7E",
                  }}
                  variation={"tertiary"}
                  label={"Share"}
                  icon={"Share"}
                  iconFill={"#007E7E"}
                  className={"take-action-btn-class"}
                ></Button>
              </div> */}
            </div>
          </div>
          {userRoles.includes("EMPLOYEE") ? (
            <div className="evidence-header-wrapper">
              <div className="evidence-hearing-header" style={{ background: "transparent" }}>
                <div className="evidence-actions">
                  <Button
                    variation={"primary"}
                    label={t("TAKE_ACTION_LABEL")}
                    icon={showMenu ? "ExpandLess" : "ExpandMore"}
                    isSuffix={true}
                    onClick={handleTakeAction}
                    className={"take-action-btn-class"}
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
                </div>
              </div>
            </div>
          ) : (
            showMakeSubmission && (
              <Button variation={"primary"} label={t("MAKE_SUBMISSION")} onClick={() => handleSelect(t("MAKE_SUBMISSION"))}></Button>
            )
          )}
        </div>
      </div>
      <div
        className="join-video-conference"
        style={{
          display: "flex",
          padding: "12px 16px",
          gap: "12px",
          borderRadius: "4px 0 0 0",
          border: "1px solid #0000001A",
          backgroundColor: "#FFF8FD",
          alignItems: "center",
          justifyContent: "flex-start",
        }}
      >
        <div
          className="conference-info"
          style={{
            display: "flex",
            gap: "12px",
          }}
        >
          <VideoIcon></VideoIcon>
        </div>
        <Button
          variation={"tertiary"}
          label={t("JOIN_VIDEO_CONFERENCE")}
          onClick={() => {
            window.open(hearingLink, "_blank");
          }}
          style={{
            backgroundColor: "transparent",
          }}
          textStyles={{
            fontFamily: "Lato",
            fontSize: "16px",
            fontWeight: 700,
            lineHeight: "19.2px",
            textAlign: "left",
            color: "#007E7E",
            margin: "0px",
          }}
        ></Button>
      </div>
      <div className="search-tabs-container" style={{ marginBottom: "24px" }}>
        <div style={{ width: "100%" }}>
          <button
            className={activeTab === "Transcript/Summary" ? "search-tab-head-selected" : "search-tab-head"}
            onClick={() => setActiveTab("Transcript/Summary")}
          >
            {t("TRANSCRIPT_SUMMARY")}
          </button>
          <button
            className={activeTab === "Witness Deposition" ? "search-tab-head-selected" : "search-tab-head"}
            onClick={() => setActiveTab("Witness Deposition")}
          >
            {t("WITNESS_DEPOSITION")}
          </button>
        </div>
      </div>
    </div>
  );
};
export default EvidenceHearingHeader;
