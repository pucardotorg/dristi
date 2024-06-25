import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { Header, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import {
  applicationTypeConfig,
  configsBail,
  configsCaseSettlement,
  configsCaseTransfer,
  configsIssueSummons,
  configsOrderMandatorySubmissions,
  configsOrderSection202CRPC,
  configsOrderSubmissionExtension,
  configsOrderTranferToADR,
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
import { CaseWorkflowAction } from "../../utils/caseWorkflow";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";

const fieldStyle = { marginRight: 0 };

const GenerateOrders = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSignatureModal, setShowSignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formdata, setFormdata] = useState({});

  const configKeys = {
    SECTION_202_CRPC: configsOrderSection202CRPC,
    DOCUMENT_SUBMISSION: configsOrderMandatorySubmissions,
    EXTENSION_OF_DOCUMENT_SUBMISSION_DATE: configsOrderSubmissionExtension,
    TRANSFER_TO_ADR: configsOrderTranferToADR,
    NEXT_HEARING: configsScheduleHearingDate,
    ORDER_TYPE_RESCHEDULE_OF_HEARING_DATE: configsRescheduleHearingDate,
    VOLUNTARY_SUBMISSION_STATUS: configsVoluntarySubmissionStatus,
    CASE_TRANSFER: configsCaseTransfer,
    CASE_SETTLEMENT: configsCaseSettlement,
    SUMMONS: configsIssueSummons,
    BAIL: configsBail,
  };

  const { data: caseData, isCaseDetailsLoading } = useSearchCaseService(
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

  const cnrNumber = useMemo(() => caseData?.criteria?.[0]?.responseList?.[0]?.cnrNumber, [caseData]);
  const { data: ordersData, refetch: refetchOrdersData, isOrdersLoading, isFetching: isOrdersFetching } = useSearchOrdersService(
    { tenantId },
    { tenantId, filingNumber, applicationNumber: "", cnrNumber },
    filingNumber,
    Boolean(filingNumber)
  );

  const orderList = useMemo(() => ordersData?.list, [ordersData]);
  const orderType = useMemo(() => formdata?.orderType || {}, [formdata]);
  const currentOrder = useMemo(() => orderList?.[selectedOrder], [orderList, selectedOrder]);

  const modifiedFormConfig = useMemo(() => {
    return !orderType?.code
      ? applicationTypeConfig
      : configKeys.hasOwnProperty(orderType?.code)
      ? [...applicationTypeConfig, ...configKeys[orderType?.code]]
      : applicationTypeConfig;
  }, [orderType]);

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

  const handleUpdateOrder = ({ action, oldOrderData, orderType }) => {
    const newAdditionalData =
      action === CaseWorkflowAction.SAVE_DRAFT ? { ...oldOrderData.additionalDetails, formdata } : { ...oldOrderData.additionalDetails };
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
        refetchOrdersData();
        if (action === CaseWorkflowAction.ESIGN) {
          setShowSuccessModal(true);
        }
      })
      .catch(() => {
        refetchOrdersData();
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

  const handleSaveDraft = () => {
    handleUpdateOrder({
      action: CaseWorkflowAction.SAVE_DRAFT,
      oldOrderData: currentOrder,
      orderType: orderType?.code,
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
      prev == deleteOrderIndex && deleteOrderIndex ? prev - 1 : prev;
    });
    setDeleteOrderIndex(null);
  };

  const handleReviewOrder = () => {
    handleSaveDraft();
    setShowReviewModal(true);
  };

  const handleCloseSignaturePopup = () => {
    setShowSignatureModal(false);
    setShowReviewModal(true);
  };

  const handleCloseSuccessModal = () => {
    setShowSuccessModal(false);
    history.back(); // go to view case screen when clicking on close button.
  };

  if (isOrdersLoading || isOrdersFetching || isCaseDetailsLoading) {
    return <Loader />;
  }

  return (
    <div style={{ display: "flex", gap: "5%", marginBottom: "200px" }}>
      <div style={{ width: "20%" }}>
        <div style={{ color: "#007E7E" }} onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <div>
          {orderList?.map((order, index) => {
            return (
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "space-between",
                  alignItems: "center",
                  cursor: "pointer",
                  ...(selectedOrder === index ? { background: "#E8E8E8" } : {}),
                }}
                onClick={() => setSelectedOrder(index)}
              >
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
        </div>
      </div>
      <div style={{ minWidth: "70%" }}>
        {orderList?.length > 0 && <Header className="main-card-header">{`${t("ORDER")} ${selectedOrder + 1}`}</Header>}
        {orderList?.length > 0 && (
          <FormComposerV2
            key={selectedOrder}
            label={t("REVIEW_ORDER")}
            config={modifiedFormConfig}
            defaultValues={structuredClone(currentOrder?.additionalDetails?.formdata) || {}}
            onFormValueChange={onFormValueChange}
            onSubmit={handleReviewOrder}
            onSecondayActionClick={handleSaveDraft}
            secondaryLabel={t("SAVE_AS_DRAFT")}
            showSecondaryLabel={true}
          />
        )}
      </div>
      {deleteOrderIndex !== null && (
        <OrderDeleteModal t={t} deleteOrderIndex={deleteOrderIndex} setDeleteOrderIndex={setDeleteOrderIndex} handleDeleteOrder={handleDeleteOrder} />
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
      {showsignatureModal && <OrderSignatureModal t={t} order={currentOrder} handleIssueOrder={handleIssueOrder} />}
      {showSuccessModal && <OrderSucessModal t={t} order={currentOrder} />}
    </div>
  );
};

export default GenerateOrders;
