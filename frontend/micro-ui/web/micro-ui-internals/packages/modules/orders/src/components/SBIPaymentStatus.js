import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Banner } from "@egovernments/digit-ui-react-components";
import { Button, InfoCard } from "@egovernments/digit-ui-components";
import CustomCopyTextDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCopyTextDiv";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { paymentType } from "../utils/paymentType";
import { ordersService } from "../hooks/services";
import { Urls } from "../hooks/services/Urls";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";

const getStatusMessage = (status) => {
  switch (status) {
    case "FAIL":
      return "CS_PAYMENT_FAILED";
    case "PENDING":
      return "CS_PAYMENT_PENDING";
    case "ERROR":
      return "CS_ERROR";
    default:
      return "CS_PAYMENT_SUCCESS";
  }
};

const getPaymentDueMessage = (status, amount) => {
  return status === "ERROR" ? "Something went wrong" : `You have a payment due ${amount || "Rs 11/-"}. `;
};

const SBIPaymentStatus = ({ path }) => {
  const { t } = useTranslation();
  const { status, businessService, serviceNumber } = Digit.Hooks.useQueryParams();
  const { state } = useLocation();
  const history = useHistory();
  const localStorageData = localStorage?.getItem("paymentReceiptData");
  const storedData = localStorageData ? JSON.parse(localStorageData) : {};
  const receiptData = storedData?.receiptData;
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      const billAfterPayment = await DRISTIService.callSearchBill(
        {},
        { tenantId: tenantId, consumerCode: receiptData?.consumerCode, service: businessService }
      );
      setAmount(billAfterPayment?.Bill?.[0]?.totalAmount);
      if (status === "SUCCESS" && billAfterPayment?.Bill?.[0]?.status === "PAID") {
        setLoading(true);
        setError(null);
        try {
          await Promise.all([
            ordersService.customApiService(Urls.orders.pendingTask, {
              pendingTask: {
                name: `MAKE_PAYMENT_FOR_SUMMONS_POST`,
                entityType: paymentType.ASYNC_ORDER_SUBMISSION_MANAGELIFECYCLE,
                referenceId: `MANUAL_${serviceNumber}`,
                status: paymentType.PAYMENT_PENDING_POST,
                assignedTo: [],
                assignedRole: [],
                cnrNumber: receiptData?.filteredTasks?.[0]?.cnrNumber,
                filingNumber: receiptData?.filingNumber,
                isCompleted: true,
                stateSla: "",
                additionalDetails: {},
                tenantId,
              },
            }),
          ]);
        } catch (err) {
          console.error("Error fetching payment tasks:", err);
          setError("Failed to fetch payment tasks. Please try again later."); // Set error message
        } finally {
          setLoading(false); // Reset loading state
        }
      }
    };

    fetchData();
  }, [status]);

  const commonProps = {
    whichSvg: status === "SUCCESS" ? "tick" : null,
    headerStyles: { fontSize: "32px" },
    style: { minWidth: "100%", marginTop: "10px" },
  };

  const bannerProps = {
    ...commonProps,
    successful: status === "SUCCESS",
    message: t(getStatusMessage(status)),
  };

  return (
    <div className="user-registration">
      <div className="e-filing-payment" style={{ minHeight: "100%", height: "100%" }}>
        <Banner
          successful={status === "SUCCESS"}
          message={status === "SUCCESS" ? t("CS_PAYMENT_SUCCESS") : t(getStatusMessage(status))}
          info={`${state?.showID ? t("SUBMISSION_ID") : ""}`}
          whichSvg={status === "SUCCESS" ? "tick" : null}
          {...bannerProps}
        />
        {status === "SUCCESS" ? (
          <div>
            <CustomCopyTextDiv
              t={t}
              keyStyle={{ margin: "8px 0px" }}
              valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
              data={receiptData?.caseInfo}
              tableDataClassName={"e-filing-table-data-style"}
              tableValueClassName={"e-filing-table-value-style"}
            />
          </div>
        ) : (
          <InfoCard
            className="payment-status-info-card"
            headerWrapperClassName="payment-status-info-header"
            populators={{
              name: "infocard",
            }}
            variant="default"
            text={getPaymentDueMessage(status, amount)}
            label={"Note"}
            style={{ marginTop: "1.5rem" }}
            textStyle={{
              color: "#3D3C3C",
              margin: "0.5rem 0",
            }}
          />
        )}

        <div className="button-field" style={{ width: "100%", marginTop: 16 }}>
          <Button
            className="tertiary-button-selector"
            label={t("CS_GO_TO_HOME")}
            style={{ width: "100%" }}
            labelClassName="tertiary-label-selector"
            onClick={() => {
              localStorage.removeItem("paymentReceiptData");
              history.replace(`/${window?.contextPath}/citizen/home/home-pending-task`);
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default SBIPaymentStatus;
