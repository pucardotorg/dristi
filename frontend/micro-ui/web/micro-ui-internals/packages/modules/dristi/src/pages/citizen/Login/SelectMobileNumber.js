import React from "react";
import FormStep from "../../../components/FormStep";
import { Link } from "react-router-dom/cjs/react-router-dom.min";

const SelectMobileNumber = ({ t, onSelect, mobileNumber, onMobileChange, config, canSubmit, isRegister, isUserLoggedIn }) => {
  return (
    <div style={{ minWidth: "100%", display: "flex", justifyContent: "center" }}>
      <FormStep
        isDisabled={!(mobileNumber.length === 10 && mobileNumber.match(window?.Digit.Utils.getPattern("MobileNo")) && canSubmit)}
        onSelect={onSelect}
        config={config}
        t={t}
        componentInFront="+91"
        onChange={onMobileChange}
        value={mobileNumber}
        buttonStyle={{ color: "red" }}
        cardStyle={{ minWidth: "100%", alignItems: "center", padding: "40px" }}
      >
        {!isRegister && (
          <h3>
            {t("CS_REGISTER_ACCOUNT")}
            <span className="link">
              <Link
                to={
                  !isUserLoggedIn
                    ? `/digit-ui/citizen/dristi/home/registration/mobile-number`
                    : `/digit-ui/citizen/dristi/home/registration/user-name`
                }
              >
                {String(t("CS_REGISTER_LINK"))}
              </Link>
            </span>
          </h3>
        )}
      </FormStep>
    </div>
  );
};

export default SelectMobileNumber;
