import React, { useEffect, useMemo, useState } from "react";
import { Button, Loader, TextInput } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { useTranslation } from "react-i18next";
import TasksComponent from "../../components/TaskComponent";
import { FileUploadIcon } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";
import AuthenticatedLink from "@egovernments/digit-ui-module-dristi/src/Utils/authenticatedLink";
import { Urls } from "../../hooks";
import { CloseSvg, InfoCard } from "@egovernments/digit-ui-components";
import { HomeService } from "../../hooks/services";

const getStyles = () => ({
  container: { display: "flex", flexDirection: "row", height: "100vh" },
  centerPanel: { flex: 3, padding: "24px 40px 16px 16px", border: "1px solid #e0e0e0", marginLeft: "20px" },
  title: { width: "584px", height: "38px", color: "#0A0A0A", fontSize: "32px", fontWeight: 700, marginBottom: "20px" },
  rightPanel: { flex: 1, padding: "24px 16px 24px 24px", borderLeft: "1px solid #ccc" },
  signaturePanel: { display: "flex", flexDirection: "column" },
  signatureTitle: { fontSize: "24px", fontWeight: 700, color: "#3D3C3C" },
  goButton: { padding: 20, boxShadow: "none" },
});

const CloseBtn = ({ onClick }) => {
  return (
    <div
      onClick={onClick}
      style={{
        height: "100%",
        display: "flex",
        alignItems: "center",
        paddingRight: "20px",
        cursor: "pointer",
      }}
    >
      <CloseSvg />
    </div>
  );
};

const Heading = ({ label }) => {
  return (
    <div className="evidence-title">
      <h1 className="heading-m">{label}</h1>
    </div>
  );
};

const formatDate = (date) => {
  if (!date) return "";
  const convertedDate = new Date(date);
  return convertedDate.toLocaleDateString();
};

