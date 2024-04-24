import { FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { advocateClerkConfig } from "./config";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

function AdvocateClerkAdditionalDetail({ userTypeDetail, individualId, refetch = () => {} }) {
  console.log("use", userTypeDetail);
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onDocumentUpload = async (fileData) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type };
  };

  const onSubmit = (data) => {
    if (data?.clientDetails?.selectUserType?.apiDetails && data?.clientDetails?.selectUserType?.apiDetails?.serviceName && individualId) {
      onDocumentUpload(data?.clientDetails?.barCouncilId[0][1]?.file).then((document) => {
        const requestBody = {
          [data?.clientDetails?.selectUserType?.apiDetails?.requestKey]: [
            {
              tenantId: tenantId,
              individualId: individualId,
              isActive: false,
              workflow: {
                action: "REGISTER",
                comments: `Applying for ${data?.clientDetails?.selectUserType?.apiDetails?.requestKey} registration`,
                documents: [
                  {
                    id: null,
                    documentType: document.fileType,
                    fileStore: document.file?.files?.[0]?.fileStoreId,
                    documentUid: "",
                    additionalDetails: {},
                  },
                ],
                assignes: [],
                rating: null,
              },
              documents: [
                {
                  id: null,
                  documentType: document.fileType,
                  fileStore: document.file?.files?.[0]?.fileStoreId,
                  documentUid: "",
                  additionalDetails: {},
                },
              ],
              additionalDetails: {
                username:
                  data?.userDetails?.firstName && data?.userDetails?.lastName ? `${data?.userDetails?.firstName} ${data?.userDetails?.lastName}` : "",
              },
              ...data?.clientDetails?.selectUserType?.apiDetails?.AdditionalFields?.reduce((res, curr) => {
                res[curr] = data?.clientDetails[curr];
                return res;
              }, {}),
            },
          ],
        };
        Digit.DRISTIService.advocateClerkService(data?.clientDetails?.selectUserType?.apiDetails?.serviceName, requestBody, tenantId, true, {
          roles: [
            {
              name: "Citizen",
              code: "CITIZEN",
              tenantId: tenantId,
            },
            {
              name: "USER_REGISTER",
              code: "USER_REGISTER",
              tenantId: tenantId,
            },
          ],
        })
          .then(() => {
            history.push(`/digit-ui/citizen/dristi/home/response`, { response: "success" });
          })
          .catch(() => {
            history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
          })
          .finally(() => {
            refetch();
          });
      });
    }
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_ADDITIONAL_DETAILS")}</Header>
      </div>
      <FormComposerV2
        config={advocateClerkConfig}
        t={t}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        label={"CS_COMMON_SUBMIT"}
        headingStyle={{ textAlign: "center" }}
        defaultValues={{ clientDetails: { selectUserType: userTypeDetail } } || {}}
        cardStyle={{ minWidth: "100%" }}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default AdvocateClerkAdditionalDetail;
