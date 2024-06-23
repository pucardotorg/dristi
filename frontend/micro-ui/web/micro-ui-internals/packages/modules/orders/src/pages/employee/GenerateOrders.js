import React, { useState, useEffect, useCallback, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { Header, InboxSearchComposer, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { TabSearchconfig } from "../../../../orders/src/configs/GenerateOrdersConfig";
import { configs } from "../../configs/ordersCreateConfig";
import { CustomAddIcon, CustomDeleteIcon } from "../../../../dristi/src/icons/svgIndex";
import Modal from "../../../../dristi/src/components/Modal";
import { Button, CloseSvg } from "@egovernments/digit-ui-components";
import { DRISTIService } from "../../../../dristi/src/services";
import DeleteOrderModal from "../../pageComponents/DeleteOrderModal";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
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
  const urlParams = new URLSearchParams(window.location.search);
  const orderId = urlParams.get("orderId");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const [currentSelectedOrderIndex, setCurrentSelectedOrderIndex] = useState(0);
  const [selectedOrder, setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [currentSelectedOrderInReview, setCurrentSelectedOrderInReview] = useState(0);
  const [showSignatureModal, setShowSignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formdata, setFormdata] = useState([{}]);
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

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, orderindex) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata[orderindex])) {
      setFormdata((prev) => {
        return prev.map((item, ind) => {
          return ind == orderindex ? formData : item;
        });
      });
    }
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };
  const handleCreateOrder = () => {
    const reqbody = {
      order: {
        tenantId,
        applicationNumber: ["CLERK-2024-04-29-000124"],
        hearingNumber: "3244d158-c5cb-4769-801f-a0f94f383679",
        cnrNumber: "CNR111",
        filingNumber: "F-C.1973.002-2024-000130",
        statuteSection: {
          tenantId,
          auditDetails: {
            createdBy: "3244d158-c5cb-4769-801f-a0f94f383679",
            lastModifiedBy: "3244d158-c5cb-4769-801f-a0f94f383679",
            createdTime: 1714384374834,
            lastModifiedTime: 1714384374834,
          },
        },
        orderType: "Bail",
        status: "INWORKFLOW",
        isActive: true,
        createdDate: "02-02-2024",
        workflow: {
          action: "CREATE",
          comments: "Creating for order registration",
          documents: [
            {
              id: "bb86207f-6ec0-4314-8260-bcdd4e441e1d",
              documentType: "application/pdf",
              fileStore: "349a95a7-d4ef-48d6-889c-4faef7b8289e",
              documentUid: "bb86207f-6ec0-4314-8260-bcdd4e441e1d",
              additionalDetails: {},
            },
          ],
          assignes: null,
          rating: null,
        },
        documents: [
          {
            id: "bb86207f-6ec0-4314-8260-bcdd4e441e1d",
            documentType: "application/pdf",
            fileStore: "349a95a7-d4ef-48d6-889c-4faef7b8289e",
            documentUid: "bb86207f-6ec0-4314-8260-bcdd4e441e1d",
            additionalDetails: {},
          },
        ],
        auditDetails: {
          createdBy: "3244d158-c5cb-4769-801f-a0f94f383679",
          lastModifiedBy: "3244d158-c5cb-4769-801f-a0f94f383679",
          createdTime: 1714384374834,
          lastModifiedTime: 1714384374834,
        },
        additionalDetails: {
          username: "nil",
        },
      },
    };
    DRISTIService.createOrder(reqbody, { tenantId })
      .then((res) => {
        console.debug(res);
        // history.push(`${path}/case?caseId=${res?.cases[0]?.id}`);
      })
      .catch((err) => {
        console.debug(err);
      });
  };

  const handleAddOrder = () => {
    setFormdata((prev) => {
      return [...prev, {}];
    });
  };

  const updateCurrentSelectedOrder = (index) => {
    setCurrentSelectedOrderIndex(index);
    setSelectedOrder(index);
  };

  const updateCurrentSelectedOrderInReview = (index) => {
    setCurrentSelectedOrderInReview(index);
  };

  const handleDeleteOrder = (index) => {
    console.debug(index);
    setFormdata((prev) => {
      prev.splice(index, 1);
      return prev;
    });
    setDeleteOrderIndex(null);
    // also delete it or discard it if the order ID has been created
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
        <div style={{ color: "#007E7E" }} onClick={handleAddOrder}>{`+ ${t("CS_ADD_ORDER")}`}</div>
        <div>
          {formdata.map((order, index) => {
            return (
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "space-between",
                  alignItems: "center",
                  cursor: "pointer",
                  ...(selectedOrder === index ? { background: "#E8E8E8" } : {}),
                }}
                onClick={() => updateCurrentSelectedOrder(index)}
              >
                <h1>{`${t("CS_ORDER")} ${index + 1}`}</h1>
                {formdata?.length > 1 && (
                  <span
                    onClick={() => {
                      setDeleteOrderIndex(index);
                    }}
                  >
                    <CustomDeleteIcon />
                  </span>
                )}
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
          defaultValues={{}}
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
      {deleteOrderIndex !== null && <DeleteOrderModal deleteOrderIndex={deleteOrderIndex} setDeleteOrderIndex={setDeleteOrderIndex} />}
      {showReviewModal && <OrderReviewModal t={t} formdata={formdata} setShowReviewModal={setShowReviewModal}  currentSelectedOrderIndex={currentSelectedOrderIndex}/>}
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
                {formdata.map((order, index) => {
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
