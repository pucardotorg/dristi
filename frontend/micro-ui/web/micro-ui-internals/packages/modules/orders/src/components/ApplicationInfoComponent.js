import React from "react";
import { useTranslation } from "react-i18next";

const ApplicationInfoComponent = ({ infos, links }) => {
  const { t } = useTranslation();
  return (
    <React.Fragment>
      <div className="application-info" style={{ maxWidth: "616px", flexWrap: "wrap" }}>
        <div className={`info-row-wrapper ${links && links?.length > 0 ? "with-link" : ""}`}>
          {infos &&
            infos?.map((info, index) => (
              <div className={`info-row`} key={index}>
                <div className="info-key">
                  <h3>{t(info?.key)}</h3>
                </div>
                <div className="info-value">
                  <h3>{t(info?.value)}</h3>
                </div>
              </div>
            ))}
        </div>
        <div className="info-link-wrapper">
          {/* {links &&
            links?.map((link, index) => (
              <h3
                key={index}
                className="review-summon-order"
                onClick={() => {
                  if (link?.onClick) link.onClick();
                  else return;
                }}
              >
                {link?.text}
              </h3>
            ))} */}
        </div>
      </div>
    </React.Fragment>
  );
};

export default ApplicationInfoComponent;
