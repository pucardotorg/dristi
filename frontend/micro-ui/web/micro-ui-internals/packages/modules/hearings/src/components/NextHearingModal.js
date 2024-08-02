import React, { useEffect, useMemo } from "react";
import { useState } from "react";
import { Button, Modal } from "@egovernments/digit-ui-react-components";
import { Card } from "@egovernments/digit-ui-react-components";
import useGetAvailableDates from "../hooks/hearings/useGetAvailableDates";
import { useHistory } from "react-router-dom";

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

const NextHearingModal = ({ hearingId, hearing, stepper, setStepper }) => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [caseDetails, setCaseDetails] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();

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
              filingNumber: hearing?.filingNumber?.[0],
            },
          ],
          tenantId,
        },
        {}
      );
      setCaseDetails(response?.criteria[0]?.responseList[0]);
    } catch (error) {
      console.log("error fetching case details", error);
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

  const onGenerateOrder = () => {};

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
        <div className="footClass">
          Dates Doesn't work? <Button label={"Select Custom Date"} variation={"teritiary"} onClick={() => {}} style={{ border: "none" }} />
        </div>
      </Modal>
    </div>
  );
};

export default NextHearingModal;
