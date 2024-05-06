import { FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { advocateClerkConfig } from "./config";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

function AdvocateClerkAdditionalDetail({ userTypeDetail, individualId, individualUser = "", refetch = () => {} }) {
  const { t } = useTranslation();
  const Digit = window.Digit || {};
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onDocumentUpload = async (fileData) => {
    const fileUploadRes = await Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type };
  };

  const validateFormData = (data) => {
    let isValid = true;
    advocateClerkConfig.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (
          input.isDependentOn &&
          data[curr.body[0].key][input.isDependentOn] &&
          !Boolean(
            input.dependentKey[input.isDependentOn].reduce((res, current) => {
              if (!res) return res;
              res = data[curr.body[0].key][input.isDependentOn][current];
              if (
                Array.isArray(data[curr.body[0].key][input.isDependentOn][current]) &&
                data[curr.body[0].key][input.isDependentOn][current].length === 0
              ) {
                res = false;
              }
              return res;
            }, true)
          )
        ) {
          return;
        }
        if (Array.isArray(data[curr.body[0].key][input.name]) && data[curr.body[0].key][input.name].length === 0) {
          isValid = false;
        }
        if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
          isValid = false;
        }
      });
    });
    return isValid;
  };
  const onFormValueChange = (setValue, formData, formState) => {
    let isDisabled = false;
    advocateClerkConfig.forEach((curr) => {
      if (isDisabled) return;
      if (!(curr.body[0].key in formData) || !formData[curr.body[0].key]) {
        return;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (isDisabled) return;
        if (Array.isArray(input.name)) return;
        if (
          formData[curr.body[0].key][input.name] &&
          formData[curr.body[0].key][input.name].length > 0 &&
          !["documentUpload", "radioButton"].includes(input.type) &&
          input.validation &&
          !formData[curr.body[0].key][input.name].match(Digit.Utils.getPattern(input.validation.patternType) || input.validation.pattern)
        ) {
          isDisabled = true;
        }
        if (Array.isArray(formData[curr.body[0].key][input.name]) && formData[curr.body[0].key][input.name].length === 0) {
          isDisabled = true;
        }
      });
    });
    if (isDisabled) {
      setIsDisabled(isDisabled);
    } else {
      setIsDisabled(false);
    }
    if (formState?.submitCount) {
      setIsDisabled(true);
    }
  };

  const onSubmit = (data) => {
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }

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
                username: individualUser ? individualUser : "",
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
          ],
        })
          .then(() => {
            refetch().then(() => {
              history.push(`/digit-ui/citizen/dristi/home`);
            });
          })
          .catch(() => {
            history.push(`/digit-ui/citizen/dristi/home/response`, { response: "error" });
          })
          .finally(() => {
            refetch();
            setIsDisabled(false);
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
        isDisabled={isDisabled}
        label={"CS_COMMON_SUBMIT"}
        headingStyle={{ textAlign: "center" }}
        defaultValues={{ clientDetails: { selectUserType: userTypeDetail } } || {}}
        cardStyle={{ minWidth: "100%" }}
        onFormValueChange={onFormValueChange}
      ></FormComposerV2>
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
}

export default AdvocateClerkAdditionalDetail;
