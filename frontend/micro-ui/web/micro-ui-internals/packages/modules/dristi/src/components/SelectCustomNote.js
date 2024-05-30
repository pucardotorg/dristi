import React, { useMemo } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";

function SelectCustomNote({ t, config }) {
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
          <CustomErrorTooltip message={t(input?.infoTooltipMessage)} showTooltip={Boolean(input?.infoTooltipMessage) || input?.showTooltip} />
          <h2>{t(input?.infoHeader)}</h2>
        </div>
        <div className="custom-note-info-div">{<p>{t(input?.infoText)}</p>}</div>
      </div>
    );
  });
}

export default SelectCustomNote;
