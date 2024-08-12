import { Card } from "@egovernments/digit-ui-react-components";
import React from "react";

const CustomDetailsCard = ({ header, subtext, serialNumber, subnote, style }) => {
  return (
    <div
      style={{
        display: "flex",
        width: "100%",
        flexDirection: "row",
        alignItems: "flex-start",
        borderBottom: "1px #E8E8E8 solid",
        padding: '16px 0px 16px 0px',
        gap: "12px",
        ...style,
      }}
    >
      {serialNumber && (
        <p style={{
          fontFamily: 'Roboto',
          fontSize: '16px',
          fontWeight: 700,
          lineHeight: '18.75px',
          textAlign: 'left',
          color: '#77787B',
          margin: '0px',
        }}>{serialNumber}</p>
      )}
      <div style={{ width: "90%" }}>
        {header && <p style={{
          fontFamily: 'Roboto',
          fontSize: '16px',
          fontWeight: 700,
          lineHeight: '18.75px',
          textAlign: 'left',
          color: '#0A0A0A',
          margin: '0px 0px 12px',
        }}>{header}</p>}
        {subtext && <p style={{
          fontFamily: 'Roboto',
          fontSize: '16px',
          fontWeight: 400,
          lineHeight: '18.75px',
          textAlign: 'left',
          color: '#0A0A0A',
          margin: '0px 0px 4px',
        }}>{subtext}</p>}
        {subnote && <p style={{
          fontFamily: 'Roboto',
          fontSize: '14px',
          fontWeight: 400,
          lineHeight: '16.41px',
          textAlign: 'left',
          color: '#77787B',
          margin: '0px',
        }}>{subnote}</p>}
      </div>
    </div>
  );
};

export default CustomDetailsCard;
