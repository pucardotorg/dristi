import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
const EnterAdhaar = ({ t, onSelect, config, params }) => {
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const validateFormData = (data) => {
    let isValid = true;
    config.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (input.disableMandatoryFieldFor) {
          if (input.disableMandatoryFieldFor.some((field) => !data[curr.body[0].key][field]) && data[curr.body[0].key][input.name]) {
            if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
              isValid = false;
            }
            return;
          } else {
            if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
              isValid = false;
            }
          }
          return;
        }
        if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
          isValid = false;
        }
        if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
          isValid = false;
        }
      });
    });
    return isValid;
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };
  if (!params?.indentity) {
    history.push("/digit-ui/citizen/dristi/home/login");
  }
  return (
    <React.Fragment>
      <FormComposerV2
        config={config}
        t={t}
        noBoxShadow
        inline
        label={"Next"}
        onSecondayActionClick={() => {}}
        headingStyle={{ textAlign: "center" }}
        cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
        sectionHeadStyle={{ marginBottom: "20px", fontSize: "40px" }}
        onSubmit={(data) => {
          if (!validateFormData(data)) {
            setShowErrorToast(!validateFormData(data));
          } else {
            onSelect(data?.AdhaarInput?.aadharNumber);
          }
          return;
        }}
        submitInForm
      ></FormComposerV2>

      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </React.Fragment>
  );
};

export default EnterAdhaar;
