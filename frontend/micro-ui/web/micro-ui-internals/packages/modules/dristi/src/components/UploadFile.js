import React, { useEffect, useRef, useState, Fragment, useCallback } from "react";
import { Close, RemoveableTag, ButtonSelector } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { UploadIcon } from "../icons/svgIndex";

const getRandomId = () => {
  return Math.floor((Math.random() || 1) * 139);
};

const ButtonBody = ({ t }) => {
  return (
    <div>
      <h1 style={{ color: "#007E7E", fontSize: "16px", fontWeight: "600" }}>{t("CS_COMMON_CHOOSE_FILE")}</h1>
    </div>
  );
};

const getCitizenStyles = (value) => {
  let citizenStyles = {};
  if (value === "OBPS") {
    citizenStyles = {
      containerStyles: {
        display: "flex",
        flexDirection: "row",
        justifyContent: "flex-start",
        alignItems: "center",
        flexWrap: "wrap",
        margin: "0px",
        padding: "0px",
      },
      tagContainerStyles: {
        margin: "0px",
        padding: "0px",
        maxWidth: "90%",
      },
      tagStyles: {
        padding: "8px",
        width: "100%",
        margin: "5px",
      },
      textStyles: {
        wordBreak: "break-word",
        height: "auto",
        lineHeight: "16px",
        overflow: "hidden",
        maxHeight: "28px",
        textOverflow: "ellipsis",
        paddingBottom: "10px",
        whiteSpace: "pre",
      },
      inputStyles: {
        width: "80%",
        height: "2rem !important",
        opacity: "0.2",
        marginLeft: "10px",
      },
      buttonStyles: {
        height: "auto",
        minHeight: "40px",
        width: "40%",
        maxHeight: "40px",
        marginTop: "-32px",
        padding: "1px 0px 0px 5px",
        display: "block",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "red",
      },
      closeIconStyles: {
        width: "20px",
        marginTop: "-5px",
      },
      uploadFile: {
        minHeight: "40px",
        maxHeight: "42px",
      },
    };
  } else {
    citizenStyles = {
      textStyles: {},
      tagStyles: {},
      inputStyles: {},
      buttonStyles: {},
      tagContainerStyles: {},
    };
  }
  return citizenStyles;
};

const UploadFile = (props) => {
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const inpRef = useRef();
  const [hasFile, setHasFile] = useState(false);
  const [prevSate, setprevSate] = useState(null);
  const user_type = Digit.SessionStorage.get("userType");
  let extraStyles = {};
  const handleChange = () => {
    if (inpRef.current.files[0]) {
      setHasFile(true);
      setprevSate(inpRef.current.files[0]);
    } else setHasFile(false);
  };

  extraStyles = getCitizenStyles("OBPS");

  const handleDelete = () => {
    inpRef.current.value = "";
    props.onDelete();
  };
  const handleReupload = () => {
    inpRef.current.click();
  };

  const handleEmpty = useCallback(() => {
    if (inpRef.current.files.length <= 0 && prevSate !== null) {
      inpRef.current.value = "";
      props.onDelete();
    }
  }, [prevSate, props]);

  if (props.uploadMessage && inpRef.current.value) {
    handleDelete();
    setHasFile(false);
  }

  useEffect(() => handleEmpty(), [handleEmpty, inpRef?.current?.files]);

  useEffect(() => handleChange(), [props.message]);

  const showHint = props?.showHint || false;

  return (
    <Fragment>
      {showHint && <p className="cell-text">{t(props?.hintText)}</p>}
      <div
        className="upload-file-div-main"
        style={{ display: "flex", maxWidth: "540px", gap: "2%", alignItems: "center", justifyContent: "space-between" }}
      >
        <div className="upload-file-div-sub" style={{ minWidth: "73%" }}>
          <div
            className={`upload-file ${user_type === "employee" ? "" : "upload-file-max-width"} ${props.disabled ? " disabled" : ""}`}
            style={extraStyles?.uploadFile ? extraStyles?.uploadFile : {}}
          >
            <div style={extraStyles ? extraStyles?.containerStyles : null}>
              {props?.uploadedFiles?.map((file, index) => {
                const fileDetailsData = file[1];
                return (
                  <div className="tag-container" style={extraStyles ? extraStyles?.tagContainerStyles : null}>
                    <RemoveableTag
                      extraStyles={extraStyles}
                      key={index}
                      text={props?.displayName || file[0]}
                      onClick={(e) => props?.removeTargetedFile(fileDetailsData, e)}
                    />
                  </div>
                );
              })}
              {props?.uploadedFiles.length === 0 && <h2 className="file-upload-status">{props?.message}</h2>}
              {!hasFile || props.error ? (
                <h2 className="file-upload-status">{props.message}</h2>
              ) : (
                <div className="tag-container" style={extraStyles ? extraStyles?.tagContainerStyles : null}>
                  <div className="tag" style={extraStyles ? extraStyles?.tagStyles : null}>
                    <span className="text" style={extraStyles ? extraStyles?.textStyles : null}>
                      {typeof inpRef.current.files[0]?.name !== "undefined" && !props?.file ? inpRef.current.files[0]?.name : props.file?.name}
                    </span>
                    <span onClick={() => handleDelete()} style={extraStyles ? extraStyles?.closeIconStyles : null}>
                      <Close style={props.Multistyle} className="close" />
                    </span>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
        <div
          className="upload-file-upload-button-div"
          style={{ maxWidth: "25%", height: "40px", border: "solid 1px #007E7E", display: "flex", alignItems: "center", position: "relative" }}
        >
          <input
            type="file"
            accept={props.accept}
            onChange={(e) => props.onUpload(e)}
            onClick={(event) => {
              if (props?.disabled) {
                event.preventDefault();
                return;
              }
              const { target = {} } = event || {};
              target.value = "";
            }}
            ref={inpRef}
            style={{ opacity: 0, maxWidth: "100%", minHeight: "40px" }}
          />
          <span
            style={{ minWidth: "100%", cursor: "pointer", alignItems: "center", display: "flex", justifyContent: "center", position: "absolute" }}
            onClick={handleReupload}
          >
            <span
              style={{ color: "#007E7E", display: "flex", flexDirection: "row", justifyContent: "center", marginTop: "5px" }}
              className="upload-button-custimised"
            >
              <span style={{ marginRight: "4px" }}>
                <UploadIcon />
              </span>
              {t("CS_COMMON_CHOOSE_FILE")}
            </span>
          </span>
        </div>
      </div>
      {props.iserror && <p style={{ color: "red" }}>{props.iserror}</p>}
      {props?.showHintBelow && <p className="cell-text">{t(props?.hintText)}</p>}
    </Fragment>
  );
};

export default UploadFile;
