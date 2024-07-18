import { BackButton, CheckSvg, CloseSvg, EditIcon, FormComposerV2, Header, Loader, TextInput, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { Redirect, useHistory, useLocation } from "react-router-dom";
import ReactTooltip from "react-tooltip";
import { CaseWorkflowAction, CaseWorkflowState } from "../../../Utils/caseWorkflow";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import Modal from "../../../components/Modal";
import SendCaseBackModal from "../../../components/SendCaseBackModal";
import SuccessModal from "../../../components/SuccessModal";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomArrowDownIcon, FlagIcon } from "../../../icons/svgIndex";
import { DRISTIService } from "../../../services";
import { formatDate } from "../../citizen/FileCase/CaseType";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";

function ViewCaseFile({ t, inViewCase = false }) {
  const history = useHistory();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isScrutiny = roles.some((role) => role.code === "CASE_REVIEWER");
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const [actionModal, setActionModal] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [showEditCaseNameModal, setShowEditCaseNameModal] = useState(false);
  const [newCaseName, setNewCaseName] = useState("");
  const [modalCaseName, setModalCaseName] = useState("");
  const [highlightChecklist, setHighlightChecklist] = useState(false);

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
        if (section[key]?.scrutinyMessage?.FSOError) {
          total++;
          sectionErrors++;
        }
        section[key]?.form?.forEach((item) => {
          Object.keys(item)?.forEach((field) => {
            if (item[field]?.FSOError && field != "image" && field != "title") {
              total++;
              inputErrors++;
            }
          });
        });
      }
    });

    return { total, inputErrors, sectionErrors };
  };

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
  const caseDetails = useMemo(() => caseFetchResponse?.criteria?.[0]?.responseList?.[0] || null, [caseFetchResponse]);

  const defaultScrutinyErrors = useMemo(() => {
    return caseDetails?.additionalDetails?.scrutiny || {};
  }, [caseDetails]);

  const isPrevScrutiny = useMemo(() => {
    return Object.keys(defaultScrutinyErrors).length > 0;
  }, [defaultScrutinyErrors]);

  function mergeErrors(formdata, defaultScrutinyErrors) {
    // Helper function to handle the comparison and merging of objects
    function compareAndReplace(formDataNode, defaultNode) {
      // Iterate over each key in the formdata node
      for (let key in formDataNode) {
        // Check if the key exists in both formdata and defaultScrutinyErrors
        if (defaultNode?.hasOwnProperty(key)) {
          // If the value is an object, recursively compare and replace
          if (typeof formDataNode[key] === "object" && formDataNode[key] !== null) {
            compareAndReplace(formDataNode[key], defaultNode[key]);
          } else {
            // If the value is a string (for FSOError and scrutinyMessage)
            if (key === "FSOError" || key === "scrutinyMessage") {
              if (formDataNode[key] === defaultNode[key]) {
                formDataNode[key] = "";
              } else {
                formDataNode[key] = formDataNode[key];
              }
            }
          }
        }
      }
    }
    // Clone the formdata object to avoid modifying the original one
    const result = JSON.parse(JSON.stringify(formdata));
    // Start the comparison and replacement
    compareAndReplace(result.data, defaultScrutinyErrors.data);
    return result;
  }

  const newScrutinyData = useMemo(() => {
    return mergeErrors(formdata, defaultScrutinyErrors);
  }, [formdata, defaultScrutinyErrors]);

  const scrutinyErrors = useMemo(() => {
    const errorCount = {};
    for (const key in newScrutinyData?.data) {
      if (typeof newScrutinyData.data[key] === "object" && newScrutinyData.data[key] !== null) {
        if (!errorCount[key]) {
          errorCount[key] = { total: 0, sectionErrors: 0, inputErrors: 0 };
        }
        const temp = countSectionErrors(newScrutinyData.data[key]);
        errorCount[key] = {
          total: errorCount[key].total + temp.total,
          sectionErrors: errorCount[key].sectionErrors + temp.sectionErrors,
          inputErrors: errorCount[key].inputErrors + temp.inputErrors,
        };
      }
    }
    return errorCount;
  }, [newScrutinyData]);

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
  const isDisabled = useMemo(() => totalErrors?.total > 0);

  const state = useMemo(() => caseDetails?.status, [caseDetails]);
  const formConfig = useMemo(() => {
    if (!caseDetails) return null;
    return [
      ...reviewCaseFileFormConfig.map((form) => {
        return {
          ...form,
          body: form.body.map((section) => {
            return {
              ...section,
              isPrevScrutiny,
              populators: {
                ...section.populators,
                inputs: section.populators.inputs?.map((input) => {
                  delete input.data;
                  return {
                    ...input,
                    data: caseDetails?.additionalDetails?.[input?.key]?.formdata || caseDetails?.caseDetails?.[input?.key]?.formdata || {},
                    prevErrors: defaultScrutinyErrors?.data?.[section.key]?.[input.key] || {},
                  };
                }),
              },
            };
          }),
        };
      }),
    ];
  }, [reviewCaseFileFormConfig, caseDetails, defaultScrutinyErrors]);

  const primaryButtonLabel = useMemo(() => {
    if (isScrutiny) {
      return "CS_REGISTER_CASE";
    }
    //write admission condition here
  }, [isScrutiny]);
  const secondaryButtonLabel = useMemo(() => {
    if (isScrutiny) {
      return "CS_SEND_BACK";
    }
  }, [isScrutiny]);

  const updateCaseDetails = async (action) => {
    const scrutinyObj = action === CaseWorkflowAction.VALIDATE ? {} : CaseWorkflowAction.SEND_BACK && isPrevScrutiny ? newScrutinyData : formdata;
    const newcasedetails = {
      ...caseDetails,
      additionalDetails: { ...caseDetails.additionalDetails, scrutiny: scrutinyObj },
      caseTitle: newCaseName !== "" ? newCaseName : caseDetails?.caseTitle,
    };

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

  const handlePrimaryButtonClick = () => {
    if (isScrutiny) {
      // setActionModal("sendCaseBackPotential");
      setActionModal("registerCase");
    }
    // Write isAdmission condition here
  };
  const handleSecondaryButtonClick = () => {
    if (isScrutiny) {
      setActionModal("sendCaseBack");
    }
    // Write isAdmission condition here
  };
  const handleNextCase = () => {
    setActionModal(false);
    history.push("/digit-ui/employee/dristi/cases");
  };
  const handleAllocationJudge = () => {
    setActionModal(false);
    history.push("/digit-ui/employee/dristi/cases");
  };
  const handleCloseSucessModal = () => {
    setActionModal(false);
    history.push("/digit-ui/employee/dristi/cases");
  };
  const handleRegisterCase = () => {
    updateCaseDetails(CaseWorkflowAction.VALIDATE).then((res) => {
      setActionModal("caseRegisterSuccess");
    });
  };
  const handleSendCaseBack = () => {
    updateCaseDetails(CaseWorkflowAction.SEND_BACK).then((res) => {
      setActionModal("caseSendBackSuccess");
    });
  };
  const handlePotentialConfirm = () => {
    setActionModal("caseRegisterPotential");
  };
  const handleCloseModal = () => {
    setActionModal(false);
    setHighlightChecklist(true);
    setTimeout(() => {
      setHighlightChecklist(false);
    }, 2000);
  };

  if (!caseId) {
    return <Redirect to="cases" />;
  }

  if (isLoading) {
    return <Loader />;
  }
  if (isScrutiny && state !== CaseWorkflowState.UNDER_SCRUTINY) {
    history.push("/digit-ui/employee/dristi/cases");
  }
  const sidebar = ["litigentDetails", "caseSpecificDetails", "additionalDetails"];
  const labels = {
    litigentDetails: "CS_LITIGENT_DETAILS",
    caseSpecificDetails: "CS_CASE_SPECIFIC_DETAILS",
    additionalDetails: "CS_ADDITIONAL_DETAILS",
  };
  const checkList = [
    "CS_SPELLING_MISTAKES",
    "CS_WRONG_INPUTS",
    "CS_MISSING_DETAILS",
    "CS_FIELDS_NO_MATCHUP",
    "CS_WRONG_DOCUMENT",
    "CS_POOR_UPLOAD",
    "CS_WRONG_JURISDICTION",
    "CS_PHOTO_MISMATCH",
  ];
  const caseInfo = [
    {
      key: "CASE_CATEGORY",
      value: caseDetails?.caseCategory,
    },
    {
      key: "CASE_TYPE",
      value: "NIA S138",
    },
    {
      key: "SUBMITTED_ON",
      value: caseDetails?.filingDate,
    },
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

  const scrollToHeading = (heading) => {
    const scroller = Array.from(document.querySelectorAll(".label-field-pair .accordion-title")).find((el) => el.textContent === heading);
    scroller.scrollIntoView({ block: "center", behavior: "smooth" });
  };

  return (
    <div className={"case-and-admission"}>
      <div className="view-case-file">
        <div className="file-case">
          <div className="file-case-side-stepper">
            <div className="file-case-select-form-section">
              {sidebar.map((key, index) => (
                <div className="accordion-wrapper">
                  <div key={index} className="accordion-title" onClick={() => scrollToHeading(`${index + 1}. ${t(labels[key])}`)}>
                    <div>{`${index + 1}. ${t(labels[key])}`}</div>
                    {!inViewCase && <div>{scrutinyErrors[key]?.total ? `${scrutinyErrors[key].total} ${t("CS_ERRORS")}` : t("CS_NO_ERRORS")}</div>}
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="file-case-form-section">
            <div className="employee-card-wrapper">
              {!inViewCase && (
                <div>
                  <div className="back-button-home">
                    <BackButton />
                  </div>
                  <div className="header-content">
                    <div className="header-details">
                      <div className="header-title-icon">
                        <Header>
                          {t("Review Case")}: {newCaseName !== "" ? newCaseName : caseDetails?.caseTitle}
                        </Header>
                        <div
                          className="case-edit-icon"
                          onClick={() => {
                            setShowEditCaseNameModal(true);
                          }}
                        >
                          <React.Fragment>
                            <span style={{ color: "#77787B", position: "relative" }} data-tip data-for={`Click`}>
                              {" "}
                              <EditIcon />
                            </span>
                            <ReactTooltip id={`Click`} place="bottom" content={t("CS_CLICK_TO_EDIT") || ""}>
                              {t("CS_CLICK_TO_EDIT")}
                            </ReactTooltip>
                          </React.Fragment>
                        </div>
                      </div>
                      <div className="header-icon" onClick={() => {}}>
                        <CustomArrowDownIcon />
                      </div>
                    </div>
                    <CustomCaseInfoDiv data={caseInfo} t={t} />
                  </div>
                </div>
              )}
              <FormComposerV2
                label={primaryButtonLabel}
                config={formConfig}
                onSubmit={handlePrimaryButtonClick}
                onSecondayActionClick={handleSecondaryButtonClick}
                defaultValues={structuredClone(defaultScrutinyErrors?.data)}
                onFormValueChange={onFormValueChange}
                cardStyle={{ minWidth: "100%" }}
                isDisabled={isDisabled}
                cardClassName={`e-filing-card-form-style review-case-file`}
                secondaryLabel={secondaryButtonLabel}
                showSecondaryLabel={totalErrors?.total > 0}
                actionClassName="e-filing-action-bar"
              />

              {!inViewCase && (
                <div className="error-flag-class">
                  <FlagIcon isError={totalErrors?.total > 0} />
                  <h3>
                    {totalErrors.total
                      ? `${totalErrors.inputErrors} ${t("CS_TOTAL_INPUT_ERRORS")} & ${totalErrors.sectionErrors} ${t("CS_TOTAL_SECTION_ERRORS")}`
                      : t("CS_NO_ERRORS")}
                  </h3>
                </div>
              )}

              {showErrorToast && (
                <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />
              )}
            </div>
          </div>
          {!inViewCase && (
            <div className={highlightChecklist ? "file-case-checklist-highlight" : "file-case-checklist"}>
              <div className="checklist-main">
                <h3 className="checklist-title">{t("CS_CHECKLIST_HEADER")}</h3>
                {checkList.map((item, index) => {
                  return (
                    <div className="checklist-item" key={index}>
                      <div className="item-logo">
                        <CheckSvg />
                      </div>
                      <h3 className="item-text">{t(item)}</h3>
                    </div>
                  );
                })}
              </div>
            </div>
          )}

          {showEditCaseNameModal && (
            <Modal
              headerBarEnd={
                <CloseBtn
                  onClick={() => {
                    setShowEditCaseNameModal(false);
                  }}
                />
              }
              // actionCancelLabel={t(actionCancelLabel)}
              actionCancelOnSubmit={() => setShowEditCaseNameModal(false)}
              actionSaveLabel={t("CS_COMMON_CONFIRM")}
              actionSaveOnSubmit={() => {
                setNewCaseName(modalCaseName);
                setShowEditCaseNameModal(false);
              }}
              formId="modal-action"
              headerBarMain={<Heading label={t("CS_CHANGE_CASE_NAME")} />}
              className="edit-case-name-modal"
            >
              <h3 className="input-label">{t("CS_CASE_NAME")}</h3>
              <TextInput defaultValue={newCaseName || caseDetails?.caseTitle} type="text" onChange={(e) => setModalCaseName(e.target.value)} />
            </Modal>
          )}
          {actionModal == "sendCaseBack" && (
            <SendCaseBackModal
              actionCancelLabel={"CS_COMMON_BACK"}
              actionSaveLabel={"CS_COMMON_CONFIRM"}
              t={t}
              totalErrors={totalErrors?.total || 0}
              onCancel={handleCloseModal}
              onSubmit={handleSendCaseBack}
              heading={"CS_SEND_CASE_BACK_FOR_CORRECTION"}
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
              heading={"CS_REGISTER_CASE_CONFIRMATION"}
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
              heading={"CS_SEND_CASE_BACK_FOR_CORRECTION"}
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
              heading={"CS_SEND_CASE_BACK_FOR_CORRECTION"}
              type="sendCaseBackPotential"
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
              data={{
                caseId: caseDetails?.filingNumber,
                caseName: newCaseName !== "" ? newCaseName : caseDetails?.caseTitle,
                errorsMarked: totalErrors.total,
              }}
            />
          )}
        </div>
        {actionModal == "sendCaseBack" && (
          <SendCaseBackModal
            actionCancelLabel={"CS_COMMON_BACK"}
            actionSaveLabel={"CS_COMMON_CONFIRM"}
            t={t}
            totalErrors={totalErrors?.total || 0}
            onCancel={handleCloseModal}
            onSubmit={handleSendCaseBack}
            heading={"CS_SEND_CASE_BACK_FOR_CORRECTION"}
            type="sendCaseBack"
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
            heading={"CS_SEND_CASE_BACK_FOR_CORRECTION"}
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
            heading={"CS_SEND_CASE_BACK_FOR_CORRECTION"}
            type="sendCaseBackPotential"
          />
        )}

        {actionModal === "caseSendBackSuccess" && (
          <SuccessModal
            header={"Vaibhav"}
            t={t}
            actionCancelLabel={"BACK_TO_HOME"}
            actionSaveLabel={"NEXT_CASE"}
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
