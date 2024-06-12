import React, { useMemo } from "react";
import SignatureCard from "./SignatureCard";

function SelectSignature({ t, config, onSelect, formData = {}, errors }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          key: "complaintDetails",
          label: "CS_COMPLAINT_DETAILS",
          icon: "LitigentIcon",
          config: [{ type: "title", value: "name" }],
          data: [{ name: "Sheetal Arora" }, { name: "Mehul Das" }],
        },
      ],
    [config?.populators?.inputs]
  );

  return (
    <div>
      {inputs.map((input, inputIndex) => (
        <div>
          <div style={{ paddingTop: "10px", paddingBottom: "10px", fontWeight: 700, fontSize: "24px", color: "#3D3C3C" }}>{config?.label}</div>
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
      ))}
    </div>
  );
}

export default SelectSignature;
