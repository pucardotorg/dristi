import { CheckSvg, FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useLocation, Redirect } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";
import SendCaseBackModal from "../../../components/SendCaseBackModal";
import SuccessModal from "../../../components/SuccessModal";
import { formatDate } from "../../citizen/FileCase/CaseType";

function ViewCaseFile({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const [actionModal, setActionModal] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata.data)) {
      setFormdata((prev) => {
        return { ...prev, data: formData };
      });
    }
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const countSectionErrors = (section) => {
    let total = 0;
    let sectionErrors = 0;
    let inputErrors = 0;
    Object.keys(section)?.forEach((key) => {
      if (section[key]) {
        if (section[key]?.scrutinyMessage) {
          total++;
          sectionErrors++;
        }
        section[key]?.form?.forEach((item) => {
          Object.keys(item)?.forEach((field) => {
            if (item[field]?.FSOError) {
              total++;
              inputErrors++;
            }
          });
        });
      }
    });

    return { total, inputErrors, sectionErrors };
  };

  const scrutinyErrors = useMemo(() => {
    const errorCount = {};
    for (const key in formdata?.data) {
      if (typeof formdata.data[key] === "object" && formdata.data[key] !== null) {
        if (!errorCount[key]) {
          errorCount[key] = { total: 0, sectionErrors: 0, inputErrors: 0 };
        }
        const temp = countSectionErrors(formdata.data[key]);
        errorCount[key] = {
          total: errorCount[key].total + temp.total,
          sectionErrors: errorCount[key].sectionErrors + temp.sectionErrors,
          inputErrors: errorCount[key].inputErrors + temp.inputErrors,
        };
      }
    }
    return errorCount;
  }, [formdata]);

  const totalErrors = useMemo(() => {
    let total = 0;
    let sectionErrors = 0;
    let inputErrors = 0;

    for (const key in scrutinyErrors) {
      total += scrutinyErrors[key].total || 0;
      sectionErrors += scrutinyErrors[key].sectionErrors || 0;
      inputErrors += scrutinyErrors[key].inputErrors || 0;
    }

    return {
      total,
      sectionErrors,
      inputErrors,
    };
  }, [scrutinyErrors]);

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
  const caseData = useMemo(() => caseFetchResponse?.criteria?.[0]?.responseList?.[0] || null, [caseFetchResponse]);

  const formConfig = useMemo(() => {
    if (!caseData) return null;
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
                    data: caseData?.additionalDetails?.[input?.key]?.formdata || caseData?.caseDetails?.[input?.key]?.formdata || {},
                  };
                }),
              },
            };
          }),
        };
      }),
    ];
  }, [reviewCaseFileFormConfig, caseData]);

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const updateCaseDetails = (data) => {
    const newcasedetails = { ...caseDetails, additionalDetails: { ...caseDetails.additionalDetails, scrutiny: data } };
    const reqbody = {
      cases: {
        ...newcasedetails,
        linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
        filingDate: formatDate(new Date()),
        workflow: {
          ...caseDetails?.workflow,
          action: "UNDER_SCRUTINY",
        },
      },
      tenantId,
    };

    // return DRISTIService.caseUpdateService(
    //   {
    //     cases: {
    //       newcasedetails,
    //       linkedCases: caseDetails?.linkedCases ? caseDetails?.linkedCases : [],
    //       filingDate: formatDate(new Date()),
    //       workflow: {
    //         ...caseDetails?.workflow,
    //         action: "UNDER_SCRUTINY",
    //       },
    //     },
    //     tenantId,
    //   },
    //   tenantId
    // );
  };

  const handleRegisterClick = () => {
    // setActionModal("sendCaseBackPotential");
    setActionModal("registerCase");
  };
  const handleSendBackClick = () => {
    setActionModal("sendCaseBack");
  };
  const handleNextCase = () => {
    setActionModal(false);
  };
  const handleAllocationJudge = () => {
    setActionModal(false);
  };
  const handleCloseSucessModal = () => {
    setActionModal(false);
  };
  const handleRegisterCase = () => {
    setActionModal("caseRegisterSuccess");
  };
  const handleSendCaseBack = () => {
    let body = {};
    for (const key in formdata.data) {
      body = { ...body, ...formdata.data[key] };
    }
    updateCaseDetails(body);
    setActionModal("caseSendBackSuccess");
  };
  const handlePotentialConfirm = () => {
    setActionModal("caseRegisterPotential");
  };
  const handleCloseModal = () => {
    setActionModal(false);
  };

  if (!caseId) {
    return <Redirect to="cases" />;
  }

  if (isLoading) {
    return <Loader />;
  }
  const sidebar = ["litigentDetails", "caseSpecificDetails", "additionalDetails"];
  const labels = { litigentDetails: "CS_LITIGENT_DETAIL", caseSpecificDetails: "CS_CASE_SPECIFIC_DETAIL", additionalDetails: "CS_ADDITIONAL_DETAIL" };
  return (
    <div className="view-case-file">
      <div className="file-case">
        <div className="file-case-side-stepper">
          <div className="file-case-select-form-section">
            {sidebar.map((key, index) => (
              <div className="accordion-wrapper">
                <div key={index} className="accordion-title">
                  <div>{`${index + 1}. ${labels[key]}`}</div>
                  <div>{scrutinyErrors[key]?.total ? `${scrutinyErrors[key].total} ${t("CS_ERRORS")}` : t("CS_NO_ERRORS")}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="file-case-form-section">
          <div className="employee-card-wrapper">
            <div className="header-content">
              <div className="header-details">
                <Header>{t("Review Case")}</Header>
                <div className="header-icon" onClick={() => { }}>
                  <CustomArrowDownIcon />
                </div>
              </div>
            </div>
            <FormComposerV2
              label={t("CS_REGISTER_CASE")}
              config={formConfig}
              onSubmit={handleRegisterClick}
              onSecondayActionClick={handleSendBackClick}
              defaultValues={{}}
              onFormValueChange={onFormValueChange}
              cardStyle={{ minWidth: "100%" }}
              isDisabled={isDisabled}
              cardClassName={`e-filing-card-form-style review-case-file`}
              secondaryLabel={t("CS_SEND_BACK")}
              showSecondaryLabel={true}
              actionClassName="e-filing-action-bar"
            />

            {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
          </div>
        </div>
        <div className="file-case-checklist">
          <div className="checklist-main">
            <h3 className="checklist-title">
              Make Sure you Check For
            </h3>
            <div className="checklist-item">
              <div className="item-logo">
                <CheckSvg />
              </div>
              <h3 className="item-text">Spelling mistakes</h3>
            </div>
            <div className="checklist-item">
              <div className="item-logo">
                <CheckSvg />
              </div>
              <h3 className="item-text">Spelling mistakes</h3>
            </div>
            <div className="checklist-item">
              <div className="item-logo">
                <CheckSvg />
              </div>
              <h3 className="item-text">Spelling mistakes</h3>
            </div>
          </div>
        </div>
        {actionModal == "sendCaseBack" && (
          <SendCaseBackModal
            actionCancelLabel={"CS_COMMON_BACK"}
            actionSaveLabel={"CS_COMMON_CONFIRM"}
            t={t}
            totalErrors={totalErrors?.total || 0}
            onCancel={handleCloseModal}
            onSubmit={handleSendCaseBack}
            heading={"CS_SEND_CASE_BACK"}
            type="sendCaseBack"
          />
        )}
        {actionModal == "registerCase" && (
          <SendCaseBackModal
            actionCancelLabel={"CS_COMMON_BACK"}
            actionSaveLabel={"CS_COMMON_CONFIRM"}
            t={t}
            totalErrors={totalErrors?.total || 0}
            onCancel={handleCloseModal}
            onSubmit={handleRegisterCase}
            heading={"CS_REGISTER_CASE"}
            type="registerCase"
          />
        )}

        {actionModal == "sendCaseBackPotential" && (
          <SendCaseBackModal
            actionCancelLabel={"CS_NO_REGISTER_CASE"}
            actionSaveLabel={"CS_COMMON_CONFIRM"}
            t={t}
            totalErrors={totalErrors?.total || 0}
            handleCloseModal={handleCloseModal}
            onCancel={handlePotentialConfirm}
            onSubmit={handleSendCaseBack}
            heading={"CS_SEND_CASE_BACK"}
            type="sendCaseBackPotential"
          />
        )}
        {actionModal == "caseRegisterPotential" && (
          <SendCaseBackModal
            actionCancelLabel={"CS_SEE_POTENTIAL_ERRORS"}
            actionSaveLabel={"CS_DELETE_ERRORS_REGISTER"}
            t={t}
            totalErrors={totalErrors?.total || 0}
            onCancel={handleCloseModal}
            onSubmit={handleSendCaseBack}
            heading={"CS_SEND_CASE_BACK"}
            type="sendCaseBackPotential"
          />
        )}

        {actionModal === "caseSendBackSuccess" && (
          <SuccessModal
            header={"Vaibhav"}
            t={t}
            actionCancelLabel={"CS_COMMON_CLOSE"}
            actionSaveLabel={"CS_NEXT_CASE"}
            bannerMessage={"CS_CASE_SENT_BACK_SUCCESS"}
            onCancel={handleCloseSucessModal}
            onSubmit={handleNextCase}
            type={"caseSendBackSuccess"}
            data={{ caseId: "KA92327392232", caseName: "Complainant vs. Respondent", errorsMarked: totalErrors.total }}
          />
        )}

        {actionModal === "caseRegisterSuccess" && (
          <SuccessModal
            header={"Vaibhav"}
            t={t}
            actionCancelLabel={"CS_COMMON_CLOSE"}
            actionSaveLabel={"CS_ALLOCATE_JUDGE"}
            bannerMessage={"CS_CASE_REGISTERED_SUCCESS"}
            onCancel={handleCloseSucessModal}
            onSubmit={handleAllocationJudge}
            type={"caseRegisterSuccess"}
            data={{ caseId: "KA92327392232", caseName: "Complainant vs. Respondent", errorsMarked: totalErrors.total }}
          />
        )}
      </div>
    </div>
  );
}

export default ViewCaseFile;
