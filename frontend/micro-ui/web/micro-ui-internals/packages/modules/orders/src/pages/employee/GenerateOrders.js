import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { Header, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configsBail,
  configsCaseSettlement,
  configsCaseTransfer,
  configsIssueOfWarrants,
  configsIssueSummons,
  configsOrderMandatorySubmissions,
  configsOrderSection202CRPC,
  configsOrderSubmissionExtension,
  configsOrderTranferToADR,
  configsOthers,
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

const GenerateOrders = () => {
  const { t } = useTranslation();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formdata, setFormdata] = useState({});
  const [prevOrder, setPrevOrder] = useState();

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
        return { code: item?.additionalDetails?.fullName || "Respondant", name: item?.additionalDetails?.fullName || "Respondant" };
      });
  }, [caseDetails]);

  const respondants = useMemo(() => {
    // return caseDetails?.litigants
    //   ?.filter((item) => item?.partyType === "respondant.primary")
    //   .map((item) => {
    //     return { code: item?.additionalDetails?.fullName || "Respondant", name: item?.additionalDetails?.fullName || "Respondant" };
    //   });
    return [{ code: "Respondant", name: "Respondant" }];
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
      APPROVE_REJECT_VOLUNTARY_SUBMISSIONS: configsVoluntarySubmissionStatus,
      CASE_TRANSFER: configsCaseTransfer,
      SETTLEMENT: configsCaseSettlement,
      SUMMONS: configsIssueSummons,
      BAIL: configsBail,
      WARRANT: configsIssueOfWarrants,
      OTHERS: configsOthers,
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
    return newConfig;
  }, [complainants, orderType?.code, respondants]);

  const defaultValue = useMemo(() => {
    return structuredClone(currentOrder?.additionalDetails?.formdata) || {};
  }, [currentOrder]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, orderindex) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata)) {
      setFormdata(formData);
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
    });
  };

  const handleDeleteOrder = () => {
    handleUpdateOrder({
      action: CaseWorkflowAction.ABANDON,
      oldOrderData: orderList[deleteOrderIndex],
      orderType: orderList[deleteOrderIndex].orderType,
    });
    selectedOrder((prev) => {
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
        {orderList?.length > 0 && (
          <FormComposerV2
            className={"generate-orders"}
            key={selectedOrder}
            label={t("REVIEW_ORDER")}
            config={modifiedFormConfig}
            defaultValues={defaultValue}
            onFormValueChange={onFormValueChange}
            onSubmit={() => {
              handleSaveDraft({ modal: "reviewModal" });
            }}
            onSecondayActionClick={handleSaveDraft}
            secondaryLabel={t("SAVE_AS_DRAFT")}
            showSecondaryLabel={true}
            cardClassName={`order-type-form-composer`}
            actionClassName={"order-type-action"}
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
    </div>
  );
};

export default GenerateOrders;
