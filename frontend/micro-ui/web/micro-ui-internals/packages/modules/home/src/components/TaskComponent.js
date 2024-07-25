import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Dropdown } from "@egovernments/digit-ui-components";
import { LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { Loader } from "@egovernments/digit-ui-react-components";
import { useGetPendingTask } from "../hooks/useGetPendingTask";
import { useTranslation } from "react-i18next";
import PendingTaskAccordion from "./PendingTaskAccordion";
import { HomeService, Urls } from "../hooks/services";
import { selectTaskType, taskTypes } from "../configs/HomeConfig";
import { formatDate } from "@egovernments/digit-ui-module-dristi/src/pages/citizen/FileCase/CaseType";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

export const CaseWorkflowAction = {
  SAVE_DRAFT: "SAVE_DRAFT",
  ESIGN: "E-SIGN",
  ABANDON: "ABANDON",
};

const TasksComponent = ({ taskType, setTaskType, isLitigant, uuid, userInfoType, filingNumber }) => {
  const tenantId = useMemo(() => Digit.ULBService.getCurrentTenantId(), []);
  const [pendingTasks, setPendingTasks] = useState([]);
  const history = useHistory();
  const { t } = useTranslation();
  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles?.map((role) => role?.code) || [], []);
  const taskTypeCode = useMemo(() => taskType?.code, [taskType]);
  const [searchCaseLoading, setSearchCaseLoading] = useState(false);

  const { data: pendingTaskDetails = [], isLoading, refetch } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          entityType: taskType?.code || "case",
          ...(filingNumber && { filingNumber: filingNumber }),
          isCompleted: false,
          ...(isLitigant && { assignedTo: uuid }),
          ...(!isLitigant && { assignedRole: [...roles] }),
        },
        limit: 10000,
        offset: 0,
      },
    },
    params: { tenantId },
    key: taskType?.code,
    config: { enable: Boolean(taskType.code && tenantId) },
  });

  useEffect(() => {
    refetch();
  }, [refetch]);

  const pendingTaskActionDetails = useMemo(() => (isLoading ? [] : pendingTaskDetails?.data || []), [pendingTaskDetails, isLoading]);

  const getCaseDetailByFilingNumber = useCallback(
    async (payload) => {
      setSearchCaseLoading(true);
      const caseData = await HomeService.customApiService(Urls.caseSearch, {
        tenantId,
        ...payload,
      });
      setSearchCaseLoading(false);
      return caseData || {};
    },
    [tenantId]
  );
  const handleCreateOrder = (cnrNumber, filingNumber, caseId) => {
    let reqBody = {
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "REFERRAL_CASE_TO_ADR",
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

    HomeService.customApiService(Urls.orderCreate, reqBody, { tenantId })
      .then(() => {
        history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}`, { caseId: caseId, tab: "Orders" });
      })
      .catch((err) => {});
  };

  const getCustomFunction = {
    handleCreateOrder,
  };

  const fetchPendingTasks = useCallback(
    async function () {
      if (isLoading) return;
      const listOfFilingNumber = pendingTaskActionDetails?.map((data) => ({
        filingNumber: data?.fields?.find((field) => field.key === "filingNumber")?.value || "",
      }));
      const allPendingTaskCaseDetails = await getCaseDetailByFilingNumber({
        criteria: listOfFilingNumber,
      });
      const pendingTaskToCaseDetailMap = new Map();
      allPendingTaskCaseDetails?.criteria?.forEach((element) => {
        pendingTaskToCaseDetailMap.set(element?.filingNumber, element?.responseList?.[0]);
      });
      const tasks = await Promise.all(
        pendingTaskActionDetails?.map(async (data) => {
          const filingNumber = data?.fields?.find((field) => field.key === "filingNumber")?.value || "";
          const caseDetail = pendingTaskToCaseDetailMap.get(filingNumber);
          const status = data?.fields?.find((field) => field.key === "status")?.value;
          const dueInSec = data?.fields?.find((field) => field.key === "businessServiceSla")?.value;
          const isCompleted = data?.fields?.find((field) => field.key === "isCompleted")?.value;
          const actionName = data?.fields?.find((field) => field.key === "name")?.value;
          const referenceId = data?.fields?.find((field) => field.key === "referenceId")?.value;
          const updateReferenceId = referenceId.startsWith("MANUAL_") ? referenceId.substring("MANUAL_".length) : referenceId;
          const defaultObj = { referenceId: updateReferenceId, ...caseDetail };
          const pendingTaskActions = selectTaskType?.[taskTypeCode];
          const searchParams = new URLSearchParams();
          const dayCount = Math.abs(Math.ceil(dueInSec / (1000 * 3600 * 24)));
          pendingTaskActions?.[status]?.redirectDetails?.params?.forEach((item) => {
            searchParams.set(item?.key, item?.value ? defaultObj?.[item?.value] : item?.defaultValue);
          });
          const redirectUrl = `/${window?.contextPath}/${userInfoType}${
            pendingTaskActions?.[status]?.redirectDetails?.url
          }?${searchParams.toString()}`;
          return {
            actionName: actionName || pendingTaskActions?.[status]?.actionName,
            caseTitle: caseDetail?.caseTitle || "",
            filingNumber: filingNumber,
            caseType: "NIA S138",
            due: dayCount > 1 ? `Due in ${dayCount} Days` : `Due today`,
            dayCount,
            isCompleted,
            redirectUrl,
          };
        })
      );
      setPendingTasks(tasks);
    },
    [getCaseDetailByFilingNumber, isLoading, pendingTaskActionDetails, taskTypeCode, userInfoType]
  );

  useEffect(() => {
    fetchPendingTasks();
  }, [fetchPendingTasks]);

  console.log("pendingTasks", pendingTasks, pendingTaskActionDetails);
  const { pendingTaskDataInWeek, allOtherPendingTask } = useMemo(
    () => ({
      pendingTaskDataInWeek: pendingTasks.filter((data) => data?.dayCount < 7 && !data?.isCompleted).map((data) => data) || [],
      allOtherPendingTask: pendingTasks.filter((data) => data?.dayCount >= 7 && !data?.isCompleted).map((data) => data) || [],
    }),
    [pendingTasks]
  );
  if (isLoading) {
    return <Loader />;
  }
  return (
    <div className="tasks-component">
      <h2>Your Tasks</h2>
      <div className="task-filters">
        <LabelFieldPair>
          <Dropdown
            option={[{ name: "NIA S138", code: "NIA S138" }]}
            selected={{ name: "NIA S138", code: "NIA S138" }}
            optionKey={"code"}
            select={(value) => {}}
            placeholder={t("CS_CASE_TYPE")}
          />
        </LabelFieldPair>
        <LabelFieldPair>
          <Dropdown
            option={taskTypes}
            optionKey={"name"}
            selected={taskType}
            select={(value) => {
              setTaskType(value);
            }}
            placeholder={t("CS_CASE_TYPE")}
          />
        </LabelFieldPair>
      </div>
      {searchCaseLoading && <Loader />}
      {!searchCaseLoading && (
        <React.Fragment>
          <div className="task-section">
            <PendingTaskAccordion
              pendingTasks={pendingTaskDataInWeek}
              accordionHeader={"Complete this week"}
              t={t}
              totalCount={pendingTaskDataInWeek?.length}
              isHighlighted={true}
              isAccordionOpen={true}
            />
          </div>
          <div className="task-section">
            <PendingTaskAccordion
              pendingTasks={allOtherPendingTask}
              accordionHeader={"All other tasks"}
              t={t}
              totalCount={allOtherPendingTask?.length}
            />
          </div>
          <div className="task-section"></div>
        </React.Fragment>
      )}
    </div>
  );
};
export default TasksComponent;
