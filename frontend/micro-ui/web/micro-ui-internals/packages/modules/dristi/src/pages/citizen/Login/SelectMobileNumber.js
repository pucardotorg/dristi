import { CheckBox, FormStep } from "@egovernments/digit-ui-react-components";
import React from "react";

const SelectMobileNumber = ({
  t,
  onSelect,
  showRegisterLink,
  mobileNumber,
  onMobileChange,
  config,
  canSubmit,
  handleRememberMeChange,
  isRememberMe,
}) => {
  return (
    <FormStep
      isDisabled={!(mobileNumber.length === 10 && mobileNumber.match(Digit.Utils.getPattern("MobileNo")) && canSubmit)}
      onSelect={onSelect}
      config={config}
      t={t}
      componentInFront="+91"
      onChange={onMobileChange}
      value={mobileNumber}
      cardStyle={{ minWidth: "100%" }}
    >
      <CheckBox
        onChange={handleRememberMeChange}
        checked={isRememberMe}
        label={"Remember me"}
        name={"Checkbox"}
        styles={{ alignItems: "center", textAlign: "center" }}
        labeStyle={{ paddingTop: "10px" }}
      />
    </FormStep>
  );
};

export default SelectMobileNumber;
