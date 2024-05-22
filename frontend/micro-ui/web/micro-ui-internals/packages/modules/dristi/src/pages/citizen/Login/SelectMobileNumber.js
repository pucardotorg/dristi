import React from "react";
import FormStep from "../../../components/FormStep";
import { Link } from "react-router-dom/cjs/react-router-dom.min";

const SelectMobileNumber = ({ t, onSelect, mobileNumber, onMobileChange, config, canSubmit, isRegister }) => {
  return (
    <FormStep
      isDisabled={!(mobileNumber.length === 10 && mobileNumber.match(window?.Digit.Utils.getPattern("MobileNo")) && canSubmit)}
      onSelect={onSelect}
      config={config}
      t={t}
      componentInFront="+91"
      onChange={onMobileChange}
      value={mobileNumber}
      cardStyle={{ minWidth: "100%", alignItems: "center" }}
    >
      {!isRegister && (
        <h3>
          {t("CS_REGISTER_ACCOUNT")}
          <span className="link">
            <Link to={`/digit-ui/citizen/dristi/home/registration/mobile-number`}>{String(t("CS_REGISTER_LINK"))}</Link>
          </span>
        </h3>
      )}
    </FormStep>
  );
};

export default SelectMobileNumber;
