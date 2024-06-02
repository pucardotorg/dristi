import React from "react";
import CustomReviewCardRow from "./CustomReviewCardRow";

function CustomReviewCard({ titleIndex, config, data, isScrutiny, t, dataIndex, handleOpenPopup, input, dataErrors, configKey }) {
  return (
    <div className="item-body">
      {config.map((item, i) => (
        <CustomReviewCardRow
          type={item.type}
          label={item?.label}
          value={item.value}
          key={i}
          data={data}
          handleOpenPopup={handleOpenPopup}
          isScrutiny={isScrutiny}
          titleIndex={titleIndex}
          dataIndex={dataIndex}
          name={input.name}
          configKey={configKey}
        />
      ))}
    </div>
  );
}

export default CustomReviewCard;
