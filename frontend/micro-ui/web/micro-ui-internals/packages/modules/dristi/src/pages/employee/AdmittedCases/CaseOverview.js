import { Button, Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import OrderReviewModal from "../../../../../orders/src/pageComponents/OrderReviewModal";
import useGetHearings from "../../../hooks/dristi/useGetHearings";
import useGetOrders from "../../../hooks/dristi/useGetOrders";

const CaseOverview = () => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const cnr = searchParams.get("cnr");
  const title = searchParams.get("title");
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState({});

  const { data: hearingRes, refetch: refetchHearingsData, isLoading: isHearingsLoading } = useGetHearings(
    {},
    {
      filingNumber: filingNumber,
      cnrNumber: cnr,
      applicationNumber: "",
      tenantId: tenantId,
    },
    cnr + filingNumber,
    true
  );

  const { data: ordersRes, refetch: refetchOrdersData, isLoading: isOrdersLoading } = useGetOrders(
    {},
    {
      filingNumber: filingNumber,
      cnrNumber: cnr,
      applicationNumber: "",
      tenantId: tenantId,
    },
    cnr + filingNumber,
    true
  );

  const scheduledHearing = hearingRes?.HearingList?.filter((hearing) => hearing.startTime > Date.now()).sort(
    (hearing1, hearing2) => hearing1.startTime - hearing2.startTime
  )[0];
  const previousHearing = hearingRes?.HearingList?.filter((hearing) => hearing.endTime < Date.now()).sort(
    (hearing1, hearing2) => hearing2.endTime - hearing1.endTime
  )[0];
  console.log(previousHearing);
  const formattedTime = () => {
    const date1 = new Date(scheduledHearing?.startTime);
    const date2 = new Date(scheduledHearing?.endTime);
    const formattedDate = `
    ${date1.toLocaleTimeString("en-in", {
      hour: "2-digit",
      minute: "2-digit",
    })}
     - 
     ${date2.toLocaleTimeString("en-in", {
       hour: "2-digit",
       minute: "2-digit",
     })}`;
    return formattedDate;
  };

  const formattedDate = () => {
    const date1 = new Date(scheduledHearing?.startTime);
    // const date2 = new Date(scheduledHearing?.endTime);
    const formattedDate = `${date1.toLocaleDateString("en-in", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })}`;
    return formattedDate;
  };

  return (
    <React.Fragment>
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
          Next Hearing
        </div>
        <hr style={{ border: "1px solid #FFF6E880" }} />
        <div style={{ display: "flex", justifyContent: "space-between", padding: "10px" }}>
          <div>
            <div
              style={{
                fontWeight: 700,
                fontSize: "16px",
                lineHeight: "18.75px",
                color: "#231F20",
              }}
            >
              {formattedDate()}
            </div>
            <div
              style={{
                fontWeight: 700,
                fontSize: "24px",
                lineHeight: "28.13px",
                color: "#231F20",
                marginTop: "5px",
              }}
            >
              {formattedTime()}
            </div>
            <div
              style={{
                fontWeight: 400,
                fontSize: "14px",
                lineHeight: "16.41px",
                color: "#3D3C3C",
                marginTop: "5px",
              }}
            >
              {`${scheduledHearing?.hearingType.charAt(0).toUpperCase()}${scheduledHearing?.hearingType.slice(1).toLowerCase()} Hearing`}
            </div>
          </div>
          <Button variation={"outlined"} label={"Start Now"} />
        </div>
      </Card>
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
          {`Previous Hearing - ${previousHearing?.hearingType.charAt(0).toUpperCase()}${previousHearing?.hearingType.slice(1).toLowerCase()} Hearing`}
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
          {ordersRes?.list?.map((order) => (
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
