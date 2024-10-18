import React from "react";
import { useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Banner } from "@egovernments/digit-ui-react-components";
import { Button, InfoCard } from "@egovernments/digit-ui-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomCopyTextDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCopyTextDiv";

const PaymentStatus = ({ path }) => {
  const location = useLocation();
  const { t } = useTranslation();

  const isResponseSuccess = location.state.state.success;
  const { state } = useLocation();
  const fileStoreId = location.state.state.fileStoreId;
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const caseId = location.state.state.caseId;
  const receiptData = location.state.state.receiptData;
  const history = useHistory();
  const { downloadPdf } = Digit.Hooks.dristi.useDownloadCasePdf();

  const commonProps = {
    whichSvg: "tick",
    headerStyles: { fontSize: "32px" },
    style: { minWidth: "100%", marginTop: "10px" },
  };

  const bannerProps = isResponseSuccess
    ? {
        ...commonProps,
        successful: true,
      }
    : {
        ...commonProps,
        successful: false,
        message: t("CS_PAYMENT_FAILED"),
      };

  return (
    <div className=" user-registration">
      <div className="e-filing-payment" style={{ minHeight: "100%", height: "100%" }}>
        <Banner
          successful={isResponseSuccess}
          message={isResponseSuccess ? "Payment Successful" : "Payment Failed"}
          info={`${state?.showID ? t("SUBMISSION_ID") : ""}`}
          whichSvg={`${isResponseSuccess ? "tick" : null}`}
          {...bannerProps}
        />
        {isResponseSuccess ? (
          <div>
            <div className="payment-status-message">The Summons would be sent to the relevant party.</div>
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
            text={"You have a payment due of Rs 525/-. This is a mandatory step to send summons via selected delivery channel for your case."}
            label={"Note"}
            style={{ marginTop: "1.5rem" }}
            textStyle={{
              color: "#3D3C3C",
              margin: "0.5rem 0",
            }}
          />
        )}
        <div className="button-field" style={{ width: "100%", marginTop: 16 }}>
          {!fileStoreId && caseId ? (
            <Button
              variation={"secondary"}
              className={"secondary-button-selector"}
              label={t("Retry Payment")}
              labelClassName={"secondary-label-selector"}
              onClick={() => {
                history.goBack();
              }}
            />
          ) : (
            <Button
              variation={"secondary"}
              className={"secondary-button-selector"}
              label={t("CS_PRINT_RECEIPT")}
              labelClassName={"secondary-label-selector"}
              onClick={() => {
                downloadPdf(tenantId, fileStoreId);
              }}
            />
          )}

          <Button
            className={"tertiary-button-selector"}
            label={t("CS_GO_TO_HOME")}
            labelClassName={"tertiary-label-selector"}
            onClick={() => {
              history.replace(`/${window?.contextPath}/citizen/home/home-pending-task`);
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default PaymentStatus;
