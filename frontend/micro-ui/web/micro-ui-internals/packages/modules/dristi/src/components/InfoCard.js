import React, { useMemo } from "react";
import { InfoBannerIcon } from "@egovernments/digit-ui-react-components";
import { WarningIcon } from "@egovernments/digit-ui-react-components";
import { ErrorIcon } from "@egovernments/digit-ui-react-components";

const RoundedCheck = ({ className, height = "24", width = "24", style = {}, fill = "#FFFFFF", onClick = null }) => {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" height={height} width={width} viewBox="0 0 24 24" fill={fill} className={className}>
      <path d="M0 0h24v24H0V0z" fill="none" />
      <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM9.29 16.29L5.7 12.7c-.39-.39-.39-1.02 0-1.41.39-.39 1.02-.39 1.41 0L10 14.17l6.88-6.88c.39-.39 1.02-.39 1.41 0 .39.39.39 1.02 0 1.41l-7.59 7.59c-.38.39-1.02.39-1.41 0z" />
    </svg>
  );
};
const InfoCard = (props) => {
  //if type is "component", then props will come from props?.props.
  //if the component has props passed directly, then props will be used.
  props = props?.props ? props?.props : props;
  const showInfo = props?.showInfo ? props?.showInfo : true;

  const Icon = useMemo(() => {
    switch (props?.variant) {
      case "info":
        return InfoBannerIcon;
      case "success":
        return RoundedCheck;
      case "error":
        return ErrorIcon;
      case "warning":
        return WarningIcon;

      default:
        return <React.Fragment></React.Fragment>;
    }
  }, [props?.variant]);

  return (
    <div className={`info-banner-wrap ${props?.className ? props?.className : ""}`} style={props?.style}>
      {showInfo && (
        <div>
          <Icon fill={props?.fill} styles={props?.iconStyle} />
          <h2 style={props?.textStyle}>{props?.info}</h2>
        </div>
      )}
      <p style={props?.textStyle}>{props?.text}</p>
    </div>
  );
};

export default InfoCard;
