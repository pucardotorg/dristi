import React from "react";
import CustomReviewCardRow from "./CustomReviewCardRow";

function CustomReviewCard({
  titleIndex,
  config,
  data,
  isScrutiny,
  isJudge,
  t,
  dataIndex,
  handleOpenPopup,
  input,
  dataErrors,
  configKey,
  titleHeading,
  handleClickImage,
  prevDataErrors,
  isPrevScrutiny,
  setShowImageModal,
  isCaseReAssigned,
}) {
  if (data?.isAdvocateRepresenting?.code === "NO") {
    return null;
  }
  return (
    <div className="item-body">
      {config.map((item, i) => {
        const dataError = Array.isArray(item.value) ? dataErrors : dataErrors?.[item.value]?.FSOError;
        const prevDataError = Array.isArray(item.value) ? prevDataErrors : prevDataErrors?.[item.value]?.FSOError;
        return (
          <CustomReviewCardRow
            config={item}
            key={i}
            data={data}
            handleOpenPopup={handleOpenPopup}
            isScrutiny={isScrutiny}
            isJudge={isJudge}
            titleIndex={titleIndex}
            dataIndex={dataIndex}
            name={input.name}
            configKey={configKey}
            dataError={dataError}
            prevDataError={prevDataError}
            isPrevScrutiny={isPrevScrutiny}
            t={t}
            titleHeading={titleHeading}
            handleClickImage={handleClickImage}
            setShowImageModal={setShowImageModal}
            isCaseReAssigned={isCaseReAssigned}
            disableScrutiny={input?.disableScrutiny}
          />
        );
      })}
    </div>
  );
}

export default CustomReviewCard;
