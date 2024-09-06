import React, { useMemo } from "react";
import CustomErrorTooltip from "./CustomErrorTooltip";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

function SelectCustomNote({ t, config, onClick = () => {} }) {
  const history = useHistory();
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
        <div className="custom-note-info-div">
          {
            <p>
              {`${t(input?.infoText)} `}
              {!input?.key && <br />}
              {input?.linkText && (
                <span
                  style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline" }}
                  onClick={() => {
                    if (input.key === "witnessNote" || input.key === "evidenceNote") {
                      if (input?.customFunction) {
                        input?.customFunction();
                      }
                      history.push(
                        `/${window?.contextPath}/employee/dristi/home/view-case?caseId=${input?.caseId}&filingNumber=${input?.filingNumber}&tab=${input?.tab}`
                      );
                    } else {
                      onClick();
                    }
                  }}
                >
                  {String(t(input?.linkText))}
                </span>
              )}
            </p>
          }
        </div>
        {input?.children}
      </div>
    );
  });
}

export default SelectCustomNote;
