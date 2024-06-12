import { FormComposerV2, Header, Loader, Toast } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useLocation, Redirect } from "react-router-dom";
import useSearchCaseService from "../../../hooks/dristi/useSearchCaseService";
import { CustomArrowDownIcon } from "../../../icons/svgIndex";
import { reviewCaseFileFormConfig } from "../../citizen/FileCase/Config/reviewcasefileconfig";
import SendCaseBackModal from "../../../components/SendCaseBackModal";

function ViewCaseFile({ t }) {
  const [isDisabled, setIsDisabled] = useState(false);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const caseId = searchParams.get("caseId");
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [formdata, setFormdata] = useState({ isenabled: true, data: {}, displayindex: 0 });
  const [actionModal, setActionModal] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata.data)) {
      setFormdata((prev) => {
        return { ...prev, data: formData };
      });
    }
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const countSectionErrors = (section) => {
    let total = 0;
    let sectionErrors = 0;
    let inputErrors = 0;
    Object.keys(section)?.forEach((key) => {
      if (section[key]) {
        if (section[key]?.scrutinyMessage) {
          total++;
          sectionErrors++;
        }
        section[key]?.form?.forEach((item) => {
          Object.keys(item)?.forEach((field) => {
            if (item[field]?.FSOError) {
              total++;
              inputErrors++;
            }
          });
        });
      }
    });

    return { total, inputErrors, sectionErrors };
  };

  const scrutinyErrors = useMemo(() => {
    const errorCount = {};
    for (const key in formdata?.data) {
      if (typeof formdata.data[key] === "object" && formdata.data[key] !== null) {
        if (!errorCount[key]) {
          errorCount[key] = { total: 0, sectionErrors: 0, inputErrors: 0 };
        }
        const temp = countSectionErrors(formdata.data[key]);
        errorCount[key] = {
          total: errorCount[key].total + temp.total,
          sectionErrors: errorCount[key].sectionErrors + temp.sectionErrors,
          inputErrors: errorCount[key].inputErrors + temp.inputErrors,
        };
      }
    }
    return errorCount;
  }, [formdata]);

  const totalErrors = useMemo(() => {
    let total = 0;
    let sectionErrors = 0;
    let inputErrors = 0;

    for (const key in scrutinyErrors) {
      total += scrutinyErrors[key].total || 0;
      sectionErrors += scrutinyErrors[key].sectionErrors || 0;
      inputErrors += scrutinyErrors[key].inputErrors || 0;
    }

    return {
      total,
      sectionErrors,
      inputErrors,
    };
  }, [scrutinyErrors]);

  const { data: caseFetchResponse, refetch: refetchCaseData, isLoading } = useSearchCaseService(
    {
      criteria: [
        {
          caseId: caseId,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    caseId,
    Boolean(caseId)
  );

  const caseData = useMemo(() => caseFetchResponse?.criteria?.[0]?.responseList?.[0] || null, [caseFetchResponse]);

  const formConfig = useMemo(() => {
    if (!caseData) return null;
    return [
      ...reviewCaseFileFormConfig.map((form) => {
        return {
          ...form,
          body: form.body.map((section) => {
            return {
              ...section,
              populators: {
                ...section.populators,
                inputs: section.populators.inputs?.map((input) => {
                  delete input.data;
                  return {
                    ...input,
                    data: caseData.additionalDetails[input.key]?.formdata,
                  };
                }),
              },
            };
          }),
        };
      }),
    ];
  }, [reviewCaseFileFormConfig, caseData]);

  const onRegisterCase = () => {};
  const onSendCaseBack = () => {
    console.debug(formdata);
    setActionModal(false);
  };

  if (!caseId) {
    return <Redirect to="cases" />;
  }

  if (isLoading) {
    return <Loader />;
  }
  const sidebar = ["litigentDetails", "caseSpecificDetails", "additionalDetails"];
  const labels = { litigentDetails: "CS_LITIGENT_DETAIL", caseSpecificDetails: "CS_CASE_SPECIFIC_DETAIL", additionalDetails: "CS_ADDITIONAL_DETAIL" };
  return (
    <div className="file-case">
      <div className="file-case-side-stepper">
        <div className="file-case-select-form-section">
          {sidebar.map((key, index) => (
            <div key={index} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <div>{labels[key]}</div>
              <div>{scrutinyErrors[key]?.total ? `${scrutinyErrors[key].total} ${t("CS_ERRORS")}` : t("CS_NO_ERRORS")}</div>
            </div>
          ))}
        </div>
      </div>
      <div className="file-case-form-section">
        <div className="employee-card-wrapper">
          <div className="header-content">
            <div className="header-details">
              <Header>{t("Review Case")}</Header>
              <div className="header-icon" onClick={() => {}}>
                <CustomArrowDownIcon />
              </div>
            </div>
          </div>
          <FormComposerV2
            label={t("CS_REGISTER_CASE")}
            config={formConfig}
            onSubmit={() => {
              setActionModal("registerCase");
            }}
            onSecondayActionClick={() => {
              setActionModal("sendCaseBack");
            }}
            defaultValues={{}}
            onFormValueChange={onFormValueChange}
            cardStyle={{ minWidth: "100%" }}
            isDisabled={isDisabled}
            cardClassName={`e-filing-card-form-style review-case-file`}
            secondaryLabel={t("CS_SEND_BACK")}
            showSecondaryLabel={true}
            actionClassName="e-filing-action-bar"
          />

          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
      <div style={{ display: "flex", flexDirection: "column", justifyContent: "flex-end" }}>
        <div>Make Sure you Check For</div>
      </div>
      {actionModal == "sendCaseBack" && (
        <SendCaseBackModal
          t={t}
          totalErrors={totalErrors?.total || 0}
          onCancel={() => {
            setActionModal(false);
          }}
          onSubmit={onSendCaseBack}
          heading={"CS_SEND_CASE_BACK"}
          type="sendCaseBack"
        />
      )}
      {actionModal == "registerCase" && (
        <SendCaseBackModal
          t={t}
          totalErrors={totalErrors?.total || 0}
          onCancel={() => {
            setActionModal(false);
          }}
          onSubmit={onRegisterCase}
          heading={"CS_REGISTER_CASE"}
          type="registerCase"
        />
      )}
    </div>
  );
}

export default ViewCaseFile;
