import React, { useEffect, useMemo } from "react";
import SignatureCard from "./SignatureCard";
import { DRISTIService } from "../services";
import isEqual from "lodash/isEqual";
import useSearchCaseService from "../hooks/dristi/useSearchCaseService";

function SelectSignature({ t, config, onSelect, formData = {}, errors }) {
  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          key: "complainantDetails",
          label: "CS_COMPLAINT_DETAILS",
          icon: "LitigentIcon",
          config: [{ type: "title", value: "name" }],
          data: [{ name: "Sheetal Arora" }, { name: "Mehul Das" }],
        },
      ],
    [config?.populators?.inputs]
  );

  const isSignSuccess = useMemo(() => localStorage.getItem("isSignSuccess"), []);
  const storedESignObj = useMemo(() => localStorage.getItem("signStatus"), []);
  const parsedESignObj = JSON.parse(storedESignObj || "{}");
  const storedData = localStorage.getItem("formData");
  const parsedObj = JSON.parse(storedData);
  let allKeys = Object.keys(parsedESignObj);
  const urlParams = new URLSearchParams(window.location.search);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const caseId = urlParams.get("caseId");

  const { data: caseData } = useSearchCaseService(
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
    true,
    true
  );

  function setValue(configkey, value, input) {
    if (Array.isArray(input)) {
      onSelect(configkey, {
        ...formData[configkey],
        ...input.reduce((res, curr) => {
          res[curr] = value[curr];
          return res;
        }, {}),
      });
    } else onSelect(configkey, { ...formData[configkey], [input]: value });
  }

  useEffect(() => {
    if (isSignSuccess === "success") {
      let newobj = structuredClone(parsedObj);
      allKeys.forEach((key) => {
        newobj[key] = { ...parsedObj[key], ...parsedESignObj[key] };
      });

      Object.keys(newobj).forEach((key) => {
        newobj[key] && !isEqual(formData[key], newobj[key]) && setValue(key, newobj[key], Object.keys(newobj[key]));
      });
      localStorage.removeItem("signStatus");
      localStorage.removeItem("isSignSuccess");
      localStorage.removeItem("formdata");
    }
  }, [isSignSuccess, formData]);

  const caseDetails = useMemo(() => caseData?.criteria[0]?.responseList[0], [caseData]);
  const EsignFileStoreID = caseDetails?.additionalDetails?.signedCaseDocument;
  const handleAadharClick = async (data, name) => {
    try {
      localStorage.setItem("signStatus", JSON.stringify({ [config.key]: { [name]: [true] } }));
      localStorage.setItem("formData", JSON.stringify(formData));
      const eSignResponse = await DRISTIService.eSignService({
        ESignParameter: {
          uidToken: "3456565",
          consent: "6564",
          authType: "6546",
          fileStoreId: EsignFileStoreID,
          tenantId: "kl",
          pageModule: "ci",
        },
      });
      if (eSignResponse) {
        const eSignData = {
          path: window.location.pathname,
          param: window.location.search,
          isEsign: true,
          data: data,
        };
        localStorage.setItem("eSignWindowObject", JSON.stringify(eSignData));
        const form = document.createElement("form");
        form.method = "POST";
        form.action = "https://es-staging.cdac.in/esignlevel1/2.1/form/signdoc";
        const eSignRequestInput = document.createElement("input");
        eSignRequestInput.type = "hidden";
        eSignRequestInput.name = "eSignRequest";
        eSignRequestInput.value = eSignResponse?.ESignForm?.eSignRequest;
        const aspTxnIDInput = document.createElement("input");
        aspTxnIDInput.type = "hidden";
        aspTxnIDInput.name = "aspTxnID";
        aspTxnIDInput.value = eSignResponse?.ESignForm?.aspTxnID;
        const contentTypeInput = document.createElement("input");
        contentTypeInput.type = "hidden";
        contentTypeInput.name = "Content-Type";
        contentTypeInput.value = "application/xml";
        form.appendChild(eSignRequestInput);
        form.appendChild(aspTxnIDInput);
        form.appendChild(contentTypeInput);
        document.body.appendChild(form);
        form.submit();
        document.body.removeChild(form);
      }
    } catch (error) {
      console.error("API call failed:", error);
    }
  };

  return (
    <div className="select-signature-main">
      {inputs.map((input, inputIndex) => (
        <React.Fragment>
          <div className="select-signature-header">
            <h1 className="signature-label">{config?.label}</h1>
          </div>
          <div className="select-signature-list">
            {input.data.map((item, itemIndex) => (
              <SignatureCard
                key={inputIndex + itemIndex}
                index={itemIndex}
                data={item}
                input={input}
                t={t}
                formData={formData}
                onSelect={onSelect}
                configKey={config.key}
                handleAadharClick={handleAadharClick}
              />
            ))}
          </div>
        </React.Fragment>
      ))}
    </div>
  );
}

export default SelectSignature;
