import React, { useCallback, useEffect, useMemo, useState } from "react";
import { CardLabel, Dropdown } from "@egovernments/digit-ui-components";
import { LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { Loader } from "@egovernments/digit-ui-react-components";
import { useGetPendingTask } from "../hooks/useGetPendingTask";
import { useTranslation } from "react-i18next";
import PendingTaskAccordion from "./PendingTaskAccordion";
import { HomeService, Urls } from "../hooks/services";
import { caseTypes, selectTaskType, taskTypes } from "../configs/HomeConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

export const CaseWorkflowAction = {
  SAVE_DRAFT: "SAVE_DRAFT",
  ESIGN: "E-SIGN",
  ABANDON: "ABANDON",
};
const dayInMillisecond = 1000 * 3600 * 24;

const TasksComponent = ({ taskType, setTaskType, caseType, setCaseType, isLitigant, uuid, filingNumber, inCase = false }) => {
  const tenantId = useMemo(() => Digit.ULBService.getCurrentTenantId(), []);
  const [pendingTasks, setPendingTasks] = useState([]);
  const history = useHistory();
  const { t } = useTranslation();
  const roles = useMemo(() => Digit.UserService.getUser()?.info?.roles?.map((role) => role?.code) || [], []);
  const taskTypeCode = useMemo(() => taskType?.code, [taskType]);
  const [searchCaseLoading, setSearchCaseLoading] = useState(false);
  const userInfo = Digit.UserService.getUser()?.info;
  const todayDate = useMemo(() => new Date().getTime(), []);
  const [totalPendingTask, setTotalPendingTask] = useState(0);
  const userType = useMemo(() => (userInfo.type === "CITIZEN" ? "citizen" : "employee"), [userInfo.type]);
  const { data: pendingTaskDetails = [], isLoading, refetch } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          ...(taskType?.code && { entityType: taskType?.code }),
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
    key: `${taskType?.code}-${filingNumber}`,
    config: { enabled: Boolean(taskType.code && tenantId) },
  });

  useEffect(() => {
    refetch();
  }, [refetch]);

  const pendingTaskActionDetails = useMemo(() => {
    if (!totalPendingTask) {
      setTotalPendingTask(pendingTaskDetails?.data?.length);
    }
    return isLoading ? [] : pendingTaskDetails?.data || [];
  }, [totalPendingTask, isLoading, pendingTaskDetails?.data]);

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

  const getApplicationDetail = useCallback(
    async (applicationNumber) => {
      setSearchCaseLoading(true);
      const applicationData = await HomeService.customApiService(Urls.applicationSearch, {
        criteria: {
          filingNumber,
          tenantId,
          applicationNumber,
        },
        tenantId,
      });
      setSearchCaseLoading(false);
      return applicationData?.applicationList?.[0] || {};
    },
    [filingNumber, tenantId]
  );

  const getOrderDetail = useCallback(
    async (orderNumber) => {
      setSearchCaseLoading(true);
      const orderData = await HomeService.customApiService(Urls.orderSearch, {
        criteria: {
          filingNumber,
          tenantId,
          orderNumber,
        },
        tenantId,
      });
      setSearchCaseLoading(false);
      return orderData?.list?.[0] || {};
    },
    [filingNumber, tenantId]
  );

  const handleReviewOrder = useCallback(
    async ({ filingNumber, caseId, referenceId }) => {
      const orderDetails = await getOrderDetail(referenceId);
      history.push(`/${window.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Orders`, {
        orderObj: orderDetails,
      });
    },
    [getOrderDetail, history, userType]
  );

  const handleReviewSubmission = useCallback(
    async ({ filingNumber, caseId, referenceId }) => {
      const getDate = (value) => {
        const date = new Date(value);
        const day = date.getDate().toString().padStart(2, "0");
        const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
        const year = date.getFullYear();
        const formattedDate = `${day}-${month}-${year}`;
        return formattedDate;
      };
      const applicationDetails = await getApplicationDetail(referenceId);
      const defaultObj = {
        status: applicationDetails?.status,
        details: {
          applicationType: applicationDetails?.applicationType,
          applicationSentOn: getDate(parseInt(applicationDetails?.auditDetails?.createdTime)),
          sender: applicationDetails?.owner,
          additionalDetails: applicationDetails?.additionalDetails,
          applicationId: applicationDetails?.id,
          auditDetails: applicationDetails?.auditDetails,
        },
        applicationContent: null,
        comments: applicationDetails?.comment ? applicationDetails?.comment : [],
        applicationList: applicationDetails,
      };

      const docObj = applicationDetails?.documents?.map((doc) => {
        return {
          status: applicationDetails?.status,
          details: {
            applicationType: applicationDetails?.applicationType,
            applicationSentOn: getDate(parseInt(applicationDetails?.auditDetails?.createdTime)),
            sender: applicationDetails?.owner,
            additionalDetails: applicationDetails?.additionalDetails,
            applicationId: applicationDetails?.id,
            auditDetails: applicationDetails?.auditDetails,
          },
          applicationContent: {
            tenantId: applicationDetails?.tenantId,
            fileStoreId: doc.fileStore,
            id: doc.id,
            documentType: doc.documentType,
            documentUid: doc.documentUid,
            additionalDetails: doc.additionalDetails,
          },
          comments: applicationDetails?.comment ? applicationDetails?.comment : [],
          applicationList: applicationDetails,
        };
      }) || [defaultObj];

      history.push(`/${window.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Submissions`, {
        applicationDocObj: docObj,
      });
    },
    [getApplicationDetail, history, userType]
  );

  const handleCreateOrder = useCallback(
    async ({ cnrNumber, filingNumber, orderType, referenceId }) => {
      let reqBody = {
        order: {
          createdDate: new Date().getTime(),
          tenantId,
          cnrNumber,
          filingNumber: filingNumber,
          statuteSection: {
            tenantId,
          },
          orderType: orderType,
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
          additionalDetails: {
            formdata: {
              orderType: {
                code: orderType,
                type: orderType,
                name: `ORDER_TYPE_${orderType}`,
              },
              refApplicationId: referenceId,
            },
          },
        },
      };
      try {
        const res = await HomeService.customApiService(Urls.orderCreate, reqBody, { tenantId });
        HomeService.customApiService(Urls.pendingTask, {
          pendingTask: {
            name: "Order Created",
            entityType: "order-managelifecycle",
            referenceId: `MANUAL_${referenceId}`,
            status: "SAVE_DRAFT",
            assignedTo: [],
            assignedRole: ["JUDGE_ROLE"],
            cnrNumber: null,
            filingNumber: filingNumber,
            isCompleted: true,
            stateSla: null,
            additionalDetails: {},
            tenantId,
          },
        });
        history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`);
      } catch (error) {}
    },
    [history, tenantId]
  );

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

      const getCustomFunction = {
        handleCreateOrder,
        handleReviewSubmission,
        handleReviewOrder,
      };

      const tasks = await Promise.all(
        pendingTaskActionDetails?.map(async (data) => {
          const filingNumber = data?.fields?.find((field) => field.key === "filingNumber")?.value || "";
          const cnrNumber = data?.fields?.find((field) => field.key === "cnrNumber")?.value || "";
          const caseDetail = pendingTaskToCaseDetailMap.get(filingNumber);
          const status = data?.fields?.find((field) => field.key === "status")?.value;
          const dueInSec = data?.fields?.find((field) => field.key === "businessServiceSla")?.value;
          const stateSla = data?.fields?.find((field) => field.key === "stateSla")?.value;
          const isCompleted = data?.fields?.find((field) => field.key === "isCompleted")?.value;
          const actionName = data?.fields?.find((field) => field.key === "name")?.value;
          const referenceId = data?.fields?.find((field) => field.key === "referenceId")?.value;
          const entityType = data?.fields?.find((field) => field.key === "entityType")?.value;
          const updateReferenceId = referenceId.startsWith("MANUAL_") ? referenceId.substring("MANUAL_".length) : referenceId;
          const defaultObj = { referenceId: updateReferenceId, ...caseDetail };
          const pendingTaskActions = selectTaskType?.[entityType || taskTypeCode];
          const isCustomFunction = Boolean(pendingTaskActions?.[status]?.customFunction);
          const dayCount = stateSla
            ? Math.abs(Math.ceil((Number(stateSla) - todayDate) / dayInMillisecond))
            : dueInSec
            ? Math.abs(Math.ceil(dueInSec / dayInMillisecond))
            : null;
          const additionalDetails = pendingTaskActions?.[status]?.additionalDetailsKeys?.reduce((result, current) => {
            result[current] = data?.fields?.find((field) => field.key === `additionalDetails.${current}`)?.value;
            return result;
          }, {});
          const searchParams = new URLSearchParams();
          pendingTaskActions?.[status]?.redirectDetails?.params?.forEach((item) => {
            searchParams.set(item?.key, item?.value ? defaultObj?.[item?.value] : item?.defaultValue);
          });
          const redirectUrl = isCustomFunction
            ? getCustomFunction[pendingTaskActions?.[status]?.customFunction]
            : `/${window?.contextPath}/${userType}${pendingTaskActions?.[status]?.redirectDetails?.url}?${searchParams.toString()}`;
          const due = dayCount > 1 ? `Due in ${dayCount} Days` : dayCount === 1 || dayCount === 0 ? `Due today` : `No Due Date`;
          return {
            actionName: actionName || pendingTaskActions?.[status]?.actionName,
            caseTitle: caseDetail?.caseTitle || "",
            filingNumber: filingNumber,
            caseType: "NIA S138",
            due: due,
            dayCount: dayCount ? dayCount : dayCount === 0 ? 0 : Infinity,
            isCompleted,
            dueDateColor: due === "Due today" ? "#9E400A" : "",
            redirectUrl,
            params: { ...additionalDetails, cnrNumber, filingNumber, caseId: caseDetail?.id, referenceId: updateReferenceId },
            isCustomFunction,
          };
        })
      );
      setPendingTasks(tasks);
    },
    [
      getCaseDetailByFilingNumber,
      handleCreateOrder,
      handleReviewOrder,
      handleReviewSubmission,
      isLoading,
      pendingTaskActionDetails,
      taskTypeCode,
      todayDate,
      userType,
    ]
  );

  useEffect(() => {
    fetchPendingTasks();
  }, [fetchPendingTasks]);

  const { pendingTaskDataInWeek, allOtherPendingTask } = useMemo(
    () => ({
      pendingTaskDataInWeek:
        pendingTasks
          .filter((data) => data?.dayCount < 7 && !data?.isCompleted)
          .map((data) => data)
          .sort((data) => data?.dayCount) || [],
      allOtherPendingTask:
        pendingTasks
          .filter((data) => data?.dayCount >= 7 && !data?.isCompleted)
          .map((data) => data)
          .sort((data) => data?.dayCount) || [],
    }),
    [pendingTasks]
  );
  if (isLoading) {
    return <Loader />;
  }
  return (
    <div className="tasks-component">
      <h2>{!isLitigant ? "Your Tasks" : t("ALL_PENDING_TASK_TEXT")}</h2>
      {totalPendingTask !== undefined && totalPendingTask > 0 ? (
        <React.Fragment>
          <div className="task-filters">
            <LabelFieldPair>
              <CardLabel style={{ fontSize: "16px" }} className={"card-label"}>{`Case Type`}</CardLabel>
              <Dropdown
                option={caseTypes}
                selected={caseType}
                optionKey={"name"}
                select={(value) => {
                  setCaseType(value);
                }}
                placeholder={t("CS_CASE_TYPE")}
              />
            </LabelFieldPair>
            <LabelFieldPair>
              <CardLabel style={{ fontSize: "16px" }} className={"card-label"}>{`Task Type`}</CardLabel>
              <Dropdown
                option={taskTypes}
                optionKey={"name"}
                selected={taskType}
                select={(value) => {
                  setTaskType(value);
                }}
                placeholder={t("CS_TASK_TYPE")}
              />
            </LabelFieldPair>
          </div>

          <React.Fragment>
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
          </React.Fragment>
        </React.Fragment>
      ) : (
        <div
          style={{
            fontSize: "20px",
            fontStyle: "italic",
            lineHeight: "23.44px",
            fontWeight: "500",
            font: "Roboto",
            color: "#77787B",
          }}
        >
          {!isLitigant ? t("NO_TASK_TEXT") : t("NO_PENDING_TASK_TEXT")}
        </div>
      )}
    </div>
  );
};
export default TasksComponent;
