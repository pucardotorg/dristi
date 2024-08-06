import { Button, Card, Loader } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory, useRouteMatch } from "react-router-dom";
import useGetIndividualAdvocate from "../../../hooks/dristi/useGetIndividualAdvocate";
import useGetOrders from "../../../hooks/dristi/useGetOrders";
import { OrderWorkflowState } from "../../../Utils/orderWorkflow";
import PublishedOrderModal from "./PublishedOrderModal";
import TasksComponent from "../../../../../home/src/components/TaskComponent";
import NextHearingCard from "./NextHearingCard";

const CaseOverview = ({ caseData, openHearingModule, handleDownload, handleSubmitDocument, handleExtensionRequest }) => {
  const { t } = useTranslation();
  const filingNumber = caseData.filingNumber;
  const history = useHistory();
  const cnrNumber = caseData.cnrNumber;
  const caseId = caseData.caseId;
  const { path } = useRouteMatch();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState({});
  const [taskType, setTaskType] = useState({});
  const userInfo = Digit.UserService.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const userRoles = userInfo?.roles?.map((role) => role.code);
  const showSubmissionButtons = useMemo(() => {
    const submissionParty = currentOrder?.additionalDetails?.formdata?.submissionParty?.map((item) => item.uuid).flat();
    return submissionParty?.includes(userInfo?.uuid) && userRoles.includes("APPLICATION_CREATOR");
  }, [currentOrder, userInfo?.uuid, userRoles]);

  const { data: hearingRes, isLoading: isHearingsLoading } = Digit.Hooks.hearings.useGetHearings(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    cnrNumber + filingNumber,
    true
  );

  const { data: ordersRes, isLoading: isOrdersLoading } = useGetOrders(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    cnrNumber + filingNumber,
    true
  );

  const previousHearing = hearingRes?.HearingList?.filter((hearing) => hearing.endTime < Date.now()).sort(
    (hearing1, hearing2) => hearing2.endTime - hearing1.endTime
  )[0];

  const navigateOrdersGenerate = () => {
    history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`);
  };

  const orderList = userRoles.includes("CITIZEN")
    ? ordersRes?.list.filter((order) => order.status === "PUBLISHED")
    : ordersRes?.list?.filter((order) => order.status !== "DRAFT_IN_PROGRESS");

  const handleMakeSubmission = () => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}`);
  };

  if (isHearingsLoading || isOrdersLoading) {
    return <Loader />;
  }
  return (
    <div style={{ display: "flex" }}>
      <div style={{ width: "70%" }}>
        {hearingRes?.HearingList?.length === 0 && orderList?.length === 0 ? (
          <div
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              height: "50vh",
            }}
          >
            <div
              style={{
                width: "50%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                flexDirection: "column",
                gap: "16px",
              }}
            >
              <div
                style={{
                  color: "#5F5F5F",
                  fontWeight: 700,
                  fontSize: "24px",
                  lineHeight: "28.13px",
                  textAlign: "center",
                }}
              >
                An overview of this case will appear here!
              </div>
              <div>
                <div
                  style={{
                    color: "#5F5F5F",
                    fontWeight: 400,
                    fontSize: "16px",
                    lineHeight: "24px",
                    textAlign: "center",
                  }}
                >
                  A summary of this case's proceedings, hearings, orders and other activities will be visible here. Take your first action on the case
                </div>
                {!userRoles.includes("CITIZEN") ? (
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-evenly",
                      width: "100%",
                      marginTop: "16px",
                    }}
                  >
                    <Button variation={"outlined"} label={t("SCHEDULE_HEARING")} onButtonClick={openHearingModule} />
                    {userRoles.includes("ORDER_CREATOR") && (
                      <Button variation={"outlined"} label={t("GENERATE_ORDERS_LINK")} onButtonClick={() => navigateOrdersGenerate()} />
                    )}
                  </div>
                ) : (
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-evenly",
                      width: "100%",
                      marginTop: "16px",
                    }}
                  >
                    <Button variation={"outlined"} label={"Raise Application"} onButtonClick={handleMakeSubmission} />
                  </div>
                )}
              </div>
            </div>
          </div>
        ) : (
          <div>
            <NextHearingCard caseData={caseData} width={"100%"} />
            {hearingRes?.HearingList?.filter((hearing) => hearing.endTime < Date.now()).length !== 0 && (
              <Card>
                <div
                  style={{
                    fontWeight: 700,
                    fontSize: "16px",
                    lineHeight: "18.75px",
                    color: "#231F20",
                  }}
                >
                  {`Previous Hearing - ${previousHearing?.hearingType.charAt(0).toUpperCase()}${previousHearing?.hearingType
                    .slice(1)
                    .toLowerCase()} Hearing`}
                </div>
                <hr style={{ border: "1px solid #FFF6E880" }} />
                <div
                  style={{
                    padding: "10px",
                    color: "#505A5F",
                    fontWeight: 400,
                    fontSize: "16px",
                    lineHeight: "24px",
                  }}
                >
                  {previousHearing?.transcript.length
                    ? previousHearing?.transcript.map((transcript) => <div>{transcript}</div>)
                    : "No Transcript available for this hearing"}
                </div>
              </Card>
            )}
            {orderList?.length !== 0 && (
              <Card
                style={{
                  marginTop: "10px",
                }}
              >
                <div style={{ width: "100%", display: "flex", justifyContent: "space-between" }}>
                  <div
                    style={{
                      fontWeight: 700,
                      fontSize: "16px",
                      lineHeight: "18.75px",
                      color: "#231F20",
                      width: "40%",
                    }}
                  >
                    {t("RECENT_ORDERS")}
                  </div>
                  <div
                    style={{ color: "#007E7E", cursor: "pointer", fontWeight: 700, fontSize: "16px", lineHeight: "18.75px" }}
                    onClick={() => history.replace(`${path}?caseId=${caseId}&filingNumber=${filingNumber}&tab=Orders`)}
                  >
                    {t("ALL_ORDERS_LINK")}
                  </div>
                </div>
                <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
                  {orderList
                    ?.sort((order1, order2) => order2.auditDetails?.createdTime - order1.auditDetails?.createdTime)
                    .slice(0, 5)
                    .map((order) => (
                      <div
                        style={{
                          padding: "12px 16px",
                          fontWeight: 700,
                          fontSize: "16px",
                          lineHeight: "18.75px",
                          border: "1px solid #BBBBBD",
                          color: "#505A5F",
                          borderRadius: "4px",
                          width: "300px",
                          cursor: "pointer",
                          display: "flex",
                          justifyContent: "center",
                          alignItems: "center",
                        }}
                        onClick={() => {
                          if (order?.status === OrderWorkflowState.DRAFT_IN_PROGRESS) {
                            history.push(
                              `/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${order?.orderNumber}`
                            );
                          } else {
                            setShowReviewModal(true);
                            setCurrentOrder(order);
                          }
                        }}
                      >
                        {t(`ORDER_TYPE_${order?.orderType.toUpperCase()}`)}
                      </div>
                    ))}
                </div>
              </Card>
            )}
            {/* <Button variation={"outlined"} label={"Schedule Hearing"} onButtonClick={openHearingModule} /> */}
            {showReviewModal && (
              <PublishedOrderModal
                t={t}
                order={currentOrder}
                handleDownload={handleDownload}
                handleRequestLabel={handleExtensionRequest}
                handleSubmitDocument={handleSubmitDocument}
                showSubmissionButtons={showSubmissionButtons}
                handleOrdersTab={() => {
                  setShowReviewModal(false);
                }}
              />
            )}
          </div>
        )}
      </div>
      <div className="right-side">
        <TasksComponent
          taskType={taskType}
          setTaskType={setTaskType}
          isLitigant={userRoles.includes("CITIZEN")}
          uuid={userInfo?.uuid}
          userInfoType={userInfoType}
          filingNumber={filingNumber}
          inCase={true}
        />
      </div>
    </div>
  );
};

export default CaseOverview;
