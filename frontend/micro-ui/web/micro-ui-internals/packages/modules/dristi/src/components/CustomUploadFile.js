import React, { useEffect, useRef, useState, Fragment } from "react";
import { ButtonSelector } from "@egovernments/digit-ui-react-components";
import { Close, UploadIcon } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { RemoveableTag } from "@egovernments/digit-ui-react-components";

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
  if (value == "propertyCreate") {
    citizenStyles = {
      textStyles: {
        whiteSpace: "nowrap",
        width: "100%",
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "80%",
      },
      tagStyles: {
        width: "90%",
        flexWrap: "nowrap",
      },
      inputStyles: {
        width: "44%",
        minHeight: "2rem",
        maxHeight: "3rem",
        top: "20%",
      },
      buttonStyles: {
        height: "auto",
        minHeight: "2rem",
        width: "40%",
        maxHeight: "3rem",
      },
      tagContainerStyles: {
        width: "60%",
        display: "flex",
        marginTop: "0px",
      },
      closeIconStyles: {
        width: "20px",
        marginTop: "-5px",
      },
      containerStyles: {
        padding: "10px",
        marginTop: "0px",
      },
    };
  } else if (value == "IP") {
    citizenStyles = {
      textStyles: {
        whiteSpace: "nowrap",
        maxWidth: "250px",
        overflow: "hidden",
        textOverflow: "ellipsis",
      },
      tagStyles: {
        marginLeft: "-30px",
      },
      inputStyles: {},
      closeIconStyles: {
        position: "absolute",
        marginTop: "-12px",
      },
      buttonStyles: {},
      tagContainerStyles: {},
    };
  } else if (value == "OBPS") {
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
        margin: 0,
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
        width: "30%",
        minHeight: "40px",
        maxHeight: "42px",
        top: "5px",
        left: "420px",
      },
      buttonStyles: {
        height: "auto",
        minHeight: "40px",
        width: "100%",
        maxHeight: "40px",
        margin: "5px",
        padding: "1px 0px 0px 5px",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
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

const CustomUploadFile = (props) => {
  const { t } = useTranslation();
  const inpRef = useRef();
  const [hasFile, setHasFile] = useState(false);
  const [prevSate, setprevSate] = useState(null);
  const user_type = window?.Digit.SessionStorage.get("userType");
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

  const handleEmpty = () => {
    if (inpRef.current.files.length <= 0 && prevSate !== null) {
      inpRef.current.value = "";
      props.onDelete();
    }
  };

  if (props.uploadMessage && inpRef.current.value) {
    handleDelete();
    setHasFile(false);
  }

  useEffect(() => handleEmpty(), [inpRef?.current?.files]);

  useEffect(() => handleChange(), [props.message]);

  const showHint = props?.showHint || false;

  return (
    <Fragment>
      {showHint && <p className="cell-text">{t(props?.hintText)}</p>}
      <div style={{ display: "flex", maxWidth: "540px" }}>
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
                    text={file[0]}
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
          <input
            className={props.disabled ? "disabled" : "" + "input-mirror-selector-button"}
            style={extraStyles ? { ...extraStyles?.inputStyles, ...props?.inputStyles } : { ...props?.inputStyles }}
            ref={inpRef}
            type="file"
            id={props.id || `document-${getRandomId()}`}
            name="file"
            multiple={props.multiple}
            accept={props.accept}
            disabled={props.disabled}
            onChange={(e) => props.onUpload(e)}
            onClick={(event) => {
              if (props?.disabled) {
                event.preventDefault();
                return;
              }
              const { target = {} } = event || {};
              target.value = "";
            }}
          />
        </div>
        <div style={{ width: "18%", maxWidth: "18%", margin: "0px", maxHeight: "42px", marginLeft: "10px", border: "solid 2px #007E7E" }}>
          <ButtonSelector
            theme="border"
            ButtonBody={<ButtonBody t={t} />} // change this prop later.
            style={{ ...(extraStyles ? extraStyles?.buttonStyles : {}), ...(props.disabled ? { display: "none" } : {}), display: "contents" }}
            textStyles={{ textAlign: "center", margin: "0px", marginTop: "7px" }}
            type={props.buttonType}
          />{" "}
        </div>
      </div>
      {props.iserror && <p style={{ color: "red" }}>{props.iserror}</p>}
      {props?.showHintBelow && <p className="cell-text">{t(props?.hintText)}</p>}
    </Fragment>
  );
};

export default CustomUploadFile;
