import React from "react";
import PropTypes from "prop-types";
import { ReactComponent as UploadFileIcon } from "../images/upload.svg";

const CustomUploadButton = ({ t, onChange, fileInputRef, handleReupload }) => {
  const handleInputChange = (data) => {
    onChange(data, true);
  };
  return (
    <div className="custom-upload-button-div-main">
      <input type="file" onChange={handleInputChange} ref={fileInputRef} style={{ display: "none" }} />
      <button onClick={handleReupload}>
        <UploadFileIcon></UploadFileIcon>
        {t("CS_COMMON_REUPLOAD_FILE")}
      </button>
    </div>
  );
};

CustomUploadButton.propTypes = {};

CustomUploadButton.defaultProps = {};

export default CustomUploadButton;
