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
  popupInfo,
  t,
  index,
  handleAddError,
  handleClosePopup,
  handleOpenPopup,
  formData,
  input,
  scrutinyError,
  setScrutinyError,
}) {
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
          name={input.name}
        />
      ))}
      {isPopupOpen && (
        <CustomPopUp position={popupInfo?.position}>
          <div>
            <div>{t("CS_ERROR_DESCRIPTION")}</div>
            <TextArea
              value={scrutinyError}
              onChange={(e) => {
                const { value } = e.target;
                setScrutinyError(value);
              }}
            ></TextArea>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "20px" }}>
              <Button label={t("CS_COMMON_DELETE")} onButtonClick={handleClosePopup} />
              <Button
                label={t("CS_COMMON_UPDATE")}
                onButtonClick={() => {
                  handleAddError();
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
