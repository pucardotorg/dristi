import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { useHistory } from "react-router-dom/";
import PreHearingModal from "../../components/PreHearingModal";
import useGetHearings from "../../hooks/hearings/useGetHearings";
import useGetHearingSlotMetaData from "../../hooks/useGetHearingSlotMetaData";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { ReschedulingPurpose } from "./ReschedulingPurpose";
import TasksComponent from "../../components/TaskComponentCalander";
import { formatDate } from "../../utils";

const tenantId = window?.Digit.ULBService.getCurrentTenantId();

const MonthlyCalendar = () => {
  const history = useHistory();
  const { t } = useTranslation();
  const calendarRef = useRef(null);
  const getCurrentViewType = () => {
    const calendarApi = calendarRef.current.getApi();
    const currentViewType = calendarApi.view.type;
    return currentViewType;
  };
  const { data: courtData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "common-masters", [{ name: "Court_Rooms" }], {
    cacheTime: 0,
  });
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const userInfo = Digit.UserService.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const { data: individualData, isLoading, isFetching } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    "Home",
    "",
    userInfo?.uuid && isUserLoggedIn
  );
  const individualId = useMemo(() => individualData?.Individual?.[0]?.individualId, [individualData]);
  const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
    individualData,
  ]);

  const [dateRange, setDateRange] = useState({});
  const [taskType, setTaskType] = useState({});
  const [caseType, setCaseType] = useState({});
  const initial = userInfoType === "citizen" ? "timeGridDay" : "dayGridMonth";

  const search = window.location.search;
  const { fromDate, toDate, slot, initialView } = useMemo(() => {
    const searchParams = new URLSearchParams(search);
    const fromDate = searchParams.get("from-date") || null;
    const toDate = searchParams.get("to-date") || null;
    const slot = searchParams.get("slot") || null;
    const initialView = searchParams.get("view") || initial;
    return { fromDate, toDate, slot, initialView };
  }, [search]);

  const reqBody = {
    criteria: {
      tenantId,
      fromDate: dateRange.start ? formatDate(dateRange.start) : null,
      toDate: dateRange.end ? formatDate(dateRange.end) : null,
    },
  };

  const { data: hearingResponse, refetch: refetch } = useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", tenantId },
    `${dateRange.start?.toISOString()}-${dateRange.end?.toISOString()}`,
    Boolean(dateRange.start && dateRange.end && individualId),
    false,
    userType === "LITIGANT" && individualId
  );
  const { data: hearingSlots } = useGetHearingSlotMetaData(true);
  const hearingDetails = useMemo(() => hearingResponse?.HearingList || [], [hearingResponse]);

  const events = useMemo(() => hearingSlots || [], [hearingSlots]);

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
      date: `${year}-${month}-${day}`,
      time: `${hours}:${minutes}:${seconds}`,
    };

    return dateTimeObject;
  }

  const Calendar_events = useMemo(() => {
    const calendarEvents = {};

    hearingDetails.forEach((hearing) => {
      const dateTimeObj = epochToDateTimeObject(hearing.startTime);

      if (dateTimeObj) {
        const dateString = dateTimeObj.date;
        events.slots.forEach((slot) => {
          if (dateTimeObj.time >= slot.slotStartTime && dateTimeObj.time < slot.slotEndTime) {
            const eventKey = `${dateString}-${slot.slotName}`;

            if (!calendarEvents[eventKey]) {
              calendarEvents[eventKey] = {
                title: `${slot.slotName} Hearing`,
                start: `${dateString}T${slot.slotStartTime}`,
                end: `${dateString}T${slot.slotEndTime}`,
                extendedProps: {
                  hearings: [hearing],
                  count: 1,
                  date: new Date(dateString),
                  slot: slot.slotName,
                },
              };
            } else {
              calendarEvents[eventKey].extendedProps.count += 1;
              calendarEvents[eventKey].extendedProps.hearings.push(hearing);
            }
          }
        });
      }
    });

    const eventsArray = Object.values(calendarEvents);
    return eventsArray;
  }, [hearingDetails, events.slots]);

  const getEachHearingType = (hearingList) => {
    return [...new Set(hearingList.map((hearing) => hearing.hearingType))];
  };

  const hearingCount = (hearingList) => {
    const hearingTypeList = getEachHearingType(hearingList);
    return hearingTypeList.map((type) => {
      return {
        type: type,
        frequency: hearingList.filter((hearing) => hearing.hearingType === type).length,
      };
    });
  };

  const handleEventClick = (arg) => {
    const fromDate = new Date(arg.event.extendedProps.date);
    const toDate = new Date(fromDate);
    toDate.setDate(fromDate.getDate() + 1);
    const searchParams = new URLSearchParams(search);
    searchParams.set("from-date", formatDate(fromDate));
    searchParams.set("to-date", formatDate(toDate));
    searchParams.set("slot", arg.event.extendedProps.slot);
    searchParams.set("view", getCurrentViewType());
    history.replace({ search: searchParams.toString() });
  };

  const closeModal = () => {
    const searchParams = new URLSearchParams(search);
    searchParams.delete("from-date");
    searchParams.delete("to-date");
    searchParams.delete("slot");
    searchParams.delete("view");
    history.replace({ search: searchParams.toString() });
  };

  const Close = () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g>
        <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
      </g>
    </svg>
  );

  return (
    <div style={{ display: "flex" }}>
      <div style={{ width: "70%" }}>
        <div>
          <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
            initialView={initialView}
            headerToolbar={{
              start: "prev",
              center: "title",
              end: "next, dayGridMonth,timeGridWeek,timeGridDay",
            }}
            height={"85vh"}
            events={Calendar_events}
            eventContent={(arg) => {
              return (
                <div>
                  <div>{`${arg.event.extendedProps.slot} : ${arg.event.extendedProps.count}-hearings`}</div>
                  {hearingCount(arg.event.extendedProps.hearings).map((hearingFrequency) => (
                    <div>
                      {hearingFrequency.frequency} - {t(hearingFrequency.type)}
                    </div>
                  ))}
                </div>
              );
            }}
            eventClick={handleEventClick}
            datesSet={(dateInfo) => {
              setDateRange({ start: dateInfo.start, end: dateInfo.end });
            }}
            ref={calendarRef}
          />
          {fromDate && toDate && slot && individualId && (
            <PreHearingModal
              courtData={courtData?.["common-masters"]?.Court_Rooms}
              onCancel={closeModal}
              hearingData={{ fromDate, toDate, slot }}
              individualId={individualId}
              userType={userType}
            />
          )}
        </div>
      </div>
      <div className="right-side">
        <TasksComponent
          taskType={taskType}
          setTaskType={setTaskType}
          caseType={caseType}
          setCaseType={setCaseType}
          isLitigant={Boolean(userInfoType === "citizen")}
          uuid={userInfo?.uuid}
          userInfoType={userInfoType}
        />
      </div>
    </div>
  );
};

export default MonthlyCalendar;
