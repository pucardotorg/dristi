import { Button, CloseSvg, Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import Modal from "../../../components/Modal";
import DocViewerWrapper from "../docViewerWrapper";
import ViewCaseFile from "../scrutiny/ViewCaseFile";
import { TabSearchconfig } from "./AdmittedCasesConfig";

const fieldStyle = { marginRight: 0 };

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const AdmittedCases = () => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const cnr = searchParams.get("cnr");
  const title = searchParams.get("title");
  console.log(TabSearchconfig?.TabSearchconfig);
  const [show, setShow] = useState(false);
  const [comment, setComment] = useState("");
  const user = localStorage.getItem('user-info');
  const userRoles = JSON.parse(user).roles.map((role) => role.code);
  const [documentSubmission, setDocumentSubmission] = useState();

  const docSetFunc = (docObj) => {
    setDocumentSubmission(docObj);
    setShow(true);
  }
  const configList = useMemo(() => {
    return TabSearchconfig?.TabSearchconfig.map((tabConfig) => {
      return tabConfig.label === 'Parties' ?
        {
          ...tabConfig,
          apiDetails: {
            ...tabConfig.apiDetails,
            requestBody: {
              ...tabConfig.apiDetails.requestBody,
              criteria: [
                {
                  filingNumber: filingNumber,
                },
              ]
            },
          }
        } : tabConfig.label === 'Submissions' ? {
          ...tabConfig,
          sections: {
            ...tabConfig.sections,
            searchResult: {
              ...tabConfig.sections.searchResult,
              uiConfig: {
                ...tabConfig.sections.searchResult.uiConfig,
                columns: tabConfig.sections.searchResult.uiConfig.columns.map((column) => {
                  return column.label === "Document" || column.label === "Submission Name" ? {
                    ...column,
                    clickFunc: docSetFunc
                  } : column
                })
              }
            }
          }
        } : {
          ...tabConfig,
          apiDetails: {
            ...tabConfig.apiDetails,
            requestParam: {
              ...tabConfig.apiDetails?.requestParam,
              filingNumber: filingNumber,
              cnrNumber: cnr,
              applicationNumber: ""
            }
          }
        }
    })
  }, [filingNumber])
  console.log(configList);
  const newTabSearchConfig = {
    ...TabSearchconfig,
    TabSearchconfig: configList
  }
  console.log(newTabSearchConfig);


  console.log(filingNumber);
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const [config, setConfig] = useState(newTabSearchConfig?.TabSearchconfig?.[0]); // initially setting first index config as default from jsonarray
  const [tabData, setTabData] = useState(
    newTabSearchConfig?.TabSearchconfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  ); // setting number of tab component and making first index enable as default
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(newTabSearchConfig?.TabSearchconfig?.[n]); // as per tab number filtering the config
  };

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const Heading = (props) => {
    return (
      <div className="evidence-title">
        <h1 className="heading-m">{props.label}</h1>
        <h3 className="status">{props.status}</h3>
      </div>
    );
  };


  return (
    <React.Fragment>
      <div style={{ display: "flex", gap: "20px" }}>
        <Header styles={{ fontSize: "32px", marginTop: '40px' }}>{t(title)}</Header>
        <Button className={"generate-order"} label={"Generate Order / Notice"} />
      </div>

      <div className="inbox-search-wrapper">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          configs={config}
          defaultValues={defaultValues}
          showTab={true}
          tabData={tabData}
          onTabChange={onTabChange}
        ></InboxSearchComposer>
      </div>
      {tabData.filter((tab) => tab.label === 'Complaints')[0].active && <div style={{ marginTop: '-90px' }}><ViewCaseFile t={t} showHeader={false} /></div>}
      {show && (
        <Modal
          headerBarEnd={<CloseBtn onClick={() => setShow(false)} />}
          actionSaveLabel={
            userRoles.indexOf("APPLICATION_APPROVER") !== -1 &&
              documentSubmission[0].status === "PENDINGREVIEW" ? t('Approve') :
              (userRoles.indexOf("APPLICATION_APPROVER") !== -1 ||
                userRoles.indexOf("DEPOSITION_CREATOR") !== -1 ||
                userRoles.indexOf("DEPOSITION_ESIGN") !== -1 ||
                userRoles.indexOf("DEPOSITION_PUBLISHER") !== -1) &&
                documentSubmission[0].status !== "PENDINGREVIEW" ?
                t("Mark as Evidence") : null
          }
          actionSaveOnSubmit={() => {
            setShow(false);
          }}
          hideSubmit={userRoles.indexOf("APPLICATION_APPROVER") !== -1 && documentSubmission.status === "PENDINGREVIEW"}
          actionCancelLabel={
            userRoles.indexOf("APPLICATION_APPROVER") !== -1
              &&
              documentSubmission[0].status === "PENDINGREVIEW" ?
              t('Reject')
              :
              null
          }
          actionCancelOnSubmit={() => {
            setShow(false);
          }}
          formId="modal-action"
          headerBarMain={<Heading label={t("Document Submission")} status={documentSubmission.status} />}
          className="evidence-modal"
        >
          {documentSubmission.map((docSubmission) =>
            <div className="evidence-modal-main">
              <div className="application-details">
                <div className="application-info">
                  <div className="info-row">
                    <div className="info-key">
                      <h3>Application Type</h3>
                    </div>
                    <div className="info-value">
                      <h3>{docSubmission?.details?.applicationType}</h3>
                    </div>
                  </div>
                  <div className="info-row">
                    <div className="info-key">
                      <h3>Application Sent On</h3>
                    </div>
                    <div className="info-value">
                      <h3>{docSubmission.details.applicationSentOn}</h3>
                    </div>
                  </div>
                  <div className="info-row">
                    <div className="info-key">
                      <h3>Sender</h3>
                    </div>
                    <div className="info-value">
                      <h3>{docSubmission.details.sender}</h3>
                    </div>
                  </div>
                  <div className="info-row">
                    <div className="info-key">
                      <h3>Additional Details</h3>
                    </div>
                    <div className="info-value">
                      <h3>{docSubmission.details.additionalDetails}</h3>
                    </div>
                  </div>
                </div>
                <div className="application-view">
                  <DocViewerWrapper
                    key={docSubmission.applicationContent.fileStoreId}
                    fileStoreId={docSubmission.applicationContent.fileStoreId}
                    displayFilename={docSubmission.applicationContent.fileName}
                    tenantId={docSubmission.applicationContent.tenantId}
                    docWidth="100%"
                    docHeight="unset"
                    showDownloadOption={false}
                    documentName={docSubmission.applicationContent.fileName}
                  />
                </div>
              </div>
              {/* <div className="application-comment">
              <div className="comment-section">
                <h1 className="comment-xyzoo">Comments</h1>
                <div className="comment-main">
                  {docSubmission.comments.map((comment, index) => (
                    <CommentComponent key={index} comment={comment} />
                  ))}
                </div>
              </div>
              <div className="comment-send">
                <div className="comment-input-wrapper">
                  <TextInput
                    placeholder={"Type here..."}
                    value={comment}
                    onChange={(e) => {
                      setComment(e.target.value);
                    }}
                  />
                  <div className="send-comment-btn">
                    <RightArrow />
                  </div>
                </div>
              </div>
            </div> */}
            </div>
          )}
        </Modal>
      )
      }
    </React.Fragment >
  );
};

export default AdmittedCases;
