import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import ReactTooltip from "react-tooltip";
import { Header, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configRejectSubmission,
  configsAssignDateToRescheduledHearing,
  configsAssignNewHearingDate,
  configsBail,
  configsCaseSettlement,
  configsCaseTransfer,
  configsCaseWithdrawal,
  configsInitiateRescheduleHearingDate,
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
import { ordersService } from "../../hooks/services";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";
import { applicationTypes } from "../../utils/applicationTypes";
import useGetIndividualAdvocate from "../../../../dristi/src/hooks/dristi/useGetIndividualAdvocate";
import { DRISTIService } from "../../../../dristi/src/services";
import isEqual from "lodash/isEqual";
import { OrderWorkflowAction, OrderWorkflowState } from "../../utils/orderWorkflow";
import { Urls } from "../../hooks/services/Urls";
import { SubmissionWorkflowAction, SubmissionWorkflowState } from "../../utils/submissionWorkflow";

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
  const orderNumber = urlParams.get("orderNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, _setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formList, setFormList] = useState([]);
  const [prevOrder, setPrevOrder] = useState();
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const userInfo = Digit.UserService.getUser()?.info || {};
  const history = useHistory();
  const setSelectedOrder = (orderIndex) => {
    _setSelectedOrder(orderIndex);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const { data: caseData, isLoading: isCaseDetailsLoading } = Digit.Hooks.dristi.useSearchCaseService(
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

  const { data: applicationData, isLoading: isApplicationDetailsLoading } = Digit.Hooks.submissions.useSearchSubmissionService(
    {
      criteria: {
        filingNumber: filingNumber,
        tenantId: tenantId,
        applicationNumber: applicationNumber,
      },
      tenantId,
    },
    {},
    applicationNumber,
    applicationNumber
  );
  const applicationDetails = useMemo(() => applicationData?.applicationList?.[0], [applicationData]);

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  const cnrNumber = useMemo(() => caseDetails?.cnrNumber, [caseDetails]);

  const complainants = useMemo(() => {
    return (
      caseDetails?.litigants?.map((item) => {
        return {
          code: item?.additionalDetails?.fullName,
          name: item?.additionalDetails?.fullName,
          uuid: item?.additionalDetails?.uuid,
        };
      }) || []
    );
  }, [caseDetails]);

  const respondants = useMemo(() => {
    // return caseDetails?.litigants
    //   ?.filter((item) => item?.partyType === "respondant.primary")
    //   .map((item) => {
    //     return { code: item?.additionalDetails?.fullName || "Respondent", name: item?.additionalDetails?.fullName || "Respondent" };
    //   });
    return [{ code: "Respondent", name: "Respondent" }];
  }, []);

  const {
    data: ordersData,
    refetch: refetchOrdersData,
    isLoading: isOrdersLoading,
    isFetching: isOrdersFetching,
  } = Digit.Hooks.orders.useSearchOrdersService(
    {
      tenantId,
      criteria: { filingNumber, applicationNumber: "", cnrNumber, status: OrderWorkflowState.DRAFT_IN_PROGRESS },
      pagination: { limit: 1000, offset: 0 },
    },
    { tenantId },
    filingNumber,
    Boolean(filingNumber && cnrNumber)
  );

  const advocateIds = caseDetails.representatives?.map((representative) => {
    return {
      id: representative.advocateId,
    };
  });

  const { data: advocateDetails } = useGetIndividualAdvocate(
    {
      criteria: advocateIds,
    },
    { tenantId: tenantId },
    "DRISTI",
    cnrNumber + filingNumber,
    true
  );

  const defaultIndex = useMemo(() => {
    return formList.findIndex((order) => order.orderNumber === orderNumber);
  }, [formList, orderNumber]);

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const defaultOrderData = useMemo(
    () => ({
      createdDate: formatDate(new Date()),
      tenantId,
      cnrNumber,
      filingNumber,
      statuteSection: {
        tenantId,
      },
      status: "",
      isActive: true,
      workflow: {
        action: OrderWorkflowAction.SAVE_DRAFT,
        comments: "Creating order",
        assignes: [],
        rating: null,
        documents: [{}],
      },
      documents: [],
      additionalDetails: { formdata: {} },
    }),
    [cnrNumber, filingNumber, tenantId]
  );

  useEffect(() => {
    if (!ordersData?.list || ordersData?.list.length < 1) {
      setFormList([defaultOrderData]);
    } else {
      setFormList([...(ordersData?.list || [])].reverse());
    }
  }, [ordersData, defaultOrderData]);

  useEffect(() => {
    refetchOrdersData();
  }, [refetchOrdersData]);

  useEffect(() => {
    if (showErrorToast) {
      const timer = setTimeout(() => {
        setShowErrorToast(false);
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [showErrorToast]);

  useEffect(() => {
    if (defaultIndex && defaultIndex !== -1 && defaultIndex !== selectedOrder) {
      setSelectedOrder(defaultIndex);
    }
  }, [defaultIndex, selectedOrder]);

  const currentOrder = useMemo(() => formList?.[selectedOrder], [formList, selectedOrder]);
  const orderType = useMemo(() => currentOrder?.orderType || {}, [currentOrder]);

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
      INITIATING_RESCHEDULING_OF_HEARING_DATE: configsInitiateRescheduleHearingDate,
      ASSIGNING_DATE_RESCHEDULED_HEARING: configsAssignDateToRescheduledHearing,
      ASSIGNING_NEW_HEARING_DATE: configsAssignNewHearingDate,
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
    let newConfig = currentOrder?.orderNumber
      ? applicationTypeConfig?.map((item) => ({ body: item.body.map((input) => ({ ...input, disable: true })) }))
      : structuredClone(applicationTypeConfig);
    if (orderType && configKeys.hasOwnProperty(orderType)) {
      let orderTypeForm = configKeys[orderType];
      if (orderType === "SECTION_202_CRPC") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "applicationFilledBy") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: complainants,
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
      if (orderType === "SCHEDULE_OF_HEARING_DATE") {
        orderTypeForm = orderTypeForm?.map((section) => {
          return {
            ...section,
            body: section.body.map((field) => {
              if (field.key === "namesOfPartiesRequired") {
                return {
                  ...field,
                  populators: {
                    ...field.populators,
                    options: complainants,
                  },
                };
              }
              return field;
            }),
          };
        });
      }
      if (orderType === "MANDATORY_SUBMISSIONS_RESPONSES") {
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
  }, [complainants, currentOrder, orderType, respondants, t]);

  const defaultValue = useMemo(() => {
    if (currentOrder?.orderType && !currentOrder?.additionalDetails?.formdata) {
      return {
        orderType: {
          code: currentOrder?.orderType,
          type: currentOrder?.orderType,
          name: `ORDER_TYPE_${currentOrder?.orderType}`,
        },
      };
    }
    let updatedFormdata = structuredClone(currentOrder?.additionalDetails?.formdata || {});
    if (applicationNumber && updatedFormdata && typeof updatedFormdata === "object") {
      updatedFormdata.refApplicationId = applicationNumber;
    }
    if (orderType === "WITHDRAWAL") {
      if (applicationDetails?.applicationType === applicationTypes.WITHDRAWAL) {
        updatedFormdata.applicationOnBehalfOf = applicationDetails?.onBehalfOf;
        updatedFormdata.partyType = t(applicationDetails.additionalDetails?.partyType);
        updatedFormdata.reasonForWithdrawal = t(applicationDetails.additionalDetails?.formdata?.reasonForWithdrawal?.code);
        updatedFormdata.applicationStatus = t(applicationDetails?.status);
      }
    }
    if (orderType === "EXTENSION_OF_DOCUMENT_SUBMISSION_DATE") {
      if (applicationDetails?.applicationType === applicationTypes.EXTENSION_SUBMISSION_DEADLINE) {
        updatedFormdata.documentName = applicationDetails?.additionalDetails?.formdata?.documentType?.value;
        updatedFormdata.originalDeadline = applicationDetails.additionalDetails?.formdata?.initialSubmissionDate;
        updatedFormdata.proposedSubmissionDate = applicationDetails.additionalDetails?.formdata?.changedSubmissionDate;
        updatedFormdata.originalSubmissionOrderDate = applicationDetails.additionalDetails?.orderDate;
      }
    }
    return updatedFormdata;
  }, [currentOrder, applicationDetails, orderType]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (formData?.orderType?.code && !isEqual(formData, currentOrder?.additionalDetails?.formdata)) {
      const updatedFormData =
        currentOrder?.additionalDetails?.formdata?.orderType?.code !== formData?.orderType?.code ? { orderType: formData.orderType } : formData;
      setFormList((prev) => {
        return prev?.map((item, index) => {
          return index !== selectedOrder
            ? item
            : {
                ...item,
                orderType: formData?.orderType?.code,
                additionalDetails: { ...item.order?.additionalDetails, formdata: updatedFormData },
              };
        });
      });
    }
    if (Object.keys(formState?.errors).length) {
      setIsSubmitDisabled(true);
    } else {
      setIsSubmitDisabled(false);
    }
  };

  const updateOrder = async (order, action) => {
    try {
      return await ordersService.updateOrder({ order: { ...order, workflow: { ...order.workflow, action, documents: [{}] } } }, { tenantId });
    } catch (error) {
      return null;
    }
  };

  const createOrder = async (order) => {
    try {
      return await ordersService.createOrder({ order }, { tenantId });
    } catch (error) {}
  };

  const handleAddOrder = () => {
    setFormList((prev) => {
      return [...prev, defaultOrderData];
    });
    if (orderNumber) {
      history.push(`?filingNumber=${filingNumber}`);
    }
    setSelectedOrder(formList?.length);
  };

  const createPendingTask = async (order) => {
    if (order?.orderType === "MANDATORY_SUBMISSIONS_RESPONSES") {
      const formdata = order?.additionalDetails?.formdata;
      let entityType = formdata?.isResponseRequired?.code === "Yes" ? "asynsubmissionwithresponse" : "asyncsubmissionwithoutresponse";
      let status = "CREATE_SUBMISSION";
      let assignees = formdata?.submissionParty?.filter((item) => item?.uuid && item).map((item) => ({ uuid: item?.uuid }));
      await ordersService.customApiService(Urls.orders.pendingTask, {
        pendingTask: {
          name: "Submit Documents",
          entityType,
          referenceId: order?.orderNumber,
          status,
          assignedTo: assignees,
          assignedRole: [],
          cnrNumber: null,
          filingNumber: filingNumber,
          isCompleted: false,
          stateSla: null,
          additionalDetails: {},
          tenantId,
        },
      });
    }
    return;
  };

  const handleSaveDraft = async ({ showReviewModal }) => {
    let count = 0;
    const promises = formList.map(async (order) => {
      if (order?.orderType) {
        count += 1;
        if (order?.orderNumber) {
          return updateOrder(order, OrderWorkflowAction.SAVE_DRAFT);
        } else {
          return createOrder(order);
        }
      } else {
        return Promise.resolve();
      }
    });
    const responsesList = await Promise.all(promises);
    setFormList(
      responsesList.map((res) => {
        return res?.order;
      })
    );
    if (!showReviewModal) {
      setShowErrorToast(true);
    }
    if (selectedOrder >= count) {
      setSelectedOrder(0);
    }
    if (showReviewModal) {
      setShowReviewModal(true);
    }
  };

  const handleApplicationAction = async () => {
    if (!applicationNumber || ![SubmissionWorkflowState.PENDINGAPPROVAL, SubmissionWorkflowState.PENDINGREVIEW].includes(applicationDetails?.state)) {
      return true;
    }
    try {
      return await ordersService.customApiService(
        `/application/application/v1/update`,
        {
          application: {
            ...applicationDetails,
            workflow: { ...applicationDetails.workflow, action: true ? SubmissionWorkflowAction.APPROVE : SubmissionWorkflowAction.REJECT },
          },
        },
        { tenantId }
      );
    } catch (error) {
      return false;
    }
  };

  const handleIssueOrder = async () => {
    try {
      setPrevOrder(currentOrder);
      const applicationStatus = await handleApplicationAction();
      if (!applicationStatus) {
        // Show toast with submission approval failed and return
        return;
      }
      await updateOrder(currentOrder, OrderWorkflowAction.ESIGN);
      createPendingTask(currentOrder);
      if (orderType === "SCHEDULE_OF_HEARING_DATE") {
        const advocateData = advocateDetails.advocates.map((advocate) => {
          return {
            individualId: advocate.responseList[0].individualId,
            name: advocate.responseList[0].additionalDetails.username,
            type: "Advocate",
          };
        });
        DRISTIService.createHearings(
          {
            hearing: {
              tenantId: tenantId,
              filingNumber: [filingNumber],
              hearingType: currentOrder?.additionalDetails?.formdata?.hearingPurpose?.type,
              status: true,
              attendees: [
                ...currentOrder?.additionalDetails?.formdata?.namesOfPartiesRequired.map((attendee) => {
                  return { name: attendee.name, individualId: attendee.individualId };
                }),
                ...advocateData,
              ],
              startTime: Date.parse(currentOrder?.additionalDetails?.formdata?.hearingDate),
              endTime: Date.parse(currentOrder?.additionalDetails?.formdata?.hearingDate),
              workflow: {
                action: "CREATE",
                assignes: [],
                comments: "Create new Hearing",
                documents: [{}],
              },
              documents: [],
            },
            tenantId,
          },
          { tenantId: tenantId }
        );
      }
      setShowSuccessModal(true);
    } catch (error) {
      //show toast of API failed
      // setShowErrorToast()
    }
  };

  const handleDeleteOrder = async () => {
    try {
      if (formList[deleteOrderIndex]?.orderNumber) {
        await updateOrder(formList[deleteOrderIndex], OrderWorkflowAction.ABANDON);
      }
      setFormList((prev) => prev.filter((_, i) => i !== deleteOrderIndex));
      if (orderNumber) {
        history.push(`?filingNumber=${filingNumber}`);
      }
      setSelectedOrder((prev) => {
        return deleteOrderIndex <= prev ? prev - 1 : prev;
      });
    } catch (error) {
      //show toast of API failed
      // setShowErrorToast()
    }
    setDeleteOrderIndex(null);
  };

  const handleGoBackSignatureModal = () => {
    setShowsignatureModal(false);
    setShowReviewModal(true);
  };
  const handleOrderChange = (index) => {
    if (orderNumber) {
      history.push(`?filingNumber=${filingNumber}`);
    }
    setSelectedOrder(index);
  };
  const handleDownloadOrders = () => {
    history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
      from: "orderSuccessModal",
    });
    setShowSuccessModal(false);
    history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
      from: "orderSuccessModal",
    });
  };

  const handleClose = () => {
    history.push(`/${window.contextPath}/employee/dristi/home/view-case?tab=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
      from: "orderSuccessModal",
    });
    setShowSuccessModal(false);
  };

  if (!filingNumber) {
    history.push("/employee/home/home-pending-task");
  }

  if (applicationNumber && !currentOrder?.refApplicationId) {
    history.push(`?filingNumber=${filingNumber}`);
  }

  if (currentOrder?.refApplicationId && currentOrder?.refApplicationId !== applicationNumber) {
    if (currentOrder?.orderNumber) {
      history.push(`?filingNumber=${filingNumber}&applicationNumber=${currentOrder?.refApplicationId}&orderNumber=${orderNumber}`);
    } else {
      history.push(`?filingNumber=${filingNumber}&applicationNumber=${currentOrder?.refApplicationId}`);
    }
  }

  if (isOrdersLoading || isOrdersFetching || isCaseDetailsLoading || isApplicationDetailsLoading || !ordersData?.list) {
    return <Loader />;
  }

  return (
    <div className="generate-orders">
      <div className="orders-list-main">
        <div className="add-order-button" onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <React.Fragment>
          {formList?.map((item, index) => {
            return (
              <div className={`order-item-main ${selectedOrder === index ? "selected-order" : ""}`}>
                <h1
                  onClick={() => {
                    handleOrderChange(index);
                  }}
                  style={{ cursor: "pointer", flex: 1 }}
                >
                  {t(item?.orderType) || `${t("CS_ORDER")} ${index + 1}`}
                </h1>
                {formList?.length > 1 && (
                  <span
                    onClick={() => {
                      setDeleteOrderIndex(index);
                    }}
                    style={{ cursor: "pointer" }}
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
        {<Header className="order-header">{`${t("CS_ORDER")} ${selectedOrder + 1}`}</Header>}
        {modifiedFormConfig && (
          <FormComposerV2
            className={"generate-orders"}
            key={`${selectedOrder}=${orderType}`}
            label={t("REVIEW_ORDER")}
            config={modifiedFormConfig}
            defaultValues={defaultValue}
            onFormValueChange={onFormValueChange}
            onSubmit={() => {
              handleSaveDraft({ showReviewModal: true });
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
          handleSaveDraft={() => {}}
        />
      )}
      {showsignatureModal && (
        <OrderSignatureModal t={t} order={currentOrder} handleIssueOrder={handleIssueOrder} handleGoBackSignatureModal={handleGoBackSignatureModal} />
      )}
      {showSuccessModal && <OrderSucessModal t={t} order={prevOrder} handleDownloadOrders={handleDownloadOrders} handleClose={handleClose} />}
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
