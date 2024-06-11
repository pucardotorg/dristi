import React, { useContext, useState } from "react";
import CustomChooseDate from "../../../components/CustomChooseDate";
import { Button, CardLabel, CardText, DateRange, EventCalendar, SubmitBar, TextInput } from "@egovernments/digit-ui-react-components";
import { Link } from "react-router-dom/cjs/react-router-dom.min";
import CustomCaseInfoDiv from "../../../components/CustomCaseInfoDiv";

function ScheduleAdmission({ config, t, setShowModal, setModalInfo, modalInfo }) {
  const data = ["17th March 2024", "21st March 2024", "24th March 2024"];
  const [selectedChip, setSelectedChip] = React.useState(null);
  //   const [purposeValue, setPurposeValue] = useState();

  const setPurposeValue = (value, input) => {
    console.log(value, input);
  };
  const handleChipClick = (chipLabel) => {
    setSelectedChip(chipLabel);
  };
  const [dateSelected, setDateSelected] = useState(false);

  const selectCustomDate = () => {
    console.log("CustomDate");
    setModalInfo({ ...modalInfo, showDate: true });
  };
  const caseInfo = [
    {
      key: "Case Number",
      value: "FSM-2019-04-23-898898",
    },
    {
      key: "Court Name",
      value: "Kerala City Criminal Court",
    },
    {
      key: "Case Type",
      value: "NIA S138",
    },
  ];
  const onSubmitSchedule = (props) => {
    console.log(props);
    setModalInfo({ ...modalInfo, page: 1 });
  };
  return (
    <div>
      {dateSelected && <CustomCaseInfoDiv data={caseInfo} />}

      <CardText className="card-label-smaller">{t(config.label)}</CardText>
      <TextInput
        className="field desktop-w-full"
        name={`${config.name}`}
        onChange={(e) => {
          setPurposeValue(e.target.value, config.name);
        }}
      />
      <CardText>Select a date</CardText>
      <CustomChooseDate
        data={data}
        selectedChip={selectedChip}
        onChipClick={handleChipClick}
        dateSelected={dateSelected}
        setDateSelected={setDateSelected}
      />

      <div className="action-button-application">
        <Button variation="secondary" onButtonClick={() => setShowModal(false)} className="primary-label-btn" label={"Back"}></Button>
        <SubmitBar
          variation="primary"
          onSubmit={(props) => {
            onSubmitSchedule(props);
          }}
          className="primary-label-btn"
          label={dateSelected ? "Continue" : "Select Participants"}
        ></SubmitBar>
      </div>
      <h3>
        {t("Dates don’t work?")}{" "}
        <span style={{ color: "#007E7E" }} onClick={() => selectCustomDate()}>
          {String(t("Select Custom Date"))}
        </span>
      </h3>
    </div>
  );
}

export default ScheduleAdmission;
