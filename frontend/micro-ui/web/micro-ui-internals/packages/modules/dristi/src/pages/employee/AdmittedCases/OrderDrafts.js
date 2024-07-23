import { Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import OrderReviewModal from "../../../../../orders/src/pageComponents/OrderReviewModal";
import useGetOrders from "../../../hooks/dristi/useGetOrders";
import { CustomArrowOut } from "../../../icons/svgIndex";

const OrderDrafts = () => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const cnr = searchParams.get("cnrNumber");
  const title = searchParams.get("title");
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState({});

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

  return (
    <React.Fragment>
      <Card
        style={{
          width: "100%",
          marginTop: "10px",
        }}
      >
        <div
          style={{
            fontWeight: 700,
            fontSize: "24px",
            lineHeight: "28.8px",
            color: "#231F20",
          }}
        >
          Drafts ({ordersRes?.list?.length})
        </div>
        <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
          {ordersRes?.list?.slice(0, 5).map((order) => (
            <div
              style={{
                padding: "12px 16px",
                borderRadius: "4px",
                width: "300px",
                cursor: "pointer",
                background: "#ECF3FD66",
              }}
              onClick={() => {
                setShowReviewModal(true);
                setCurrentOrder(order);
              }}
            >
              <div style={{ width: "100%", display: "flex", justifyContent: "space-between" }}>
                <div
                  style={{
                    fontWeight: 700,
                    fontSize: "16px",
                    lineHeight: "18.75px",
                    color: "#101828",
                  }}
                >
                  Order for {order?.orderType.charAt(0).toUpperCase()}
                  {order?.orderType.slice(1).toLowerCase()}
                </div>
                <CustomArrowOut />
              </div>
              <div
                style={{
                  fontWeight: 600,
                  fontSize: "14px",
                  lineHeight: "20px",
                  color: "#101828",
                  marginTop: "12px",
                }}
              >
                Deadline:{" "}
                <span
                  style={{
                    fontWeight: 500,
                    fontSize: "14px",
                    lineHeight: "20px",
                  }}
                ></span>
              </div>
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

export default OrderDrafts;
