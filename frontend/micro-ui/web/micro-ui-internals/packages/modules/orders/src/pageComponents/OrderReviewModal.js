import { CloseSvg } from "@egovernments/digit-ui-components";
import Axios from "axios";
import React, { useEffect, useMemo, useState } from "react";
import { useQuery } from "react-query";
import Modal from "../../../dristi/src/components/Modal";
import { Urls } from "../hooks/services/Urls";

const OrderPreviewOrderTypeMap = {
  MANDATORY_SUBMISSIONS_RESPONSES: "mandatory-async-submissions-responses",
  ASSIGNING_DATE_RESCHEDULED_HEARING: "new-hearing-date-after-rescheduling",
  SCHEDULE_OF_HEARING_DATE: "schedule-hearing-date",
  SUMMONS: "summons-issue",
  INITIATING_RESCHEDULING_OF_HEARING_DATE: "accept-reschedule-request",
  OTHERS: "order-generic",
  REFERRAL_CASE_TO_ADR: "order-generic",
  EXTENSION_OF_DOCUMENT_SUBMISSION_DATE: "order-generic",
  SCHEDULING_NEXT_HEARING: "reschedule-request-judge",
  RESCHEDULE_OF_HEARING_DATE: "new-hearing-date-after-rescheduling",
  REJECTION_RESCHEDULE_REQUEST: "order-for-rejection-rescheduling-request",
  ASSIGNING_NEW_HEARING_DATE: "order-generic",
  CASE_TRANSFER: "case-transfer",
  SETTLEMENT: "case-settlement-acceptance",
  BAIL_APPROVED: "order-bail-acceptance",
  BAIL_REJECT: "order-bail-rejection",
  WARRANT: "order-generic",
  WITHDRAWAL: "order-generic",
  APPROVE_VOLUNTARY_SUBMISSIONS: "order-accept-voluntary",
  REJECT_VOLUNTARY_SUBMISSIONS: "order-reject-voluntary",
  JUDGEMENT: "order-generic",
  SECTION_202_CRPC: "order-generic",
};

const onDocumentUpload = async (fileData, filename) => {
  try {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, Digit.ULBService.getCurrentTenantId());
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  } catch (error) {
    console.error("Failed to upload document:", error);
    throw error; // or handle error appropriately
  }
};

function OrderReviewModal({ setShowReviewModal, t, order, setShowsignatureModal, showActions = true, setOrderPdfFileStoreID }) {
  const [fileStoreId, setFileStoreID] = useState(null);
  const [fileName, setFileName] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const DocViewerWrapper = Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");
  const filestoreId = "9d23b127-c9e9-4fd1-9dc8-e2e762269046";

  let orderPreviewKey = order?.orderType;
  if (order?.additionalDetails?.applicationStatus === "APPROVED") {
    orderPreviewKey = "BAIL_APPROVED";
  } else if (order?.additionalDetails?.applicationStatus === "Rejected") {
    orderPreviewKey = "BAIL_REJECT";
  }
  orderPreviewKey = OrderPreviewOrderTypeMap[orderPreviewKey] || OrderPreviewOrderTypeMap[order?.orderType];

  const { data: { file: orderPreviewPdf, fileName: orderPreviewFileName } = {}, isFetching: isLoading } = useQuery({
    queryKey: ["orderPreviewPdf", tenantId, order?.id, order?.cnrNumber, orderPreviewKey],
    queryFn: async () => {
      return Axios({
        method: "POST",
        url: Urls.orders.orderPreviewPdf,
        params: {
          tenantId: tenantId,
          orderId: order?.id,
          cnrNumber: order?.cnrNumber,
          qrCode: false,
          orderType: orderPreviewKey,
        },
        data: {
          RequestInfo: {
            authToken: Digit.UserService.getUser().access_token,
            userInfo: Digit.UserService.getUser()?.info,
            msgId: `${Date.now()}|${Digit.StoreData.getCurrentLanguage()}`,
            apiId: "Rainmaker",
          },
        },
        responseType: "blob",
      }).then((res) => ({ file: res.data, fileName: res.headers["content-disposition"]?.split("filename=")[1] }));
    },
    onError: (error) => {
      console.error("Failed to fetch order preview PDF:", error);
    },
    enabled: !!order?.id && !!order?.cnrNumber && !!orderPreviewKey,
  });

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

  useEffect(() => {
    if (order?.filesData) {
      const numberOfFiles = order?.filesData.length;
      let finalDocumentData = [];
      if (numberOfFiles > 0) {
        order?.filesData.forEach((value) => {
          finalDocumentData.push({
            fileName: value?.[0],
            fileStoreId: value?.[1]?.fileStoreId,
            documentType: value?.[1]?.file?.type,
          });
        });
      }
      if (numberOfFiles > 0) {
        onDocumentUpload(order?.filesData[0][1]?.file, order?.filesData[0][0]).then((document) => {
          setFileName(order?.filesData[0][0]);
          setFileStoreID(document.file?.files?.[0]?.fileStoreId);
        });
      }
    }
  }, [order, tenantId]);

  const showDocument = useMemo(() => {
    return (
      <div
        className=""
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          height: "100%",
          width: "100%",
          maxHeight: "100%",
          maxWidth: "100%",
        }}
      >
        {orderPreviewPdf ? (
          <DocViewerWrapper
            docWidth={"calc(80vw* 62/ 100)"}
            docHeight={"60vh"}
            selectedDocs={[orderPreviewPdf]}
            displayFilename={fileName}
            showDownloadOption={false}
          />
        ) : isLoading ? (
          <h2>{t("LOADING")}</h2>
        ) : (
          <h2>{t("PREVIEW_DOC_NOT_AVAILABLE")}</h2>
        )}
      </div>
    );
  }, [orderPreviewPdf, fileName, isLoading, t]);

  return (
    <Modal
      headerBarMain={<Heading label={t("REVIEW_ORDERS_HEADING")} />}
      headerBarEnd={<CloseBtn onClick={() => setShowReviewModal(false)} />}
      actionSaveLabel={showActions && t("ADD_SIGNATURE")}
      actionSaveOnSubmit={() => {
        if (showActions) {
          const pdfFile = new File([orderPreviewPdf], orderPreviewFileName, { type: "application/pdf" });
          console.debug(pdfFile, orderPreviewFileName);
          onDocumentUpload(pdfFile, pdfFile.name)
            .then((document) => {
              const fileStoreId = document.file?.files?.[0]?.fileStoreId;
              if (fileStoreId) {
                setOrderPdfFileStoreID(fileStoreId);
              }
            })
            .catch((e) => {
              console.error("Failed to upload document:", e);
            })
            .finally(() => {
              setShowsignatureModal(true);
              setShowReviewModal(false);
            });
        }
      }}
      className={"review-order-modal"}
    >
      <div className="review-order-body-main">
        <div className="review-order-modal-list-div">
          <div className="review-order-type-side-stepper">
            <h1> {t(order?.orderType)}</h1>
          </div>
        </div>
        <div className="review-order-modal-document-div">{showDocument} </div>
      </div>
    </Modal>
  );
}

export default OrderReviewModal;
