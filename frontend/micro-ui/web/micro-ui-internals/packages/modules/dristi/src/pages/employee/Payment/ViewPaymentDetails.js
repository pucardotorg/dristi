import { Loader, SubmitBar, ActionBar, CustomDropdown, CardLabel, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { ReactComponent as LocationOnMapIcon } from "../../../images/location_onmap.svg";
import { useToast } from "../../../components/Toast/useToast";
import { userTypeOptions } from "../../citizen/registration/config";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { DRISTIService } from "../../../services";
import { formatDate } from "../../citizen/FileCase/CaseType";

const paymentCalculation = [
  { key: "Amount Due", value: 600, currency: "Rs" },
  { key: "Court Fees", value: 400, currency: "Rs" },
  { key: "Advocate Fees", value: 1000, currency: "Rs" },
  { key: "Total Fees", value: 2000, currency: "Rs", isTotalFee: true },
];

const paymentOption = [
  {
    code: "PAY_BY_OWNER",
    i18nKey: "PT_PAY_BY_OWNER",
    name: "I am making the payment as the owner/ consumer of the service",
  },
  {
    code: "PAY_BEHALF_OWNER",
    i18nKey: "PT_PAY_BEHALF_OWNER",
    name: "I am making the payment on behalf of the owner/ consumer of the service",
  },
];

const paymentOptionConfig = {
  label: "CS_MODE_OF_PAYMENT",
  type: "dropdown",
  name: "selectIdTypeType",
  optionsKey: "i18nKey",
  validation: {},
  isMandatory: true,
  options: paymentOption,
  styles: {
    width: "50%",
  },
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFFFF">
    <path d="M0 0h24v24H0V0z" fill="none" />
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div className="icon-bg-secondary" onClick={props.onClick}>
      <Close />
    </div>
  );
};

const LocationContent = ({ latitude = 17.2, longitude = 17.2 }) => {
  return (
    <div style={{ fontSize: "16px", display: "flex", marginTop: "-2px" }}>
      <div>
        <a
          href={`https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`}
          target="_blank"
          rel="noreferrer"
          style={{ color: "#F47738" }}
        >
          View on map
        </a>
      </div>
      <div style={{ marginLeft: "10px" }}>
        <LocationOnMapIcon></LocationOnMapIcon>
      </div>
    </div>
  );
};

