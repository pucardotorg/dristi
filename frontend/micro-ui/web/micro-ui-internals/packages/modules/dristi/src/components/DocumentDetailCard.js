import { Header } from "@egovernments/digit-ui-react-components";
import React from "react";

function DocumentDetailCard({ cardData, header }) {
  return (
    <div className="notice_and_circular_main gap-ten" style={{ flex: 1, margin: "5px 20px" }}>
      <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
        {header && <Header styles={{ fontSize: "24px" }}>{header}</Header>}
        {cardData.map((row) => (
          <div>
            <div style={{ display: "flex", justifyContent: "space-between" }}>
              <p className="documentDetails_title">{row?.title}</p> <p>{row?.content}</p>
            </div>
            {row?.image?.content}
            {row?.icon}
          </div>
        ))}
      </div>
    </div>
  );
}

export default DocumentDetailCard;
