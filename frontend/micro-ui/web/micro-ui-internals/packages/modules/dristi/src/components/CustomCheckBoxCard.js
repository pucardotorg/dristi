import { CardHeader, CardLabelError, CardText, CheckBox, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";

const CustomCheckBoxCard = ({ t, config, onSelect, formData = {}, errors, label }) => {
  const inputs = useMemo(() => config?.populators?.inputs, [config?.populators?.inputs]);
  const [value, setValue] = useState([]);

  function setFormValue(value, name, input) {
    onSelect(config.key, { ...formData[config.key], [name]: value });
  }

  return (
    <div className="custom-checkbox-card">
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        console.log("currentValue :>> ", currentValue);
        return (
          <React.Fragment key={index}>
            <LabelFieldPair style={{ width: "100%", display: "flex", flexDirection: "column", alignItems: "center" }}>
              <CardHeader style={{ fontSize: "30px" }} className="card-label-smaller">
                {t(input.label)}
              </CardHeader>

              <CardText style={{ fontWeight: 200 }} className="card-label-smaller">
                {t(input.subLabel)}
              </CardText>

              <div className="field">
                {input?.options?.map((option, index) => (
                  <CheckBox
                    onChange={(e) => {
                      console.log("e.target.checked :>> ", e.target.checked);
                      let tempData = value;
                      const isFound = value?.some((val) => val?.code === option?.code);
                      if (isFound) tempData = value?.filter((val) => val?.code !== option?.code);
                      else tempData.push(option);
                      setFormValue(tempData, input?.name);
                      setValue(tempData);
                    }}
                    key={index}
                    value={value?.find((val) => val?.code === option?.code)}
                    checked={value?.find((val) => val?.code === option?.code)}
                    label={t(option?.name)}
                  />
                ))}

                {currentValue && currentValue.length > 0 && input.validation && (
                  <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "16px", marginBottom: "12px" }}>
                    <span style={{ color: "#FF0000" }}> {t(input.validation?.errMsg || "CORE_COMMON_INVALID")}</span>
                  </CardLabelError>
                )}
              </div>
            </LabelFieldPair>
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default CustomCheckBoxCard;