const ADiaryPage = ({ path }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const getCurrentDate = (date) => {
    const today = date ? new Date(parseInt(date)) : new Date();
    return new Date(today.getTime() - today.getTimezoneOffset() * 60000).toISOString().split("T")[0];
  };
  const Digit = window.Digit || {};

  const queryStrings = Digit.Hooks.useQueryParams();

  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const userInfo = Digit?.UserService?.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const userRoles = Digit?.UserService?.getUser?.()?.info?.roles || [];
  const styles = getStyles();
  const [selectedDate, setSelectedDate] = useState(
    getCurrentDate(queryStrings?.date?.split("-")[1] || localStorage.getItem("selectedADiaryDate") || "")
  );
  const [entryDate, setEntryDate] = useState(
    parseInt(queryStrings?.date?.split("-")[1] || localStorage.getItem("selectedADiaryDate")) || new Date().setHours(0, 0, 0, 0)
  );

  const [offSet, setOffset] = useState(0);
  const limit = 10;
  const emblemBigImageLink = window?.globalConfigs?.getConfig("EMBLEM_BIG");
  const onCourtsImageLink = window?.globalConfigs?.getConfig("ON_COURTS_LOGO");
  const [taskType, setTaskType] = useState({});
  const name = "Signature";
  const pageModule = "en";

  const [ADiarypdf, setADiarypdf] = useState(localStorage.getItem("adiarypdf") || "");
  const [isSelectedDataSigned, setIsSelectedDataSigned] = useState(false);
  const [isSigned, setIsSigned] = useState(false);
  const [stepper, setStepper] = useState(parseInt(localStorage.getItem("adiaryStepper")) || 0);

  const [openUploadSignatureModal, setOpenUploadSignatureModal] = useState(false);
  const [formData, setFormData] = useState({});
  const [signedDocumentUploadID, setSignedDocumentUploadID] = useState("");
  const [generateAdiaryLoader, setGenerateAdiaryLoader] = useState(false);

  const DocViewerWrapper = Digit?.ComponentRegistryService?.getComponent("DocViewerWrapper");
  const MemoDocViewerWrapper = React.memo(DocViewerWrapper);
  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("Modal");
  const UploadSignatureModal = window?.Digit?.ComponentRegistryService?.getComponent("UploadSignatureModal");

  const { uploadDocuments } = Digit.Hooks.orders.useDocumentUpload();

  const { handleEsign, checkSignStatus } = Digit.Hooks.orders.useESign();

  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${ADiarypdf}`;

  const uploadModalConfig = useMemo(() => {
    return {
      key: "uploadSignature",
      populators: {
        inputs: [
          {
            name,
            type: "DragDropComponent",
            uploadGuidelines: "Ensure the image is not blurry and under 5MB.",
            maxFileSize: 5,
            maxFileErrorMessage: "CS_FILE_LIMIT_5_MB",
            fileTypes: ["JPG", "PNG", "JPEG", "PDF"],
            isMultipleUpload: false,
          },
        ],
        validation: {},
      },
    };
  }, [name]);

  const onCancel = () => {
    localStorage.setItem("adiaryStepper", parseInt(stepper) - 1);
    if (parseInt(stepper) === 1) {
      localStorage.removeItem("adiarypdf");
      localStorage.removeItem("adiaryStepper");
      localStorage.removeItem("selectedADiaryDate");
    } else if (parseInt(stepper) === 2) {
      localStorage.removeItem("fileStoreId");
    }
    setStepper(parseInt(stepper) - 1);
  };

  const onSubmit = async () => {
    if (parseInt(stepper) == 0) {
      setGenerateAdiaryLoader(true);
      try {
        const generateADiaryPDF = await HomeService.generateADiaryPDF({
          diary: {
            tenantId: tenantId,
            diaryDate: entryDate,
            diaryType: "ADiary",
            judgeId: "super",
          },
        });
        setGenerateAdiaryLoader(false);
        setADiarypdf(generateADiaryPDF?.fileStoreID);
        localStorage.setItem("adiaryStepper", parseInt(stepper) + 1);
        setStepper(parseInt(stepper) + 1);
      } catch (error) {
        console.log("Error :", error);
        setGenerateAdiaryLoader(false);
      }
    } else if (parseInt(stepper) === 1) {
      localStorage.setItem("adiaryStepper", parseInt(stepper) + 1);
      setStepper(parseInt(stepper) + 1);
      localStorage.setItem("adiarypdf", ADiarypdf);
    }
  };

  const onSelect = (key, value) => {
    if (value?.Signature === null) {
      setFormData({});
      setIsSigned(false);
    } else {
      setFormData((prevData) => ({
        ...prevData,
        [key]: value,
      }));
    }
  };
  useEffect(() => {
    const upload = async () => {
      if (formData?.uploadSignature?.Signature?.length > 0) {
        const uploadedFileId = await uploadDocuments(formData?.uploadSignature?.Signature, tenantId);
        setSignedDocumentUploadID(uploadedFileId[0]?.fileStoreId);
        setIsSigned(true);
      }
    };
    upload();
  }, [formData]);

  useEffect(() => {
    checkSignStatus(name, formData, uploadModalConfig, onSelect, setIsSigned);
  }, [checkSignStatus]);

  if (!DocViewerWrapper) {
    console.error("DocViewerWrapper is not available");
    return null;
  }

  useEffect(() => {
    const getDiarySearch = async () => {
      try {
        const diary = await HomeService.getADiarySearch({
          criteria: {
            tenantId: tenantId,
            judgeId: "super",
            date: entryDate,
          },
        });
        const diaries = diary?.diaries;
        if (Array.isArray(diaries) && diaries?.length > 0) {
          setIsSelectedDataSigned(true);
          setADiarypdf(diaries[0]?.fileStoreID);
        } else {
          setIsSelectedDataSigned(false);
        }
      } catch (error) {
        console.log("Error :", error);
      }
    };
    getDiarySearch();
  }, [entryDate, tenantId]);

  const uploadSignedPdf = async () => {
    try {
      const localStorageID = localStorage.getItem("fileStoreId");
      const upload = await HomeService.updateADiaryPDF({
        diary: {
          tenantId: tenantId,
          diaryDate: entryDate,
          diaryType: "ADiary",
          judgeId: "super",
          documents: [
            {
              tenantId: tenantId,
              fileStoreId: signedDocumentUploadID || localStorageID,
            },
          ],
        },
      });
      setStepper(0);
      setIsSelectedDataSigned(true);
      setADiarypdf(signedDocumentUploadID || localStorageID);
      localStorage.removeItem("fileStoreId");
      localStorage.removeItem("adiarypdf");
      localStorage.removeItem("adiaryStepper");
    } catch (error) {
      console.log("Error :", error);
      setIsSigned(false);
      setSignedDocumentUploadID("");
      localStorage.removeItem("fileStoreId");
      setIsSelectedDataSigned(false);
    }
  };

  const { data: diaryEntries, isLoading: isDiaryEntriesLoading, refetch: refetchDiaryEntries } = Digit.Hooks.dristi.useSearchADiaryService(
    {
      criteria: {
        tenantId: tenantId,
        judgeId: "super",
        date: entryDate,
      },
      pagination: {
        limit,
        offSet,
      },
    },
    {},
    `diary-entries-${entryDate}-${offSet}`,
    entryDate,
    Boolean(entryDate)
  );
  const handleDateChange = (e) => {
    setSelectedDate(e.target.value);
  };

  const handleGoClick = () => {
    const updatedDate = new Date(selectedDate).setHours(0, 0, 0, 0);
    setEntryDate(updatedDate);
    localStorage.setItem("selectedADiaryDate", updatedDate);
    if (queryStrings) {
      history.push(`/${window.contextPath}/employee/home/adiary`);
    }
  };

  const handleNext = () => {
    if (diaryEntries?.pagination?.totalCount > offSet + limit) {
      setOffset((prevOffset) => prevOffset + limit);
    }
  };

  const handlePrevious = () => {
    if (offSet > 0) {
      setOffset((prevOffset) => Math.max(prevOffset - limit, 0));
    }
  };

  const handleRowClick = (entry) => {
    if (entry?.referenceType === "Order") {
      history.push(
        `/${window?.contextPath}/${userInfoType}/orders/generate-orders?filingNumber=${entry?.additionalDetails?.filingNumber}&orderNumber=${entry?.referenceId}`,
        { diaryEntry: entry }
      );
    }
    if (entry?.referenceType === "Documents") {
      history.push(
        `/${window?.contextPath}/${userInfoType}/dristi/home/view-case?caseId=${entry?.additionalDetails?.caseId}&filingNumber=${entry?.additionalDetails?.filingNumber}&tab=Documents&artifactNumber=${entry?.referenceId}`,
        { diaryEntry: entry }
      );
    }
  };

  if (isDiaryEntriesLoading || generateAdiaryLoader) {
    return <Loader />;
  }
  return (
    <div style={styles.container}>
      <div style={styles.centerPanel}>
        {!isSelectedDataSigned && <div style={styles.title}>{t("SIGN_THE_A_DIARY")}</div>}
        <div style={{ display: "flex", gap: "40px", marginTop: "10px", marginBottom: "20px" }}>
          <div style={{ marginTop: "10px", fontWeight: "bold" }}>{t("A_DIARY_DATED_HEADING")}</div>
          <TextInput
            className="field desktop-w-full"
            key={"entryDate"}
            type={"date"}
            onChange={handleDateChange}
            style={{ paddingRight: "3px" }}
            defaultValue={selectedDate}
            max={new Date().toISOString().split("T")[0]}
          />
          <Button label={t("GO")} variation={"primary"} style={styles.goButton} onButtonClick={handleGoClick} />
        </div>
        {!isSelectedDataSigned ? (
          <React.Fragment>
            <div style={{ display: "flex", alignItems: "right", justifyContent: "space-between", marginBottom: "16px" }}>
              <div style={{ display: "flex", gap: "20px" }}>
                <img
                  style={{ height: "100px" }}
                  src={emblemBigImageLink || "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"}
                  alt="mSeva"
                />
                <div style={{ alignContent: "end" }}>
                  <h2 style={{ fontWeight: "bold", fontSize: "15px", margin: 0 }}>{"ജില്ലാ കോടതി കൊല്ലം"}</h2>
                  <h2 style={{ fontWeight: "bold", fontSize: "25px", margin: 0 }}>{"District Court Kollam"}</h2>
                </div>
              </div>
              <img
                style={{ height: "100px" }}
                src={onCourtsImageLink || "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"}
                alt="mSeva"
              />
            </div>
            <h2 style={{ fontSize: "15px", fontWeight: "bold", textAlign: "center", marginTop: "50px" }}>{t("IN_THE_COURT_NAME")}</h2>
            <hr style={{ border: "1px solid black", width: "100%", margin: "30px 0" }} />
            <h2 style={{ fontSize: "15px", fontWeight: "bold", textAlign: "center", marginTop: "30px" }}>{t("IN_THE_COURT_OF_JUDGE_KOLLAM")}</h2>
            <h2 style={{ fontSize: "15px", fontWeight: "bold", textAlign: "center", margin: "30px 0px" }}>{`${t("A_DIARY_DATED")}: ${formatDate(
              entryDate
            )}`}</h2>
            <table style={{ width: "100%", marginTop: "20px", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ background: "#007E7E", color: "#FFF" }}>
                  <th style={{ padding: "18px", border: "1px solid #000" }}>{t("S_NO")}</th>
                  <th style={{ padding: "18px", border: "1px solid #000" }}>{t("CASE_TYPE_CASE_NUMBER_CASE_YEAR")}</th>
                  <th style={{ padding: "18px", border: "1px solid #000" }}>{t("PROCEEDINGS_OR_BUSINESS_OF_DAY")}</th>
                  <th style={{ padding: "18px", border: "1px solid #000" }}>{t("NEXT_HEARING_DATE")}</th>
                </tr>
              </thead>
              <tbody>
                {diaryEntries?.entries?.map((entry, index) => (
                  <tr key={index} style={{ cursor: "pointer" }} onClick={() => handleRowClick(entry)}>
                    <td style={{ padding: "18px", border: "1px solid #000" }}>{index + 1}</td>
                    <td style={{ padding: "18px", border: "1px solid #000" }}>{entry?.caseNumber}</td>
                    <td style={{ padding: "18px", border: "1px solid #000" }}>{entry?.businessOfDay}</td>
                    <td style={{ padding: "18px", border: "1px solid #000" }}>{entry?.hearingDate ? formatDate(entry?.hearingDate) : ""}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div style={{ display: "flex", justifyContent: "space-between", marginTop: "30px" }}>
              <button onClick={handlePrevious} disabled={offSet === 0} style={{ padding: "8px 12px", cursor: "pointer" }}>
                {t("PREVIOUS")}
              </button>
              <span>
                {diaryEntries?.pagination?.totalCount > 0 ? offSet + 1 : 0} - {Math.min(offSet + limit, diaryEntries?.pagination?.totalCount)} of{" "}
                {diaryEntries?.pagination?.totalCount}
              </span>
              <button
                onClick={handleNext}
                disabled={offSet + limit >= diaryEntries?.pagination?.totalCount}
                style={{ padding: "8px 12px", cursor: "pointer" }}
              >
                {t("NEXT")}
              </button>
            </div>
          </React.Fragment>
        ) : (
          <MemoDocViewerWrapper
            key={ADiarypdf}
            fileStoreId={ADiarypdf}
            tenantId={tenantId}
            docWidth="100%"
            docHeight="70vh"
            showDownloadOption={false}
            documentName={"ADiary"}
          />
        )}
      </div>
      <div style={styles.rightPanel}>
        {
          <div>
            {!isSelectedDataSigned && entryDate !== new Date().setHours(0, 0, 0, 0) && (
              <Button onButtonClick={onSubmit} label={t("ADD_SIGNATURE")} style={{ margin: "20px", maxWidth: "300px", width: "100%" }} />
            )}

            <TasksComponent
              taskType={taskType}
              setTaskType={setTaskType}
              isLitigant={userRoles.includes("CITIZEN")}
              uuid={userInfo?.uuid}
              userInfoType={userInfoType}
              hideFilters={true}
              isDiary={true}
            />
          </div>
        }
      </div>
      <div className="adiary-container">
        {stepper === 1 && (
          <Modal
            headerBarEnd={<CloseBtn onClick={onCancel} />}
            headerBarMain={true}
            popupStyles={{ width: "70vw" }}
            actionCancelLabel={t("CORE_LOGOUT_CANCEL")}
            actionCancelOnSubmit={onCancel}
            actionSaveLabel={t("CS_COMMON_SUBMIT")}
            actionSaveOnSubmit={onSubmit}
            formId="modal-action"
            headerBarMainStyle={{ height: "50px" }}
          >
            <MemoDocViewerWrapper
              key={ADiarypdf}
              fileStoreId={ADiarypdf}
              tenantId={tenantId}
              docWidth="100%"
              docHeight="70vh"
              showDownloadOption={false}
              documentName={"ADiary"}
            />
          </Modal>
        )}
        {stepper === 2 && !openUploadSignatureModal && !isSigned && (
          <Modal
            headerBarMain={<Heading label={t("ADD_SIGNATURE")} />}
            headerBarEnd={<CloseBtn onClick={onCancel} />}
            actionCancelLabel={t("CS_COMMON_BACK")}
            actionCancelOnSubmit={onCancel}
            actionSaveLabel={t("submit")}
            isDisabled={!isSigned}
            actionSaveOnSubmit={uploadSignedPdf}
            className="add-signature-modal"
          >
            <div className="add-signature-main-div">
              <div className="not-signed">
                <h1>{t("YOUR_SIGNATURE")}</h1>
                <div className="sign-button-wrap">
                  <Button
                    label={t("CS_ESIGN")}
                    onButtonClick={() => handleEsign(name, pageModule, ADiarypdf, "Signature")} //as sending null throwing error in esign
                    className="aadhar-sign-in"
                    labelClassName="aadhar-sign-in"
                  />
                  <Button
                    icon={<FileUploadIcon />}
                    label={t("UPLOAD_DIGITAL_SIGN_CERTI")}
                    onButtonClick={() => {
                      setOpenUploadSignatureModal(true);
                    }}
                    className="upload-signature"
                    labelClassName="upload-signature-label"
                  />
                </div>
                <div className="donwload-submission">
                  <h2>{t("want to download Adiary")}</h2>
                  <AuthenticatedLink
                    uri={uri}
                    style={{ color: "#007E7E", cursor: "pointer", textDecoration: "underline" }}
                    displayFilename={"CLICK_HERE"}
                    t={t}
                    pdf={true}
                  />
                </div>
              </div>
            </div>
          </Modal>
        )}

        {stepper === 2 && openUploadSignatureModal && (
          <UploadSignatureModal
            t={t}
            key={name}
            name={name}
            setOpenUploadSignatureModal={setOpenUploadSignatureModal}
            onSelect={onSelect}
            config={uploadModalConfig}
            formData={formData}
          />
        )}

        {stepper === 2 && !openUploadSignatureModal && isSigned && (
          <Modal
            headerBarMain={<Heading label={t("ADD_SIGNATURE")} />}
            headerBarEnd={<CloseBtn onClick={onCancel} />}
            actionCancelLabel={t("CS_COMMON_BACK")}
            actionCancelOnSubmit={onCancel}
            actionSaveLabel={t("submit")}
            actionSaveOnSubmit={uploadSignedPdf}
            className="add-signature-modal"
          >
            <div
              style={{
                padding: "24px",
                display: "flex",
                flexDirection: "column",

                gap: "24px",
              }}
            >
              <InfoCard
                variant={"default"}
                label={t("PLEASE_NOTE")}
                additionalElements={[
                  <p key="note">
                    {t("YOU_ARE_ADDING_YOUR_SIGNATURE_TO_THE")}
                    <span style={{ fontWeight: "bold" }}>{`${t("ADIARY")} - ${formatDate(entryDate)}`}</span>
                  </p>,
                ]}
                inline
                textStyle={{}}
                className={`custom-info-card`}
              />
              <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
                <h1
                  style={{
                    margin: 0,
                    fontFamily: "Roboto",
                    fontSize: "24px",
                    fontWeight: 700,
                    lineHeight: "28.13px",
                    textAlign: "left",
                    color: "#3d3c3c",
                  }}
                >
                  {t("YOUR_SIGNATURE")}
                </h1>
                <h2
                  style={{
                    margin: 0,
                    fontFamily: "Roboto",
                    fontSize: "14px",
                    fontWeight: 400,
                    lineHeight: "16.41px",
                    textAlign: "center",
                    color: "#00703c",
                    padding: "6px",
                    backgroundColor: "#e4f2e4",
                    borderRadius: "999px",
                  }}
                >
                  {t("SIGNED")}
                </h2>
              </div>
            </div>
          </Modal>
        )}
      </div>
    </div>
  );
};

export default ADiaryPage;
