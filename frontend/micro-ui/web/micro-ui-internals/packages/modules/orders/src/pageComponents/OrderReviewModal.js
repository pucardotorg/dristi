import { CloseSvg } from "@egovernments/digit-ui-components";
import React, { useEffect, useMemo, useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
function OrderReviewModal({ setShowReviewModal, t, order, setShowsignatureModal, handleSaveDraft, showActions = true }) {
  const [fileStoreId, setFileStoreID] = useState();
  const [fileName, setFileName] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const DocViewerWrapper = Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");
  const MultiUploadWrapper = Digit?.ComponentRegistryService?.getComponent("MultiUploadWrapper");

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

  // !! Do NOT REMOVE THIS LOGIC !!
  // TO DO: Optimize this logic when filesData is available for orders.
  // const onDocumentUpload = async (fileData, filename) => {
  //   const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
  //   return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  // };

  // !! Do NOT REMOVE THIS LOGIC !!
  // useEffect(() => {
  //   const numberOfFiles = order?.filesData.length;
  //   let finalDocumentData = [];
  //   if (numberOfFiles > 0) {
  //     order?.filesData.forEach((value) => {
  //       finalDocumentData.push({
  //         fileName: value?.[0],
  //         fileStoreId: value?.[1]?.fileStoreId,
  //         documentType: value?.[1]?.file?.type,
  //       });
  //     });
  //   }
  //   if (numberOfFiles > 0) {
  //     onDocumentUpload(order?.filesData[0][1]?.file, order?.filesData[0][0]).then((document) => {
  //       setFileName(order?.filesData[0][0]);
  //       setFileStoreID(document.file?.files?.[0]?.fileStoreId);
  //     });
  //   }
  // }, [order]);

  // !! Do NOT REMOVE THIS !!
  // Render showDocument when FilesData is available in orders.
  // const showDocument = useMemo(() => {
  //   return (
  //     <div
  //       className=""
  //       style={{
  //         display: "flex",
  //         flexDirection: "column",
  //         alignItems: "center",
  //         height: "100%",
  //         width: "100%",
  //         maxHeight: "100%",
  //         maxWidth: "100%",
  //       }}
  //     >
  //       <DocViewerWrapper
  //         docWidth={"calc(80vw* 62/ 100)"}
  //         docHeight={"60vh"}
  //         fileStoreId={fileStoreId}
  //         tenantId={tenantId}
  //         displayFilename={fileName}
  //       />
  //     </div>
  //   );
  // }, [fileStoreId]);

  return (
    <Modal
      headerBarMain={<Heading label={t("REVIEW_ORDERS_HEADING")} />}
      headerBarEnd={<CloseBtn onClick={() => setShowReviewModal(false)} />}
      actionCancelLabel={showActions && t("SAVE_DRAFT")}
      actionCancelOnSubmit={handleSaveDraft}
      actionSaveLabel={showActions && t("ADD_SIGNATURE")}
      actionSaveOnSubmit={() => {
        setShowsignatureModal(true);
        setShowReviewModal(false);
      }}
      className={"review-order-modal"}
    >
      <div className="review-order-body-main">
        <div className="review-order-modal-list-div">
          <div className="review-order-type-side-stepper">
            <h1> {order?.orderType}</h1>
          </div>
        </div>
        <div className="review-order-modal-document-div">{/* {showDocument}  */} // Document will be shown here when filesData is available.</div>
      </div>
    </Modal>
  );
}

export default OrderReviewModal;
