import React from "react";
import { ReactComponent as UploadFileIcon } from "../images/upload.svg";
import { Button } from "@egovernments/digit-ui-react-components";

const CustomUploadButton = ({ t, handleChange, fileInputRef, handleReupload, input }) => {
  const acceptTypes = input?.fileTypes?.map((type) => `image/${type.toLowerCase()}`).join(",");

  const handleInputChange = (data) => {
    handleChange(data, input, true);
  };
  return (
    <div className="custom-upload-button-div-main">
      <input type="file" onChange={handleInputChange} ref={fileInputRef} style={{ display: "none" }} accept={acceptTypes} />
      <Button onButtonClick={handleReupload}>
        <UploadFileIcon></UploadFileIcon>
        {t("CS_COMMON_REUPLOAD_FILE")}
      </Button>
    </div>
  );
};

CustomUploadButton.propTypes = {};

CustomUploadButton.defaultProps = {};

export default CustomUploadButton;
