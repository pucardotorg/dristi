import { Card } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import OrderReviewModal from "../../../../../orders/src/pageComponents/OrderReviewModal";
import useGetOrders from "../../../hooks/dristi/useGetOrders";
import { CustomArrowOut } from "../../../icons/svgIndex";
import { useHistory } from "react-router-dom";

const OrderDrafts = ({ caseData, setOrderModal }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const filingNumber = caseData.filingNumber;
  const caseId = caseData.id;
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
    filingNumber,
    filingNumber
  );

  return ordersRes?.list?.filter((order) => order.status === "DRAFT_IN_PROGRESS").length ? (
    <React.Fragment>
      <Card
        style={{
          width: "100%",
          marginTop: "10px",
        }}
      >
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <div
            style={{
              fontWeight: 700,
              fontSize: "24px",
              lineHeight: "28.8px",
              color: "#231F20",
            }}
          >
            Drafts ({ordersRes?.list?.filter((order) => order.status === "DRAFT_IN_PROGRESS").length})
          </div>
          <div
            onClick={() => setOrderModal(ordersRes?.list?.filter((order) => order.status === "DRAFT_IN_PROGRESS"))}
            style={{ cursor: "pointer", fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#0A5757" }}
          >
            {t("VIEW_ALL_LINK")}
          </div>
        </div>
        <div style={{ display: "flex", gap: "16px", marginTop: "10px" }}>
          {ordersRes?.list
            ?.filter((order) => order.status === "DRAFT_IN_PROGRESS")
            .slice(0, 3)
            .map((order) => (
              <div
                style={{
                  padding: "12px 16px",
                  borderRadius: "4px",
                  width: "33%",
                  cursor: "pointer",
                  background: "#ECF3FD66",
                }}
                onClick={() => {
                  setCurrentOrder(order);
                  history.push(
                    `/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${order.orderNumber}`,
                    {
                      caseId: caseId,
                      tab: "Orders",
                    }
                  );
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
                    {t(`ORDER_TYPE_${order?.orderType?.toUpperCase()}`)}
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
  ) : null;
};

export default OrderDrafts;
