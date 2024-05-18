import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Card, FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CustomAddIcon, CustomDeleteIcon } from "../../../icons/svgIndex";
import Accordion from "../../../components/Accordion";
import { sideMenuConfig } from "./config";
import { ReactComponent as InfoIcon } from '../../../icons/info.svg';

function EFilingCases({ path }) {
  const [params, setParmas] = useState({});
  const Digit = window?.Digit || {};
  const { t } = useTranslation();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [formdata, setFormdata] = useState([{ isenabled: true, data: {}, displayindex: 0 }]);
  const [accordion, setAccordion] = useState(sideMenuConfig);
  const [pageConfig, setPageConfig] = useState(sideMenuConfig?.[0]?.children?.[0]?.pageConfig);

  const formConfig = useMemo(() => {
    return pageConfig?.formconfig;
  }, [pageConfig?.formconfig]);

  const isDependentEnabled = useMemo(() => {
    let result = false;
    formConfig.forEach((config) => {
      if (config?.body && Array.isArray(config?.body)) {
        config?.body?.forEach((bodyItem) => {
          if (bodyItem?.populators?.isDependent) {
            result = true;
          }
        });
      }
    });
    return result;
  }, [formConfig]);

  const modifiedFormConfig = useMemo(() => {
    if (!isDependentEnabled) {
      return formdata.map(() => formConfig);
    }
    return formdata.map(({ data }) => {
      return formConfig.filter((config) => {
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
  }, [isDependentEnabled, formdata, formConfig]);

  const activeForms = useMemo(() => {
    return formdata.filter((item) => item.isenabled === true).length;
  }, [formdata]);

  const handleAddForm = () => {
    setFormdata([...formdata, { isenabled: true, data: {}, displayindex: activeForms }]);
  };

  const handleDeleteForm = (index) => {
    const newArray = formdata.map((item, i) => ({ ...item, isenabled: index === i ? false : item.isenabled, displayindex: i < index ? i : i - 1 }));
    setFormdata(newArray);
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {
    if (JSON.stringify(formData) !== JSON.stringify(formdata[index].data)) {
      setFormdata(
        formdata.map((item, i) => {
          return i === index ? { ...item, data: formData } : item;
        })
      );
    }
  };

  const handleAccordionClick = (index) => {
    setAccordion((prevAccordion) => {
      const newAccordion = prevAccordion.map((parent, pIndex) => ({
        ...parent,
        isOpen: pIndex === index ? !parent.isOpen : false,
      }));
      return newAccordion;
    });
  };

  const handlePageChange = (parentIndex, childIndex) => {
    const newPageConfig = accordion?.[parentIndex]?.children?.[childIndex]?.pageConfig;
    if (!newPageConfig || accordion?.[parentIndex]?.children?.[childIndex].checked) {
      return null;
    }
    setAccordion((prevAccordion) => {
      const newAccordion = prevAccordion.map((parent, pIndex) => ({
        ...parent,
        children: parent.children.map((child, cIndex) => ({
          ...child,
          checked: pIndex === parentIndex && cIndex === childIndex ? 1 : 0,
        })),
      }));
      return newAccordion;
    });
    setPageConfig(newPageConfig);
    setFormdata([{ isenabled: true, data: {}, displayindex: 0 }]);
  };

  return (
    <div className="file-case">
      <div className="file-case-side-stepper">
        <div className="side-stepper-info">
          <div className="header">
            <InfoIcon />
            <span>You are filing a case</span>
          </div>
          <p>
            Under
            <span className="act-name"> S-138, Negotiable Instrument Act</span> In
            <span className="place-name"> Kollam S 138 Special Court</span>
          </p>
        </div>
        <div className="file-case-select-form-section">
          {accordion.map((item, index) => (
            <Accordion
              t={t}
              title={item.title}
              handlePageChange={handlePageChange}
              handleAccordionClick={() => {
                handleAccordionClick(index);
              }}
              key={index}
              children={item.children}
              parentIndex={index}
              isOpen={item.isOpen}
            />
          ))}
        </div>
      </div>

      <div className="file-case-form-section">
        <div className="employee-card-wrapper" style={{ flex: 1, flexDirection: "column", marginLeft: "40px" }}>
          <div className="header-content">
            <Header>{t(pageConfig.header)}</Header>
          </div>
          {modifiedFormConfig.map((config, index) => {
            return formdata[index].isenabled ? (
              <div>
                {pageConfig?.addFormText && (
                  <Card style={{ minWidth: "100%", display: "flex", justifyContent: "space-between", flexDirection: "row", alignItems: "center" }}>
                    <h1>{`${pageConfig?.formItemName} ${formdata[index].displayindex + 1}`}</h1>
                    {(activeForms > 1 || pageConfig?.isOptional) && (
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
                )}
                <FormComposerV2
                  label={t("CS_COMMONS_NEXT")}
                  config={config}
                  onSubmit={(props) => {
                    console.debug("Vaibhav");
                  }}
                  defaultValues={{}}
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index);
                  }}
                  cardStyle={{ minWidth: "100%" }}
                  isDisabled={isDisabled}
                  cardClassName={"e-filing-card-form-style"}
                />
              </div>
            ) : null;
          })}
          {pageConfig?.addFormText && (
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
              <span>{pageConfig.addFormText}</span>
            </div>
          )}

          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </div>
      </div>
    </div>
  );
}

export default EFilingCases;
