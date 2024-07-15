import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Dropdown } from "@egovernments/digit-ui-components";
import { CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { Loader } from "@egovernments/digit-ui-react-components";
import { useGetPendingTask } from "../hooks/useGetPendingTask";
import { useTranslation } from "react-i18next";
import PendingTaskAccordion from "./PendingTaskAccordion";
import { HomeService } from "../hooks/services";
import { pendingTaskCaseActions, pendingTaskSubmissionActions } from "../configs/HomeConfig";
const TasksComponent = ({ taskType, setTaskType }) => {
  const tenantId = useMemo(() => Digit.ULBService.getCurrentTenantId(), []);
  const [pendingTasks, setPendingTasks] = useState([]);
  const { t } = useTranslation();
  const taskTypeCode = useMemo(() => taskType?.code, [taskType]);
  const { data: pendingTaskDetails = [], isLoading } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          entityType: taskType?.code || "case",
        },
        limit: 10,
        offset: 0,
      },
    },
    params: { tenantId },
    key: taskType?.code,
    config: { enable: Boolean(taskType.code && tenantId) },
  });

  const pendingTaskActionDetails = useMemo(() => (isLoading ? [] : pendingTaskDetails?.data || []), [pendingTaskDetails, isLoading]);

  const getCaseDetailByFilingNumber = async (filingNumber) => {
    const caseData = await HomeService.customApiService("/case/case/v1/_search", {
      tenantId,
      criteria: [
        {
          filingNumber,
          pagination: { offSet: 0, limit: 1 },
        },
      ],
    });
    return caseData?.criteria?.[0]?.responseList?.[0] || {};
  };

  const fetchPendingTasks = async function () {
    if (isLoading) return;
    const tasks = await Promise.all(
      pendingTaskActionDetails?.map(async (data) => {
        const filingNumber = data?.fields?.find((field) => field.key === "referenceId")?.value || "";
        const caseDetail = await getCaseDetailByFilingNumber(filingNumber);
        const status = data?.fields?.find((field) => field.key === "status")?.value;
        const dueInSec = data?.fields?.find((field) => field.key === "businessServiceSla")?.value;
        const pendingTaskActions =
          taskTypeCode === "case" ? pendingTaskCaseActions : taskTypeCode === "hearing" ? pendingTaskSubmissionActions : pendingTaskCaseActions;
        return {
          actionName: pendingTaskActions?.[status]?.actionName,
          caseTitle: caseDetail?.caseTitle || "",
          filingNumber: filingNumber,
          caseType: "NIA S138",
          due: Math.abs(dueInSec / (3600 * 24)) > 1 ? `Due in ${Math.abs(Math.ceil(dueInSec / (3600 * 24)))} Days` : `Due today`,
        };
      })
    );
    setPendingTasks(tasks);
  };

  useEffect(() => {
    fetchPendingTasks();
  }, [pendingTaskActionDetails, taskTypeCode]);

  console.log("pendingTasks", pendingTasks, pendingTaskActionDetails);
  const pendingTaskData = useMemo(
    () =>
      pendingTasks || [
        {
          actionName: "Reschedule hearing request",
          caseTitle: "Aparna vs Sandesh",
          caseType: "NIA S138",
          filingNumber: "PB-PT-2023",
          due: "Due today",
        },
        {
          actionName: "Reschedule hearing request",
          caseTitle: "Raj vs Anushka",
          caseType: "NIA S138",
          filingNumber: "PB-PT-2023",
          due: "Hearing in 2 days",
        },
      ],
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
          pendingTasks={pendingTaskData}
          accordionHeader={"Complete this week"}
          t={t}
          totalCount={pendingTaskData?.length}
          isHighlighted={true}
          isAccordionOpen={true}
        />
      </div>
      <div className="task-section">
        <PendingTaskAccordion pendingTasks={pendingTaskData} accordionHeader={"All other tasks"} t={t} totalCount={pendingTaskData?.length} />
      </div>
      <div className="task-section"></div>
    </div>
  );
};
export default TasksComponent;
