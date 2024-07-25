import React, { useMemo, useState } from "react";
import { FormComposerV2, Header, Loader, Toast, BackButton } from "@egovernments/digit-ui-react-components";
import { CustomArrowDownIcon, RightArrow } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";
import AdmissionActionModal from "./AdmissionActionModal";
import { Redirect, useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { DRISTIService } from "../../../services";
import { formatDate } from "../../citizen/FileCase/CaseType";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import { selectParticipantConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import { admitCaseSubmitConfig, scheduleCaseSubmitConfig, sendBackCase } from "../../citizen/FileCase/Config/admissionActionConfig";
import { OrderTypes, OrderWorkflowAction } from "../../../Utils/orderWorkflow";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import { getAllAssignees } from "../../citizen/FileCase/EfilingValidationUtils";
import { Urls } from "../../../hooks";

function CaseFileAdmission({ t, path }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [modalInfo, setModalInfo] = useState(null);
  const [submitModalInfo, setSubmitModalInfo] = useState(null);
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [caseAdmitLoader, setCaseADmitLoader] = useState(false);
  const { data: caseFetchResponse, isLoading } = useSearchCaseService(
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
    Boolean(caseId)
  );
  const caseDetails = useMemo(() => caseFetchResponse?.criteria?.[0]?.responseList?.[0] || null, [caseFetchResponse]);

  const formConfig = useMemo(() => {
    if (!caseDetails) return null;
    return [
      ...reviewCaseFileFormConfig?.map((form) => {
        return {
          ...form,
          body: form.body?.map((section) => {
            return {
              ...section,
              populators: {
                ...section.populators,
                inputs: section.populators.inputs?.map((input) => {
                  delete input.data;
                  return {
                    ...input,
                    data: caseDetails?.additionalDetails?.[input?.key]?.formdata || caseDetails?.caseDetails?.[input?.key]?.formdata || {},
                  };
                }),
              },
            };
          }),
        };
      }),
    ];
  }, [caseDetails]);

  const updateCaseDetails = async (action, data = {}) => {
    const newcasedetails = { ...caseDetails, additionalDetails: { ...caseDetails.additionalDetails, judge: data } };

    return DRISTIService.caseUpdateService(
      {
        cases: {
          ...newcasedetails,
          linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
          workflow: {
            ...caseDetails?.workflow,
            action,
            ...(action === "SEND_BACK" && { assignes: getAllAssignees(caseDetails) || [] }),
          },
        },
        tenantId,
      },
      tenantId
    );
  };

  const caseInfo = [
    {
      key: "CASE_NUMBER",
      value: caseDetails?.filingNumber,
    },
    {
      key: "CASE_CATEGORY",
      value: caseDetails?.caseCategory,
    },
    {
      key: "CASE_TYPE",
      value: "NIA S138",
    },
    {
      key: "COURT_NAME",
      value: "Kerala City Criminal Court",
    },
    {
      key: "SUBMITTED_ON",
      value: caseDetails?.filingDate,
    },
  ];
  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata.data)) {
      setFormdata((prev) => {
        return { ...prev, data: formData };
      });
    }
  };
  const onSubmit = () => {
    setSubmitModalInfo({ ...admitCaseSubmitConfig, caseInfo: caseInfo });

    setModalInfo({ type: "admitCase", page: "0" });
    setShowModal(true);
  };
  const onSaveDraft = () => {
    setShowModal(true);
    setSubmitModalInfo({
      ...scheduleCaseSubmitConfig,
      caseInfo: [...caseInfo],
      shortCaseInfo: [
        {
          key: "CASE_NUMBER",
          value: caseDetails?.caseNumber,
        },
        {
          key: "COURT_NAME",
          value: "Kerala City Criminal Court",
        },
        {
          key: "CASE_TYPE",
          value: "NIA S138",
        },
      ],
    });
    setModalInfo({ type: "schedule", page: "0" });
  };
  const onSendBack = () => {
    setSubmitModalInfo({
      ...sendBackCase,
      caseInfo: [{ key: "CASE_FILE_NUMBER", value: caseDetails?.filingNumber }],
    });
    setShowModal(true);
    setModalInfo({ type: "sendCaseBack", page: "0" });
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const handleSendCaseBack = (props) => {
    updateCaseDetails("SEND_BACK", { comment: props?.commentForLitigant }).then((res) => {
      setModalInfo({ ...modalInfo, page: 1 });
    });
  };

  const fetchBasicUserInfo = async () => {
    const individualData = await window?.Digit.DRISTIService.searchIndividualUser(
      {
        Individual: {
          userUuid: [caseDetails?.auditDetails?.createdBy],
        },
      },
      { tenantId, limit: 1000, offset: 0 },
      "",
      caseDetails?.auditDetails?.createdBy
    );

    return individualData?.Individual?.[0]?.individualId;
  };

  const handleAdmitCase = async () => {
    setCaseADmitLoader(true);
    const individualId = await fetchBasicUserInfo();
    let documentList = [];
    documentList = [
      ...documentList,
      ...caseDetails?.caseDetails?.chequeDetails?.formdata?.map((form) => form?.data?.bouncedChequeFileUpload?.document),
      ...caseDetails?.caseDetails?.chequeDetails?.formdata?.map((form) => form?.data?.depositChequeFileUpload?.document),
      ...caseDetails?.caseDetails?.chequeDetails?.formdata?.map((form) => form?.data?.returnMemoFileUpload?.document),
      ...caseDetails?.caseDetails?.debtLiabilityDetails?.formdata?.map((form) => form?.data?.debtLiabilityFileUpload?.document),
      ...caseDetails?.caseDetails?.demandNoticeDetails?.formdata?.map((form) => form?.data?.legalDemandNoticeFileUpload?.document),
      ...caseDetails?.caseDetails?.demandNoticeDetails?.formdata?.map((form) => form?.data?.proofOfAcknowledgmentFileUpload?.document),
      ...caseDetails?.caseDetails?.demandNoticeDetails?.formdata?.map((form) => form?.data?.proofOfDispatchFileUpload?.document),
      ...caseDetails?.caseDetails?.demandNoticeDetails?.formdata?.map((form) => form?.data?.proofOfReplyFileUpload?.document),
      ...caseDetails?.additionalDetails?.prayerSwornStatement?.formdata?.map((form) => form?.data?.memorandumOfComplaint?.document),
      ...caseDetails?.additionalDetails?.prayerSwornStatement?.formdata?.map((form) => form?.data?.prayerForRelief?.document),
      ...caseDetails?.additionalDetails?.prayerSwornStatement?.formdata?.map((form) => form?.data?.swornStatement?.document),
      ...caseDetails?.additionalDetails?.respondentDetails?.formdata?.map((form) => form?.data?.inquiryAffidavitFileUpload?.document),
      ...caseDetails?.additionalDetails?.advocateDetails?.formdata?.map((form) => form?.data?.vakalatnamaFileUpload?.document),
    ].flat();

    await Promise.all(
      documentList
        ?.filter((data) => data)
        ?.map(async (data) => {
          await DRISTIService.createEvidence({
            artifact: {
              artifactType: "DOCUMENTARY",
              sourceType: "COMPLAINANT",
              sourceID: individualId,
              caseId: caseDetails?.id,
              filingNumber: caseDetails?.filingNumber,
              tenantId,
              comments: [],
              file: {
                documentType: data?.fileType || data?.documentType,
                fileStore: data?.fileStore,
                fileName: data?.fileName,
                documentName: data?.documentName,
              },
              workflow: {
                action: "TYPE DEPOSITION",
                documents: [
                  {
                    documentType: data?.documentType,
                    fileName: data?.fileName,
                    documentName: data?.documentName,
                    fileStoreId: data?.fileStore,
                  },
                ],
              },
            },
          });
        })
    );

    updateCaseDetails("ADMIT", formdata).then((res) => {
      setModalInfo({ ...modalInfo, page: 1 });
    });
    setCaseADmitLoader(false);
  };
  const handleScheduleCase = (props) => {
    setSubmitModalInfo({
      ...scheduleCaseSubmitConfig,
      caseInfo: [
        ...caseInfo,
        {
          key: "CS_NEXT_HEARING",
          value: props.date,
        },
      ],
    });
    updateCaseDetails("SCHEDULE_ADMISSION_HEARING", props).then((res) => {
      setModalInfo({ ...modalInfo, page: 2 });
    });
  };

  const handleScheduleNextHearing = () => {
    const reqBody = {
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber: caseDetails?.cnrNumber,
        filingNumber: caseDetails?.filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: OrderTypes.SCHEDULE_OF_HEARING_DATE,
        status: "",
        isActive: true,
        workflow: {
          action: OrderWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {
          formdata: {
            orderType: {
              code: OrderTypes.SCHEDULE_OF_HEARING_DATE,
              type: OrderTypes.SCHEDULE_OF_HEARING_DATE,
              name: `ORDER_TYPE_${OrderTypes.SCHEDULE_OF_HEARING_DATE}`,
            },
          },
        },
      },
    };
    DRISTIService.customApiService(Urls.dristi.ordersCreate, reqBody, { tenantId })
      .then(() => {
        history.push(`/digit-ui/employee/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}`, {
          caseId: caseId,
          tab: "Orders",
        });
      })
      .catch();
  };

  const updateConfigWithCaseDetails = (config, caseDetails) => {
    const litigantsNames = caseDetails.litigants?.map((litigant) => {
      return { name: litigant.additionalDetails.fullName, individualId: litigant.individualId };
    });

    config.checkBoxes.forEach((checkbox) => {
      if (checkbox.key === "Litigants") {
        checkbox.dependentFields = litigantsNames;
      }
    });

    return config;
  };

  const updatedConfig = caseDetails && updateConfigWithCaseDetails(selectParticipantConfig, caseDetails);
  const sidebar = ["litigentDetails", "caseSpecificDetails", "additionalDetails"];
  const labels = {
    litigentDetails: "CS_LITIGENT_DETAILS",
    caseSpecificDetails: "CS_CASE_SPECIFIC_DETAILS",
    additionalDetails: "CS_ADDITIONAL_DETAILS",
  };

  if (!caseId || (caseDetails && caseDetails?.status === CaseWorkflowState.CASE_ADMITTED)) {
    return <Redirect to="/" />;
  }

  if (isLoading) {
    return <Loader />;
  }
  return (
    <div className={"case-and-admission"}>
      <div className="view-case-file">
        <div className="file-case">
          <div className="file-case-side-stepper">
            <div className="file-case-select-form-section">
              {sidebar?.map((key, index) => (
                <div className="accordion-wrapper">
                  <div key={index} className="accordion-title">
                    <div>{`${index + 1}. ${t(labels[key])}`}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="file-case-form-section">
            <BackButton style={{ marginBottom: 0 }}></BackButton>
            <div className="employee-card-wrapper">
              <div className="header-content">
                <div className="header-details">
                  <Header>{caseDetails?.caseTitle}</Header>
                  <div className="header-icon" onClick={() => {}}>
                    <CustomArrowDownIcon />
                  </div>
                </div>
              </div>
              <CustomCaseInfoDiv t={t} data={caseInfo} style={{ margin: "24px 0px" }} />
              <FormComposerV2
                label={t("CS_ADMIT_CASE")}
                config={formConfig}
                onSubmit={onSubmit}
                // defaultValues={}
                onSecondayActionClick={onSaveDraft}
                defaultValues={{}}
                onFormValueChange={onFormValueChange}
                cardStyle={{ minWidth: "100%" }}
                isDisabled={isDisabled}
                cardClassName={`e-filing-card-form-style review-case-file`}
                secondaryLabel={t("CS_SCHEDULE_ADMISSION_HEARING")}
                showSecondaryLabel={caseDetails?.status !== CaseWorkflowState.ADMISSION_HEARING_SCHEDULED}
                actionClassName="case-file-admission-action-bar"
                showSkip={caseDetails?.status !== CaseWorkflowState.ADMISSION_HEARING_SCHEDULED}
                onSkip={onSendBack}
                skiplabel={t("SEND_BACK_FOR_CORRECTION")}
                noBreakLine
                submitIcon={<RightArrow />}
                skipStyle={{ position: "fixed", left: "20px", bottom: "18px", color: "#007E7E", fontWeight: "700" }}
              />
              {showErrorToast && (
                <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />
              )}
              {showModal && (
                <AdmissionActionModal
                  t={t}
                  setShowModal={setShowModal}
                  setSubmitModalInfo={setSubmitModalInfo}
                  submitModalInfo={submitModalInfo}
                  modalInfo={modalInfo}
                  setModalInfo={setModalInfo}
                  handleSendCaseBack={handleSendCaseBack}
                  handleAdmitCase={handleAdmitCase}
                  path={path}
                  handleScheduleCase={handleScheduleCase}
                  updatedConfig={updatedConfig}
                  tenantId={tenantId}
                  handleScheduleNextHearing={handleScheduleNextHearing}
                  caseAdmitLoader={caseAdmitLoader}
                ></AdmissionActionModal>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CaseFileAdmission;
