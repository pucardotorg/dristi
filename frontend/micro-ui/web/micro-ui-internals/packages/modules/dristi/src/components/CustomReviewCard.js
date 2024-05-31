import React, { useState } from "react";
import CustomPopUp from "./CustomPopUp";
import CustomReviewCardRow from "./CustomReviewCardRow";
import { Button, TextArea } from "@egovernments/digit-ui-react-components";

function CustomReviewCard({
  titleIndex,
  config,
  data,
  isScrutiny,
  isPopupOpen,
  popupPosition,
  t,
  index,
  handleAddError,
  handleClosePopup,
  handleOpenPopup,
  formData,
  input,
}) {
  const [error, setError] = useState("");
  let currentValue = (formData && formData[config.key] && formData[config.key][input.name]) || "";
  return (
    <div className="item-body">
      {config.map((item, index) => (
        <CustomReviewCardRow
          type={item.type}
          label={item?.label}
          value={item.value}
          key={index}
          data={data}
          handleOpenPopup={handleOpenPopup}
          isScrutiny={isScrutiny}
          titleIndex={titleIndex}
          index={index}
        />
      ))}
      {isPopupOpen && (
        <CustomPopUp position={popupPosition}>
          <div>
            <div>{t("CS_ERROR_DESCRIPTION")}</div>
            <TextArea
              value={error}
              onChange={(e) => {
                const { value } = e.target;
                setError(value);
              }}
            ></TextArea>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "20px" }}>
              <Button label={t("CS_COMMON_DELETE")} onButtonClick={handleClosePopup} />
              <Button
                label={t("CS_COMMON_UPDATE")}
                onButtonClick={() => {
                  handleAddError(input, index, error);
                }}
              />
            </div>
          </div>
        </CustomPopUp>
      )}
    </div>
  );
}

export default CustomReviewCard;
