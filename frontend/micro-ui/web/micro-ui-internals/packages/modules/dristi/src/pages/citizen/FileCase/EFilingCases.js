import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Card, FormComposerV2, Header, Label, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { respondentconfig } from "./respondentconfig";
import { CustomAddIcon, CustomDeleteIcon } from "../../../icons/svgIndex";
import Accordion from "../../../components/Accordion";
import { complainantDetailsConfig } from "./config";

const accordion = [
  {
    title: "Litigant Details",
    children: [
      { label: "Complainant Details", checked: 0, isCompleted: 0, canAddMultipleForm: true, config: complainantDetailsConfig },
      { label: "Respondent Details", checked: 0, isCompleted: 0, canAddMultipleForm: true, config: respondentconfig },
    ],
  },
  {
    title: "Case Specific Details",
    children: [
      { label: "Cheque Details", checked: 1, isCompleted: 0 },
      { label: "Debt/ Liability Details", checked: 1, isCompleted: 0 },
    ],
    checked: 0,
    isCompleted: 0,
  },
];

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [formDetails, setFormDetails] = useState({});
  const [formdata, setformdata] = useState([{ isenabled: true, data: {}, displayindex: 0 }]);
  const isOptional = false;

  const validateFormData = (data) => {
    let isValid = true;
    complainantDetailsConfig.forEach((curr) => {
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

  const isDependentEnabled = useMemo(() => {
    let result = false;
    complainantDetailsConfig.forEach((config) => {
      if (config?.body && Array.isArray(config?.body)) {
        config?.body?.forEach((bodyItem) => {
          if (bodyItem?.populators?.isDependent) {
            result = true;
          }
        });
      }
    });
    return result;
  }, []);

  const modifiedConfig = useMemo(() => {
    if (!isDependentEnabled) {
      return formdata.map(() => complainantDetailsConfig);
    }
    return formdata.map(({ data }) => {
      return complainantDetailsConfig.filter((config) => {
        const dependentKeys = config?.dependentKey;
        if (!dependentKeys) {
          return config;
        }
        let show = true;
        for (const key in dependentKeys) {
          const nameArray = dependentKeys[key];
          for (const name of nameArray) {
            show = show && Boolean(data?.[key]?.[name]);
          }
        }
        return show && config;
      });
    });
  }, [isDependentEnabled, formdata]);

  const activeForms = useMemo(() => {
    return formdata.filter((item) => item.isenabled === true).length;
  }, [formdata]);

  const handleAddForm = () => {
    setformdata([...formdata, { isenabled: true, data: {}, displayindex: activeForms }]);
  };

  const handleDeleteForm = (index) => {
    const newArray = formdata.map((item, i) => ({ ...item, isenabled: index === i ? false : item.isenabled, displayindex: i < index ? i : i - 1 }));
    setformdata(newArray);
  };

  const onSubmit = (data) => {
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }
    setParmas({ ...params, registrationData: data });
    history.push(`${path}/terms-conditions`);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata[index].data)) {
      setformdata(
        formdata.map((item, i) => {
          return i === index ? { ...item, data: formData } : item;
        })
      );
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "space-between" }}>
      <div className="file-case-select-form-section">
        {accordion.map((item, index) => (
          <Accordion
            t={t}
            count={item.count}
            title={item.title}
            onClick={() => {
              setFormDetails(item);
            }}
            key={index}
            children={item.children}
          />
        ))}
      </div>
      <div className="file-case-form-section">
        <div className="employee-card-wrapper" style={{ flex: 1, flexDirection: "column" }}>
          <div className="e-filing-header-content">
            <Header>{t("COMPLAINANT_DETAILS_HEADER")}</Header>
            <div className="e-filing-header-subtext">{t("COMPLAINANT_DETAILS_SUBTEXT")}</div>
          </div>
          {modifiedConfig.map((formConfig, index) => {
            return formdata[index].isenabled ? (
              <div>
                <Card style={{ minWidth: "100%", display: "flex", justifyContent: "space-between", flexDirection: "row", alignItems: "center" }}>
                  <h1>{`Respondent ${formdata[index].displayindex + 1}`}</h1>
                  {(activeForms > 1 || isOptional) && (
                    <span
                      style={{ cursor: "pointer" }}
                      onClick={() => {
                        handleDeleteForm(index);
                      }}
                    >
                      <CustomDeleteIcon />
                    </span>
                  )}
                </Card>
                <FormComposerV2
                  label={t("CS_COMMONS_NEXT")}
                  config={formConfig}
                  onSubmit={(props) => {
                    // onSubmit(props);
                    console.debug("Vaibhav");
                  }}
                  defaultValues={{}}
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index);
                  }}
                  cardStyle={{ minWidth: "100%" }}
                  isDisabled={isDisabled}
                />
              </div>
            ) : null;
          })}
          <div
            onClick={handleAddForm}
            style={{
              display: "flex",
              cursor: "pointer",
              alignItems: "center",
              justifyContent: "space-around",
              width: "150px",
              color: "#007E7E",
            }}
          >
            <CustomAddIcon />
            {<span>{}</span>}
          </div>

          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
    </div>
  );
}

export default EFilingCases;
