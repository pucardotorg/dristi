import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import OverlayDropdown from "../components/HearingOverlayDropdown";
import { hearingService } from "../hooks/services";

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
              action: (history, column) => {
                column.openRescheduleDialog(row);
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
