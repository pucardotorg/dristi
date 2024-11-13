import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { SummonsTabsConfig } from "../../configs/SuumonsConfig";
import { useTranslation } from "react-i18next";
import DocumentModal from "../../components/DocumentModal";
import PrintAndSendDocumentComponent from "../../components/Print&SendDocuments";
import DocumentViewerWithComment from "../../components/DocumentViewerWithComment";
import AddSignatureComponent from "../../components/AddSignatureComponent";
import CustomStepperSuccess from "../../components/CustomStepperSuccess";
import UpdateDeliveryStatusComponent from "../../components/UpdateDeliveryStatusComponent";
import { ordersService, taskService } from "../../hooks/services";
import { Urls } from "../../hooks/services/Urls";
import { convertToDateInputFormat } from "../../utils/index";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";
import { useHistory } from "react-router-dom";
import isEqual from "lodash/isEqual";

const defaultSearchValues = {
  eprocess: "",
  caseId: "",
};

const handleTaskDetails = (taskDetails) => {
  try {
    // Check if taskDetails is a string
    if (typeof taskDetails === "string") {
      // First, remove escape characters like backslashes if present
      const cleanedDetails = taskDetails.replace(/\\n/g, "").replace(/\\/g, "");
      return JSON.parse(cleanedDetails);
    }
    // If taskDetails is not a string, return it as it is
    return taskDetails;
  } catch (error) {
    console.error("Failed to parse taskDetails:", error);
    return null;
  }
};

