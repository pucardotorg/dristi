import { InfoBannerIcon } from "@egovernments/digit-ui-components";
import { CustomArrowDownIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import DocumentModal from "@egovernments/digit-ui-module-orders/src/components/DocumentModal";
import React, { useMemo, useState } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
// import { CustomArrowDownIcon, CustomArrowUpIcon } from "../icons/svgIndex";

function PendingTaskAccordion({
  handlePageChange,
  pendingTasks,
  t,
  showConfirmModal,
  totalCount,
  handleGoToPage,
  selected,
  accordionHeader = "COMPLETE_THIS_WEEK",
  accordionKey = "accordion",
  isHighlighted = false,
  isAccordionOpen = false,
  setShowSubmitResponseModal,
  setResponsePendingTask,
  allPendingTasks,
}) {
  const history = useHistory();
  const [isOpen, setIsOpen] = useState(isAccordionOpen);
  const [check, setCheck] = useState(false);
  const [isOpenDCA, setIsOpenDCA] = useState(false);
  const [redirectUrl, setRedirectUrl] = useState("");

  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles?.map((role) => role?.code) || [], []);
  const isJudge = roles.includes("JUDGE_ROLE");

  const handleAccordionClick = () => {
    setIsOpen(!isOpen);
  };

  const redirectPendingTaskUrl = async (url, isCustomFunction = () => {}, params = {}) => {
    if (isCustomFunction) {
      await url(params);
    } else {
      history.push(url, {
        state: {
          params: params,
        },
      });
      setCheck(!check);
    }
  };
  const delayCondonationTextStyle = {
    margin: "0px",
    fontFamily: "Roboto",
    fontSize: "16px",
    fontWeight: 400,
    lineHeight: "21.6px",
    color: "#231F20",
  };

  const sumbitResponseConfig = useMemo(() => {
    return {
      handleClose: () => {
        setIsOpenDCA(false);
      },
      heading: { label: "" },
      actionSaveLabel: "",
      isStepperModal: true,
      actionSaveOnSubmit: () => {},
      steps: [
        {
          heading: { label: "DELAY_CONDONATION_APPLICATION_OPEN" },
          actionSaveLabel: "ISSUE_JOINT_ORDER",
          modalBody: (
            <div style={{ width: "527px", padding: "12px 16px" }}>
              <p style={delayCondonationTextStyle}>{t("DELAY_CONDONATION_APPLICATION_OPEN_MESSAGE")}</p>
            </div>
          ),
          actionSaveOnSubmit: () => {
            history.push(redirectUrl, {
              triggerAdmitCase: true,
            });
          },
          actionCancelLabel: "BACK",
          actionCancelOnSubmit: () => {
            setIsOpenDCA(false);
          },
        },
      ],
    };
  }, [redirectUrl]);

  return (
    <div key={accordionKey} className="accordion-wrapper" style={{ border: "1px solid #E8E8E8", padding: 16, borderRadius: 4 }}>
      <div
        className={`accordion-title ${isOpen ? "open" : ""}`}
        style={{ cursor: "default", marginBottom: isOpen && totalCount ? 16 : 0, transition: "margin-bottom 0.25s" }}
        onClick={handleAccordionClick}
      >
        <span
          style={{
            color: isHighlighted ? "#9E400A" : "black",
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: "700",
            lineHeight: "18.75px",
            textAlign: "left",
            display: "flex",
            alignItems: "center",
            gap: 8,
          }}
          className="accordion-header"
        >
          {isHighlighted && (
            <span>
              <InfoBannerIcon fill="#9E400A" />
            </span>
          )}
          <span>{`${t(accordionHeader)}${totalCount ? ` (${totalCount})` : ""}`}</span>
        </span>
        <div
          className="icon"
          style={{
            marginRight: 4,
          }}
        >
          <span className="reverse-arrow" style={{ cursor: "pointer" }} onClick={handleAccordionClick}>
            <CustomArrowDownIcon />
          </span>
        </div>
      </div>
      <div className={`accordion-item ${!isOpen ? "collapsed" : ""}`}>
        <div
          className={`accordion-item ${!isOpen ? "collapsed" : ""}`}
          style={{ overflowY: "auto", maxHeight: "40vh", paddingRight: "8px", "&::WebkitScrollbar": { width: 0 } }}
        >
          {pendingTasks?.map((item) => (
            <div
              className={`task-item ${item?.due === "Due today" && "due-today"}`}
              key={item?.filingNumber}
              style={{ cursor: "pointer" }}
              onClick={() => {
                if (item?.status === "PENDING_RESPONSE") {
                  if (isJudge) {
                    const isDelayCondonationPendingTaskPresent = allPendingTasks?.some(
                      (task) => task?.actionName === "Accept/Reject Delay Condonation Application" && task?.filingNumber === item?.filingNumber
                    );
                    const caseId = item?.params?.caseId;
                    const filingNumber = item?.params?.filingNumber;
                    if (isDelayCondonationPendingTaskPresent) {
                      setRedirectUrl(
                        `/${window.contextPath}/employee/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Overview`
                      );
                      setIsOpenDCA(true);
                    } else {
                      history.push(
                        `/${window.contextPath}/employee/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Overview`,
                        {
                          triggerAdmitCase: true,
                        }
                      );
                    }
                  } else {
                    setResponsePendingTask(item);
                    setShowSubmitResponseModal(true);
                  }
                } else redirectPendingTaskUrl(item?.redirectUrl, item?.isCustomFunction, item?.params);
              }}
            >
              <input type="checkbox" value={check} />
              <div className="task-details" style={{ display: "flex", flexDirection: "column", gap: 8, marginLeft: 8 }}>
                <span className="task-title">
                  {t(item?.actionName)} : {item?.caseTitle}
                </span>
                <span className="task-info">
                  {item?.caseType} - {item?.filingNumber} -{" "}
                  <span style={{ ...(item?.dueDateColor && { color: item?.dueDateColor }) }}>{item?.due}</span>
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>
      {isOpenDCA && <DocumentModal config={sumbitResponseConfig} />}
    </div>
  );
}

export default PendingTaskAccordion;
