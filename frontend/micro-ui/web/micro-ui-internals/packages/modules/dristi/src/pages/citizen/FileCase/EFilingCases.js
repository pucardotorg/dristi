import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Card, CloseSvg, FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { CustomAddIcon, CustomArrowDownIcon, CustomDeleteIcon } from "../../../icons/svgIndex";
import Accordion from "../../../components/Accordion";
import { sideMenuConfig } from "./Config";
import { ReactComponent as InfoIcon } from '../../../icons/info.svg';
import Modal from "../../../components/Modal";
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
  const [{ setFormErrors }, setState] = useState({ setFormErrors: null });

  const formConfig = useMemo(() => {
    return pageConfig?.formconfig;
  }, [pageConfig?.formconfig]);

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

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
    if (!setFormErrors) {
      setState((prev) => ({
        ...prev,
        setFormErrors: setError,
      }));
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
    setParmas({ ...params, [pageConfig.key]: formdata });
    setFormdata([{ isenabled: true, data: {}, displayindex: 0 }]);
    setIsOpen(false);
  };

  const validateData = (data) => {
    let isValid = true;
    formConfig.forEach((config) => {
      config?.body?.forEach((body) => {
        if (body?.type === "component") {
          body?.populators?.inputs?.forEach((input) => {
            if (input?.isMandatory) {
              if (input?.validation) {
                if (input?.validation?.isArray) {
                  if (!formdata?.[body.key]?.[input.name] || formdata?.[body.key]?.[input.name]?.length === 0) {
                    isValid = false;
                    setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
                  } else {
                    setFormErrors(body.key, { [input.name]: "" });
                  }
                } else {
                  if (!formdata?.[body.key]?.[input.name]) {
                    isValid = false;
                    setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
                  } else {
                    setFormErrors(body.key, { [input.name]: "Please Enter the mandatory field" });
                  }
                }
              }
            }
          });
        }
      });
    });
    return isValid;
  };

  const onSubmit = (props) => {
    if (!validateData(props)) {
      return null;
    }
  };
  const onSaveDraft = (props) => {
    setParmas({ ...params, [pageConfig.key]: formdata });
    setFormdata([{ isenabled: true, data: {}, displayindex: 0 }]);
  };

  const [isOpen, setIsOpen] = useState(false);

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
        {isOpen && <Modal
          headerBarEnd={<CloseBtn onClick={() => { setIsOpen(false) }} />}
          hideSubmit={true}
        >
          <div>
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
        </Modal>}



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
        <div className="employee-card-wrapper">
          <div className="header-content">
            <div className="header-details">
              <Header>{t(pageConfig.header)}</Header>
              <div className="header-icon" onClick={() => { setIsOpen(true) }}>
                <CustomArrowDownIcon />
              </div>
            </div>
            <p>{t(pageConfig.subtext || "Please provide the necessary details")}</p>
          </div>
          {modifiedFormConfig.map((config, index) => {
            return formdata[index].isenabled ? (
              <div key={index} className="form-wrapper-d">
                {pageConfig?.addFormText && (
                  <div className="form-item-name">
                    <h1>{`${pageConfig?.formItemName} ${formdata[index]?.displayindex + 1}`}</h1>
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
                  </div>
                )}
                <FormComposerV2
                  label={t("CS_COMMON_CONTINUE")}
                  config={config}
                  onSubmit={onSubmit}
                  onSecondayActionClick={onSaveDraft}
                  defaultValues={{}}
                  onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                    onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index);
                  }}
                  cardStyle={{ minWidth: "100%" }}
                  isDisabled={isDisabled}
                  cardClassName={`e-filing-card-form-style ${pageConfig.className}`}
                  secondaryLabel={t("CS_SAVE_DRAFT")}
                  showSecondaryLabel={true}
                  actionClassName="e-filing-action-bar"
                />
              </div>
            ) : null;
          })}
          {pageConfig?.addFormText && (
            <div
              onClick={handleAddForm}
              className="add-new-form"
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