const ReviewSummonsNoticeAndWarrant = () => {
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues);
  const [config, setConfig] = useState(SummonsTabsConfig?.SummonsTabsConfig?.[0]);
  const [showActionModal, setShowActionModal] = useState(false);
  const [isSigned, setIsSigned] = useState(false);
  const [actionModalType, setActionModalType] = useState("");
  const [isDisabled, setIsDisabled] = useState(true);
  const [rowData, setRowData] = useState({});
  const { taskNumber } = Digit.Hooks.useQueryParams();
  const [nextHearingDate, setNextHearingDate] = useState();
  const [step, setStep] = useState(0);
  const [signatureId, setSignatureId] = useState("");
  const [deliveryChannel, setDeliveryChannel] = useState("");
  const [reload, setReload] = useState(false);
  // const [taskDetails, setTaskDetails] = useState({});
  const [tasksData, setTasksData] = useState(null);
  const [selectedDelievery, setSelectedDelievery] = useState({});
  const history = useHistory();
  const dayInMillisecond = 24 * 3600 * 1000;
  const todayDate = new Date().getTime();

  const [tabData, setTabData] = useState(
    SummonsTabsConfig?.SummonsTabsConfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  );

  // const handleOpen = (props) => {
  //change status to signed or unsigned
  // };

  const handleDownload = useCallback(() => {
    const fileStoreId = rowData?.documents?.filter((data) => data?.documentType === "SIGNED")?.[0]?.fileStore;
    const url = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId}`;
    const link = document.createElement("a");
    link.href = url;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }, [rowData, tenantId]);

  const { data: fetchedTasksData, refetch } = Digit.Hooks.hearings.useGetTaskList(
    {
      criteria: {
        tenantId: tenantId,
        taskNumber: rowData?.taskNumber,
      },
    },
    {},
    rowData?.taskNumber,
    Boolean(showActionModal || step)
  );

  const getTaskDetailsByTaskNumber = useCallback(
    async function () {
      const response = await DRISTIService.customApiService("/task/v1/table/search", {
        criteria: {
          searchText: taskNumber,
          tenantId,
        },
      });
      handleRowClick({ original: response?.list?.[0] });
    },
    [taskNumber, tenantId]
  );

  useEffect(() => {
    if (fetchedTasksData && !isEqual(fetchedTasksData, tasksData)) {
      setTasksData(fetchedTasksData); // Store tasksData only if it's different
    }
  }, [fetchedTasksData, tasksData]);

  const { data: orderData } = Digit.Hooks.orders.useSearchOrdersService(
    { tenantId, criteria: { id: tasksData?.list[0]?.orderId } },
    { tenantId },
    tasksData?.list[0]?.orderId,
    Boolean(tasksData)
  );

  const orderType = useMemo(() => orderData?.list[0]?.orderType, [orderData]);

  const handleSubmitButtonDisable = (disable) => {
    setIsDisabled(disable);
  };

  const handleClose = useCallback(() => {
    localStorage.removeItem("SignedFileStoreID");
    setShowActionModal(false);
    if (taskNumber) history.replace(`/${window?.contextPath}/employee/orders/Summons&Notice`);
    setReload(!reload);
  }, [history, reload, taskNumber]);

  const handleSubmit = useCallback(async () => {
    localStorage.removeItem("SignedFileStoreID");
    const { data: tasksData } = await refetch();
    if (tasksData) {
      try {
        const task = tasksData?.list?.[0];
        const reqBody = {
          task: {
            ...task,
            ...(typeof task?.taskDetails === "string" && { taskDetails: JSON.parse(task?.taskDetails) }),
            workflow: {
              ...tasksData?.list?.[0]?.workflow,
              action: "SEND",
              documents: [{}],
            },
          },
        };
        await taskService.updateTask(reqBody, { tenantId });
        setShowActionModal(false);
        setReload(!reload);
      } catch (error) {
        console.error("Error updating task data:", error);
      }
    }
  }, [refetch, reload, tasksData, tenantId]);

  const handleUpdateStatus = useCallback(async () => {
    const { data: tasksData } = await refetch();
    if (tasksData) {
      try {
        const task = tasksData?.list?.[0];
        const reqBody = {
          task: {
            ...task,
            ...(typeof task?.taskDetails === "string" && { taskDetails: JSON.parse(task?.taskDetails) }),
            workflow: {
              ...tasksData?.list?.[0]?.workflow,
              action:
                selectedDelievery?.key === "DELIVERED"
                  ? orderType === "WARRANT"
                    ? "DELIVERED"
                    : "SERVED"
                  : orderType === "WARRANT"
                  ? "NOT_DELIVERED"
                  : "NOT_SERVED",
              documents: [{}],
            },
          },
        };
        await taskService.updateTask(reqBody, { tenantId }).then(async (res) => {
          if (res?.task && selectedDelievery?.key === "NOT_DELIVERED" && orderType !== "WARRANT") {
            await taskService.updateTask(
              {
                task: {
                  ...res.task,
                  workflow: {
                    ...res.task?.workflow,
                    action: orderType === "SUMMONS" ? "NEW_SUMMON" : "NEW_NOTICE",
                  },
                },
              },
              { tenantId }
            );
          }
        });
        if (selectedDelievery?.key === "NOT_DELIVERED") {
          ordersService.customApiService(Urls.orders.pendingTask, {
            pendingTask: {
              name: `Re-issue ${orderType === "NOTICE" ? "Notice" : "Summon"}`,
              entityType: "order-default",
              referenceId: `MANUAL_${orderData?.list[0]?.hearingNumber}`,
              status: `RE-ISSUE_${orderType === "NOTICE" ? "NOTICE" : "SUMMON"}`,
              assignedTo: [],
              assignedRole: ["JUDGE_ROLE"],
              cnrNumber: tasksData?.list[0]?.cnrNumber,
              filingNumber: tasksData?.list[0]?.filingNumber,
              isCompleted: false,
              stateSla: 3 * dayInMillisecond + todayDate,
              additionalDetails: {},
              tenantId,
            },
          });
        }
        setShowActionModal(false);
        setReload(!reload);
      } catch (error) {
        console.error("Error updating task data:", error);
      }
    }
  }, [dayInMillisecond, orderData, orderType, refetch, reload, selectedDelievery, tasksData, tenantId, todayDate]);

  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
    const isSignSuccess = localStorage.getItem("esignProcess");
    const isRowData = JSON.parse(localStorage.getItem("ESignSummons"));
    const delieveryCh = localStorage.getItem("delieveryChannel");
    if (isSignSuccess) {
      if (rowData) {
        setRowData(isRowData);
      }
      if (delieveryCh) {
        setDeliveryChannel(delieveryCh);
      }
      setShowActionModal(true);
      setActionModalType("SIGN_PENDING");
      setStep(1);
      localStorage.removeItem("esignProcess");
      localStorage.removeItem("ESignSummons");
    }
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(SummonsTabsConfig?.SummonsTabsConfig?.[n]); // as per tab number filtering the config
  };

  function findNextHearings(objectsList) {
    const now = Date.now();
    const futureStartTimes = objectsList.filter((obj) => obj.startTime > now);
    futureStartTimes.sort((a, b) => a.startTime - b.startTime);
    return futureStartTimes.length > 0 ? futureStartTimes[0] : null;
  }

  const getHearingFromCaseId = async () => {
    try {
      const response = await Digit.HearingService.searchHearings(
        {
          criteria: {
            tenantId: Digit.ULBService.getCurrentTenantId(),
            filingNumber: rowData?.filingNumber,
          },
        },
        {}
      );
      setNextHearingDate(findNextHearings(response?.HearingList));
    } catch (error) {
      console.error("error :>> ", error);
    }
  };

  const infos = useMemo(() => {
    if (rowData?.taskDetails || nextHearingDate) {
      const caseDetails = handleTaskDetails(rowData?.taskDetails);
      return [
        { key: "Issued to", value: caseDetails?.respondentDetails?.name },
        { key: "Issued Date", value: convertToDateInputFormat(rowData?.createdDate) },
        // { key: "Next Hearing Date", value: nextHearingDate?.startTime ? formatDate(nextHearingDate?.startTime) : "N/A" },
        { key: "AMOUNT_PAID_TEXT", value: `Rs. ${caseDetails?.deliveryChannels?.fees || 100}` },
        { key: "CHANNEL_DETAILS_TEXT", value: caseDetails?.deliveryChannels?.channelName },
      ];
    }
  }, [rowData, nextHearingDate]);

  const links = useMemo(() => {
    return [{ text: "View order", link: "" }];
  }, []);

  const documents = useMemo(() => {
    if (rowData?.documents)
      return rowData?.documents?.map((document) => {
        return { ...document, fileName: `${t(rowData?.taskType)} ${t("DOCUMENT_TEXT")}` };
      });
  }, [rowData, orderType]);

  const submissionData = useMemo(() => {
    return [
      { key: "SUBMISSION_DATE", value: "25-08-2001", copyData: false },
      { key: "SUBMISSION_ID", value: "875897348579453457", copyData: true },
    ];
  }, []);

  const successMessage = useMemo(() => {
    let msg = "";
    if (documents) {
      if (orderType === "NOTICE") {
        msg = t("SUCCESSFULLY_SIGNED_NOTICE");
      } else if (orderType === "WARRANT") {
        msg = t("SUCCESSFULLY_SIGNED_WARRANT");
      } else {
        msg = t("SUCCESSFULLY_SIGNED_SUMMON");
      }
    } else {
      if (orderType === "NOTICE") {
        msg = t("SENT_NOTICE_VIA");
      } else if (orderType === "WARRANT") {
        msg = t("SENT_WARRANT_VIA");
      } else {
        msg = t("SENT_SUMMONS_VIA");
      }
    }
    return `${msg}${!documents ? " " + deliveryChannel : ""}`;
  }, [documents, orderType, deliveryChannel]);

  const handleSubmitEsign = useCallback(async () => {
    try {
      const localStorageID = localStorage.getItem("fileStoreId");
      const documents = Array.isArray(rowData?.documents) ? rowData.documents : [];
      const documentsFile =
        signatureId !== "" || localStorageID
          ? {
              documentType: "SIGNED_TASK_DOCUMENT",
              fileStore: signatureId || localStorageID,
            }
          : null;
      localStorage.removeItem("fileStoreId");
      localStorage.setItem("SignedFileStoreID", documentsFile?.fileStore);
      const reqBody = {
        task: {
          ...rowData,
          ...(typeof rowData?.taskDetails === "string" && { taskDetails: JSON.parse(rowData?.taskDetails) }),
          documents: documentsFile ? [...documents, documentsFile] : documents,
          tenantId,
        },
        tenantId,
      };

      // Attempt to upload the document and handle the response
      const update = await taskService.UploadTaskDocument(reqBody, { tenantId });
    } catch (error) {
      // Handle errors that occur during the upload process
      console.error("Error uploading document:", error);
    }
  }, [rowData, signatureId, tenantId]);

  const unsignedModalConfig = useMemo(() => {
    return {
      handleClose: handleClose,
      heading: { label: `${t("REVIEW_DOCUMENT_TEXT")} ${t(rowData?.taskType)} ${t("DOCUMENT_TEXT")}` },
      actionSaveLabel: t("E_SIGN_TEXT"),
      isStepperModal: true,
      actionSaveOnSubmit: () => {},
      steps: [
        {
          type: "document",
          modalBody: <DocumentViewerWithComment infos={infos} documents={documents} links={links} />,
          actionSaveOnSubmit: () => {},
        },
        {
          heading: { label: t("ADD_SIGNATURE") },
          actionSaveLabel: deliveryChannel === "Email" ? t("SEND_EMAIL_TEXT") : t("PROCEED_TO_SENT"),
          actionCancelLabel: t("BACK"),
          modalBody: (
            <AddSignatureComponent
              t={t}
              isSigned={isSigned}
              setIsSigned={setIsSigned}
              handleSigned={() => setIsSigned(true)}
              rowData={rowData}
              setSignatureId={setSignatureId}
              deliveryChannel={deliveryChannel}
            />
          ),
          isDisabled: isSigned ? false : true,
          actionSaveOnSubmit: handleSubmitEsign,
        },
        {
          type: "success",
          hideSubmit: true,
          modalBody: (
            <CustomStepperSuccess
              successMessage={successMessage}
              bannerSubText={t("PARTY_NOTIFIED_ABOUT_DOCUMENT")}
              submitButtonText={documents ? "MARK_AS_SENT" : "CS_CLOSE"}
              closeButtonText={documents ? "CS_CLOSE" : "DOWNLOAD_DOCUMENT"}
              closeButtonAction={handleClose}
              submitButtonAction={handleSubmit}
              t={t}
              submissionData={submissionData}
              documents={documents}
              deliveryChannel={deliveryChannel}
              orderType={orderType}
            />
          ),
        },
      ],
    };
  }, [deliveryChannel, documents, handleClose, handleSubmit, handleSubmitEsign, infos, isSigned, links, orderType, rowData, submissionData, t]);

  const handleCloseActionModal = useCallback(() => {
    setShowActionModal(false);
    if (taskNumber) history.replace(`/${window?.contextPath}/employee/orders/Summons&Notice`);
  }, [history, taskNumber]);

  const signedModalConfig = useMemo(() => {
    return {
      handleClose: () => handleCloseActionModal(),
      heading: { label: t("PRINT_SEND_DOCUMENT") },
      actionSaveLabel: t("MARK_AS_SENT"),
      isStepperModal: false,
      modalBody: (
        <PrintAndSendDocumentComponent infos={infos} documents={documents?.filter((docs) => docs.documentType === "SIGNED")} links={links} t={t} />
      ),
      actionSaveOnSubmit: handleSubmit,
    };
  }, [documents, handleCloseActionModal, handleSubmit, infos, links, t]);

  const sentModalConfig = useMemo(() => {
    return {
      handleClose: () => handleCloseActionModal(),
      heading: { label: t("DELIVERY_STATUS_AND_DETAILS") },
      actionSaveLabel: t("UPDATE_STATUS"),
      actionCancelLabel: t("VIEW_DOCUMENT_TEXT"),
      isStepperModal: false,
      modalBody: (
        <UpdateDeliveryStatusComponent
          infos={infos}
          links={links}
          t={t}
          handleSubmitButtonDisable={handleSubmitButtonDisable}
          rowData={rowData}
          selectedDelievery={selectedDelievery}
          setSelectedDelievery={setSelectedDelievery}
          orderType={orderType}
        />
      ),
      actionSaveOnSubmit: handleUpdateStatus,
      actionCancelOnSubmit: handleDownload,
      isDisabled: isDisabled,
    };
  }, [handleCloseActionModal, handleDownload, handleUpdateStatus, infos, isDisabled, links, orderType, rowData, selectedDelievery, t]);

  useEffect(() => {
    // if (rowData?.id) getTaskDocuments();
    if (rowData?.filingNumber) getHearingFromCaseId();
    setSelectedDelievery(
      rowData?.status === "NOTICE_SENT" || rowData?.status === "SUMMON_SENT" || rowData?.status === "DELIVERED"
        ? {
            key: "DELIVERED",
            value: "Delivered",
          }
        : {}
    );
  }, [rowData]);

  const handleRowClick = (props) => {
    if (props?.original?.status === "DELIVERED") {
      return; // Do nothing if the row's status is 'Completed'
    }

    setRowData(props?.original);
    setActionModalType(props?.original?.documentStatus);
    setShowActionModal(true);
    setStep(0);
    setIsSigned(props?.original?.documentStatus === "SIGN_PENDING" ? false : true);
    setDeliveryChannel(handleTaskDetails(props?.original?.taskDetails)?.deliveryChannels?.channelName);
    // setTaskDetails(handleTaskDetails(props?.original?.taskDetails));
  };

  useEffect(() => {
    if (taskNumber) {
      getTaskDetailsByTaskNumber();
    }
  }, [taskNumber, getTaskDetailsByTaskNumber]);

  return (
    <div className="review-summon-warrant">
      <div className="header-wraper">
        <Header>{t("REVIEW_SUMMON_NOTICE_WARRANTS_TEXT")}</Header>
      </div>

      <div className="inbox-search-wrapper pucar-home home-view">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          key={`inbox-composer-${reload}`}
          configs={config}
          defaultValues={defaultValues}
          showTab={true}
          tabData={tabData}
          onTabChange={onTabChange}
          additionalConfig={{
            resultsTable: {
              onClickRow: handleRowClick, // Use the new row click handler
            },
          }}
        ></InboxSearchComposer>
        {showActionModal && (
          <DocumentModal
            config={config?.label === "Pending" ? (actionModalType !== "SIGN_PENDING" ? signedModalConfig : unsignedModalConfig) : sentModalConfig}
            currentStep={step}
          />
        )}
      </div>
    </div>
  );
};

export default ReviewSummonsNoticeAndWarrant;
