import React from "react";
import PropTypes from "prop-types";
import { ErrorIcon } from "@egovernments/digit-ui-react-components";
import CustomErrorTooltip from "./CustomErrorTooltip";
import ReactTooltip from "react-tooltip";

const CustomNote = ({ t, infoText }) => {
  return (
    <div className="custom-note-main-div">
      <div className="custom-note-heading-div">
        <CustomErrorTooltip message={"tooltip message"} visible />
        <h2>{t("ES_COMMON_NOTE")}</h2>
      </div>
      <div className="custom-note-info-div">{<p>{infoText}</p>}</div>
    </div>
  );
};

CustomNote.propTypes = {};

CustomNote.defaultProps = {};

export default CustomNote;
