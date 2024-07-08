import {
  BackButton,
  CheckSvg,
  CloseButton,
  CloseSvg,
  EditIcon,
  FormComposerV2,
  Header,
  Loader,
  TextInput,
  Toast,
} from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useLocation, Redirect, useHistory } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomArrowDownIcon, FlagIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";
import SendCaseBackModal from "../../../components/SendCaseBackModal";
import SuccessModal from "../../../components/SuccessModal";
import { formatDate } from "../../citizen/FileCase/CaseType";
import { DRISTIService } from "../../../services";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";
import Modal from "../../../components/Modal";

function ViewCaseFile({ t }) {
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
  const isDisabled = useMemo(() => totalErrors?.total > 0);

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
  const defaultScrutinyErrors = useMemo(() => caseDetails?.additionalDetails?.scrutiny || {}, [caseDetails]);
  const state = useMemo(() => caseDetails?.workflow?.action, [caseDetails]);

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
  const updateCaseDetails = async (action, data = {}) => {
    const newcasedetails = {
      ...caseDetails,
      additionalDetails: { ...caseDetails.additionalDetails, scrutiny: data },
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
    updateCaseDetails("VALIDATE").then((res) => {
      setActionModal("caseRegisterSuccess");
    });
  };
  const handleSendCaseBack = () => {
    updateCaseDetails("SEND_BACK", formdata).then((res) => {
      setActionModal("caseSendBackSuccess");
    });
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
  if (isScrutiny && state !== "UNDER_SCRUTINY") {
    // if state is not under scrutiny, don't allow
    // history.push("/digit-ui/employee/dristi/cases");
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
                    <div>{scrutinyErrors[key]?.total ? `${scrutinyErrors[key].total} ${t("CS_ERRORS")}` : t("CS_NO_ERRORS")}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="file-case-form-section">
            <div className="employee-card-wrapper">
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
                      <EditIcon />
                    </div>
                  </div>
                  <div className="header-icon" onClick={() => {}}>
                    <CustomArrowDownIcon />
                  </div>
                </div>
                <CustomCaseInfoDiv data={caseInfo} t={t} />
              </div>
              <FormComposerV2
                label={primaryButtonLabel}
                config={formConfig}
                onSubmit={handlePrimaryButtonClick}
                onSecondayActionClick={handleSecondaryButtonClick}
                defaultValues={defaultScrutinyErrors?.data}
                onFormValueChange={onFormValueChange}
                cardStyle={{ minWidth: "100%" }}
                isDisabled={isDisabled}
                cardClassName={`e-filing-card-form-style review-case-file`}
                secondaryLabel={secondaryButtonLabel}
                showSecondaryLabel={totalErrors?.total > 0}
                actionClassName="e-filing-action-bar"
              />

              <div className="error-flag-class">
                <FlagIcon isError={totalErrors?.total > 0} />
                <h3>
                  {totalErrors.total
                    ? `${totalErrors.inputErrors} ${t("CS_TOTAL_INPUT_ERRORS")} & ${totalErrors.sectionErrors} ${t("CS_TOTAL_SECTION_ERRORS")}`
                    : t("CS_NO_ERRORS")}
                </h3>
              </div>

              {showErrorToast && (
                <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />
              )}
            </div>
          </div>
          <div className="file-case-checklist">
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
              headerBarMain={<Heading label={t("CS_Change_Case_Name")} />}
              className="edit-case-name-modal"
            >
              <h3 className="input-label">{t("CS_CASE_NAME")}</h3>
              <TextInput defaultValue={caseDetails?.caseTitle} type="text" onChange={(e) => setModalCaseName(e.target.value)} />
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
        <div className="file-case-checklist">
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
