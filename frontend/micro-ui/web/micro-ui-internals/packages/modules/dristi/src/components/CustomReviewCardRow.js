import { InfoCard } from "@egovernments/digit-ui-components";
import { EditPencilIcon } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useMemo } from "react";
import { FlagIcon } from "../icons/svgIndex";
import DocViewerWrapper from "../pages/employee/docViewerWrapper";

const LocationIcon = () => (
  <svg width="10" height="14" viewBox="0 0 10 14" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path
      d="M4.9987 0.0820312C2.4187 0.0820312 0.332031 2.1687 0.332031 4.7487C0.332031 8.2487 4.9987 13.4154 4.9987 13.4154C4.9987 13.4154 9.66537 8.2487 9.66537 4.7487C9.66537 2.1687 7.5787 0.0820312 4.9987 0.0820312ZM4.9987 6.41536C4.0787 6.41536 3.33203 5.6687 3.33203 4.7487C3.33203 3.8287 4.0787 3.08203 4.9987 3.08203C5.9187 3.08203 6.66536 3.8287 6.66536 4.7487C6.66536 5.6687 5.9187 6.41536 4.9987 6.41536Z"
      fill="#77787B"
    />
  </svg>
);
const LocationContent = ({ latitude = 17.2, longitude = 17.2 }) => {
  return (
    <div style={{ fontSize: "14px", display: "flex", marginTop: "-2px", alignItems: "center" }}>
      <div>
        <a
          href={`https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`}
          target="_blank"
          rel="noreferrer"
          style={{ color: "#77787B" }}
        >
          View on map
        </a>
      </div>
      <div style={{ marginLeft: "10px", display: "flex" }}>
        <LocationIcon></LocationIcon>
      </div>
    </div>
  );
};

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
  const handleImageClick = useCallback(
    (configKey, name, dataIndex, fieldName, data) => {
      if (isScrutiny && data) {
        handleClickImage(null, configKey, name, dataIndex, fieldName, data, [type, fieldName]);
      }
      return null;
    },
    [handleClickImage, isScrutiny]
  );
  const renderCard = useMemo(() => {
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
                    handleOpenPopup(e, configKey, name, dataIndex, Array.isArray(value) ? type : value, [...value, type]);
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
                {Array.isArray(files)
                  ? files?.map((file) =>
                      file && Array.isArray(file) ? (
                        file?.map((data, index) => {
                          if (data?.fileStore) {
                            return (
                              <div
                                style={{ cursor: "pointer" }}
                                onClick={() => {
                                  handleImageClick(configKey, name, dataIndex, value, data);
                                }}
                              >
                                <DocViewerWrapper
                                  key={`${file.fileStore}-${index}`}
                                  fileStoreId={data?.fileStore}
                                  displayFilename={data?.fileName}
                                  tenantId={tenantId}
                                  docWidth="250px"
                                  showDownloadOption={false}
                                  documentName={data?.fileName}
                                  preview
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
                                    key={`${file.fileStore}-${index}`}
                                    fileStoreId={data?.fileStore}
                                    displayFilename={data?.fileName}
                                    tenantId={tenantId}
                                    docWidth="250px"
                                    showDownloadOption={false}
                                    documentName={data?.fileName}
                                    preview
                                  />
                                </div>
                              );
                            });
                          } else {
                            return null;
                          }
                        })
                      ) : file ? (
                        <div
                          style={{ cursor: "pointer" }}
                          onClick={() => {
                            handleImageClick(configKey, name, dataIndex, value, data);
                          }}
                        >
                          <DocViewerWrapper
                            key={`${value}-${file?.name}`}
                            fileStoreId={file?.fileStore}
                            displayFilename={file?.fileName}
                            tenantId={tenantId}
                            docWidth="250px"
                            showDownloadOption={false}
                            documentName={data?.fileName}
                            preview
                          />
                        </div>
                      ) : null
                    )
                  : null}
              </div>
              <div
                className="flag"
                onClick={(e) => {
                  handleOpenPopup(e, configKey, name, dataIndex, Array.isArray(value) ? type : value, [...value, type]);
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
            return {
              address: `${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state} - ${addressDetails?.pincode}`,
              coordinates: addressDetails?.coordinates,
            };
          });
        } else {
          address = [
            {
              address: `${addressDetails?.locality}, ${addressDetails?.city}, ${addressDetails?.district}, ${addressDetails?.state} - ${addressDetails?.pincode}`,
              coordinates: addressDetails?.coordinates,
            },
          ];
        }

        return (
          <div className={`address-main ${isScrutiny && dataError && "error"}`}>
            <div className="address">
              <div className="label">{t(label)}</div>
              <div className={`value ${!isScrutiny ? "column" : ""}`}>
                {address.map((item) => {
                  return (
                    <p>
                      {item?.address}{" "}
                      <LocationContent latitude={item?.coordinates?.latitude || 31.6160638} longitude={item?.coordinates?.longitude || 74.8978579} />
                    </p>
                  );
                })}
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
  }, [
    badgeType,
    configKey,
    data,
    dataError,
    dataIndex,
    handleImageClick,
    handleOpenPopup,
    isScrutiny,
    label,
    name,
    t,
    tenantId,
    titleHeading,
    titleIndex,
    type,
    value,
  ]);

  return renderCard;
};

export default CustomReviewCardRow;
