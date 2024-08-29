import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React, { useState } from "react";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";
import { updateEPostConfig } from "../../configs/EpostFormConfigs";

const EpostUpdateStatus = ({ rowData, form, setForm, setShowDocument, infos, links, setUpdatedData }) => {
  const [currentStatus, setCurrentStatus] = useState(rowData?.original?.deliveryStatus);

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues, index) => {
    if (
      JSON.stringify(formData.currentStatus) !== JSON.stringify(form?.currentStatus) ||
      (formData?.dateOfDelivery && JSON.stringify(formData.dateOfDelivery) !== JSON.stringify(form?.dateOfDelivery))
    ) {
      setCurrentStatus(formData.currentStatus.code);
      setForm((prevForm) => ({
        ...prevForm,
        ...formData,
      }));
    }
  };

  const config = (status) => {
    return updateEPostConfig(status);
  };
  const getOption = (currentStatus) => {
    switch (currentStatus) {
      case "IN_TRANSIT":
        return {
          code: "IN_TRANSIT",
          name: "In Transit",
          isEnabled: true,
        };
      case "DELIVERED":
        return {
          code: "DELIVERED",
          name: "Delivered",
          isEnabled: true,
        };
      case "NOT_DELIVERED":
        return {
          code: "NOT_DELIVERED",
          name: "Not Delivered",
          isEnabled: true,
        };
      default:
        return {
          code: "NOT_UPDATED",
          name: "Not Updated",
          isEnabled: true,
        };
    }
  };

  return (
    <div style={{ padding: "16px", width: "720px" }}>
      <div style={{ marginBottom: "16px" }}>
        <FormComposerV2
          key={"updateEPost"}
          className="form-print-and-summon"
          config={config(currentStatus)}
          onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
            onFormValueChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues);
          }}
          defaultValues={{
            currentStatus: getOption(`${rowData?.original?.deliveryStatus}`),
            dateOfDelivery: `${rowData?.original?.receivedDate}`,
          }}
        />
        {currentStatus === "Delivered" && (
          <div>
            Date of Delivery : <span>{rowData?.original?.receivedDate}</span>
          </div>
        )}
        {currentStatus === "Not Delivered" && (
          <div>
            Date of Delivery Attempted : <span>{rowData?.original?.receivedDate}</span>
          </div>
        )}
      </div>
      <ApplicationInfoComponent infos={infos} links={links} />
    </div>
  );
};

export default EpostUpdateStatus;
