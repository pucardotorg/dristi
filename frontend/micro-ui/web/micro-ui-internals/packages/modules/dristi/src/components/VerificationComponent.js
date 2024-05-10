import { CardLabel, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import Button from "./Button";
import InfoCard from "./InfoCard";

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
            <LabelFieldPair style={{ width: "100%", display: "flex", alignItem: "center" }}>
              <CardLabel style={{ fontWeight: 700 }} className="card-label-smaller">
                {t(input.label)}
              </CardLabel>
            </LabelFieldPair>
            <div className="button-field" style={{ width: "50%" }}>
              <Button
                variation={"secondary"}
                className={"secondary-button-selector"}
                label={t("VERIFY_AADHAR")}
                labelClassName={"secondary-label-selector"}
              />
              <Button className={"tertiary-button-selector"} label={t("VERIFY_ID_PROOF")} labelClassName={"tertiary-label-selector"} />
            </div>
            <InfoCard />
          </React.Fragment>
        );
      })}
    </div>
  );
}

export default VerificationComponent;
