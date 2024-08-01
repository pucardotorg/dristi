import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import OverlayDropdown from "../components/HearingOverlayDropdown";
import { hearingService } from "../hooks/services";

const formatDate = (date) => {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${year}-${month}-${day}`;
};

export const UICustomizations = {
  PreHearingsConfig: {
    preProcess: (requestCriteria) => {
      const updatedCriteria = {
        pagination: {
          limit: 5,
          offset: 0,
        },
        limit: 5,
        fromDate: requestCriteria?.params.fromDate,
        toDate: requestCriteria?.params.toDate,
      };

      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          criteria: updatedCriteria,
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
      const userType = userInfo.type === "CITIZEN" ? "citizen" : "employee";
      const searchParams = new URLSearchParams();
      searchParams.set("hearingId", row.hearingId);
      switch (key) {
        case "Actions":
          return (
            <div style={{ display: "flex", justifyContent: "center", alignItems: "center" }}>
              {row.hearing.status === "SCHEDULED" && userInfo.roles.map((role) => role.code).includes("HEARING_START") && (
                <Button
                  variation={"secondary"}
                  label={""}
                  onButtonClick={() => {
                    hearingService.startHearing({ hearing: row.hearing }).then(() => {
                      window.location.href = `/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`;
                    });
                  }}
                  style={{ marginRight: "1rem" }}
                >
                  <strong>{t(`START_HEARING`)}</strong>
                </Button>
              )}
              {row.hearing.status === "SCHEDULED" && !userInfo.roles.map((role) => role.code).includes("HEARING_START") && (
                <span>{t("HEARING_AWAITING_START")}</span>
              )}
              {row.hearing.status === "INPROGRESS" && (
                <Button
                  variation={"secondary"}
                  label={""}
                  onButtonClick={() => {
                    window.location.href = `/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`;
                  }}
                  style={{ marginRight: "1rem" }}
                >
                  <strong>{t("JOIN_HEARING")}</strong>
                </Button>
              )}
              <OverlayDropdown style={{ position: "absolute" }} column={column} row={row} master="commonUiConfig" module="PreHearingsConfig" />
            </div>
          );
        default:
          return t("ES_COMMON_NA");
      }
    },
    dropDownItems: (row) => {
      const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
      const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
      const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
      const userType = userInfo.type === "CITIZEN" ? "citizen" : "employee";
      const searchParams = new URLSearchParams();
      const future = row.hearing.startTime > Date.now();
      if (userInfo.roles.map((role) => role.code).includes("EMPLOYEE")) {
        if (future) {
          return [
            {
              label: "View Case",
              id: "view_case",
              action: (history) => {
                searchParams.set("caseId", row.caseId);
                searchParams.set("filingNumber", row.filingNumber);

                history.push({ pathname: `/${window.contextPath}/${userType}/dristi/home/view-case`, search: searchParams.toString() });
              },
            },
            {
              label: "Reschedule hearing",
              id: "reschedule",
              action: (history) => {
                const date = new Date(row.hearing.startTime);
                const requestBody = {
                  order: {
                    createdDate: new Date().getTime(),
                    tenantId: Digit.ULBService.getCurrentTenantId(),
                    filingNumber: row.filingNumber,
                    statuteSection: {
                      tenantId: Digit.ULBService.getCurrentTenantId(),
                    },
                    orderType: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                    status: "",
                    isActive: true,
                    workflow: {
                      action: OrderWorkflowAction.SAVE_DRAFT,
                      comments: "Creating order",
                      assignes: null,
                      rating: null,
                      documents: [{}],
                    },
                    documents: [],
                    additionalDetails: {
                      formdata: {
                        orderType: {
                          type: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                          isactive: true,
                          code: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                          name: "ORDER_TYPE_INITIATING_RESCHEDULING_OF_HEARING_DATE",
                        },
                        originalHearingDate: formatDate(date),
                      },
                    },
                  },
                };
                ordersService
                  .createOrder(requestBody, { tenantId: Digit.ULBService.getCurrentTenantId() })
                  .then((res) => {
                    searchParams.set("filingNumber", row.filingNumber);
                    searchParams.set("orderNumber", res.order.orderNumber);
                    history.push({
                      pathname: `/${window.contextPath}/${userType}/orders/generate-orders`,
                      search: searchParams.toString(),
                      state: {
                        caseId: row.caseId,
                        tab: "Orders",
                      },
                    });
                  })
                  .catch((err) => {
                    console.error(err);
                  });
              },
            },
          ];
        }
        return [
          {
            label: "View Case",
            id: "view_case",
            action: (history) => {
              searchParams.set("caseId", row.caseId);
              searchParams.set("filingNumber", row.filingNumber);

              history.push({ pathname: `/${window.contextPath}/${userType}/dristi/home/view-case`, search: searchParams.toString() });
            },
          },
          {
            label: "View transcript",
            id: "view_transcript",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View witness deposition",
            id: "view_witness",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View pending task",
            id: "view_pending_tasks",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
        ];
      }

      if (userInfo.roles.map((role) => role.code).includes("CITIZEN")) {
        if (future) {
          return [
            {
              label: "View Case",
              id: "view_case",
              action: (history) => {
                searchParams.set("caseId", row.caseId);
                searchParams.set("filingNumber", row.filingNumber);

                history.push({ pathname: `/${window.contextPath}/${userType}/dristi/home/view-case`, search: searchParams.toString() });
              },
            },
            {
              label: "Request for Reschedule",
              id: "reschedule",
              action: (history) => {
                searchParams.set("hearingId", row.hearingId);
                searchParams.set("applicationType", "RE_SCHEDULE");
                searchParams.set("filingNumber", row.filingNumber);
                history.push({ pathname: `/${window.contextPath}/${userType}/submissions/submissions-create`, search: searchParams.toString() });
              },
            },
          ];
        }
        return [
          {
            label: "View Case",
            id: "view_case",
            action: (history) => {
              searchParams.set("caseId", row.caseId);
              searchParams.set("filingNumber", row.filingNumber);

              history.push({ pathname: `/${window.contextPath}/${userType}/dristi/home/view-case`, search: searchParams.toString() });
            },
          },
          {
            label: "View transcript",
            id: "view_transcript",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View witness deposition",
            id: "view_witness",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View pending task",
            id: "view_pending_tasks",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
        ];
      }
      return [];
    },
  },
};
