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
  handleClickImage,
}) {
  if (data?.isAdvocateRepresenting?.code === "NO") {
    return null;
  }
  return (
    <div className="item-body">
      {config.map((item, i) => {
        const dataError = Array.isArray(item.value) ? dataErrors : dataErrors?.[item.value]?.FSOError;
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
            handleClickImage={handleClickImage}
          />
        );
      })}
    </div>
  );
}

export default CustomReviewCard;
