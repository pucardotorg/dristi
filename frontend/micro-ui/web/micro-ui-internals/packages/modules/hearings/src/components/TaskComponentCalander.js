import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Loader } from "@egovernments/digit-ui-react-components";
import Accordian from "./Accordian";
import { Urls } from "../hooks/services/Urls";
import { hearingService } from "../hooks/services";

const DownloadIcon = () => {
  return (
    <svg width="12" height="15" viewBox="0 0 12 15" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M11.8337 5H8.50033V0H3.50033V5H0.166992L6.00033 10.8333L11.8337 5ZM0.166992 12.5V14.1667H11.8337V12.5H0.166992Z" fill="#0B0C0C" />
    </svg>
  );
};

const getFormattedDate = () => {
  const date = new Date();
  const day = date.getDate().toString().padStart(2, "0");
  const month = date.toLocaleString("en-US", { month: "short" });
  return `Today, ${day} ${month}`;
};

const groupByFilingNumber = (data) => {
  const groupedData = data.reduce((acc, item) => {
    const filingField = item.fields.find((field) => field.key === "filingNumber");
    const filingNumber = filingField ? filingField.value : null;

    if (!acc[filingNumber]) {
      acc[filingNumber] = [];
    }
    acc[filingNumber].push(item);
    return acc;
  }, {});

  // Filter out entries where filingNumber is null
  return Object.entries(groupedData)
    .filter(([filingNumber]) => filingNumber !== null && filingNumber !== "null")
    .map(([filingNumber, data]) => ({
      filingNumber,
      data,
    }));
};

const TaskComponentCalander = ({ isLitigant, uuid, filingNumber, inCase = false }) => {
  const tenantId = useMemo(() => Digit.ULBService.getCurrentTenantId(), []);
  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles?.map((role) => role?.code) || [], []);
  const todayDate = getFormattedDate();
  const [groupedData, setGroupedData] = useState([]);
  const [searchCaseLoading, setSearchCaseLoading] = useState(false);
  const [caseDataDetails, setCaseDataDetails] = useState([]);

  const { data: pendingTaskDetails = [], isLoading, refetch } = Digit.Hooks.home.useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          isCompleted: false,
          ...(isLitigant && { assignedTo: uuid }),
          ...(!isLitigant && { assignedRole: [...roles] }),
          ...(inCase && { filingNumber: filingNumber }),
        },
        limit: 10000,
        offset: 0,
      },
    },
    params: { tenantId },
    key: `${filingNumber}`,
    config: { enabled: Boolean(tenantId) },
  });

  useEffect(() => {
    const fetchData = async () => {
      await refetch();

      if (!isLoading && pendingTaskDetails?.data?.length > 0) {
        const grouped = groupByFilingNumber(pendingTaskDetails?.data);
        setGroupedData(grouped);
      }
    };

    fetchData();
  }, [refetch, isLoading, pendingTaskDetails.length]);

  const pendingTaskActionDetails = useMemo(() => {
    return isLoading ? [] : pendingTaskDetails?.data || [];
  }, [isLoading, pendingTaskDetails?.data]);

  const getCaseDetailByFilingNumber = useCallback(
    async (payload) => {
      setSearchCaseLoading(true);
      const caseData = await hearingService.customApiService(Urls.case.caseSearch, {
        tenantId,
        ...payload,
      });
      setSearchCaseLoading(false);
      return caseData || {};
    },
    [tenantId]
  );

  const fetchPendingTasks = useCallback(
    async function () {
      if (isLoading) return;

      const filingNumberSet = new Set();
      pendingTaskActionDetails?.forEach((data) => {
        const filingNumber = data?.fields?.find((field) => field.key === "filingNumber")?.value;
        if (filingNumber) {
          filingNumberSet.add(filingNumber);
        }
      });

      const criteriaList = [];

      for (const filingNumber of filingNumberSet.values()) {
        criteriaList.push({ filingNumber });
      }

      const allPendingTaskCaseDetails = await getCaseDetailByFilingNumber({
        criteria: criteriaList,
      });
      const caseDataDetailsArray =
        allPendingTaskCaseDetails?.criteria?.map((element) => ({
          filingNumber: element?.filingNumber,
          caseDetail: element?.responseList?.[0],
        })) || [];
      setCaseDataDetails(caseDataDetailsArray);
    },
    [getCaseDetailByFilingNumber, isLoading, pendingTaskActionDetails]
  );

  useEffect(() => {
    fetchPendingTasks();
  }, [fetchPendingTasks]);

  const downloadCauseList = async () => {
    return await hearingService.customApiService(
      Urls.hearing.causeList,
      {
        tenantId,
        SearchCriteria: {
          courtId: window?.globalConfigs?.getConfig("COURT_ID"),
        },
      },
      {},
      false,
      true
    );
  };
  const handleDownload = async () => {
    const pdfData = await downloadCauseList();
    const blob = new Blob([pdfData.data], { type: "application/pdf" });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    const currentDate = new Date().toISOString().split("T")[0];
    link.setAttribute("download", `cause_list_${currentDate}.pdf`); // Name of the downloaded file
    document.body.appendChild(link);
    link.click();
    link.parentNode.removeChild(link);
    window.URL.revokeObjectURL(url);
  };

  return (
    <React.Fragment>
      <div style={{ width: "100%", borderBottom: "1px solid #E8E8E8", padding: "24px" }}>
        <div style={{ fontWeight: 700, fontSize: "24px", color: "#231F20" }}>{todayDate}</div>
        <button
          style={{
            width: "330px",
            height: "60px",
            borderRadius: "4px",
            border: "1px solid #E8E8E8",
            backgroundColor: "#FFFFFF",
            display: "flex",
            alignItems: "center",
            justifyContent: "flex-start",
            padding: "16px 12px 16px 20px",
            marginTop: "18px",
          }}
          onClick={handleDownload}
        >
          <DownloadIcon />
          <div style={{ fontWeight: 700, fontSize: "16px", marginLeft: "10px" }}>Download Cause List</div>
        </button>
      </div>
      <div style={{ width: "100%", padding: "24px" }}>
        {!searchCaseLoading ? (
          <div className="task-section">
            <Accordian groupedData={groupedData} caseDataDetails={caseDataDetails} />
          </div>
        ) : (
          <Loader />
        )}
      </div>
    </React.Fragment>
  );
};

export default TaskComponentCalander;
