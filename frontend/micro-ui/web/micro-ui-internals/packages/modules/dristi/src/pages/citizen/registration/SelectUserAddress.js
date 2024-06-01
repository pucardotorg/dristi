import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const SelectUserAddress = ({ config, onSelect, t, params, pathOnRefresh }) => {
  const history = useHistory();
  const [isDisabled, setIsDisabled] = useState(false);

  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    config.forEach((curr) => {
      if (isDisabled) return;
      if (!(curr.body[0].key in formData) || !formData[curr.body[0].key]) {
        return;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (isDisabled) return;
        if (Array.isArray(input.name)) return;
        if (
          formData[curr.body[0].key][input.name] &&
          formData[curr.body[0].key][input.name].length > 0 &&
          !["documentUpload", "radioButton"].includes(input.type) &&
          input.validation &&
          !formData[curr.body[0].key][input.name].match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
        ) {
          isDisabled = true;
        }
        if (Array.isArray(formData[curr.body[0].key][input.name]) && formData[curr.body[0].key][input.name].length === 0) {
          isDisabled = true;
        }
      });
    });
    if (isDisabled) {
      setIsDisabled(isDisabled);
    } else {
      setIsDisabled(false);
    }
  };
  if (!params?.name) {
    history.push(pathOnRefresh);
  }
  return (
    <div className="user-address">
      <FormComposerV2
        config={config}
        t={t}
        onSubmit={(props) => onSelect(props)}
        noBoxShadow
        inline
        label={"CS_COMMON_CONTINUE"}
        onSecondayActionClick={() => { }}
        cardStyle={{ padding: 40, flexDirection: "column" }}
        submitInForm
        onFormValueChange={onFormValueChange}
        isDisabled={isDisabled}
        defaultValues={params?.address || {}}
      ></FormComposerV2>
    </div>
  );
};

export default SelectUserAddress;
