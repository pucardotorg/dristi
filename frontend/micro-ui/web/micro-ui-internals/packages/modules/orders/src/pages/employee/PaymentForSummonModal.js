import React, { useMemo, useState } from "react";
import { Modal, Button, CardText, RadioButtons, CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { InfoCard } from "@egovernments/digit-ui-components";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";
import DocumentModal from "../../components/DocumentModal";
import { formatDate } from "../../../../hearings/src/utils";

const modeOptions = [
  { label: "E-Post (3-5 days)", value: "e-post" },
  { label: "Registered Post (10-15 days)", value: "registered-post" },
];

const PaymentForSummonComponent = ({ infos, links, feeOptions, orderDate }) => {
  const { t } = useTranslation();
  const CustomErrorTooltip = window?.Digit?.ComponentRegistryService?.getComponent("CustomErrorTooltip");

  const [selectedOption, setSelectedOption] = useState({});

  const getDateWithMonthName = (orderDate) => {
    let today = new Date();

    today.setDate(today.getDate() - 15);

    // Array of month names
    const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

    let dd = String(today.getDate()).padStart(2, "0");
    let mm = monthNames[today.getMonth()];
    let yyyy = today.getFullYear();

    let formattedDate = `${dd} ${mm} ${yyyy}`;

    return formattedDate; // Output: formatted date 15 days ago with month name
  };

  return (
    <div className="payment-for-summon">
      <InfoCard
        variant={"warning"}
        label={"Complete in 2 days"}
        additionalElements={[
          <p>
            It takes 10-15 days via physical post and 3-5 days via e-post for Summon Delivery. Pay by{" "}
            <span style={{ fontWeight: "bold" }}>{getDateWithMonthName(orderDate)}</span> for on-time delivery before next hearing.
          </p>,
        ]}
        inline
        textStyle={{}}
        className={`custom-info-card warning`}
      />
      <ApplicationInfoComponent infos={infos} links={links} />
      <LabelFieldPair className="case-label-field-pair">
        <div className="join-case-tooltip-wrapper">
          <CardLabel className="case-input-label">{t("Select preferred mode of post to pay")}</CardLabel>
          <CustomErrorTooltip message={t("Select date")} showTooltip={true} icon />
        </div>
        <RadioButtons
          additionalWrapperClass="mode-of-post-pay"
          options={modeOptions}
          selectedOption={selectedOption}
          optionsKey={"label"}
          onSelect={(value) => setSelectedOption(value)}
        />
      </LabelFieldPair>
      {selectedOption?.value && (
        <div className="summon-payment-action-table">
          {feeOptions[selectedOption?.value]?.map((action, index) => (
            <div className={`${index === 0 ? "header-row" : "action-row"}`}>
              <div className="payment-label">{t(action?.label)}</div>
              <div className="payment-amount">{index === 0 ? action?.amount : `Rs. ${action?.amount}/-`}</div>
              <div className="payment-action">
                {index === 0 ? (
                  t(action?.action)
                ) : action?.action !== "offline-process" ? (
                  <Button label={t(action.action)} onClick={action.onClick} />
                ) : (
                  <p className="offline-process-text">
                    This is an offline process. <span className="learn-more-text">Learn More</span>
                  </p>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const PaymentForSummonModal = () => {
  const history = useHistory();
  const { filingNumber, orderNumber } = Digit.Hooks.useQueryParams();
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const { data: caseData } = Digit.Hooks.dristi.useSearchCaseService(
    {
      criteria: [
        {
          filingNumber: filingNumber,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    Boolean(filingNumber)
  );

  console.log("caseData :>> ", caseData);

  const { data: orderData, isloading: isOrdersLoading } = Digit.Hooks.orders.useSearchOrdersService(
    { tenantId, criteria: { orderNumber: orderNumber } },
    { tenantId },
    orderNumber,
    Boolean(orderNumber)
  );

  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
        hearingId: orderData?.list?.[0]?.hearingNumber,
      },
    },
    { applicationNumber: "", cnrNumber: "" },
    orderData?.list?.[0]?.hearingNumber,
    Boolean(orderData?.list?.[0]?.hearingNumber)
  );

  console.log("hearingsData :>> ", hearingsData);

  console.log("orderData :>> ", orderData);

  const onPayOnline = () => {};

  const feeOptions = {
    "e-post": [
      {
        label: "Fee Type",
        amount: "Amount",
        action: "Actions",
      },
      { label: "Court Fees", amount: 5, action: "Pay Online", onClick: () => onPayOnline("courtFees") },
      { label: "Delivery Partner Fee", amount: 520, action: "Pay Online", onClick: () => onPayOnline("deliveryFee") },
    ],
    "registered-post": [
      {
        label: "Fee Type",
        amount: "Amount",
        action: "Actions",
      },
      { label: "Court Fees", amount: 520, action: "Pay Online", onClick: () => onPayOnline("courtFees") },
      { label: "Delivery Partner Fee", amount: 120, action: "offline-process", onClick: () => onPayOnline("deliveryFee") },
    ],
  };

  const handleClose = () => {
    history.goBack();
  };

  const infos = useMemo(() => {
    const name = `${orderData?.list?.[0]?.additionalDetails?.formdata?.SummonsOrder?.party?.data?.firstName} ${orderData?.list?.[0]?.additionalDetails?.formdata?.SummonsOrder?.party?.data?.lastName}`;
    const addressDetails = orderData?.list?.[0]?.additionalDetails?.formdata?.SummonsOrder?.party?.data?.addressDetails?.[0]?.addressDetails;
    console.log("addressDetails :>> ", addressDetails);
    return [
      { key: "Issued to", value: name },
      { key: "Next Hearing Date", value: formatDate(new Date(hearingsData?.HearingList?.[0]?.startTime)) },
      {
        key: "Delivery Channel",
        value: `Post (${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state}, ${addressDetails?.pincode})`,
      },
    ];
  }, [hearingsData?.HearingList, orderData?.list]);

  const orderDate = useMemo(() => {
    return hearingsData?.HearingList?.[0]?.startTime;
  }, [hearingsData?.HearingList]);

  const links = useMemo(() => {
    return [{ text: "View order", link: "" }];
  }, []);

  const paymentForSummonModalConfig = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: "Payment for Summon via post" },
      isStepperModal: false,
      modalBody: <PaymentForSummonComponent infos={infos} links={links} feeOptions={feeOptions} orderDate={orderDate} />,
    };
  }, [feeOptions, infos, links]);

  return <DocumentModal config={paymentForSummonModalConfig} />;
};

export default PaymentForSummonModal;
