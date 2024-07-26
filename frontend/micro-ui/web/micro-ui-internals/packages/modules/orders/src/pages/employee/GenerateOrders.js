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
  configsCreateOrderWarrant,
} from "../../configs/ordersCreateConfig";
import { CustomDeleteIcon } from "../../../../dristi/src/icons/svgIndex";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
import OrderSignatureModal from "../../pageComponents/OrderSignatureModal";
import OrderDeleteModal from "../../pageComponents/OrderDeleteModal";
import { ordersService } from "../../hooks/services";
import useSearchCaseService from "../../../../dristi/src/hooks/dristi/useSearchCaseService";
import { CaseWorkflowState } from "../../../../dristi/src/Utils/caseWorkflow";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";
import { applicationTypes } from "../../utils/applicationTypes";
import useSearchSubmissionService from "../../../../submissions/src/hooks/submissions/useSearchSubmissionService";
import useGetIndividualAdvocate from "../../../../dristi/src/hooks/dristi/useGetIndividualAdvocate";
import { DRISTIService } from "../../../../dristi/src/services";
import isEqual from "lodash/isEqual";
import { OrderWorkflowAction, OrderWorkflowState } from "../../utils/orderWorkflow";

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
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const applicationNumber = urlParams.get("applicationNumber");
  const orderNumber = urlParams.get("orderNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, _setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSignatureModal, setShowSignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [newformdata, setNewFormdata] = useState([]);
  const [prevOrder, setPrevOrder] = useState();
  const [stepper, setStepper] = useState("-1");
  const [isBailable, setIsBailable] = useState();
  const [isSurety, setIsSurety] = useState();
  const [isCash, setIsCash] = useState();
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const userInfo = Digit.UserService.getUser()?.info || {};
  const uuid = userInfo?.uuid;
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
    "dristi",
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
    return caseDetails?.litigants?.map((item) => {
      return {
        code: item?.additionalDetails?.fullName,
        name: item?.additionalDetails?.fullName,
        individualId: item?.individualId,
      };
    });
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

  const { data: advocateDetails, isLoading: isAdvocatesLoading } = useGetIndividualAdvocate(
    {
      criteria: advocateIds,
    },
    { tenantId: tenantId },
    "DRISTI",
    cnrNumber + filingNumber,
    true
  );
  const defaultIndex = useMemo(() => {
    return ordersData?.list?.findIndex((order) => order.orderNumber === orderNumber);
  }, [ordersData, orderNumber]);

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
        assignes: [uuid],
        rating: null,
        documents: [{}],
      },
      documents: [],
      additionalDetails: { formdata: {} },
    }),
    [cnrNumber, filingNumber, tenantId, uuid]
  );

  useEffect(() => {
    if (!ordersData?.list || ordersData?.list.length < 1) {
      setNewFormdata([defaultOrderData]);
    } else {
      setNewFormdata(ordersData?.list);
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
      clearTimeout(timer);
    }
  }, [showErrorToast]);
  useEffect(() => {
    if (defaultIndex && defaultIndex !== -1 && defaultIndex !== selectedOrder) {
      setSelectedOrder(defaultIndex);
    }
  }, [defaultIndex, selectedOrder]);

  const currentOrder = useMemo(() => newformdata?.[selectedOrder], [newformdata, selectedOrder]);
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
      // WARRANT: configsIssueOfWarrants,
      WARRANT: configsCreateOrderWarrant,
      WITHDRAWAL: configsCaseWithdrawal,
      OTHERS: configsOthers,
      APPROVE_VOLUNTARY_SUBMISSIONS: configsVoluntarySubmissionStatus,
      REJECT_VOLUNTARY_SUBMISSIONS: configRejectSubmission,
      JUDGEMENT: configsJudgement,
    };
    if (!orderType?.code) {
      return applicationTypeConfig;
    }
    let config = configKeys[orderType?.code]
      ? structuredClone(applicationTypeConfig).concat(configKeys[orderType?.code])
      : structuredClone(applicationTypeConfig);
    if (orderType?.code === "WARRANT" && isBailable) {
      config = config.map((item) => {
        if (item.defaultValues?.orderType?.code === "WARRANT") {
          item.body.push(
            {
              isMandatory: true,
              type: "radio",
              key: "numberOfSureties",
              label: "No. Of Sureties",
              populators: {
                name: "numberOfSurety",
                label: "numberOfSuretyButton",
                type: "radioButton",
                optionsKey: "name",
                error: "Error!",
                required: false,
                isMandatory: true,
                isDependent: true,
                options: [
                  {
                    code: "1",
                    name: "1",
                  },
                  {
                    code: "2",
                    name: "2",
                  },
                ],
              },
            },
            {
              key: "bailAmount",
              type: "number",
              label: "Amount (in Rupees) by each surety in sum of",
              isMandatory: true,
              populators: { name: "money", error: "Required", validation: { min: 0, max: 9999999999 } },
            }
          );
        }
        return item;
      });
    } else if (orderType?.code === "WARRANT" && !isBailable) {
      config = config.map((item) => {
        if (item.defaultValues?.orderType?.code === "WARRANT") {
          item.body = item.body.filter((field) => field.key !== "numberOfSureties" && field.key !== "bailAmount");
        }
        return item;
      });
    }

    if (orderType?.code === "BAIL" && isSurety && !isCash) {
      config.map((item) => {
        if (item.defaultValues?.orderType?.code === "BAIL") {
          item.body = item.body.filter((field) => field.key !== "bailAmount" && field.key !== "dueDate");
          item.body.push(
            {
              inline: true,
              label: "Conditions",
              type: "textarea",
              key: "conditions",
              populators: {
                name: "conditions",
              },
            },
            {
              label: "Additional Details",
              type: "textarea",
              key: "additionalDetails",
              populators: {
                name: "additionalDetails",
              },
            },
            {
              label: "Attachment",
              type: "multiupload",
              key: "bailDocuments",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              allowedFileTypes: /(.*?)(png|jpeg|jpg|pdf)$/i,
              populators: {
                name: "bailDocuments",
              },
            }
          );
        }
        return item;
      });
    } else if (orderType?.code === "BAIL" && !isSurety && isCash) {
      config.map((item) => {
        if (item.defaultValues?.orderType?.code === "BAIL") {
          item.body = item.body.filter((field) => field.key !== "conditions" && field.key !== "additionalDetails" && field.key != "bailDocuments");
          item.body.push(
            {
              inline: true,
              label: "BAIL_AMOUNT",
              isMandatory: false,
              key: "bailAmount",
              type: "text",
              populators: { name: "bailAmount" },
            },
            {
              type: "date",
              label: "Due Date",
              key: "dueDate",
              isMandatory: true,
              populators: {
                name: "dueDate",
                validation: {
                  max: new Date().toISOString().split("T")[0],
                },
              },
            }
          );
        }
        return item;
      });
    }


    let newConfig = structuredClone(applicationTypeConfig);
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
                    options: complainants ? complainants : [],
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
     config = newConfig.map((config) => {
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
    return config;
  }, [complainants, orderType?.code, respondants, isBailable, isSurety, isCash]);


  const defaultValue = useMemo(() => {
    let updatedFormdata = structuredClone(currentOrder?.additionalDetails?.formdata);
    if (applicationDetails?.referenceId) {
      updatedFormdata.refApplicationId = applicationDetails?.referenceId;
    }
    if (orderType === "EXTENSION_OF_DOCUMENT_SUBMISSION_DATE") {
      if (applicationDetails?.applicationType === applicationTypes.EXTENSION_SUBMISSION_DEADLINE) {
        updatedFormdata.documentName = applicationDetails?.additionalDetails?.formdata?.documentType?.name;
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
      setNewFormdata((prev) => {
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
    const bailableField = formData?.bailable;

    if (bailableField && bailableField.code === "Yes") {
      setIsBailable(true);
    } else {
      setIsBailable(false);
    }

    const bailType = formData?.bailType;
    if (bailType && bailType.code === "SURETY") {
      setIsSurety(true);
      setIsCash(false);
    } else if (bailType && bailType.code === "CASH") {
      setIsSurety(false);
      setIsCash(true);
    }
  };

  useEffect(() => {
    if (orderType == null || orderType?.code === "SUMMONS") {
      if (formdata["date"] == null || formdata["SummonsOrder"] == null || formdata["SummonsOrder"]?.["selectedChannels"] == null) setIsSubmitDisabled(true);
      else setIsSubmitDisabled(false);
    }
  }, [modifiedFormConfig, formdata]);

  const updateOrder = async (order, action) => {
    console.debug(order);
    console.debug({ order: { ...order, workflow: { ...order.workflow, action, documents: [{}] } } });
    try {
      return await ordersService.updateOrder({ order: { ...order, workflow: { ...order.workflow, action, documents: [{}] } } }, { tenantId });
    } catch (error) {
      return null;
    }
  };

  const createOrder = async (order) => {
    try {
      await ordersService.createOrder({ order }, { tenantId });
    } catch (error) {}
  };

  const handleAddOrder = () => {
    setNewFormdata((prev) => {
      return [...prev, defaultOrderData];
    });
    if (orderNumber) {
      history.push(`?filingNumber=${filingNumber}`);
    }
    setSelectedOrder(newformdata?.length);
  };

  const handleSaveDraft = async ({ showReviewModal }) => {
    let count = 0;
    const promises = newformdata.map(async (order) => {
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
    await Promise.all(promises);
    refetchOrdersData();
    if (selectedOrder >= count) {
      setSelectedOrder(0);
    }

    if (showReviewModal) {
      setShowReviewModal(true);
    }
  };

  const handleIssueOrder = async () => {
    try {
      setPrevOrder(currentOrder);
      await updateOrder(currentOrder, OrderWorkflowAction.ESIGN);
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
      if (newformdata[deleteOrderIndex]?.orderNumber) {
        await updateOrder(newformdata[deleteOrderIndex], OrderWorkflowAction.ABANDON);
        refetchOrdersData();
      } else {
        setNewFormdata((prev) => prev.filter((_, i) => i !== deleteOrderIndex));
      }
      if (orderNumber) {
        history.push(`?filingNumber=${filingNumber}`);
      }
      setSelectedOrder((prev) => {
        return deleteOrderIndex < prev ? prev - 1 : prev;
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
    setShowSuccessModal(false);
    // history.push(`/${window.contextPath}/employee/dristi/home/view-case?${searchParams.toString()}`, { from: "orderSuccessModal" });
  };

  const handleClose = () => {
    history.push(`/${window.contextPath}/employee/dristi/home/view-case?tabs=${"Orders"}&caseId=${caseDetails?.id}&filingNumber=${filingNumber}`, {
      from: "orderSuccessModal",
    });
    setShowSuccessModal(false);
  };

  if (!filingNumber) {
    history.push("/employee/home/home-pending-task");
  }

  if (isOrdersLoading || isOrdersFetching || isCaseDetailsLoading || isApplicationDetailsLoading) {
    return <Loader />;
  }

  const handleStepper = (val) => {
    setStepper(parseInt(stepper) + parseInt(val));
  };

  const closeModal = () => {
    setStepper("-1");
  };

  return (
    <div className="generate-orders">
      <div className="orders-list-main">
        <div className="add-order-button" onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <React.Fragment>
          {newformdata?.map((_, index) => {
            return (
              <div className={`order-item-main ${selectedOrder === index ? "selected-order" : ""}`}>
                <h1
                  onClick={() => {
                    handleOrderChange(index);
                  }}
                  style={{ cursor: "pointer", flex: 1 }}
                >
                  {t(newformdata[index]?.orderType) || `${t("CS_ORDER")} ${index + 1}`}
                </h1>
                {newformdata?.length > 1 && (
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
            key={`${selectedOrder}=${orderType.code}`}
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
