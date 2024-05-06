import { CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import { Button } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";

function VerificationComponent({ t, config, onSelect, formData = {}, errors }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "CS_PIN_LOCATION",
          type: "LocationSearch",
          name: [],
        },
      ],
    [config?.populators?.inputs]
  );
  return (
    <div>
      {inputs?.map((input, index) => {
        let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
        return (
          <React.Fragment key={index}>
            <LabelFieldPair style={{ width: "100%", display: "flex" }}>
              <CardLabel className="card-label-smaller">{t(input.label)}</CardLabel>
              <div className="field" style={{ width: "50%" }}>
                <Button variation={"secondary"} className={"secondary-button-selector"} label={t("VERIFY_AADHAR")} />
                <Button className={"tertiary-button-selector"} label={t("VERIFY_ID_PROOF")} />
              </div>
            </LabelFieldPair>
          </React.Fragment>
        );
      })}
    </div>
  );
}

export default VerificationComponent;
