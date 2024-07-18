import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import ReactTooltip from "react-tooltip";
import { Header, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configRejectSubmission,
  configsBail,
  configsCaseSettlement,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsIssueOfWarrants,
  configsIssueSummons,
  configsJudgement,
  configsOrderMandatorySubmissions,
  configsOrderSection202CRPC,
  configsOrderSubmissionExtension,
  configsOrderTranferToADR,
  configsOthers,
  configsRejectRescheduleHeadingDate,
  configsRescheduleHearingDate,
  configsScheduleHearingDate,
  configsVoluntarySubmissionStatus,
} from "../../configs/ordersCreateConfig";
import { CustomDeleteIcon } from "../../../../dristi/src/icons/svgIndex";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
import OrderSignatureModal from "../../pageComponents/OrderSignatureModal";
import OrderDeleteModal from "../../pageComponents/OrderDeleteModal";
import useSearchOrdersService from "../../hooks/orders/useSearchOrdersService";
import { ordersService } from "../../hooks/services";
import useSearchCaseService from "../../../../dristi/src/hooks/dristi/useSearchCaseService";
import { CaseWorkflowAction, CaseWorkflowState } from "../../utils/caseWorkflow";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";
import useSearchSubmissionService from "../../../../submissions/src/hooks/submissions/useSearchSubmissionService";

const OutlinedInfoIcon = () => (
  <svg width="19" height="19" viewBox="0 0 19 19" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ position: "absolute", right: -22, top: 0 }}>
    <g clip-path="url(#clip0_7603_50401)">
      <path
        d="M8.70703 5.54232H10.2904V7.12565H8.70703V5.54232ZM8.70703 8.70898H10.2904V13.459H8.70703V8.70898ZM9.4987 1.58398C5.1287 1.58398 1.58203 5.13065 1.58203 9.50065C1.58203 13.8707 5.1287 17.4173 9.4987 17.4173C13.8687 17.4173 17.4154 13.8707 17.4154 9.50065C17.4154 5.13065 13.8687 1.58398 9.4987 1.58398ZM9.4987 15.834C6.00745 15.834 3.16536 12.9919 3.16536 9.50065C3.16536 6.0094 6.00745 3.16732 9.4987 3.16732C12.9899 3.16732 15.832 6.0094 15.832 9.50065C15.832 12.9919 12.9899 15.834 9.4987 15.834Z"
        fill="#3D3C3C"
      />
    </g>
    <defs>
      <clipPath id="clip0_7603_50401">
        <rect width="19" height="19" fill="white" />
      </clipPath>
    </defs>
  </svg>
);

