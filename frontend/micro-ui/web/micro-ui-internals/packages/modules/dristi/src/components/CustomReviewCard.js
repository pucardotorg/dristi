import React from "react";
import CustomReviewCardRow from "./CustomReviewCardRow";

function CustomReviewCard({
  titleIndex,
  config,
  data,
  isScrutiny,
  t,
  dataIndex,
  handleOpenPopup,
  input,
  dataErrors,
  configKey,
  titleHeading,
  setIsImageModal,
}) {
  return (
    <div className="item-body">
      {config.map((item, i) => {
        const dataError = Array.isArray(item.value) ? dataErrors?.[item.type]?.FSOError : dataErrors?.[item.value]?.FSOError;
        return (
          <CustomReviewCardRow
            config={item}
            key={i}
            data={data}
            handleOpenPopup={handleOpenPopup}
            isScrutiny={isScrutiny}
            titleIndex={titleIndex}
            dataIndex={dataIndex}
            name={input.name}
            configKey={configKey}
            dataError={dataError}
            t={t}
            titleHeading={titleHeading}
            setIsImageModal={setIsImageModal}
          />
        );
      })}
    </div>
  );
}

export default CustomReviewCard;
