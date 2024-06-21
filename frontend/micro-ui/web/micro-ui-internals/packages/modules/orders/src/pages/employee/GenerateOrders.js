import React, { useState, useEffect, useCallback, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { Header, InboxSearchComposer, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { TabSearchconfig } from "../../../../orders/src/configs/GenerateOrdersConfig";
import { configs } from "../../configs/ordersCreateConfig";
import { CustomDeleteIcon } from "../../../../dristi/src/icons/svgIndex";
import Modal from "../../../../dristi/src/components/Modal";
import { Button, CloseSvg } from "@egovernments/digit-ui-components";
// import { ReactComponent as SmallInfoIcon } from "../images/smallInfoIcon.svg";
// import { ReactComponent as UploadIcon } from "../images/uploadIcon.svg";

const fieldStyle = { marginRight: 0 };

const getFormattedDate = () => {
  const currentDate = new Date();
  const year = String(currentDate.getFullYear()).slice(-2);
  const month = String(currentDate.getMonth() + 1).padStart(2, "0");
  const day = String(currentDate.getDate()).padStart(2, "0");

  return `${month}/${day}/${year}`;
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const GenerateOrders = () => {
  const defaultValue = {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const [currentSelectedOrderIndex, setCurrentSelectedOrderIndex] = useState(0);
  const [showDeleteWarningModal, setShowDeleteWarningModal] = useState(false);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentSelectedOrderInReview, setCurrentSelectedOrderInReview] = useState(0);
  const [showSignatureModal, setShowSignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const ordersList = [
    { key: "order1", value: {} },
    { key: "order2", value: {} },
  ];

  const orderTypes = [{ name: "order for doc submission" }, { name: "order for summons" }];

  const deleteWarningText = useMemo(() => {
    return (
      <div className="delete-warning-text">
        <h3>{`${t("THIS_CAN_NOT_BE_REVERSED")}`}</h3>
      </div>
    );
  }, []);

  const updateCurrentSelectedOrder = (index) => {
    setCurrentSelectedOrderIndex(index);
  };

  const updateCurrentSelectedOrderInReview = (index) => {
    setCurrentSelectedOrderInReview(index);
  };

  const handleDeleteOrder = (index) => {
    // console.log(index);
  };

  const handleReviewOrder = (data, index) => {
    setShowReviewModal(true);
  };

  const handleCloseSignaturePopup = () => {
    setShowSignatureModal(false);
    setShowReviewModal(true);
  };

  const handleCloseSuccessModal = () => {
    setShowSuccessModal(false);
    history.back(); // go to view case screen when clicking on close button.
  };

  const ifAllSignaturesCompleted = true; // update useMemo logic here while integrating.

  return (
    <div style={{ display: "flex", gap: "5%", marginBottom: "200px" }}>
      <div style={{ width: "20%" }}>
        <div style={{ color: "#007E7E" }}> + Add Order</div>
        <div>
          {ordersList.map((order, index) => {
            return (
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "space-between",
                  alignItems: "center",
                  cursor: "pointer",
                  ...(currentSelectedOrderIndex === index ? { background: "#E8E8E8" } : {}),
                }}
                onClick={() => updateCurrentSelectedOrder(index)}
              >
                <h1> Order {index}</h1>
                <span onClick={() => setShowDeleteWarningModal(true)}>
                  <CustomDeleteIcon></CustomDeleteIcon>
                </span>
              </div>
            );
          })}
        </div>
      </div>
      <div style={{ minWidth: "70%" }}>
        <Header className="main-card-header">{`${t("ORDER")} ${currentSelectedOrderIndex + 1}`}</Header>
        <FormComposerV2
          label={t("REVIEW_ORDERS")}
          config={configs.map((config) => {
            return {
              ...config,
            };
          })}
          defaultValues={defaultValue}
          onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
            console.log(formData, "formData");
          }}
          onSubmit={(data) => handleReviewOrder(data, currentSelectedOrderIndex)}
          onSecondayActionClick={() => {}}
          secondaryLabel={t("SAVE_AS_DRAFT")}
          showSecondaryLabel={true}
          fieldStyle={fieldStyle}
        />
      </div>
      {showDeleteWarningModal && (
        <Modal
          headerBarMain={<Heading label={t("ARE_YOU_SURE")} />}
          headerBarEnd={<CloseBtn onClick={() => setShowDeleteWarningModal(false)} />}
          actionCancelLabel={t("CANCEL")}
          actionCancelOnSubmit={() => setShowDeleteWarningModal(false)}
          actionSaveLabel={t("DELETE_ORDER")}
          children={deleteWarningText}
          actionSaveOnSubmit={() => {
            handleDeleteOrder(index);
          }}
          // className={""}
          style={{ height: "40px" }}
        ></Modal>
      )}
      {showReviewModal && (
        <Modal
          headerBarMain={<Heading label={t("REVIEW_ORDERS_HEADING")} />}
          headerBarEnd={<CloseBtn onClick={() => setShowReviewModal(false)} />}
          actionCancelLabel={t("SAVE_DRAFT")}
          actionCancelOnSubmit={() => {}}
          actionSaveLabel={t("ADD_SIGNATURE")}
          // children={deleteWarningText}
          actionSaveOnSubmit={() => {}}
          className={"review-order-modal"}
          // style={{ height: "240px" }}
        >
          <div className="review-order-body-main">
            <div className="review-order-modal-list-div">
              <div>
                {orderTypes.map((orderType, index) => {
                  return (
                    <div
                      style={{ cursor: "pointer", ...(currentSelectedOrderIndex === index ? { background: "#E8E8E8" } : {}) }}
                      onClick={() => updateCurrentSelectedOrderInReview(index)}
                    >
                      <h1> {orderType?.name}</h1>
                    </div>
                  );
                })}
              </div>
            </div>
            <div className="review-order-modal-document-div">/// document here</div>
          </div>
        </Modal>
      )}
      {showSignatureModal && (
        <Modal
          headerBarMain={<Heading label={`${t("ADD_SIGNATURE")} (${1})`} />}
          headerBarEnd={<CloseBtn onClick={() => handleCloseSignaturePopup()} />}
          actionCancelLabel={t("BACK")}
          actionCancelOnSubmit={() => handleCloseSignaturePopup()}
          actionSaveLabel={ifAllSignaturesCompleted ? t("ISSUE_ORDERS") : t("NEXT")} // check here if signatures are added for all orders- Issue Orders, ow Next.
          // children={deleteWarningText}
          actionSaveOnSubmit={() => {}}
          className={"add-signature-modal"}
          // style={{ height: "240px" }}
        >
          <div className="add-signature-main-div">
            <div className="note-div">
              <div className="icon-div">
                {/* <SmallInfoIcon></SmallInfoIcon> */}
                <span>PLEASE_NOTE</span>
              </div>
              <h2>YOU_ARE_ADDING_YOUR_SIGNATURE_TO_THE</h2> <span style={{ fontWeight: "bold" }}>{"order type here"}</span>
            </div>
            {ifCurrentSigned ? (
              <div className="not-signed">
                <h1>YOUR_SIGNATURE</h1>
                <div className="signature-button-div">
                  <Button
                    label={t("E_SIGN")}
                    style={{ alignItems: "center" }}
                    // isDisabled={}
                    onButtonClick={() => {}}
                  />
                  <div>
                    <span> {/* <UploadIcon></UploadIcon> */}</span>
                    <h1>{t("UPLOAD_DIGITAL_SIGNATURE_CERTIFICATE")}</h1>
                  </div>
                </div>
                <div>
                  <h2>{t("WANT_TO_DOWNLOAD")}</h2>
                  <span>
                    <a href="">{t("CLICK_HERE")}</a>
                  </span>
                </div>
              </div>
            ) : (
              <div className="signed">
                <h1>{t("YOUR_SIGNATURE")}</h1>
                <span>{t("SIGNED")}</span>
              </div>
            )}
          </div>
        </Modal>
      )}
      {showSuccessModal && (
        <Modal
          actionCancelLabel={t("DOWNLOAD_ORDER")}
          actionCancelOnSubmit={() => {}}
          actionSaveLabel={t("CLOSE")}
          actionSaveOnSubmit={() => handleCloseSuccessModal()}
          className={"orders-success-modal"}
        >
          <div className="success-modal-main-div">
            <div className="success-message-div">
              <h1>{`${t("SUCCCESSFULLY_ISSUED")} ${10} ${t("ORDERS")}`}</h1>
              {/* <SmallInfoIcon></SmallInfoIcon>  */}
            </div>
            <h3>{t("PARTIES_WILL_BE_NOTIFIED")}</h3>
            <div className="order-id-info-div">
              <div className="order-issue-date-div">
                <h2>{t("ORDER_ISSUE_DATE")}</h2>
                <span>{getFormattedDate()}</span>
              </div>
              <div className="order-ids-list-div">
                {ordersList.map((order, index) => {
                  return (
                    <div>
                      <h2>{`${t("ORDER_ID")} ${index + 1} : ${"ORDER-TYPE-HERE"}`}</h2>
                      <span>
                        <h2>{"KA01234"}</h2>
                        <span>
                          <h2>{t("COPY")}</h2>
                        </span>
                      </span>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
};

export default GenerateOrders;
