import React, { useState } from "react";
import DependentCheckBoxComponent from "../../../components/DependentCheckBoxComponent";
import { Button, CardHeader, CardLabel, SubmitBar } from "@egovernments/digit-ui-react-components";

function SelectParticipant({ config, setShowModal, modalInfo, setModalInfo }) {
  const [selectedValues, setSelectedValues] = useState({});

  const handleInputChange = (values) => {
    setSelectedValues(values);
  };
  const onSubmitSchedule = (props) => {
    console.log(props);
    setModalInfo({ ...modalInfo, page: 2 });
  };
  console.log(selectedValues);
  return (
    <div>
      <CardLabel>{config?.header}</CardLabel>
      <DependentCheckBoxComponent options={config} onInputChange={handleInputChange} />
      <div className="action-button-application">
        <Button
          variation="secondary"
          onButtonClick={() => setModalInfo({ ...modalInfo, page: 0 })}
          className="primary-label-btn"
          label={"Back"}
        ></Button>
        <SubmitBar
          variation="primary"
          onSubmit={(props) => {
            onSubmitSchedule(props);
          }}
          className="primary-label-btn"
          label={"Schedule"}
        ></SubmitBar>
      </div>
    </div>
  );
}

export default SelectParticipant;