const ViewPaymentDetails = ({ location, match }) => {
  const urlParams = new URLSearchParams(window.location.search);

  const toast = useToast();

  const individualId = urlParams.get("individualId");
  const applicationNo = urlParams.get("applicationNo");
  const type = urlParams.get("type") || "advocate";
  const moduleCode = "DRISTI";
  const { t } = useTranslation();
  const history = useHistory();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [isAction, setIsAction] = useState(false);

  const { caseId, filingNumber } = window?.Digit.Hooks.useQueryParams();

  const { data: caseData, isLoading: isCaseSearchLoading } = useSearchCaseService(
    {
      criteria: [
        caseId
          ? {
              caseId,
            }
          : {
              filingNumber,
            },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId || filingNumber,
    caseId || filingNumber
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  const { data: individualData, isLoading: isGetUserLoading } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        individualId,
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    individualId,
    Boolean(individualId)
  );

  const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
    individualData?.Individual,
  ]);

  const identifierIdDetails = useMemo(
    () => JSON.parse(individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "identifierIdDetails")?.value || "{}"),
    [individualData?.Individual]
  );

  const { data: searchData, isLoading: isSearchLoading } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [applicationNo ? { applicationNumber: applicationNo } : { individualId }],
      tenantId,
    },
    {},
    applicationNo + individualId,
    userType && individualId,
    userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );
  const { data: fetchBillData, isLoading: isFetchBillLoading } = window?.Digit.Hooks.useFetchBillsForBuissnessService(
    {
      // criteria: [applicationNo ? { applicationNumber: applicationNo } : { individualId }],
      // tenantId,
    },
    {},
    applicationNo + individualId,
    userType && individualId,
    userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const { isLoading: isWorkFlowLoading, data: workFlowDetails } = window?.Digit.Hooks.useWorkflowDetails({
    tenantId,
    id: applicationNo,
    moduleCode,
    config: {
      enabled: false,
      cacheTime: 0,
    },
  });
  const actions = useMemo(() => workFlowDetails?.processInstances?.[0]?.state?.actions?.map((action) => action.action), [
    workFlowDetails?.processInstances,
  ]);

  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`]?.[0]?.responseList;
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);
  const fileStoreId = useMemo(() => {
    return searchResult?.[0]?.documents?.[0]?.fileStore;
  }, [searchResult]);
  const fileName = useMemo(() => {
    return searchResult?.[0]?.documents?.[0]?.additionalDetails?.fileName;
  }, [searchResult]);
  useEffect(() => {
    setIsAction(searchResult?.[0]?.status === "INWORKFLOW");
  }, [searchResult]);

  const applicationNumber = useMemo(() => {
    return searchResult?.[0]?.applicationNumber;
  }, [searchResult]);

  const onSubmitCase = async () => {
    const resposne = await window?.Digit.PaymentService.createReciept(tenantId, {});
    // await DRISTIService.caseUpdateService(
    //   {
    //     cases: {
    //       ...caseDetails,
    //       caseTitle: `${caseDetails?.additionalDetails?.complaintDetails?.formdata?.[0]?.data?.firstName} ${caseDetails?.additionalDetails?.complaintDetails?.formdata?.[0]?.data?.lastName} VS ${caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentFirstName} ${caseDetails?.additionalDetails?.respondentDetails?.formdata?.[0]?.data?.respondentLastName}`,
    //       filingDate: formatDate(new Date()),
    //       workflow: {
    //         ...caseDetails?.workflow,
    //         action: "MAKE_PAYMENT",
    //       },
    //     },
    //     tenantId,
    //   },
    //   tenantId
    // );
    history.push(`/${window?.contextPath}/employee/dristi/home/pending-payment-inbox`);
  };

  if (isSearchLoading || isGetUserLoading || isWorkFlowLoading || isCaseSearchLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div className="home-screen-wrapper" style={{ minHeight: "calc(100vh - 90px)", width: "100%", padding: "30px" }}>
        <div className="header-class" style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div className="header">{t("CS_RECORD_PAYMENT_HEADER_TEXT")}</div>
          <div className="sub-header">{t("CS_RECORD_PAYMENT_SUBHEADER_TEXT")}</div>
        </div>
        <div className="payment-calculator-wrapper">
          {paymentCalculation.map((item) => (
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                borderTop: item.isTotalFee && "1px solid #BBBBBD",
                paddingTop: item.isTotalFee && "20px",
              }}
            >
              <span>{item.key}</span>
              <span>
                {item.currency} {item.value}
              </span>
            </div>
          ))}
        </div>
        <div style={{ marginTop: 40 }}>
          <div className="payment-case-name">{`${t("CS_CASE_ID")}: ${caseDetails?.filingNumber}`}</div>
          <div className="payment-case-detail-wrapper">
            <LabelFieldPair>
              <CardLabel>{`${t("CORE_COMMON_PROFILE_NAME")}`}</CardLabel>
              <TextInput t={t} style={{ width: "50%" }} type={"text"} isMandatory={false} name="name" onChange={(e) => {}} disable={true} />
            </LabelFieldPair>
            <LabelFieldPair style={{ alignItems: "flex-start", fontSize: "16px", fontWeight: 400 }}>
              <CardLabel>{t(paymentOptionConfig.label)}</CardLabel>
              <CustomDropdown
                label={paymentOptionConfig.label}
                t={t}
                defaulValue={paymentOption[0]}
                onChange={(e) => {}}
                config={paymentOptionConfig}
              ></CustomDropdown>
            </LabelFieldPair>
          </div>
        </div>
        <ActionBar>
          <SubmitBar
            label={t("CS_GENERATE_RECEIPT")}
            onSubmit={() => {
              onSubmitCase();
            }}
          />
        </ActionBar>
      </div>
    </React.Fragment>
  );
};

export default ViewPaymentDetails;
