import { InfoCard } from "@egovernments/digit-ui-components";
import { EditPencilIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { FlagIcon } from "../icons/svgIndex";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const CustomReviewCardRow = ({
  isScrutiny,
  data,
  handleOpenPopup,
  titleIndex,
  dataIndex,
  name,
  configKey,
  dataError,
  t,
  config,
  titleHeading,
  handleClickImage,
}) => {
  const { type = null, label = null, value = null, badgeType = null, docName = {} } = config;
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const extractValue = (data, key) => {
    if (!key.includes(".")) {
      return data[key];
    }
    const keyParts = key.split(".");
    let value = data;
    keyParts.forEach((part) => {
      if (value && value.hasOwnProperty(part)) {
        value = value[part];
      } else {
        value = undefined;
      }
    });
    return value;
  };
  const handleImageClick = (configKey, name, dataIndex, fieldName, data) => {
    if (isScrutiny && data) {
      handleClickImage(null, configKey, name, dataIndex, fieldName, data);
    }
    return null;
  };
  switch (type) {
    case "title":
      let title = "";
      if (Array.isArray(value)) {
        title = value.map((key) => extractValue(data, key)).join(" ");
      } else {
        title = extractValue(data, value);
      }
      return (
        <div className={`title-main ${isScrutiny && dataError && "error"}`}>
          <div className={`title ${isScrutiny && (dataError ? "column" : "")}`}>
            <div>{`${titleIndex}. ${titleHeading ? t("CS_CHEQUE_NO") + " " : ""}${title}`}</div>
            {badgeType && <div>{extractValue(data, badgeType)}</div>}

            {isScrutiny && (
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, Array.isArray(value) ? type : value);
                }}
                key={dataIndex}
              >
                {/* {badgeType && <div>{extractValue(data, badgeType)}</div>} */}
                {dataError ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "text":
      const textValue = extractValue(data, value);
      return (
        <div className={`text-main ${isScrutiny && dataError && "error"}`}>
          <div className="text">
            <div className="label">{t(label)}</div>
            <div className="value">
              {Array.isArray(textValue) && textValue.map((text) => <div> {text} </div>)}
              {!Array.isArray(textValue) && textValue}
            </div>
            {isScrutiny && (
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                {dataError && isScrutiny ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );

    case "infoBox":
      if (!data?.[value]?.header) {
        return null;
      }
      return (
        <div className={`text-main ${isScrutiny && dataError && "error"}`}>
          <div className="value info-box">
            <InfoCard
              variant={"default"}
              label={t(data?.[value]?.header)}
              additionalElements={[
                <React.Fragment>
                  {Array.isArray(data?.[value]?.data) && (
                    <ul style={{ listStyleType: "disc", margin: "4px" }}>
                      {data?.[value]?.data.map((data) => (
                        <li>{t(data)}</li>
                      ))}
                    </ul>
                  )}
                </React.Fragment>,
              ]}
              inline
              text={typeof data?.[value]?.data === "string" && data?.[value]?.data}
              textStyle={{}}
              className={`adhaar-verification-info-card`}
            />
          </div>
          {isScrutiny && (
            <div
              className="flag"
              onClick={(e) => {
                handleOpenPopup(e, configKey, name, dataIndex, value);
              }}
              key={dataIndex}
            >
              {dataError && isScrutiny ? <EditPencilIcon /> : <FlagIcon />}
            </div>
          )}
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );

    case "amount":
      return (
        <div className={`amount-main ${isScrutiny && dataError && "error"}`}>
          <div className="amount">
            <div className="label">{t(label)}</div>
            <div className="value"> {`₹${extractValue(data, value)}`} </div>
            {isScrutiny && (
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                {dataError && isScrutiny ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "phonenumber":
      const numbers = extractValue(data, value);
      return (
        <div className={`phone-number-main ${isScrutiny && dataError && "error"}`}>
          <div className="phone-number">
            <div className="label">{t(label)}</div>
            <div className="value">
              {Array.isArray(numbers) && numbers.map((number) => <div> {`+91-${number}`} </div>)}
              {!Array.isArray(numbers) && numbers ? `+91-${numbers}` : ""}
            </div>
            {isScrutiny && (
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                {dataError && isScrutiny ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "image":
      const files = value?.map((value) => extractValue(data, value)) || [];
      let hasImages = false;
      files.forEach((file) => {
        if (file && file?.length > 0) {
          hasImages = true;
        }
      });
      if (!hasImages) {
        return null;
      }
      return (
        <div className={`image-main ${isScrutiny && dataError && "error"}`}>
          <div className={`image ${!isScrutiny ? "column" : ""}`}>
            <div className="label">{t(label)}</div>
            <div className={`value ${!isScrutiny ? "column" : ""}`} style={{ overflowX: "scroll", width: "100%" }}>
              {Array.isArray(value)
                ? value?.map((value) =>
                    extractValue(data, value) && Array.isArray(extractValue(data, value)) ? (
                      extractValue(data, value)?.map((data, index) => {
                        if (data?.fileStore) {
                          return (
                            <div
                              style={{ cursor: "pointer" }}
                              onClick={() => {
                                handleImageClick(configKey, name, dataIndex, value, data);
                              }}
                            >
                              <DocViewerWrapper
                                key={`${value}-${index}`}
                                fileStoreId={data?.fileStore}
                                displayFilename={data?.fileName}
                                tenantId={tenantId}
                                docWidth="250px"
                                showDownloadOption={false}
                                documentName={docName?.[value]}
                              />
                            </div>
                          );
                        } else if (data?.document) {
                          return data?.document?.map((data, index) => {
                            return (
                              <div
                                style={{ cursor: "pointer" }}
                                onClick={() => {
                                  handleImageClick(configKey, name, dataIndex, value, data);
                                }}
                              >
                                <DocViewerWrapper
                                  key={`${value}-${index}`}
                                  fileStoreId={data?.fileStore}
                                  displayFilename={data?.fileName}
                                  tenantId={tenantId}
                                  docWidth="250px"
                                  showDownloadOption={false}
                                  documentName={docName?.[value]}
                                />
                              </div>
                            );
                          });
                        } else {
                          return null;
                        }
                      })
                    ) : extractValue(data, value) ? (
                      <div
                        style={{ cursor: "pointer" }}
                        onClick={() => {
                          handleImageClick(configKey, name, dataIndex, value, data);
                        }}
                      >
                        <DocViewerWrapper
                          key={`${value}-${extractValue(data, value)?.name}`}
                          fileStoreId={extractValue(data, value)?.fileStore}
                          displayFilename={extractValue(data, value)?.fileName}
                          tenantId={tenantId}
                          docWidth="250px"
                          showDownloadOption={false}
                          documentName={docName?.[value]}
                        />
                      </div>
                    ) : null
                  )
                : null}
            </div>
            <div
              className="flag"
              onClick={(e) => {
                handleOpenPopup(e, configKey, name, dataIndex, value);
              }}
              key={dataIndex}
            >
              {isScrutiny && (dataError ? <EditPencilIcon /> : <FlagIcon />)}
            </div>
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    case "address":
      const addressDetails = extractValue(data, value);
      let address = [""];
      if (Array.isArray(addressDetails)) {
        address = addressDetails.map(({ addressDetails }) => {
          return `${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state} - ${addressDetails?.pincode}`;
        });
      } else {
        address = [
          `${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state} - ${addressDetails?.pincode}`,
        ];
      }

      return (
        <div className={`address-main ${isScrutiny && dataError && "error"}`}>
          <div className="address">
            <div className="label">{t(label)}</div>
            <div className={`value ${!isScrutiny ? "column" : ""}`}>
              {address.map((item) => (
                <p>{item}</p>
              ))}
            </div>

            {isScrutiny && (
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                {dataError && isScrutiny ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          {dataError && isScrutiny && (
            <div className="scrutiny-error input">
              <FlagIcon isError={true} />
              {dataError}
            </div>
          )}
        </div>
      );
    default:
      const defaulValue = extractValue(data, value);
      return (
        <div>
          <div className="text">
            <div className="label">{t(label)}</div>
            <div className="value">
              {Array.isArray(defaulValue) && defaulValue.map((text) => <div> {text} </div>)}
              {!Array.isArray(defaulValue) && defaulValue}
            </div>
            {isScrutiny && (
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, value);
                }}
                key={dataIndex}
              >
                {dataError && isScrutiny ? <EditPencilIcon /> : <FlagIcon />}
              </div>
            )}
          </div>
          <div className="scrutiny-error input">
            <FlagIcon isError={true} />
            {dataError}
          </div>
        </div>
      );
  }
};

export default CustomReviewCardRow;
