import React, { useEffect, useMemo, useState } from "react";
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
  configsCreateOrderWarrant,
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
  // const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSignatureModal, setShowSignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formdata, setFormdata] = useState({});
  const [disable, setDisable] = useState(true);
  const [stepper, setStepper] = useState("-1");
  const [isBailable, setIsBailable] = useState();
  const [isSurety, setIsSurety] = useState(false);
  const [isCash, setIsCash] = useState(false);

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
    WARRANT: configsCreateOrderWarrant,
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
  const { data: ordersData, refetch: refetchOrdersData, isOrdersLoading, isFetching: isOrdersFetching } = Digit.Hooks.orders.useSearchOrdersService(
    {
      tenantId,
      criteria: {
        tenantId: tenantId,
        filingNumber: filingNumber,
      },
    },
    { tenantId, filingNumber, applicationNumber: "", cnrNumber },
    filingNumber,
    Boolean(filingNumber)
  );

  const orderList = useMemo(() => ordersData?.list?.filter((item) => item.status === CaseWorkflowState.DRAFT_IN_PROGRESS), [ordersData]);
  const orderType = useMemo(() => formdata?.orderType || {}, [formdata]);
  const currentOrder = useMemo(() => orderList?.[selectedOrder], [orderList, selectedOrder]);

  const modifiedFormConfig = useMemo(() => {
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
    return config;
  }, [orderType, isBailable, isSurety, isCash]);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, orderindex) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata)) {
      setFormdata(formData);
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
      if (formdata["date"] == null || formdata["SummonsOrder"] == null || formdata["SummonsOrder"]?.["selectedChannels"] == null) setDisable(true);
      else setDisable(false);
    } else setDisable(false);
  }, [modifiedFormConfig, formdata]);

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleUpdateOrder = ({ action, oldOrderData, orderType }) => {
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
        refetchOrdersData();
        if (action === CaseWorkflowAction.ESIGN) {
          setStepper(3);
          // setShowSuccessModal(true);
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
    alert("save draaft");
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
    setDeleteOrderIndex(null);
    handleUpdateOrder({
      action: CaseWorkflowAction.ABANDON,
      oldOrderData: orderList[deleteOrderIndex],
      orderType: orderList[deleteOrderIndex].orderType,
    });
    selectedOrder((prev) => {
      prev == deleteOrderIndex && deleteOrderIndex ? prev - 1 : prev;
    });
  };

  const handleReviewOrder = (data) => {
    console.log(data, "form data ");
    handleSaveDraft();
    handleStepper(1);
    // setShowReviewModal(true);
  };

  const handleCloseSignaturePopup = () => {
    // setShowSignatureModal(false);
    // setShowReviewModal(true);
    setStepper(0)
  };

  const handleCloseSuccessModal = () => {
    // setShowSuccessModal(false);
    setStepper(-1)
    history.back(); // go to view case screen when clicking on close button.
  };

  if (isOrdersLoading || isOrdersFetching || isCaseDetailsLoading) {
    return <Loader />;
  }

  const handleStepper = (val) => {
    setStepper(parseInt(stepper) + parseInt(val));
  };

  const closeModal = () => {
    setStepper("-1");
  };

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
      <div style={{ minWidth: "40%", maxWidth: "550px" }}>
        {orderList?.length > 0 && <Header className="main-card-header">{`${t("ORDER")} ${selectedOrder + 1}`}</Header>}
        {orderList?.length > 0 && (
          <FormComposerV2
            key={selectedOrder}
            isDisabled={disable}
            inline
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
      {stepper === 0 && (
        <OrderReviewModal t={t} order={currentOrder} closeModal={closeModal} handleStepper={handleStepper} handleSaveDraft={handleSaveDraft} />
      )}
      {(stepper === 1 || stepper === 2) && (
        <OrderSignatureModal
          t={t}
          order={currentOrder}
          handleIssueOrder={handleIssueOrder}
          handleStepper={handleStepper}
          closeModal={handleCloseSignaturePopup}
          stepper={stepper}
        />
      )}
      {stepper === 3 && <OrderSucessModal t={t} order={currentOrder} handleStepper={handleStepper} closeModal={handleCloseSuccessModal} />}
    </div>
  );
};

export default GenerateOrders;
