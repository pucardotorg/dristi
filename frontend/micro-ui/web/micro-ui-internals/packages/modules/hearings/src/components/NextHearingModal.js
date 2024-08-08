import React, { useEffect, useMemo } from "react";
import { useState } from "react";
import { Button, Modal } from "@egovernments/digit-ui-react-components";
import { Card } from "@egovernments/digit-ui-react-components";
import useGetAvailableDates from "../hooks/hearings/useGetAvailableDates";
import { useHistory } from "react-router-dom";
import CustomCalendar from "../../../dristi/src/components/CustomCalendar";
import { useTranslation } from "react-i18next";
import { formatDateInMonth } from "@egovernments/digit-ui-module-dristi/src/Utils";
import { formatDate } from "../utils";

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <g>
      <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
    </g>
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div style={{ padding: "10px" }} onClick={props.onClick}>
      <Close />
    </div>
  );
};

const DateCard = ({ date, isSelected, onClick }) => (
  <div
    className="date-card"
    style={{
      border: `1px solid ${isSelected ? "#007BFF" : "#DDD"}`,
    }}
    onClick={onClick}
  >
    {date}
  </div>
);

const NextHearingModal = ({ hearingId, hearing, stepper, setStepper, transcript }) => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [isCustomDateSelected, setIsCustomDateSelected] = useState(false);
  const [isCalendarModalOpen, setIsCalendarModalOpen] = useState(false);
  const [caseDetails, setCaseDetails] = useState();
  const [selectedCustomDate, setSelectedCustomDate] = useState(new Date());
  const { t } = useTranslation();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
  const userInfo = Digit.UserService.getUser()?.info;
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);

  const history = useHistory();

  useEffect(() => {
    getCaseDetails();
  }, []);

  const getCaseDetails = async () => {
    try {
      const response = await window?.Digit?.DRISTIService.searchCaseService(
        {
          criteria: [
            {
              filingNumber: hearing?.filingNumber[0],
            },
          ],
          tenantId,
        },
        {}
      );
      setCaseDetails(response?.criteria[0]?.responseList[0]);
    } catch (error) {
      // console.log("error fetching case details", error);
    }
  };

  const { data: datesResponse, refetch: refetchGetAvailableDates } = useGetAvailableDates(true);
  const Dates = useMemo(() => datesResponse || [], [datesResponse]);

  const { data: MdmsCourtList, isLoading: loading } = Digit.Hooks.useCustomMDMS(
    Digit.ULBService.getStateId(),
    "common-masters",
    [{ name: "Court_Rooms" }],
    {
      cacheTime: 0,
    }
  );

  const courtDetails = useMemo(() => {
    if (!MdmsCourtList) return null;
    return MdmsCourtList?.["common-masters"]?.Court_Rooms.find((court) => court.code === caseDetails?.courtId);
  }, [MdmsCourtList, caseDetails?.courtId]);

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}${path}`);
  };

  const closeSetDate = () => {
    handleNavigate(`/employee/hearings/inside-hearing?hearingId=${hearingId}`);
  };

  const onGenerateOrder = () => {
    const requestBody = {
      order: {
        createdDate: new Date().getTime(),
        tenantId: Digit.ULBService.getCurrentTenantId(),
        filingNumber: caseDetails?.filingNumber,
        cnrNumber: caseDetails?.cnrNumber,
        statuteSection: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        orderType: "SCHEDULING_NEXT_HEARING",
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
              type: "SCHEDULING_NEXT_HEARING",
              isactive: true,
              code: "SCHEDULING_NEXT_HEARING",
              name: "ORDER_TYPE_SCHEDULING_NEXT_HEARING",
            },
            hearingDate: formatDate(new Date(selectedDate)),
            lastHearingTranscript: { text: transcript },
          },
        },
      },
    };
    ordersService
      .createOrder(requestBody, { tenantId: Digit.ULBService.getCurrentTenantId() })
      .then((res) => {
        history.push(
          `/${window.contextPath}/${userType}/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}&orderNumber=${res.order.orderNumber}`
        );
      })
      .catch((err) => {
        console.error(err);
      });
  };

  const onBack = () => {
    setStepper(stepper - 1);
  };

  return (
    <div>
      <Modal
        headerBarMain={<Heading label={"Set Next Hearing Date"} />}
        headerBarEnd={<CloseBtn onClick={() => handleNavigate(`/employee/hearings/inside-hearing?hearingId=${hearingId}`)} />}
        onClose={closeSetDate}
        actionSaveLabel="Generate Order"
        actionSaveOnSubmit={onGenerateOrder}
        actionCancelLabel="Back"
        actionCancelOnSubmit={onBack}
        style={{ marginTop: "5px" }}
        popupStyles={{ width: "50%", height: "auto" }}
        isDisabled={selectedDate === null}
      >
        <Card style={{ marginTop: "20px" }}>
          <div className="case-card">
            <div className="case-details">
              Case Number:
              <div> {caseDetails?.caseNumber} </div>
            </div>
            <div className="case-details">
              Court Name:
              <div> {courtDetails?.name} </div>
            </div>
            <div className="case-details">
              Case Type:
              <div> {caseDetails?.Case_Type || "NIA S 138"} </div>
            </div>
          </div>
        </Card>
        <div style={{ margin: "10px" }}>Select a Date</div>
        <Card>
          <div className="case-card">
            {Dates.map((date, index) => (
              <DateCard key={index} date={date} isSelected={selectedDate === date} onClick={() => setSelectedDate(date)} />
            ))}
          </div>
        </Card>
        {isCustomDateSelected ? (
          <div className="footClass">
            {formatDateInMonth(selectedCustomDate)}{" "}
            <Button
              label={"Select Another Date"}
              variation={"teritiary"}
              onButtonClick={() => {
                setIsCalendarModalOpen(true);
              }}
              style={{ border: "none" }}
            />
          </div>
        ) : (
          <div className="footClass">
            Dates Doesn't work?{" "}
            <Button
              label={"Select Custom Date"}
              variation={"teritiary"}
              onButtonClick={() => {
                setIsCalendarModalOpen(true);
              }}
              style={{ border: "none" }}
            />
          </div>
        )}
      </Modal>
      {isCalendarModalOpen && (
        <Modal
          headerBarMain={<Heading label={t("CS_SELECT_CUSTOM_DATE")} />}
          headerBarEnd={
            <CloseBtn
              onClick={() => {
                setIsCalendarModalOpen(false);
              }}
            />
          }
          // actionSaveLabel={t("CS_COMMON_CONFIRM")}
          hideSubmit={true}
          popmoduleClassName={"custom-date-selector-modal"}
        >
          <CustomCalendar
            config={{
              headModal: "CS_SELECT_CUSTOM_DATE",
              label: "CS_HEARINGS_SCHEDULED",
              showBottomBar: true,
              buttonText: "CS_COMMON_CONFIRM",
            }}
            t={t}
            onCalendarConfirm={(date) => {
              // setScheduleHearingParam({ ...scheduleHearingParams, date: formatDateInMonth(date) });
              // setSelectedCustomDate(date);
              setSelectedDate(selectedCustomDate);
              setIsCustomDateSelected(true);
              setIsCalendarModalOpen(false);
            }}
            handleSelect={(date) => {
              // setScheduleHearingParam({ ...scheduleHearingParams, date: formatDateInMonth(date) });
              setSelectedCustomDate(date);
            }}
            selectedCustomDate={selectedCustomDate}
            tenantId={tenantId}
          />
        </Modal>
      )}
    </div>
  );
};

export default NextHearingModal;
