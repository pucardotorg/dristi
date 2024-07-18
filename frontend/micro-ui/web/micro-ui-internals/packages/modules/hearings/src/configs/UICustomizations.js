import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import OverlayDropdown from "../components/HearingOverlayDropdown";

const formatDate = (date) => {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
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
              <Button
                variation={"secondary"}
                label={""}
                onButtonClick={() => {
                  window.location.href = `/${window.contextPath}/${userType}/hearings/inside-hearing?${searchParams.toString()}`;
                }}
                style={{ marginRight: "1rem" }}
              >
                <strong>Start</strong>
              </Button>
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
      if (true || userInfo.roles.includes("JUDGE_ROLE")) {
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
              const requestBody = {
                order: {
                  createdDate: formatDate(new Date()),
                  tenantId: Digit.ULBService.getCurrentTenantId(),
                  cnrNumber: row.cnrNumber,
                  filingNumber: row.filingNumber,
                  statuteSection: {
                    tenantId: Digit.ULBService.getCurrentTenantId(),
                  },
                  orderType: "Bail",
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
                  additionalDetails: {},
                },
              };
              ordersService
                .createOrder(requestBody, { tenantId: Digit.ULBService.getCurrentTenantId() })
                .then(() => {
                  history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${row.filingNumber}`, {
                    caseId: row.caseId,
                    tab: "Orders",
                  });
                })
                .catch((err) => {});
              searchParams.set("filingNumber", row.filingNumber);
              history.push({ pathname: `/${window.contextPath}/${userType}/orders/generate-orders`, search: searchParams.toString() });
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
    },
  },
};
