import { Banner, CardLabel, CloseSvg, Loader, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "../../../components/Button";
import { InfoCard } from "@egovernments/digit-ui-components";
import { Link, useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";

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

function EFilingPayment({ t, setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo, amount = 2000, path }) {
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const history = useHistory();
  const onCancel = () => {
    setShowPaymentModal(false);
  };
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const { caseId } = window?.Digit.Hooks.useQueryParams();

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

  const submitInfoData = useMemo(() => {
    return {
      ...mockSubmitModalInfo,
      caseInfo: [
        {
          key: "CS_CASE_NUMBER",
          value: caseDetails?.filingNumber,
        },
      ],
      isArrow: false,
      showTable: true,
      showCopytext: true,
    };
  }, [caseDetails?.filingNumber]);

  if (isLoading) {
    return <Loader />;
  }
  return (
    <div className=" user-registration">
      <div className="e-filing-payment">
        <Banner
          whichSvg={"tick"}
          successful={true}
          message={submitModalInfo?.header}
          headerStyles={{ fontSize: "32px" }}
          style={{ minWidth: "100%" }}
        ></Banner>
        {submitInfoData?.subHeader && <CardLabel className={"e-filing-card-label"}>{submitInfoData?.subHeader}</CardLabel>}
        {submitInfoData?.showTable && (
          <CustomCaseInfoDiv
            t={t}
            data={submitInfoData?.caseInfo}
            tableDataClassName={"e-filing-table-data-style"}
            copyData={true}
            tableValueClassName={"e-filing-table-value-style"}
          />
        )}
        <div className="button-field">
          <Button
            variation={"secondary"}
            className={"secondary-button-selector"}
            label={t("CS_PRINT_CASE_FILE")}
            labelClassName={"secondary-label-selector"}
            onButtonClick={() => {}}
          />
          <Button
            className={"tertiary-button-selector"}
            label={t("CS_MAKE_PAYMENT")}
            labelClassName={"tertiary-label-selector"}
            onButtonClick={() => {
              setShowPaymentModal(true);
            }}
          />
        </div>
        {showPaymentModal && (
          <Modal
            headerBarEnd={<CloseBtn onClick={onCancel} />}
            actionSaveLabel={t("CS_PAY_ONLINE")}
            formId="modal-action"
            actionSaveOnSubmit={() => history.push(`${path}/e-filing-payment-response`)}
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
                  label={"CS_COMMON_NOTE"}
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
        )}
      </div>
    </div>
  );
}

export default EFilingPayment;
