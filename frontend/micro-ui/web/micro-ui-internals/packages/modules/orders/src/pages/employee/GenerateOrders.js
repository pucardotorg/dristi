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
import Modal from "../../../../dristi/src/components/Modal";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
import OrderSignatureModal from "../../pageComponents/OrderSignatureModal";
import OrderDeleteModal from "../../pageComponents/OrderDeleteModal";
import useSearchOrdersService from "../../hooks/orders/useSearchOrdersService";
import { ordersService } from "../../hooks/services";
import useSearchCaseService from "../../../../dristi/src/hooks/dristi/useSearchCaseService";
import { CaseWorkflowAction } from "../../utils/caseWorkflow";
import { Loader } from "@egovernments/digit-ui-components";

const fieldStyle = { marginRight: 0 };

const getFormattedDate = () => {
  const currentDate = new Date();
  const year = String(currentDate.getFullYear()).slice(-2);
  const month = String(currentDate.getMonth() + 1).padStart(2, "0");
  const day = String(currentDate.getDate()).padStart(2, "0");

  return `${month}/${day}/${year}`;
};

const GenerateOrders = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const caseId = urlParams.get("caseId");
  const filingNumber = urlParams.get("filingNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [signatureIndex, setSignatureIndex] = useState(null);
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
    caseId,
    caseId
  );

  const cnrNumber = useMemo(() => caseData?.criteria?.[0]?.responseList?.[0]?.cnrNumber, [caseData]);

  const { data: ordersData, refetch: refetchOrdersData, isOrdersLoading } = useSearchOrdersService(
    { tenantId },
    { tenantId, filingNumber, applicationNumber: "", cnrNumber: "" },
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

  const handleUpdateOrder = ({ action, oldOrderData, newformdata, orderType }) => {
    const updatedreqBody = {
      hearingNumber: "3244d158-c5cb-4769-801f-a0f94f383679",
      order: {
        ...oldOrderData,
        orderType,
        workflow: {
          action,
          comments: "Creating for order registration",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: { ...oldOrderData.additionalDetails, formdata: newformdata },
      },
    };
    ordersService
      .updateOrder(updatedreqBody, { tenantId })
      .then(() => {
        refetchOrdersData();
      })
      .catch(() => {
        refetchOrdersData();
      });
  };

  const handleAddOrder = () => {
    const reqbody = {
      hearingNumber: "3244d158-c5cb-4769-801f-a0f94f383679",
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber: "CNR111",
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
      newformdata: formdata,
      orderType: orderType?.code,
    });
  };

  const handleDeleteOrder = () => {
    handleUpdateOrder({
      action: CaseWorkflowAction.ABANDON,
      oldOrderData: orderList[deleteOrderIndex],
      newformdata: {},
      orderType: orderList[deleteOrderIndex].orderType,
    });
    refetchOrdersData();
    selectedOrder((prev) => {
      prev == deleteOrderIndex && deleteOrderIndex ? prev - 1 : prev;
    });
    setDeleteOrderIndex(null);
  };

  const handleReviewOrder = (data, index) => {
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

  const ifAllSignaturesCompleted = true; // update useMemo logic here while integrating.

  if (isOrdersLoading || isCaseDetailsLoading) {
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
        <Header className="main-card-header">{`${t("ORDER")} ${selectedOrder + 1}`}</Header>
        <FormComposerV2
          key={selectedOrder}
          label={t("REVIEW_ORDERS")}
          config={modifiedFormConfig}
          defaultValues={structuredClone(currentOrder?.additionalDetails?.formdata) || {}}
          onFormValueChange={onFormValueChange}
          onSubmit={(data) => handleReviewOrder(data, selectedOrder)}
          onSecondayActionClick={handleSaveDraft}
          secondaryLabel={t("SAVE_AS_DRAFT")}
          showSecondaryLabel={true}
        />
      </div>
      {deleteOrderIndex !== null && (
        <OrderDeleteModal deleteOrderIndex={deleteOrderIndex} setDeleteOrderIndex={setDeleteOrderIndex} handleDeleteOrder={handleDeleteOrder} />
      )}
      {(showReviewModal || true) && (
        <OrderReviewModal t={t} orderList={orderList} setShowReviewModal={setShowReviewModal} setSignatureIndex={setSignatureIndex} />
      )}

      {signatureIndex !== null && <OrderSignatureModal t={t} />}
      {showSuccessModal && (
        <Modal
          actionCancelLabel={t("DOWNLOAD_ORDER")}
          actionCancelOnSubmit={() => {}}
          actionSaveLabel={t("CLOSE")}
          actionSaveOnSubmit={() => handleCloseSuccessModal()}
          className={"orders-success-modal"}
        >
          <div className="success-modal-main-div">
            <div className="success-message-div">
              <h1>{`${t("SUCCCESSFULLY_ISSUED")} ${10} ${t("ORDERS")}`}</h1>
              {/* <SmallInfoIcon></SmallInfoIcon>  */}
            </div>
            <h3>{t("PARTIES_WILL_BE_NOTIFIED")}</h3>
            <div className="order-id-info-div">
              <div className="order-issue-date-div">
                <h2>{t("ORDER_ISSUE_DATE")}</h2>
                <span>{getFormattedDate()}</span>
              </div>
              <div className="order-ids-list-div">
                {formdata.map((order, index) => {
                  return (
                    <div>
                      <h2>{`${t("ORDER_ID")} ${index + 1} : ${"ORDER-TYPE-HERE"}`}</h2>
                      <span>
                        <h2>{"KA01234"}</h2>
                        <span>
                          <h2>{t("COPY")}</h2>
                        </span>
                      </span>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
};

export default GenerateOrders;
