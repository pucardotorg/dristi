import CustomCaseInfoDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCaseInfoDiv";
import { Button, CheckSvg } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { createShorthand } from "../../../utils/joinCaseUtils";
import { formatDate } from "../../../utils";
import NameListWithModal from "../../../components/NameListWithModal";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { RightArrow } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import { useTranslation } from "react-i18next";

const JoinCaseSuccess = ({
  success,
  messageHeader,
  caseDetails,
  closeModal,
  refreshInbox,
  selectedParty,
  isAttendingHearing,
  nextHearing,
  setShow,
  setShowSubmitResponseModal,
  setShowConfirmSummonModal,
  successScreenData,
}) => {
  const { t } = useTranslation();

  const history = useHistory();

  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);

  const caseInfo = useMemo(() => {
    if (caseDetails?.caseCategory) {
      return [
        {
          key: "CS_CASE_NAME",
          value: caseDetails?.caseTitle,
        },
        {
          key: "CS_CASE_ID",
          value: caseDetails?.cnrNumber,
        },
        {
          key: "CS_FILING_NUMBER",
          value: caseDetails?.filingNumber,
        },
        {
          key: "CASE_NUMBER",
          value: caseDetails?.cmpNumber,
        },
        {
          key: "CASE_CATEGORY",
          value: caseDetails?.caseCategory,
          prefix: "CS_",
        },
        {
          key: "CASE_TYPE",
          value: `${createShorthand(caseDetails?.statutesAndSections?.[0]?.sections?.[0])} S${
            caseDetails?.statutesAndSections?.[0]?.subsections?.[0]
          }`,
        },
        {
          key: "CS_FILING_DATE",
          value: formatDate(new Date(caseDetails?.filingDate)),
        },
      ];
    }
    return [];
  }, [caseDetails]);

  return (
    <div className="join-a-case-success">
      <div className={`joining-message ${success ? "join-success" : "join-failed"}`}>
        <h3 className="message-header">{messageHeader}</h3>
        <div style={{ width: "48px", height: "48px" }}>
          <CheckSvg />
        </div>
      </div>
      {success && (
        <React.Fragment>
          {caseDetails?.cnrNumber && (
            <React.Fragment>
              <CustomCaseInfoDiv
                t={t}
                data={caseInfo}
                column={4}
                children={
                  <div>
                    <div className="complainants-respondents" style={{ display: "flex", flexWrap: "wrap", gap: "0px" }}>
                      <div
                        style={{
                          flex: "0 0 50%",
                          boxSizing: "border-box",
                        }}
                      >
                        <h2 className="case-info-title">{t("COMPLAINANTS_TEXT")}</h2>
                        <NameListWithModal t={t} data={successScreenData?.complainantList} type={"COMPLAINANTS_TEXT"} />
                      </div>
                      <div
                        style={{
                          flex: "0 0 50%",
                          boxSizing: "border-box",
                          borderLeft: "1px solid rgba(0, 0, 0, 0.10196)",
                          paddingLeft: "16px",
                        }}
                      >
                        <h2 className="case-info-title">{t("RESPONDENTS_TEXT")}</h2>
                        <NameListWithModal t={t} data={successScreenData?.respondentList} type={"RESPONDENTS_TEXT"} />
                      </div>
                    </div>
                    <div className="complainants-respondents">
                      <div style={{ width: "50%" }}>
                        <h2 className="case-info-title">{t("COMPLAINTS_ADVOCATES")}</h2>
                        <NameListWithModal t={t} data={successScreenData?.complainantAdvocateList} type={"COMPLAINTS_ADVOCATES"} />
                      </div>
                      <div style={{ width: "50%", paddingLeft: "16px", borderLeft: "1px solid rgba(0, 0, 0, 0.10196)" }}>
                        <h2 className="case-info-title">{t("ACCUSEDS_ADVOCATES")}</h2>
                        <NameListWithModal t={t} data={successScreenData?.respondentAdvocateList} type={"ACCUSEDS_ADVOCATES"} />
                      </div>
                    </div>
                  </div>
                }
              />
            </React.Fragment>
          )}
          <div className="action-button-success">
            <Button
              className={"selector-button-border"}
              label={t("BACK_HOME")}
              onButtonClick={() => {
                closeModal();
                refreshInbox();
              }}
            />
            <Button
              className={"selector-button-primary"}
              label={
                caseDetails?.status === "PENDING_RESPONSE" && selectedParty?.isRespondent
                  ? t("SUBMIT_RESPONSE_TEXT")
                  : !isAttendingHearing && nextHearing && selectedParty?.isRespondent
                  ? t("CONFIRN_SUMMON_NOTICE_RECEIPT")
                  : t("VIEW_CASE_FILE")
              }
              onButtonClick={() => {
                setShow(false);
                if (caseDetails?.status === "PENDING_RESPONSE" && selectedParty?.isRespondent) {
                  if (setShowSubmitResponseModal) setShowSubmitResponseModal(true);
                } else {
                  if (!isAttendingHearing && nextHearing && selectedParty?.isRespondent) {
                    closeModal();
                    setShowConfirmSummonModal(true);
                  } else
                    history.push(
                      `/${window?.contextPath}/${userInfoType}/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${caseDetails?.filingNumber}&tab=Overview`
                    );
                }
              }}
            >
              <RightArrow />
            </Button>
          </div>
        </React.Fragment>
      )}
    </div>
  );
};

export default JoinCaseSuccess;
