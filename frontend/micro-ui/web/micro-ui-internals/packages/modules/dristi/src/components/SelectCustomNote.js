import React, { useMemo } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";

function SelectCustomNote({ t, config, onClick = () => {} }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          infoHeader: "Note",
          infoText: "Basic Note",
          infoTooltipMessage: "Tooltip",
          type: "InfoComponent",
        },
      ],
    [config?.populators?.inputs]
  );

  return inputs.map((input) => {
    return (
      <div className="custom-note-main-div">
        <div className="custom-note-heading-div">
          <CustomErrorTooltip message={t("")} showTooltip={Boolean(input?.infoTooltipMessage) || input?.showTooltip} />
          <h2>{t(input?.infoHeader)}</h2>
        </div>
        <div className="custom-note-info-div">{<p>{t(input?.infoText)}</p>}</div>
        {input?.linkText && (
          <span style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline" }} onClick={onClick}>
            {String(t(input?.linkText))}
          </span>
        )}
      </div>
    );
  });
}

export default SelectCustomNote;
