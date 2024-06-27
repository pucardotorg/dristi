import React, { useMemo } from "react";
import SignatureCard from "./SignatureCard";

function SelectSignature({ t, config, onSelect, formData = {}, errors }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          key: "complainantDetails",
          label: "CS_COMPLAINT_DETAILS",
          icon: "LitigentIcon",
          config: [{ type: "title", value: "name" }],
          data: [{ name: "Sheetal Arora" }, { name: "Mehul Das" }],
        },
      ],
    [config?.populators?.inputs]
  );

  return (
    <div className="select-signature-main">
      {inputs.map((input, inputIndex) => (
        <React.Fragment>
          <div className="select-signature-header">
            <h1 className="signature-label">{config?.label}</h1>
          </div>
          <div className="select-signature-list">
            {input.data.map((item, itemIndex) => (
              <SignatureCard
                key={inputIndex + itemIndex}
                index={itemIndex}
                data={item}
                input={input}
                t={t}
                formData={formData}
                onSelect={onSelect}
                configKey={config.key}
              />
            ))}
          </div>
        </React.Fragment>
      ))}
    </div>
  );
}

export default SelectSignature;
