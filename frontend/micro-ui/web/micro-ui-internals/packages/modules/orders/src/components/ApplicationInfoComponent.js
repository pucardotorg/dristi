import React from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom/cjs/react-router-dom.min";

const ApplicationInfoComponent = ({ infos, links }) => {
  const { t } = useTranslation();
  return (
    <React.Fragment>
      <div className="application-info">
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
          {links &&
            links?.map((link, index) => (
              <Link key={index} className="review-summon-order" to={{ pathname: link?.link }}>
                {link?.text}
              </Link>
            ))}
        </div>
      </div>
    </React.Fragment>
  );
};

export default ApplicationInfoComponent;
