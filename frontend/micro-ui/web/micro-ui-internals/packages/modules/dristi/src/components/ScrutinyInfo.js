import React from "react";

function ScrutinyInfo({ config, t }) {
  return <div style={{ backgroundColor: "#ff000066", marginTop: -8, marginBottom: 8, padding: 4 }}>{t(config.populators.scrutinyMessage)}</div>;
}

export default ScrutinyInfo;