const GenerateOrders = () => {
  const { t } = useTranslation();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const applicationNumber = urlParams.get("applicationNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, _setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formdata, setFormdata] = useState(null);
  const [prevOrder, setPrevOrder] = useState();
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);

  const setSelectedOrder = (orderIndex) => {
    _setSelectedOrder(orderIndex);
    setFormdata(null);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  useEffect(() => {
    if (!filingNumber) {
      history.push("/employee/home/home-pending-task");
    }
  }, []);

  const { data: caseData, isLoading: isCaseDetailsLoading } = useSearchCaseService(
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
    filingNumber
  );

  const { data: applicationData, isLoading: isApplicationDetailsLoading } = useSearchSubmissionService(
    {
      criteria: {
        filingNumber: "F-C.1973.002-2024-000505",
        tenantId: tenantId,
      },
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    filingNumber
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  const cnrNumber = useMemo(() => caseDetails?.cnrNumber, [caseDetails]);

  const complainants = useMemo(() => {
    return caseDetails?.litigants
      ?.filter((item) => item?.partyType === "complainant.primary")
      .map((item) => {
        return { code: item?.additionalDetails?.fullName || "complainants", name: item?.additionalDetails?.fullName || "complainants" };
      });
  }, [caseDetails]);

  const respondants = useMemo(() => {
    // return caseDetails?.litigants
    //   ?.filter((item) => item?.partyType === "respondant.primary")
    //   .map((item) => {
    //     return { code: item?.additionalDetails?.fullName || "Respondent", name: item?.additionalDetails?.fullName || "Respondent" };
    //   });
    return [{ code: "Respondent", name: "Respondent" }];
  }, [caseDetails]);

  const { data: ordersData, refetch: refetchOrdersData, isLoading: isOrdersLoading, isFetching: isOrdersFetching } = useSearchOrdersService(
    { tenantId, criteria: { filingNumber, applicationNumber: "", cnrNumber } },
    { tenantId },
    filingNumber,
    Boolean(filingNumber && cnrNumber)
  );

  const orderList = useMemo(() => ordersData?.list?.filter((item) => item.status === CaseWorkflowState.DRAFT_IN_PROGRESS), [ordersData]);
  const orderType = useMemo(() => formdata?.orderType || {}, [formdata]);
  const currentOrder = useMemo(() => orderList?.[selectedOrder], [orderList, selectedOrder]);

  const modifiedFormConfig = useMemo(() => {
    const configKeys = {
      SECTION_202_CRPC: configsOrderSection202CRPC,
      MANDATORY_SUBMISSIONS_RESPONSES: configsOrderMandatorySubmissions,
      EXTENSION_OF_DOCUMENT_SUBMISSION_DATE: configsOrderSubmissionExtension,
      REFERRAL_CASE_TO_ADR: configsOrderTranferToADR,
      SCHEDULE_OF_HEARING_DATE: configsScheduleHearingDate,
      RESCHEDULE_OF_HEARING_DATE: configsRescheduleHearingDate,
      REJECTION_RESCHEDULE_REQUEST: configsRejectRescheduleHeadingDate,
      APPROVAL_RESCHEDULE_REQUEST: configsRescheduleHearingDate,
      CASE_TRANSFER: configsCaseTransfer,
      SETTLEMENT: configsCaseSettlement,
      SUMMONS: configsIssueSummons,
      BAIL: configsBail,
      WARRANT: configsIssueOfWarrants,
      WITHDRAWAL: configsCaseWithdrawal,
      OTHERS: configsOthers,
      APPROVE_VOLUNTARY_SUBMISSIONS: configsVoluntarySubmissionStatus,
      REJECT_VOLUNTARY_SUBMISSIONS: configRejectSubmission,
      JUDGEMENT: configsJudgement,
    };

    let newConfig = structuredClone(applicationTypeConfig);
    if (orderType?.code && configKeys.hasOwnProperty(orderType?.code)) {
      let orderTypeForm = configKeys[orderType?.code];
      if (orderType?.code === "SECTION_202_CRPC") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "applicationFilledBy") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: complainants ? complainants : [],
                  },
                };
              }
              if (field.key === "detailsSeekedOf") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: respondants,
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType?.code === "SCHEDULE_OF_HEARING_DATE") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "namesOfPartiesRequired") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: complainants ? complainants : [],
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType?.code === "MANDATORY_SUBMISSIONS_RESPONSES") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "submissionParty") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondants],
                  },
                };
              }
              if (field.key === "respondingParty") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: [...complainants, ...respondants],
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      newConfig = [...newConfig, ...orderTypeForm];
    }
    const updatedConfig = newConfig.map((config) => {
      return {
        ...config,
        body: config?.body.map((body) => {
          if (body?.labelChildren === "OutlinedInfoIcon") {
            body.labelChildren = (
              <React.Fragment>
                <span style={{ color: "#77787B", position: "relative" }} data-tip data-for={`${body.label}-tooltip`}>
                  {" "}
                  <OutlinedInfoIcon />
                </span>
                <ReactTooltip id={`${body.label}-tooltip`} place="bottom" content={body?.tooltipValue || ""}>
                  {t(body?.tooltipValue || body.label)}
                </ReactTooltip>
              </React.Fragment>
            );
          }

          if (body?.populators?.validation?.customValidationFn) {
            const customValidations =
              Digit.Customizations[body.populators.validation.customValidationFn.moduleName][
                body.populators.validation.customValidationFn.masterName
              ];

            body.populators.validation = {
              ...body.populators.validation,
              ...customValidations(),
            };
          }
          return {
            ...body,
          };
        }),
      };
    });
    return updatedConfig;
  }, [complainants, orderType?.code, respondants]);

  const defaultValue = useMemo(() => {
    let returnValue = {};
    if (formdata && currentOrder?.additionalDetails?.formdata?.orderType?.code !== formdata?.orderType?.code) {
      returnValue = formdata;
    } else if (currentOrder?.additionalDetails?.formdata) {
      returnValue = structuredClone(currentOrder?.additionalDetails?.formdata);
    } else if (currentOrder?.orderType && applicationNumber) {
      returnValue = {
        orderType: {
          type: currentOrder?.orderType,
          isactive: true,
          code: currentOrder?.orderType,
          name: "ORDER_TYPE_APPROVE_VOLUNTARY_SUBMISSIONS",
        },
      };
    } else {
      returnValue = {};
    }
    // merge returnValue with system filled;

    return returnValue;
  }, [currentOrder, applicationNumber, orderType.code]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, orderindex) => {
    if (formdata?.orderType?.code && formdata?.orderType?.code !== formData?.orderType?.code) {
      setFormdata({ orderType: formData.orderType });
    } else if (JSON.stringify(formData) !== JSON.stringify(formdata)) {
      setFormdata(formData);
    }
    if (Object.keys(formState?.errors).length) {
      setIsSubmitDisabled(true);
    } else {
      setIsSubmitDisabled(false);
    }
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleUpdateOrder = ({ action, oldOrderData, orderType, modal }) => {
    const newAdditionalData =
      action === CaseWorkflowAction.SAVE_DRAFT ? { ...oldOrderData?.additionalDetails, formdata } : { ...oldOrderData?.additionalDetails };
    const updatedreqBody = {
      order: {
        ...oldOrderData,
        createdDate: formatDate(new Date()),
        orderType,
        workflow: {
          action,
          comments: "Creating for order registration",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: newAdditionalData,
      },
    };
    ordersService
      .updateOrder(updatedreqBody, { tenantId })
      .then(() => {
        setPrevOrder(currentOrder);
        refetchOrdersData();
        if (modal !== "deleteModal" && modal !== "issueModal" && modal !== "reviewModal") {
          setShowErrorToast(true);
        }
        if (action === CaseWorkflowAction.ESIGN) {
          setShowSuccessModal(true);
        }
        if (modal === "reviewModal") {
          setShowReviewModal(true);
        }
        setShowsignatureModal(false);
        setDeleteOrderIndex(null);
      })
      .catch(() => {
        refetchOrdersData();
        setShowsignatureModal(false);
        setDeleteOrderIndex(null);
      });
  };

  const handleAddOrder = () => {
    const reqbody = {
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "Bail",
        status: "",
        isActive: true,
        workflow: {
          action: CaseWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {},
      },
    };
    ordersService
      .createOrder(reqbody, { tenantId })
      .then(() => {
        refetchOrdersData();
      })
      .catch(() => {
        refetchOrdersData();
      });
  };

  const handleSaveDraft = ({ modal }) => {
    handleUpdateOrder({
      action: CaseWorkflowAction.SAVE_DRAFT,
      oldOrderData: currentOrder,
      orderType: orderType?.code,
      modal,
    });
  };

  const handleIssueOrder = () => {
    handleUpdateOrder({
      action: CaseWorkflowAction.ESIGN,
      oldOrderData: currentOrder,
      orderType: orderType?.code,
      modal: "issueModal",
    });
  };

  const handleDeleteOrder = () => {
    handleUpdateOrder({
      action: CaseWorkflowAction.ABANDON,
      oldOrderData: orderList[deleteOrderIndex],
      orderType: orderList[deleteOrderIndex].orderType,
      modal: "deleteModal",
    });
    setSelectedOrder((prev) => {
      return deleteOrderIndex && deleteOrderIndex ? prev - 1 : prev;
    });
    setDeleteOrderIndex(null);
  };

  const handleGoBackSignatureModal = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };
  if (isOrdersLoading || isOrdersFetching || isCaseDetailsLoading) {
    return <Loader />;
  }

  return (
    <div className="generate-orders">
      <div className="orders-list-main">
        <div className="add-order-button" onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <React.Fragment>
          {orderList?.map((order, index) => {
            return (
              <div className={`order-item-main ${selectedOrder === index ? "selected-order" : ""}`} onClick={() => setSelectedOrder(index)}>
                <h1>{`${t("CS_ORDER")} ${index + 1}`}</h1>
                {orderList?.length > 1 && (
                  <span
                    onClick={() => {
                      setDeleteOrderIndex(index);
                    }}
                  >
                    <CustomDeleteIcon />
                  </span>
                )}
              </div>
            );
          })}
        </React.Fragment>
      </div>
      <div className="view-order">
        {orderList?.length > 0 && <Header className="order-header">{`${t("CS_ORDER")} ${selectedOrder + 1}`}</Header>}
        {orderList?.length > 0 && modifiedFormConfig && (
          <FormComposerV2
            className={"generate-orders"}
            key={`${selectedOrder}=${orderType.code}`}
            label={t("REVIEW_ORDER")}
            config={modifiedFormConfig}
            defaultValues={defaultValue}
            onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
              onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues);
            }}
            onSubmit={() => {
              handleSaveDraft({ modal: "reviewModal" });
            }}
            onSecondayActionClick={handleSaveDraft}
            secondaryLabel={t("SAVE_AS_DRAFT")}
            showSecondaryLabel={true}
            cardClassName={`order-type-form-composer`}
            actionClassName={"order-type-action"}
            isDisabled={isSubmitDisabled}
          />
        )}
      </div>
      {deleteOrderIndex !== null && (
        <div className="delete-order-warning-modal">
          <OrderDeleteModal
            t={t}
            deleteOrderIndex={deleteOrderIndex}
            setDeleteOrderIndex={setDeleteOrderIndex}
            handleDeleteOrder={handleDeleteOrder}
          />
        </div>
      )}
      {showReviewModal && (
        <OrderReviewModal
          t={t}
          order={currentOrder}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={setShowsignatureModal}
          handleSaveDraft={handleSaveDraft}
        />
      )}
      {showsignatureModal && (
        <OrderSignatureModal t={t} order={currentOrder} handleIssueOrder={handleIssueOrder} handleGoBackSignatureModal={handleGoBackSignatureModal} />
      )}
      {showSuccessModal && <OrderSucessModal t={t} order={prevOrder} setShowSuccessModal={setShowSuccessModal} />}
      {showErrorToast && (
        <Toast
          style={{ backgroundColor: "#00703c", zIndex: "9999999999" }}
          error={true}
          label={t("DRAFT_SAVED_SUCCESSFULLY")}
          isDleteBtn={true}
          onClose={closeToast}
        />
      )}
    </div>
  );
};

export default GenerateOrders;
