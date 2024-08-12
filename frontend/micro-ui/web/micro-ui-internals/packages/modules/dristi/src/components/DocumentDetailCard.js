import { Card, Header } from "@egovernments/digit-ui-react-components";
import React from "react";

function DocumentDetailCard({ cardData, header }) {
  return (
    <Card style={{ flex: 1, margin: "5px 20px", border: "1px solid #D9D9D9", boxShadow: "none" }}>
      <div style={{ display: "flex", flexDirection: "column" }}>
        {header && <Header styles={{ fontSize: "24px" }}>{header}</Header>}
        {cardData.map((row) => (
          <div>
            {row?.title || row?.content ? (
              <div style={{ display: "flex", justifyContent: "space-between" }}>
                <p className="documentDetails_title">{row?.title}</p>
                <p>{row?.content}</p>
              </div>
            ) : null}
            <div>{row?.doc}</div>
            {row?.image?.content}
            {row?.icon}
          </div>
        ))}
      </div>
    </Card>
  );
}

export default DocumentDetailCard;
