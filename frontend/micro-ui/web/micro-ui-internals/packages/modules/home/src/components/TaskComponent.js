import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Dropdown } from "@egovernments/digit-ui-components";
import { CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { Loader } from "@egovernments/digit-ui-react-components";
import { useGetPendingTask } from "../hooks/useGetPendingTask";
import { useTranslation } from "react-i18next";
import PendingTaskAccordion from "./PendingTaskAccordion";
import { HomeService } from "../hooks/services";
import { pendingTaskCaseActions, pendingTaskSubmissionActions } from "../configs/HomeConfig";
const TasksComponent = ({ taskType, setTaskType, isLitigant, uuid, userInfoType }) => {
  const tenantId = useMemo(() => Digit.ULBService.getCurrentTenantId(), []);
  const [pendingTasks, setPendingTasks] = useState([]);
  const { t } = useTranslation();
  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles?.map((role) => role?.code) || [], []);
  const taskTypeCode = useMemo(() => taskType?.code, [taskType]);

  const { data: pendingTaskDetails = [], isLoading } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          entityType: taskType?.code || "case",
          ...(isLitigant && { assignedTo: [uuid] }),
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

  const pendingTaskActionDetails = useMemo(() => (isLoading ? [] : pendingTaskDetails?.data || []), [pendingTaskDetails, isLoading]);

  const getCaseDetailByFilingNumber = useCallback(
    async (payload) => {
      const caseData = await HomeService.customApiService("/case/case/v1/_search", {
        tenantId,
        ...payload,
      });
      return caseData || {};
    },
    [tenantId]
  );

  const fetchPendingTasks = useCallback(
    async function () {
      if (isLoading) return;
      const listOfFilingNumber = pendingTaskActionDetails?.map((data) => ({
        filingNumber: data?.fields?.find((field) => field.key === "referenceId")?.value || "",
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
          const filingNumber = data?.fields?.find((field) => field.key === "referenceId")?.value || "";
          const caseDetail = pendingTaskToCaseDetailMap.get(filingNumber);
          const status = data?.fields?.find((field) => field.key === "status")?.value;
          const dueInSec = data?.fields?.find((field) => field.key === "businessServiceSla")?.value;
          const isCompleted = data?.fields?.find((field) => field.key === "isCompleted")?.value;
          const pendingTaskActions =
            taskTypeCode === "case" ? pendingTaskCaseActions : taskTypeCode === "hearing" ? pendingTaskSubmissionActions : pendingTaskCaseActions;
          const searchParams = new URLSearchParams();
          const dayCount = Math.abs(Math.ceil(dueInSec / (1000 * 3600 * 24)));
          pendingTaskActions?.[status]?.redirectDetails?.params.forEach((item) => {
            searchParams.set(item?.key, item?.value ? caseDetail?.[item?.value] : item?.defaultValue);
          });
          const redirectUrl = `/${window?.contextPath}/${userInfoType}${
            pendingTaskActions?.[status]?.redirectDetails?.url
          }?${searchParams.toString()}`;
          return {
            actionName: pendingTaskActions?.[status]?.actionName,
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
    <div className="tasks-component" style={{ boxShadow: "none", border: "1px solid #e0e0e0" }}>
      <h2 style={{ fontFamily: "Roboto", fontSize: "32px", fontWeight: "700", lineHeight: "37.5px", textAlign: "left" }}>Your Tasks</h2>
      <div className="filters">
        <LabelFieldPair>
          <Dropdown
            style={{ width: "16rem" }}
            option={[{ name: "NIA S138", code: "NIA S138" }]}
            selected={{ name: "NIA S138", code: "NIA S138" }}
            optionKey={"code"}
            select={(value) => {}}
            placeholder={t("CS_CASE_TYPE")}
          />
        </LabelFieldPair>
        <LabelFieldPair>
          <Dropdown
            style={{ width: "16rem" }}
            option={[
              { code: "case", name: "Case" },
              { code: "hearing", name: "Hearing" },
            ]}
            optionKey={"name"}
            selected={taskType}
            select={(value) => {
              setTaskType(value);
            }}
            placeholder={t("CS_CASE_TYPE")}
          />
        </LabelFieldPair>
      </div>
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
        <PendingTaskAccordion pendingTasks={allOtherPendingTask} accordionHeader={"All other tasks"} t={t} totalCount={allOtherPendingTask?.length} />
      </div>
      <div className="task-section"></div>
    </div>
  );
};
export default TasksComponent;
