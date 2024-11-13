import React, { useMemo, useState } from "react";
import CustomErrorTooltip from "../../../dristi/src/components/CustomErrorTooltip";
import ListOfSuretyDocumentModal from "./ListOfSuretyDocumentModal";

function CustomInfo({ t, config, onClick = () => {} }) {
  const [open, setOpen] = useState(false);
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          infoHeader: "Note",
          infoText: "Basic Note",
          infoTooltipMessage: "Tooltip",
          type: "InfoComponent",
          modalHeading: "ModalHeading",
          modalData: [],
        },
      ],
    [config?.populators?.inputs]
  );
  const handleOpen = () => {
    setOpen(!open);
  };
  return inputs.map((input) => {
    return (
      <div className="custom-note-main-div">
        <div className="custom-note-heading-div">
          <CustomErrorTooltip message={t("")} showTooltip={Boolean(input?.infoTooltipMessage) || input?.showTooltip} />
          <h2>{t(input?.infoHeader)}</h2>
        </div>
        <div className="custom-note-info-div" style={{ display: "flex", alignItems: "center" }}>
          {<p>{t(input?.infoText)}</p>}
          {input?.linkText && (
            <div style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline", marginLeft: "5px" }} onClick={handleOpen}>
              {String(t(input?.linkText))}
            </div>
          )}
        </div>
        {open && <ListOfSuretyDocumentModal handleClose={handleOpen} heading={input?.modalHeading} data={input?.modalData} />}
      </div>
    );
  });
}

export default CustomInfo;
