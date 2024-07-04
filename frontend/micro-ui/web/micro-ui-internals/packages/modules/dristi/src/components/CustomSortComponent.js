import React, { useMemo } from "react";

export const ArrowDownIcon = () => {
  return (
    <svg width="12" height="8" viewBox="0 0 12 8" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M10.59 0.589844L6 5.16984L1.41 0.589844L0 1.99984L6 7.99984L12 1.99984L10.59 0.589844Z" fill="#3D3C3C" />
    </svg>
  );
};

function CustomSortComponent({ t, config, onSelect, formData = {}, errors }) {
  const Icon = useMemo(() => {
    switch (config?.icon) {
      case "ArrowDownIcon":
        return ArrowDownIcon;

      default:
        return ArrowDownIcon;
    }
  }, [config?.icon]);
  return (
    <div
      className="select-signature-main"
      style={{ justifyContent: "center", alignItems: "center", flexDirection: "row", height: "100%", marginTop: 13, maxWidth: 250 }}
    >
      <React.Fragment>
        <button
          className="custom-sort-button"
          style={{
            height: 40,
            width: "100%",
            display: "flex",
            alignItems: "center",
            fontSize: 16,
            justifyContent: "center",
            backgroundColor: "#fff",
            border: "1px solid black",
          }}
          onClick={() => onSelect(config.key, { sortBy: config.sortBy, order: formData?.[config.key]?.order === "desc" ? "asc" : "desc" })}
        >
          <span className="custom-sort-name">{config.name} </span>
          {config?.showAdditionalText ? (
            <span className="custom-sort-text">
              &nbsp;
              {formData?.[config.key]?.order === "desc" ? config?.descText : config?.ascText}
            </span>
          ) : (
            ""
          )}
          {config?.showIcon && Icon && (
            <div
              className="custom-sort-icon"
              style={{
                marginLeft: 16,
                transform: formData?.[config.key]?.order === "desc" ? "rotate(0deg)" : "rotate(180deg)",
                transition: "transform 0.3s",
              }}
            >
              <React.Fragment>
                <Icon />
              </React.Fragment>
            </div>
          )}
        </button>
      </React.Fragment>
    </div>
  );
}

export default CustomSortComponent;
