import { Button, Card, Loader } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import OrderReviewModal from "../../../../../orders/src/pageComponents/OrderReviewModal";
import useGetHearings from "../../../hooks/dristi/useGetHearings";
import useGetOrders from "../../../hooks/dristi/useGetOrders";
import { useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import { ordersService } from "../../../../../orders/src/hooks/services";
import { CaseWorkflowAction } from "../../../../../orders/src/utils/caseWorkflow";
import ScheduleHearing from "./ScheduleHearing";
import useGetIndividualAdvocate from "../../../hooks/dristi/useGetIndividualAdvocate";

const CaseOverview = ({ caseData, setUpdateCounter, showToast }) => {
  const { t } = useTranslation();
  const filingNumber = caseData.filingNumber;
  const history = useHistory();
  const cnrNumber = caseData.cnrNumber;
  const caseId = caseData.caseId;
  const { path } = useRouteMatch();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState({});
  const user = localStorage.getItem("user-info");
  const userRoles = JSON.parse(user).roles.map((role) => role.code);
  const [showScheduleHearingModal, setShowScheduleHearingModal] = useState(false);
  const advocateIds = caseData.case.representatives?.map((representative) => {
    return {
      id: representative.advocateId,
    };
  });

  const { data: advocateDetails, isLoading: isAdvocatesLoading } = useGetIndividualAdvocate(
    {
      criteria: advocateIds,
    },
    { tenantId: tenantId },
    "DRISTI",
    cnrNumber + filingNumber,
    true
  );

  console.log(advocateDetails);

  const { data: hearingRes, refetch: refetchHearingsData, isLoading: isHearingsLoading } = useGetHearings(
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

  const { data: ordersRes, refetch: refetchOrdersData, isLoading: isOrdersLoading } = useGetOrders(
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

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const navigateOrdersGenerate = () => {
    const reqbody = {
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "Bail",
        status: "",
        isActive: true,
        workflow: {
          action: CaseWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {},
      },
    };
    ordersService
      .createOrder(reqbody, { tenantId })
      .then(() => {
        history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`);
      })
      .catch((err) => {});
  };

  const handleMakeSubmission = () => {
    history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${filingNumber}`);
  };

  const openHearingModule = () => {
    setShowScheduleHearingModal(true);
  };

  if (isHearingsLoading || isOrdersLoading || isAdvocatesLoading) {
    return <Loader />;
  }
  return hearingRes?.HearingList?.length === 0 && ordersRes?.list?.length === 0 ? (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        height: "50vh",
        width: "70%",
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
              <Button variation={"outlined"} label={"Schedule Hearing"} onButtonClick={openHearingModule} />
              {userRoles.includes("ORDER_CREATOR") && (
                <Button variation={"outlined"} label={"Generate Order"} onButtonClick={() => navigateOrdersGenerate()} />
              )}
              {showScheduleHearingModal && (
                <ScheduleHearing
                  setUpdateCounter={setUpdateCounter}
                  showToast={showToast}
                  tenantId={tenantId}
                  caseData={caseData}
                  setShowModal={setShowScheduleHearingModal}
                  advocateDetails={advocateDetails.advocates.map((advocate) => {
                    return {
                      individualId: advocate.responseList[0].individualId,
                      name: advocate.responseList[0].additionalDetails.username,
                      type: "Advocate",
                    };
                  })}
                />
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
              <Button variation={"outlined"} label={"Raise Application"} onClick={handleMakeSubmission} />
            </div>
          )}
        </div>
      </div>
    </div>
  ) : (
    <React.Fragment>
      {hearingRes?.HearingList?.filter((hearing) => hearing.endTime < Date.now()).length !== 0 && (
        <Card
          style={{
            width: "70%",
            marginTop: "10px",
          }}
        >
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
            {previousHearing?.transcript.map((transcript) => (
              <div>{transcript}</div>
            ))}
          </div>
        </Card>
      )}
      {ordersRes?.list?.length !== 0 && (
        <Card
          style={{
            width: "70%",
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
              onClick={() => history.replace(`${path}?caseId=${caseId}&tab=Orders`)}
            >
              {t("ALL_ORDERS_LINK")}
            </div>
          </div>
          <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
            {ordersRes?.list
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
                    setShowReviewModal(true);
                    setCurrentOrder(order);
                  }}
                >
                  {t(order?.orderType)}
                </div>
              ))}
          </div>
        </Card>
      )}
      {/* <Button variation={"outlined"} label={"Schedule Hearing"} onButtonClick={openHearingModule} />

      {showScheduleHearingModal && (
        <ScheduleHearing
          setUpdateCounter={setUpdateCounter}
          showToast={showToast}
          tenantId={tenantId}
          caseData={caseData}
          setShowModal={setShowScheduleHearingModal}
          advocateDetails={advocateDetails.advocates.map((advocate) => {
            return {
              individualId: advocate.responseList[0].individualId,
              name: advocate.responseList[0].additionalDetails.username,
              type: "Advocate",
            };
          })}
        />
      )} */}
      {showReviewModal && (
        <OrderReviewModal
          t={t}
          order={currentOrder}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={() => {}}
          handleSaveDraft={() => {}}
          showActions={false}
        />
      )}
    </React.Fragment>
  );
};

export default CaseOverview;
