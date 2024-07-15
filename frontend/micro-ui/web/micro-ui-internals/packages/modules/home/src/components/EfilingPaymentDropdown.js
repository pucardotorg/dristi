import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { InfoCard } from "@egovernments/digit-ui-components";
import { Link, useHistory } from "react-router-dom/cjs/react-router-dom.min";
import useSearchCaseService from "@egovernments/digit-ui-module-dristi/src/hooks/dristi/useSearchCaseService";
import { useTranslation } from "react-i18next";

const mockSubmitModalInfo = {
  header: "CS_HEADER_FOR_E_FILING_PAYMENT",
  subHeader: "CS_SUBHEADER_TEXT_FOR_E_FILING_PAYMENT",
  caseInfo: [
    {
      key: "Case Number",
      value: "FSM-2019-04-23-898898",
    },
  ],
  isArrow: false,
  showTable: true,
};

const paymentCalculation = [
  { key: "Amount Due", value: 600, currency: "Rs" },
  { key: "Court Fees", value: 400, currency: "Rs" },
  { key: "Advocate Fees", value: 1000, currency: "Rs" },
  { key: "Total Fees", value: 2000, currency: "Rs", isTotalFee: true },
];

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function EfilingPaymentBreakdown({ amount = 2000 }) {
  const history = useHistory();
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const { caseId } = window?.Digit.Hooks.useQueryParams();
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);

  const { data: caseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    caseId
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  if (caseDetails?.status && caseDetails?.status !== "PAYMENT_PENDING") {
    history.push(`/${window?.contextPath}/${userInfoType}/home/home-pending-task`);
  }

  const onCancel = () => {
    history.goBack();
  };

  const onSubmitCase = async () => {
    history.push(`/${window?.contextPath}/${userInfoType}/home/e-filing-payment-response`, {
      state: {
        success: true,
        receiptData: {
          ...mockSubmitModalInfo,
          caseInfo: [
            {
              key: "Mode of Payment",
              value: "Online",
              copyData: false,
            },
            {
              key: "Amount",
              value: "Rs 2000",
              copyData: false,
            },
            {
              key: "Transaction ID",
              value: "KA08293928392",
              copyData: true,
            },
          ],
          isArrow: false,
          showTable: true,
          showCopytext: true,
        },
      },
    });
  };

  return (
    <div className="e-filing-payment-breakdown">
      <Modal
        headerBarEnd={<CloseBtn onClick={onCancel} />}
        actionSaveLabel={t("CS_PAY_ONLINE")}
        formId="modal-action"
        actionSaveOnSubmit={() => onSubmitCase()}
        headerBarMain={<Heading label={t("CS_PAY_TO_FILE_CASE")} />}
      >
        <div className="payment-due-wrapper" style={{ display: "flex", flexDirection: "column" }}>
          <div className="payment-due-text" style={{ fontSize: "18px" }}>
            {`${t("CS_DUE_PAYMENT")} `}
            <span style={{ fontWeight: 700 }}>Rs {amount}/-.</span>
            {` ${t("CS_MANDATORY_STEP_TO_FILE_CASE")}`}
          </div>
          <div className="payment-calculator-wrapper" style={{ display: "flex", flexDirection: "column" }}>
            {paymentCalculation.map((item) => (
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  borderTop: item.isTotalFee && "1px solid #BBBBBD",
                  fontSize: item.isTotalFee && "16px",
                  fontWeight: item.isTotalFee && "700",
                  paddingTop: item.isTotalFee && "12px",
                }}
              >
                <span>{item.key}</span>
                <span>
                  {item.currency} {item.value}
                </span>
              </div>
            ))}
          </div>
          <div>
            <InfoCard
              variant={"default"}
              label={t("CS_COMMON_NOTE")}
              style={{ margin: "16px 0 0 0", backgroundColor: "#ECF3FD" }}
              additionalElements={[
                <div style={{ display: "flex", alignItems: "center", gap: 4 }}>
                  <span>{t("CS_OFFLINE_PAYMENT_STEP_TEXT")}</span>
                  <Link style={{ fontWeight: 700, color: "#0A0A0A" }}>{t("CS_LEARN_MORE")}</Link>
                </div>,
              ]}
              inline
              textStyle={{}}
              className={"adhaar-verification-info-card"}
            />
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default EfilingPaymentBreakdown;
