import get from "lodash/get";
import set from "lodash/set";

//create functions here based on module name set in mdms(eg->SearchProjectConfig)
//how to call these -> Digit?.Customizations?.[masterName]?.[moduleName]
// these functions will act as middlewares
// var Digit = window.Digit || {};

export const UICustomizations = {
  minTodayDateValidation: () => {
    return {
      min: new Date().toISOString().split("T")[0],
    };
  },
  maxTodayDateValidation: () => {
    return {
      max: new Date().toISOString().split("T")[0],
    };
  },

  orderTitleValidation: () => {
    return {
      pattern: /^(\b\w+\b\s*){0,15}$/i,
    };
  },

  alphaNumericValidation: () => {
    return {
      pattern: /[^a-zA-Z0-9\s]/g,
    };
  },

  alphaNumericInputTextValidation: () => {
    return {
      pattern: /^[a-zA-Z0-9 ]+$/i,
    };
  },
  OrderFormSchemaUtils: {
    transformers: {
      mdmsDropdown: {
        formToSchema: (option) => {
          return option?.code;
        },
        schemaToForm: async (value, mdmsConfig) => {
          if (mdmsConfig && mdmsConfig.moduleName && mdmsConfig.masterName) {
            // fetch mdms by criteria
            const mdmsData = await Digit.MDMSService.getDataByCriteria(
              Digit.ULBService.getCurrentTenantId(),
              { details: { moduleDetails: [{ moduleName: mdmsConfig.moduleName, masterDetails: [{ name: mdmsConfig.masterName }] }] } },
              mdmsConfig.moduleName
            );

            const select = mdmsConfig?.select
              ? Digit.Utils.createFunction(mdmsConfig?.select)
              : (data) => {
                  const optionsData = get(data, `${mdmsConfig?.moduleName}.${mdmsConfig?.masterName}`, []);
                  return optionsData
                    .filter((opt) => (opt?.hasOwnProperty("active") ? opt.active : true))
                    .map((opt) => ({ ...opt, name: `${mdmsConfig?.localePrefix}_${Digit.Utils.locale.getTransformedLocale(opt.code)}` }));
                };

            return select(mdmsData).find((option) => option.code === value);
          }
        },
      },
      date: {
        formToSchema: (dateString) => {
          return dateString ? new Date(dateString).getTime() : null;
        },
        schemaToForm: (date) => {
          return date ? new Date(date).toISOString().split("T")[0] : null;
        },
      },
      customDropdown: {
        formToSchema: (optionOrOptions) => {
          if (Array.isArray(optionOrOptions)) {
            return optionOrOptions.map((party) => party.name);
          } else {
            return optionOrOptions?.name;
          }
        },
        schemaToForm: (value) => {
          if (Array.isArray(value)) {
            return value.map((party) => ({ name: party }));
          } else {
            return { name: value };
          }
        },
      },
      summonsOrderPartyName: {
        formToSchema: (value) => {
          try {
            return value.party.data.firstName + " " + value.party.data.lastName;
          } catch (error) {
            console.error("Error in parsing party name", error);
            return;
          }
        },
        schemaToForm: (value) => {
          throw new Error("Not implemented");
        },
      },
      customTextArea: {
        formToSchema: (obj) => {
          return obj?.text || "";
        },
        schemaToForm: (text) => {
          return { text: text || "" };
        },
      },
    },
    formToSchema: (formData, formConfig) => {
      const transformedFormData = {};
      formConfig.forEach((section) => {
        section.body.forEach((field) => {
          const schemaKeyPath = field.schemaKeyPath;
          if (!schemaKeyPath) {
            return;
          }
          if (typeof schemaKeyPath === "string") {
            const transformer = Digit.Customizations.dristiOrders.OrderFormSchemaUtils.transformers[field.transformer]?.formToSchema;
            set(transformedFormData, schemaKeyPath, transformer ? transformer(get(formData, field.key)) : get(formData, field.key));
            return;
          } else if (typeof schemaKeyPath === "object") {
            // the schemaKeyPath is an object with multiple keys with value of {value: value , transformer: transformer} and above needs to be applied for each key
            Object.entries(schemaKeyPath).forEach(([key, value]) => {
              const transformer = Digit.Customizations.dristiOrders.OrderFormSchemaUtils.transformers[value.transformer]?.formToSchema;
              set(
                transformedFormData,
                value.value,
                transformer ? transformer(get(formData, [field.key, key].join("."))) : get(formData, [field.key, key].join("."))
              );
            });
          }
        });
      });
      return transformedFormData;
    },
    schemaToForm: async (schemaData, formConfig) => {
      // reverses the formToSchema transformation
      const transformedSchemaData = {};
      const usedMdmsMasters = {};
      formConfig.forEach((section) => {
        section.body.forEach((field) => {
          if (field.populators?.mdmsConfig) {
            usedMdmsMasters[field.populators.mdmsConfig.moduleName] = usedMdmsMasters[field.populators.mdmsConfig.moduleName] || [];
            usedMdmsMasters[field.populators.mdmsConfig.moduleName].push(field.populators.mdmsConfig.masterName);
          }
        });
      });
      const mdmsValuesFetchPromises = [];
      for (const moduleName in usedMdmsMasters) {
        const promise = await Digit.MDMSService.getDataByCriteria(
          Digit.ULBService.getCurrentTenantId(),
          { details: { moduleDetails: [{ moduleName, masterDetails: usedMdmsMasters[moduleName].map((masterName) => ({ name: masterName })) }] } },
          moduleName
        );
        mdmsValuesFetchPromises.push(promise);
      }
      await Promise.all(mdmsValuesFetchPromises);

      const transformPromises = [];
      formConfig.forEach((section) => {
        section.body.forEach(async (field) => {
          const schemaKeyPath = field.schemaKeyPath || field.key;
          if (!schemaKeyPath || field.populators.hideInForm) {
            return;
          }
          const transformer = Digit.Customizations.dristiOrders.OrderFormSchemaUtils.transformers[field.transformer]?.schemaToForm;

          if (transformer) {
            const transformedValue = await transformer(get(schemaData, schemaKeyPath), field.populators?.mdmsConfig);
            transformPromises.push(transformedValue);
            set(transformedSchemaData, field.key, transformedValue);
          } else {
            set(transformedSchemaData, field.key, get(schemaData, schemaKeyPath));
          }
        });
      });
      await Promise.all(transformPromises);

      return transformedSchemaData;
    },
  },

  ApplicationFormSchemaUtils: {
    transformers: {
      mdmsDropdown: {
        formToSchema: (option) => {
          return option?.code;
        },
        schemaToForm: async (value, mdmsConfig) => {
          if (mdmsConfig && mdmsConfig.moduleName && mdmsConfig.masterName) {
            // fetch mdms by criteria
            const mdmsData = await Digit.MDMSService.getDataByCriteria(
              Digit.ULBService.getCurrentTenantId(),
              { details: { moduleDetails: [{ moduleName: mdmsConfig.moduleName, masterDetails: [{ name: mdmsConfig.masterName }] }] } },
              mdmsConfig.moduleName
            );

            const select = mdmsConfig?.select
              ? Digit.Utils.createFunction(mdmsConfig?.select)
              : (data) => {
                  const optionsData = get(data, `${mdmsConfig?.moduleName}.${mdmsConfig?.masterName}`, []);
                  return optionsData
                    .filter((opt) => (opt?.hasOwnProperty("active") ? opt.active : true))
                    .map((opt) => ({ ...opt, name: `${mdmsConfig?.localePrefix}_${Digit.Utils.locale.getTransformedLocale(opt.code)}` }));
                };

            return select(mdmsData).find((option) => option.code === value);
          }
        },
      },
      date: {
        formToSchema: (dateString) => {
          return dateString ? new Date(dateString).getTime() : null;
        },
        schemaToForm: (date) => {
          return date ? new Date(date).toISOString().split("T")[0] : null;
        },
      },
      customDropdown: {
        formToSchema: (optionOrOptions) => {
          if (Array.isArray(optionOrOptions)) {
            return optionOrOptions.map((party) => party.name);
          } else {
            return optionOrOptions?.name;
          }
        },
        schemaToForm: (value) => {
          if (Array.isArray(value)) {
            return value.map((party) => ({ name: party }));
          } else {
            return { name: value };
          }
        },
      },
      customTextArea: {
        formToSchema: (obj) => {
          return obj?.text || "";
        },
        schemaToForm: (text) => {
          return { text: text || "" };
        },
      },
      applicationDocuments: {
        formToSchema: (obj) => {
          return (
            obj?.submissionDocuments?.map((item) => ({
              fileName: item?.document?.additionalDetails?.name,
              fileStore: item?.document?.fileStore,
              documentType: item?.documentType?.code,
              documentTitle: item?.documentTitle,
            })) || []
          );
        },
        schemaToForm: (arr) => {
          const submissionDocuments =
            arr.map((item) => ({
              document: {
                fileStore: item?.fileStore,
                additionalDetails: { name: item?.fileName },
              },
              documentType: {
                code: item?.documentType,
              },
              documentTitle: item?.documentTitle,
            })) || [];
          return { submissionDocuments };
        },
      },
    },
    formToSchema: (formData, formConfig) => {
      const transformedFormData = {};
      formConfig.forEach((section) => {
        section.body.forEach((field) => {
          const schemaKeyPath = field.schemaKeyPath;
          if (!schemaKeyPath) {
            return;
          }
          if (typeof schemaKeyPath === "string") {
            const transformer = Digit.Customizations.dristiOrders.ApplicationFormSchemaUtils.transformers[field.transformer]?.formToSchema;
            set(transformedFormData, schemaKeyPath, transformer ? transformer(get(formData, field.key)) : get(formData, field.key));
            return;
          } else if (typeof schemaKeyPath === "object") {
            // the schemaKeyPath is an object with multiple keys with value of {value: value , transformer: transformer} and above needs to be applied for each key
            Object.entries(schemaKeyPath).forEach(([key, value]) => {
              const transformer = Digit.Customizations.dristiOrders.ApplicationFormSchemaUtils.transformers[value.transformer]?.formToSchema;
              set(
                transformedFormData,
                value.value,
                transformer ? transformer(get(formData, [field.key, key].join("."))) : get(formData, [field.key, key].join("."))
              );
            });
          }
        });
      });
      return transformedFormData;
    },
    schemaToForm: async (schemaData, formConfig) => {
      // reverses the formToSchema transformation
      const transformedSchemaData = {};
      const usedMdmsMasters = {};
      formConfig.forEach((section) => {
        section.body.forEach((field) => {
          if (field.populators?.mdmsConfig) {
            usedMdmsMasters[field.populators.mdmsConfig.moduleName] = usedMdmsMasters[field.populators.mdmsConfig.moduleName] || [];
            usedMdmsMasters[field.populators.mdmsConfig.moduleName].push(field.populators.mdmsConfig.masterName);
          }
        });
      });
      const mdmsValuesFetchPromises = [];
      for (const moduleName in usedMdmsMasters) {
        const promise = await Digit.MDMSService.getDataByCriteria(
          Digit.ULBService.getCurrentTenantId(),
          { details: { moduleDetails: [{ moduleName, masterDetails: usedMdmsMasters[moduleName].map((masterName) => ({ name: masterName })) }] } },
          moduleName
        );
        mdmsValuesFetchPromises.push(promise);
      }
      await Promise.all(mdmsValuesFetchPromises);

      const transformPromises = [];
      formConfig.forEach((section) => {
        section.body.forEach(async (field) => {
          const schemaKeyPath = field.schemaKeyPath || field.key;
          if (!schemaKeyPath || field.populators.hideInForm) {
            return;
          }
          const transformer = Digit.Customizations.dristiOrders.ApplicationFormSchemaUtils.transformers[field.transformer]?.schemaToForm;

          if (transformer) {
            const transformedValue = await transformer(get(schemaData, schemaKeyPath), field.populators?.mdmsConfig);
            transformPromises.push(transformedValue);
            set(transformedSchemaData, field.key, transformedValue);
          } else {
            set(transformedSchemaData, field.key, get(schemaData, schemaKeyPath));
          }
        });
      });
      await Promise.all(transformPromises);

      return transformedSchemaData;
    },
  },
};
