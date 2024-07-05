import { Button, Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import OrderReviewModal from "../../../../../orders/src/pageComponents/OrderReviewModal";
import useGetHearings from "../../../hooks/dristi/useGetHearings";
import useGetOrders from "../../../hooks/dristi/useGetOrders";

const CaseOverview = () => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const history = useHistory();
  const cnr = searchParams.get("cnrNumber");
  const title = searchParams.get("title");
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState({});
  const user = localStorage.getItem("user-info");
  const userRoles = JSON.parse(user).roles.map((role) => role.code);

  const { data: hearingRes, refetch: refetchHearingsData, isLoading: isHearingsLoading } = useGetHearings(
    {
      criteria: {
        filingNumber: filingNumber,
        cnrNumber: cnr,
        tenantId: tenantId,
      },
    },
    {},
    cnr + filingNumber,
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
    cnr + filingNumber,
    true
  );

  const previousHearing = hearingRes?.HearingList?.filter((hearing) => hearing.endTime < Date.now()).sort(
    (hearing1, hearing2) => hearing2.endTime - hearing1.endTime
  )[0];

  const navigateOrdersGenerate = () => {
    history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`);
  };

  console.log(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`);

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
          <div
            style={{
              display: "flex",
              justifyContent: "space-evenly",
              width: "100%",
              marginTop: "16px",
            }}
          >
            <Button variation={"outlined"} label={"Schedule Hearing"} />
            {(userRoles.includes("ORDER_CREATOR") || userRoles.includes("SUPERUSER") || userRoles.includes("EMPLOYEE")) && (
              <Button variation={"outlined"} label={"Generate Order"} onButtonClick={() => navigateOrdersGenerate()} />
            )}
          </div>
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
          <div
            style={{
              fontWeight: 700,
              fontSize: "16px",
              lineHeight: "18.75px",
              color: "#231F20",
            }}
          >
            Recent Orders
          </div>
          <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
            {ordersRes?.list
              ?.sort((order1, order2) => order2.auditDetails?.createdTime - order1.auditDetails?.createdTime)
              .slice(0, 5)
              .map((order) => (
                <div
                  style={{
                    padding: "12px 16px",
                    border: "1px solid #BBBBBD",
                    color: "#BBBBBD",
                    borderRadius: "4px",
                    width: "300px",
                    cursor: "pointer",
                  }}
                  onClick={() => {
                    setShowReviewModal(true);
                    setCurrentOrder(order);
                  }}
                >
                  {order?.orderType}
                </div>
              ))}
          </div>
        </Card>
      )}
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
