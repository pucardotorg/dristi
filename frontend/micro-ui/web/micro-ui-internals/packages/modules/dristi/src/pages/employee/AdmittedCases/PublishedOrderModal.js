import { CloseSvg } from "@egovernments/digit-ui-components";
import React, { useEffect, useMemo, useState } from "react";
import Modal from "../../../components/Modal";
import { Button, SubmitBar } from "@egovernments/digit-ui-react-components";
import { CaseWorkflowState } from "../../../Utils/caseWorkflow";
import useGetAllOrderApplicationRelatedDocuments from "../../../hooks/dristi/useGetAllOrderApplicationRelatedDocuments";
import { useGetPendingTask } from "../../../hooks/dristi/useGetPendingTask";

function PublishedOrderModal({
  t,
  order,
  handleDownload,
  handleRequestLabel,
  handleSubmitDocument,
  caseStatus,
  handleOrdersTab,
  extensionApplications = [],
  productionOfDocumentApplications = [],
}) {
  const [fileStoreId, setFileStoreID] = useState(null);
  const [fileName, setFileName] = useState();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const DocViewerWrapper = Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const { documents, isLoading, fetchRecursiveData } = useGetAllOrderApplicationRelatedDocuments();
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

  const signedOrder = useMemo(() => order?.documents?.filter((item) => item?.documentType === "SIGNED")[0], [order]);
  const userInfo = Digit.UserService.getUser()?.info;
  const showSubmissionButtons = useMemo(() => {
    if (productionOfDocumentApplications?.some((item) => item?.referenceId === order?.id)) {
      return false;
    }

    //TODO : need to ask
    const isAuthority = order?.additionalDetails?.formdata?.partyId;
    const submissionParty = order?.additionalDetails?.formdata?.submissionParty?.map((item) => item.uuid) || [];
    const allSubmissionParty = [...submissionParty, isAuthority].filter(Boolean);
    return (
      allSubmissionParty?.includes(userInfo?.uuid) &&
      userRoles.includes("SUBMISSION_CREATOR") &&
      [
        CaseWorkflowState.PENDING_ADMISSION_HEARING,
        CaseWorkflowState.ADMISSION_HEARING_SCHEDULED,
        CaseWorkflowState.PENDING_NOTICE,
        CaseWorkflowState.PENDING_RESPONSE,
        CaseWorkflowState.PENDING_ADMISSION,
        CaseWorkflowState.CASE_ADMITTED,
      ].includes(caseStatus)
    );
  }, [caseStatus, order, userInfo?.uuid, userRoles, productionOfDocumentApplications]);

  const showExtensionButton = useMemo(
    () =>
      showSubmissionButtons &&
      !extensionApplications?.some((item) => item?.additionalDetails?.formdata?.refOrderId === order?.orderNumber) &&
      order?.orderType !== "SET_BAIL_TERMS",
    [extensionApplications, order, showSubmissionButtons]
  );

  useEffect(() => {
    const onDocumentUpload = async (fileData, filename) => {
      const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
      return { file: fileUploadRes?.data, fileType: fileData.type, filename };
    };

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

  // tracking of appliction based on order Generated for bail
  const { data: pendingTaskDetails = [] } = useGetPendingTask({
    data: {
      SearchCriteria: {
        tenantId,
        moduleName: "Pending Tasks Service",
        moduleSearchCriteria: {
          filingNumber: order?.filingNumber,
          isCompleted: true,
          referenceId: `MANUAL_${userInfo?.uuid}_${order?.orderNumber}`,
        },
        limit: 10000,
        offset: 0,
      },
    },
    params: { tenantId },
    key: `MANUAL_${userInfo?.uuid}_${order?.orderType}`,
    config: {
      enabled: Boolean(order && tenantId),
    },
  });

  const completedTask = useMemo(() => {
    return pendingTaskDetails?.data?.some((task) => {
      const refIdField = task?.fields?.find((field) => field?.key === "referenceId");
      const isCompletedField = task?.fields?.find((field) => field?.key === "isCompleted");

      return refIdField?.value === `MANUAL_${userInfo?.uuid}_${order?.orderNumber}` && isCompletedField?.value === true;
    });
  }, [pendingTaskDetails?.data, userInfo?.uuid, order?.orderNumber]);

  const showSubmitDocumentButton = useMemo(
    () => (order?.orderType === "SET_BAIL_TERMS" ? showSubmissionButtons && !completedTask : showSubmissionButtons),
    [completedTask, order?.orderType, showSubmissionButtons]
  );

  const showDocument = useMemo(() => {
    return (
      <div
        className=""
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          width: "100%",
          maxHeight: "60vh",
          maxWidth: "100%",
          overflowY: "auto",
          overflowX: "hidden",
        }}
      >
        {documents?.length > 0 ? (
          documents.map((docs) => (
            <DocViewerWrapper
              key={docs?.fileStore}
              docWidth={"calc(80vw * 62 / 100)"}
              docHeight={"60vh"}
              fileStoreId={docs?.fileStore}
              tenantId={tenantId}
              displayFilename={fileName}
              showDownloadOption={false}
            />
          ))
        ) : (
          <h2>{isLoading ? t("Loading.....") : t("PREVIEW_DOC_NOT_AVAILABLE")}</h2>
        )}
      </div>
    );
  }, [documents, fileName, isLoading, t, tenantId]);

  useEffect(() => {
    fetchRecursiveData(order);
  }, [fetchRecursiveData, order]);

  return (
    <Modal
      headerBarMain={<Heading label={t("VIEW_ORDER_HEADING")} />}
      headerBarEnd={<CloseBtn onClick={handleOrdersTab} />}
      actionCancelLabel={null}
      actionCancelOnSubmit={() => {}}
      actionSaveLabel={null}
      hideSubmit={true}
      actionSaveOnSubmit={() => {}}
      popupStyles={{ minHeight: "755px", minWidth: "1050px" }}
    >
      {showDocument}
      <div style={{ marginTop: "65%", display: "flex", width: "100%", justifyContent: "space-between", alignItems: "center" }}>
        <div
          onClick={() => {
            handleDownload(signedOrder?.fileStore);
          }}
          style={{ fontWeight: 700, fontSize: "16px", lineHeight: "18.75px", color: "#007E7E", cursor: "pointer" }}
        >
          {t("DOWNLOAD_ORDER_LINK")}
        </div>
        <div style={{ display: "flex", width: "50%", gap: "20px", justifyContent: "end" }}>
          {showExtensionButton && (
            <Button
              variation="secondary"
              onButtonClick={() => {
                handleRequestLabel(order.orderNumber);
              }}
              className="primary-label-btn"
              label={t("EXTENSION_REQUEST_LABEL")}
            />
          )}
          {showSubmitDocumentButton && (
            <SubmitBar
              variation="primary"
              onSubmit={() => {
                handleSubmitDocument(order.orderNumber);
              }}
              className="primary-label-btn"
              label={t("SUBMIT_DOCUMENT_LABEL")}
            ></SubmitBar>
          )}
        </div>
      </div>
    </Modal>
  );
}

export default PublishedOrderModal;
