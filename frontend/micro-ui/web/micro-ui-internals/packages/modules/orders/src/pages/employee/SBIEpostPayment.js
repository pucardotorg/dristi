import React, { useState } from "react";
import { Loader } from "@egovernments/digit-ui-components";
import { useMemo } from "react";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { SBIPaymentService } from "../../hooks/services";
import { Modal, Button, CardText, RadioButtons, CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const modeOptions = [
  { label: "Net Banking", value: "NB" },
  { label: "Credit Card", value: "CC" },
  { label: "Debit Card", value: "DC" },
  { label: "Wallet", value: "WALLET" },
  { label: "Prepaid Cards", value: "PC" },
  { label: "PayPal", value: "PAYPAL" },
  { label: "UPI", value: "UPI" },
];

const SBIEpostPayment = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const [selectedOption, setSelectedOption] = useState({});
  const [optionLoader, setOptionLoader] = useState(true);
  const [paymentLoader, setPaymentLoader] = useState(false);
  const bill = location.state.state.billData;
  const consumerCode = location.state.state.consumerCode;
  const service = location.state.state.service;
  const tenantId = Digit.ULBService.getCurrentTenantId();

  console.log(bill, "j");

  const onSBIPayment = async () => {
    console.log("sbi payment done");
    console.log(selectedOption, "lll");
    setPaymentLoader(true);
    let status;
    try {
      const gateway = await SBIPaymentService.SBIPayment(
        {
          TransactionDetails: {
            OperatingMode: "DOM",
            MerchantCountry: "IN",
            MerchantCurrency: "INR",
            PostingAmount: 4.0,
            OtherDetails: "NA",
            Paymode: selectedOption?.value,
            tenantId: tenantId,
            billId: bill?.Bill?.[0]?.billDetails?.[0]?.billId,
            totalDue: 35.0,
            businessService: "task-summons",
            serviceNumber: consumerCode,
            payerName: "John Doe",
            paidBy: "John Doe",
            mobileNumber: "1234567890",
          },
        },
        {}
      );
      if (gateway) {
        status = handleButtonClick(gateway?.transactionUrl, gateway?.encryptedString, gateway?.merchantId);
      }
    } catch (e) {
      console.log(e);
    }
    if (status === true) {
      console.log("YAAAYYYYY");
      const fileStoreId = await DRISTIService.fetchBillFileStoreId({}, { billId: bill?.Bill?.[0]?.id, tenantId });

      // await Promise.all([
      //   ordersService.customApiService(Urls.orders.pendingTask, {
      //     pendingTask: {
      //       name: "Show Summon-Warrant Status",
      //       entityType: paymentType.ORDER_MANAGELIFECYCLE,
      //       referenceId: hearingsData?.HearingList?.[0]?.hearingId,
      //       status: paymentType.SUMMON_WARRANT_STATUS,
      //       assignedTo: [],
      //       assignedRole: ["JUDGE_ROLE"],
      //       cnrNumber: filteredTasks?.[0]?.cnrNumber,
      //       filingNumber: filingNumber,
      //       isCompleted: false,
      //       stateSla: 3 * dayInMillisecond + todayDate,
      //       additionalDetails: {
      //         hearingId: hearingsData?.list?.[0]?.hearingId,
      //       },
      //       tenantId,
      //     },
      //   }),
      //   ordersService.customApiService(Urls.orders.pendingTask, {
      //     pendingTask: {
      //       name: `MAKE_PAYMENT_FOR_SUMMONS_POST`,
      //       entityType: paymentType.ASYNC_ORDER_SUBMISSION_MANAGELIFECYCLE,
      //       referenceId: `MANUAL_Post_${orderNumber}`,
      //       status: paymentType.PAYMENT_PENDING_POST,
      //       assignedTo: [],
      //       assignedRole: [],
      //       cnrNumber: filteredTasks?.[0]?.cnrNumber,
      //       filingNumber: filingNumber,
      //       isCompleted: true,
      //       stateSla: "",
      //       additionalDetails: {},
      //       tenantId,
      //     },
      //   }),
      // ]);

      // fileStoreId &&
      //   history.push(`/${window?.contextPath}/citizen/home/post-payment-screen`, {
      //     state: {
      //       success: true,
      //       receiptData: {
      //         ...mockSubmitModalInfo,
      //         caseInfo: [
      //           {
      //             key: "Case Name & ID",
      //             value: caseDetails?.caseTitle + "," + caseDetails?.filingNumber,
      //             copyData: false,
      //           },
      //           {
      //             key: "ORDER ID",
      //             value: orderData?.list?.[0]?.orderNumber,
      //             copyData: false,
      //           },
      //           {
      //             key: "Transaction ID",
      //             value: filteredTasks?.[0]?.taskNumber,
      //             copyData: true,
      //           },
      //         ],
      //         isArrow: false,
      //         showTable: true,
      //         showCopytext: true,
      //       },
      //       fileStoreId: fileStoreId?.Document?.fileStore,
      //     },
      //   });
    } else {
      console.log("NAAAYYYYY");
      // history.push(`/${window?.contextPath}/citizen/home/post-payment-screen`, {
      //   state: {
      //     success: false,
      //     receiptData: {
      //       ...mockSubmitModalInfo,
      //       caseInfo: [
      //         {
      //           key: "Case Name & ID",
      //           value: caseDetails?.caseTitle + "," + caseDetails?.filingNumber,
      //           copyData: false,
      //         },
      //         {
      //           key: "ORDER ID",
      //           value: orderData?.list?.[0]?.orderNumber,
      //           copyData: false,
      //         },
      //         {
      //           key: "Transaction ID",
      //           value: filteredTasks?.[0]?.taskNumber,
      //           copyData: true,
      //         },
      //       ],
      //       isArrow: false,
      //       showTable: true,
      //       showCopytext: true,
      //     },
      //     caseId: caseDetails?.filingNumber,
      //   },
      // });
    }
  };

  const handleButtonClick = (transactionalUrl, encryptedString, merchantId) => {
    return new Promise((resolve) => {
      const form = document.createElement("form");
      form.method = "POST";
      form.action = transactionalUrl;

      const inputDataField = document.createElement("input");
      inputDataField.type = "hidden";
      inputDataField.name = "EncryptTrans";
      inputDataField.value = { encryptedString };
      form.appendChild(inputDataField);

      const inputHeadersField = document.createElement("input");
      inputHeadersField.type = "hidden";
      inputHeadersField.name = "merchIdVal";
      inputHeadersField.value = merchantId;
      form.appendChild(inputHeadersField);

      window.document.body.appendChild(form);
      form.submit();

      setPaymentLoader(true);
      popup.document.body.removeChild(form);
    });
  };
  return (
    <div>
      {paymentLoader ? (
        <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
          <Loader />
        </div>
      ) : (
        <div className="payment-for-summon">
          <LabelFieldPair className="case-label-field-pair">
            <div className="join-case-tooltip-wrapper">
              <CardLabel className="case-input-label">{t("Select preferred mode of Payment")}</CardLabel>
            </div>
            <RadioButtons
              options={modeOptions}
              optionsKey={"label"}
              onSelect={(value) => {
                setSelectedOption(value); // Set selected option's value
                console.log(value);
              }}
              selectedOption={selectedOption} // Bind currently selected option to state
              disabled={false}
            />
          </LabelFieldPair>
          <Button label={t("SBI_PAYMENT")} onButtonClick={onSBIPayment} />
        </div>
      )}
    </div>
  );
};

export default SBIEpostPayment;
