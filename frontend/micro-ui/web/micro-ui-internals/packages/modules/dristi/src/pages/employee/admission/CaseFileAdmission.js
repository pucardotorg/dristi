import React, { useMemo, useState } from "react";
import { FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
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
import useGetHearings from "../../../hooks/dristi/useGetHearings";

function CaseFileAdmission({ t, path }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [modalInfo, setModalInfo] = useState(null);
  const [submitModalInfo, setSubmitModalInfo] = useState(null);
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const { data: caseFetchResponse, refetch: refetchCaseData, isLoading } = useSearchCaseService(
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
  const { data: hearingResponse } = useGetHearings(
    {
      hearing: { tenantId },
    },
    { applicationNumber: "", cnrNumber: "" },
    "dristi",
    true
  );
  const hearingDetails = useMemo(() => hearingResponse?.HearingList || null, [hearingResponse]);
  const caseDetails = useMemo(() => caseFetchResponse?.criteria?.[0]?.responseList?.[0] || null, [caseFetchResponse]);
  const complainantFormData = useMemo(() => caseDetails?.additionalDetails?.complaintDetails?.formdata || null, [caseDetails]);
  const respondentFormData = useMemo(() => caseDetails?.additionalDetails?.respondentDetails?.formdata || null, [caseDetails]);

  const formConfig = useMemo(() => {
    if (!caseDetails) return null;
    return [
      ...reviewCaseFileFormConfig.map((form) => {
        return {
          ...form,
          body: form.body.map((section) => {
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
  }, [reviewCaseFileFormConfig, caseDetails]);

  const updateCaseDetails = async (action, data = {}) => {
    const newcasedetails = { ...caseDetails, additionalDetails: { ...caseDetails.additionalDetails, judge: data } };

    return DRISTIService.caseUpdateService(
      {
        cases: {
          ...newcasedetails,
          linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
          filingDate: formatDate(new Date()),
          workflow: {
            ...caseDetails?.workflow,
            action,
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
      value: caseDetails?.caseNumber,
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
  const handleAdmitCase = () => {
    updateCaseDetails("ADMIT", formdata).then((res) => {
      setModalInfo({ ...modalInfo, page: 1 });
    });
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
  const updateConfigWithCaseDetails = (config, caseDetails) => {
    const complainantNames = complainantFormData?.map((form) => {
      const firstName = form?.data?.firstName || "";
      const middleName = form?.data?.middleName || "";
      const lastName = form?.data?.lastName || "";
      return `${firstName} ${middleName} ${lastName}`.trim();
    });

    const respondentNames = respondentFormData?.map((form) => {
      const firstName = form?.data?.respondentFirstName || "";
      const lastName = form?.data?.respondentLastName || "";
      return `${firstName} ${lastName}`.trim();
    });

    config.checkBoxes.forEach((checkbox) => {
      if (checkbox.key === "Compliant") {
        checkbox.dependentFields = complainantNames;
      } else if (checkbox.key === "Respondent") {
        checkbox.dependentFields = respondentNames;
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
  const complainantFirstName = complainantFormData?.[0].data?.firstName;
  const complainantLastName = complainantFormData?.[0].data?.lastName;

  const respondentFirstName = respondentFormData?.[0].data?.respondentFirstName;
  const respondentLastName = respondentFormData?.[0].data?.respondentLastName;

  if (!caseId) {
    return <Redirect to="admission" />;
  }

  if (isLoading) {
    return <Loader />;
  }
  if (showModal) {
    return (
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
        // hearingDetails={hearingDetails}
        tenantId={tenantId}
      ></AdmissionActionModal>
    );
  }

  return (
    <div className={"case-and-admission"}>
      <div className="view-case-file">
        <div className="file-case">
          <div className="file-case-side-stepper">
            <div className="file-case-select-form-section">
              {sidebar.map((key, index) => (
                <div className="accordion-wrapper">
                  <div key={index} className="accordion-title">
                    <div>{`${index + 1}. ${t(labels[key])}`}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="file-case-form-section">
            <div className="employee-card-wrapper">
              <div className="header-content">
                <div className="header-details">
                  <Header>
                    {`${complainantFirstName}  ${complainantLastName}`.trim()} <span style={{ color: "#77787B" }}>vs</span>{" "}
                    {`${respondentFirstName}  ${respondentLastName}`.trim()}
                  </Header>
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
                showSecondaryLabel={true}
                // actionClassName="admission-action-buttons"
                actionClassName="e-filing-action-bar"
                showSkip={true}
                onSkip={onSendBack}
                noBreakLine
                submitIcon={<RightArrow />}
              />
              {showErrorToast && (
                <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CaseFileAdmission;
