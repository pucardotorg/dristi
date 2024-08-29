import React, { useState, useEffect, useCallback, useMemo } from "react";
import { InboxSearchComposer, Toast } from "@egovernments/digit-ui-react-components";
import { EpostTrackingConfig } from "./../../configs/E-PostTrackingConfig";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import { useTranslation } from "react-i18next";
import DocumentModal from "../../components/DocumentModal";
import EpostPrintAndSendDocument from "./EpostPrintAndSendDocument";
import EpostUpdateStatus from "./EpostUpdateStatus";
import { EpostService, ordersService } from "../../hooks/services";
import DocumentViewerWithComment from "../../components/DocumentViewerWithComment";
import { Urls } from "../../hooks/services/Urls";

const EpostTrackingPage = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const { state } = location;
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const outboxFilters = ["DELIVERED", "NOT_DELIVERED"];
  const inboxFilters = ["IN_TRANSIT", "NOT_UPDATED"];
  const [config, setConfig] = useState(EpostTrackingConfig({ inboxFilters, outboxFilters })?.[0]);
  const [stepper, setStepper] = useState(0);
  const [rowData, setRowData] = useState();
  const [form, setForm] = useState();
  const [tempForm, setTempForm] = useState({});

  const DocViewerWrapper = Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");
  const [fileStoreId, setFileStoreID] = useState();
  const [showDocument, setShowDocument] = useState(false);
  const [show, setShow] = useState(false);
  const [tabData, setTabData] = useState(
    EpostTrackingConfig({ inboxFilters, outboxFilters })?.map((configItem, index) => ({
      key: index,
      label: configItem.label,
      active: index === 0 ? true : false,
    }))
  );
  const [updatedData, setUpdatedData] = useState(rowData?.original);
  const [toast, setToast] = useState(null);
  const dayInMillisecond = 24 * 3600 * 1000;
  const todayDate = new Date().getTime();

  const showToast = (type, message, duration = 5000) => {
    setToast({ key: type, action: message });
    setTimeout(() => {
      setToast(null);
    }, duration);
  };
  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false })));
    setConfig(EpostTrackingConfig({ inboxFilters, outboxFilters })?.[n]);
  };

  useEffect(() => {
    getTotalCountForTab(EpostTrackingConfig({ inboxFilters, outboxFilters }));
  }, [state]);

  const getTotalCountForTab = useCallback(
    async function (tabConfig) {
      const updatedTabData = await Promise.all(
        tabConfig?.map(async (configItem, index) => {
          const response = await Digit.HomeService.customApiService(configItem?.apiDetails?.serviceName, configItem?.apiDetails?.requestBody);
          const totalCount = response?.pagination?.totalCount;
          return {
            key: index,
            label: totalCount ? `${configItem.label} (${totalCount})` : `${configItem.label} (0)`,
            active: index === 0 ? true : false,
          };
        }) || []
      );
      setTabData(updatedTabData);
    },
    [tenantId]
  );

  function epochToDateTimeObject(epochTime) {
    if (!epochTime || typeof epochTime !== "number") {
      return null;
    }

    const date = new Date(epochTime);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");
    const dateTimeObject = {
      date: `${day}/${month}/${year}  ${hours}:${minutes}`,
    };

    return dateTimeObject;
  }

  const recievedOn = epochToDateTimeObject(rowData?.original?.auditDetails?.createdTime);

  const onRowClick = (row) => {
    setRowData(row);
    setFileStoreID(row?.original?.fileStoreId);
    setStepper(stepper + 1);
    setShow(true);
  };

  const printInfos = useMemo(() => {
    return [
      { key: "E-post fees", value: "Rs.100" },
      { key: "Received on", value: recievedOn ? recievedOn?.date : "04/07/2024, 12:56" },
    ];
  }, [recievedOn]);

  const printLinks = useMemo(() => {
    return [{ text: "View Details", link: "" }];
  }, []);

  const keyMapping = {
    barCode: "trackingNumber",
    dateofBooking: "bookingDate",
    currentStatus: "deliveryStatus",
    dateOfDelivery: "receivedDate",
  };

  const updateFunction = () => {
    let data = updatedData;
    for (const formKey in keyMapping) {
      const updatedDataKey = keyMapping[formKey];

      if (formKey === "currentStatus") {
        data[updatedDataKey] = form[formKey].code;
      } else if (form[formKey]) {
        data[updatedDataKey] = form[formKey];
      }
    }

    setUpdatedData(data);
  };

  const { data: taskData } = Digit.Hooks.hearings.useGetTaskList(
    {
      criteria: {
        tenantId: tenantId,
        taskNumber: rowData?.original?.taskNumber,
      },
    },
    {},
    rowData?.original?.taskNumber,
    Boolean(rowData)
  );

  const { data: orderData } = Digit.Hooks.orders.useSearchOrdersService(
    { tenantId, criteria: { id: taskData?.list[0]?.orderId } },
    { tenantId },
    taskData?.list[0]?.orderId,
    Boolean(taskData)
  );

  const onUpdateClick = async () => {
    updateFunction();
    const requestBody = {
      Individual: {
        tenantId: Digit.ULBService.getCurrentTenantId(),
      },
      EPostTracker: updatedData,
    };
    try {
      const data = await EpostService.EpostUpdate(requestBody, {});
      if (rowData?.original?.deliveryStatus === "NOT_DELIVERED") {
        ordersService.customApiService(Urls.orders.pendingTask, {
          pendingTask: {
            name: "Re-issue Summon",
            entityType: "order-default",
            referenceId: `MANUAL_${orderData?.list[0]?.hearingNumber}`,
            status: "RE-ISSUE_SUMMON",
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber: taskData?.list[0]?.cnrNumber,
            filingNumber: taskData?.list[0]?.filingNumber,
            isCompleted: false,
            stateSla: 3 * dayInMillisecond + todayDate,
            additionalDetails: {},
            tenantId,
          },
        });
      }
      showToast("success", t("CORE_COMMON_PROFILE_UPDATE_SUCCESS_WITH_PASSWORD"), 50000);
      setShow(false);
    } catch (error) {
      console.log("error updating Status");
    }
  };

  const printDocuments = useMemo(() => {
    return [
      {
        fileName: "Summons Document",
        fileStoreId: "03e93220-7254-4877-ac80-bb808a722a61",
        documentName: "file_example_JPG_100kB.jpg",
        documentType: "image/jpeg",
      },
    ];
  }, []);

  const infos = useMemo(() => {
    return [
      { key: "E-post fees", value: "Rs.100" },
      { key: "Received on", value: recievedOn ? recievedOn?.date : "04/07/2024, 12:56" },
      { key: "Bar Code", value: "1234567890" },
      { key: "Date of Booking", value: "04/07/2024" },
    ];
  }, [recievedOn]);

  const links = useMemo(() => {
    return [
      { text: "View Details", link: "", onClick: () => {} },
      {
        text: "View Document",
        link: "",
        onClick: () => {
          setShowDocument(true);
        },
      },
    ];
  }, []);

  const modalConfig = useMemo(() => {
    return {
      handleClose: () => {
        setShow(false);
      },
      isStepperModal: true,
      actionSaveOnSubmit: () => {},
      steps: [
        {
          heading: { label: "Print & Send Documents" },
          modalBody: (
            <EpostPrintAndSendDocument
              t={t}
              infos={printInfos}
              links={printLinks}
              documents={printDocuments}
              rowData={rowData}
              setTempForm={setTempForm}
              tempForm={tempForm}
            />
          ),
          actionSaveOnSubmit: () => {
            setForm(tempForm);
          },
          actionCancelType: "SKIP",
          actionSaveLabel: "Save & Proceed",
          actionCancelLabel: "Skip",
          isDisabled: rowData?.original?.deliveryStatus === "DELIVERED" || rowData?.original?.deliveryStatus === "NOT_DELIVERED" ? true : false,
        },
        {
          type: showDocument && "document",
          heading: { label: showDocument ? "Documents" : "E-post Status" },
          modalBody: showDocument ? (
            <DocumentViewerWithComment documents={printDocuments} />
          ) : (
            <EpostUpdateStatus
              t={t}
              setForm={setForm}
              setUpdatedData={setUpdatedData}
              rowData={rowData}
              form={form}
              infos={infos}
              links={links}
              setShowDocument={setShowDocument}
            />
          ),
          isDisabled: rowData?.original?.deliveryStatus === "DELIVERED" || rowData?.original?.deliveryStatus === "NOT_DELIVERED" ? true : false,
          actionSaveOnSubmit: () => onUpdateClick(),
          actionSaveLabel: !showDocument && "Update Status",
          handleClose: showDocument ? () => setShowDocument(false) : undefined,
        },
      ],
    };
  }, [printInfos, printLinks, printDocuments, t, rowData, form, showDocument, tempForm, setTempForm, setShowDocument]);

  useEffect(() => {
    setUpdatedData(rowData?.original);
  }, [rowData]);

  useEffect(() => {
    if (form) setTempForm(form);
  }, [form]);

  return (
    <div style={{ padding: "16px", margin: "24px" }} className="e-post-tracking-main">
      <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
        <svg width="40" height="41" viewBox="0 0 40 41" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
            d="M20 2.16699C14.4767 2.16699 10 6.64366 10 12.167C10 19.3103 20 30.5003 20 30.5003C20 30.5003 30 19.3103 30 12.167C30 6.64366 25.5234 2.16699 20 2.16699ZM20 5.50033C23.6767 5.50033 26.6667 8.49033 26.6667 12.167C26.6667 15.517 23.1985 21.101 19.9968 25.2659C16.7951 21.1076 13.3334 15.527 13.3334 12.167C13.3334 8.49033 16.3234 5.50033 20 5.50033ZM20 8.83366C19.116 8.83366 18.2681 9.18485 17.643 9.80997C17.0179 10.4351 16.6667 11.2829 16.6667 12.167C16.6667 13.051 17.0179 13.8989 17.643 14.524C18.2681 15.1491 19.116 15.5003 20 15.5003C20.8841 15.5003 21.7319 15.1491 22.3571 14.524C22.9822 13.8989 23.3334 13.051 23.3334 12.167C23.3334 11.2829 22.9822 10.4351 22.3571 9.80997C21.7319 9.18485 20.8841 8.83366 20 8.83366ZM8.00134 25.5003L3.33337 37.167H36.6667L31.9987 25.5003H28.0209C27.2125 26.7187 26.401 27.847 25.6543 28.8337H29.7461L31.7448 33.8337H8.25525L10.2539 28.8337H14.3457C13.5991 27.847 12.7875 26.7187 11.9792 25.5003H8.00134Z"
            fill="#77787B"
          />
        </svg>
        <strong style={{ fontSize: "24px", fontFamily: "roboto" }}>Kollam Postal Hub</strong>
      </div>
      <div style={{ margin: "24px" }}>
        <strong style={{ fontSize: "24px" }}>Online Process Memo</strong>
      </div>
      <InboxSearchComposer
        key={`e-post-track-${show}`}
        configs={config}
        showTab={true}
        tabData={tabData}
        onTabChange={onTabChange}
        additionalConfig={{
          resultsTable: {
            onClickRow: onRowClick,
          },
        }}
      ></InboxSearchComposer>
      {show && <DocumentModal config={modalConfig} setShow={setShow} />}
      {toast && (
        <Toast
          error={toast.key === "error"}
          label={t(toast.key === "success" ? `CORE_COMMON_PROFILE_UPDATE_SUCCESS` : toast.action)}
          onClose={() => setToast(null)}
          style={{ maxWidth: "670px" }}
        />
      )}
    </div>
  );
};

export default EpostTrackingPage;
