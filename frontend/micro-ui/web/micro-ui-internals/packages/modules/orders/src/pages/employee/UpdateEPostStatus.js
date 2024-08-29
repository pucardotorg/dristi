import React, { useMemo, useState } from "react";
import { Modal } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { updateEPostConfig } from "../../configs/EpostFormConfigs";
import { EpostService } from "../../hooks/services";
import { useHistory } from "react-router-dom";
import ApplicationInfoComponent from "../../components/ApplicationInfoComponent";

const UpdateEPostStatus = ({ onClose, rowData, form, setForm, setShowDocument }) => {
  const { t } = useTranslation();
  const [currentStatus, setCurrentStatus] = useState(rowData?.original?.deliveryStatus);
  const history = useHistory();
  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}/${path}`);
  };

  const config = (status) => {
    return updateEPostConfig(status);
  };

  const Close = () => (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFFFF" style={{ width: "24px", height: "24px" }}>
      <path d="M0 0h24v24H0V0z" fill="none" />
      <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
    </svg>
  );

  const CloseBtn = ({ onClick, isMobileView }) => (
    <div onClick={onClick} style={isMobileView ? { padding: 5 } : null}>
      <div style={{ backgroundColor: "#505A5F", display: "flex", justifyContent: "center", alignItems: "center", padding: "8px" }}>
        <Close />
      </div>
    </div>
  );

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

  const clickableTextStyle = {
    color: "#007bff",
    textDecoration: "underline",
    cursor: "pointer",
    fontSize: "1rem",
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

  const keyMapping = {
    barCode: "trackingNumber",
    dateofBooking: "bookingDate",
    currentStatus: "deliveryStatus",
    dateOfDelivery: "receivedDate",
  };

  const [updatedData, setUpdatedData] = useState(rowData.original);
  const updateFunction = () => {
    let data = updatedData;
    for (const formKey in keyMapping) {
      const updatedDataKey = keyMapping[formKey];

      if (formKey === "currentStatus") {
        data[updatedDataKey] = form[formKey].code;
      } else if (form[formKey]) {
        data[updatedDataKey] = form[formKey];
      }
    }

    setUpdatedData(data);
  };

  const onUpdateClick = async () => {
    updateFunction();
    const requestBody = {
      Individual: {
        tenantId: Digit.ULBService.getCurrentTenantId(),
      },
      EPostTracker: updatedData,
    };
    try {
      const data = await EpostService.EpostUpdate(requestBody, {});
      console.log("updated successfully");
      handleNavigate("employee/orders/tracking");
    } catch (error) {
      console.log("error updating Status");
    }
  };

  const infos = useMemo(() => {
    return [
      { key: "E-post fees", value: "Rs.575" },
      { key: "Received on", value: "04/07/2024, 12:56" },
      { key: "Bar Code", value: "1234567890" },
      { key: "Date of Booking", value: "04/07/2024" },
    ];
  }, []);

  const links = useMemo(() => {
    return [
      { text: "View Details", link: "", onClick: () => {} },
      {
        text: "View Document",
        link: "",
        onClick: () => {
          setShowDocument(true);
        },
      },
    ];
  }, []);

  return (
    <Modal
      popupStyles={
        {
          // height: "auto",
          // maxHeight: "700px",
          // width: "700px",
          // position: "absolute",
          // top: "50%",
          // left: "50%",
          // transform: "translate(-50%, -50%)",
          // padding: "20px",
        }
      }
      headerBarMain={<h1 style={{ fontSize: "1.25rem", fontWeight: "500" }}>{t("Update E-post Status")}</h1>}
      headerBarEnd={<CloseBtn onClick={onClose} />}
      actionCancelLabel={rowData?.original?.receivedDate ? null : t("View Document")}
      actionCancelOnSubmit={() => setShowDocument(true)}
      actionSaveLabel={t("Update Status")}
      actionSaveOnSubmit={onUpdateClick}
      isDisabled={rowData.original.deliveryStatus === "DELIVERED" || rowData.original.deliveryStatus === "NOT_DELIVERED" ? true : false}
    >
      <div style={{ padding: "16px" }}>
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
        {/* <div style={{ marginBottom: "16px", backgroundColor: "#f8f9fa", padding: "16px", borderRadius: "8px" }}>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px" }}>
            <strong>{t("E-post fees")}</strong>
            <span>₹575</span>
            <span style={clickableTextStyle} onClick={() => console.log("View Details clicked")}>
              {t("View Details")}
            </span>
          </div>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px" }}>
            <strong>{t("Received on")}</strong>
            <span>04/07/2024, 12:56</span>
            <span style={clickableTextStyle} onClick={() => setShowDocument(true)}>
              {t("View Document")}
            </span>
          </div>
          <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px" }}>
            <strong>{t("Bar Code")}</strong>
            <span>{rowData?.original?.trackingNumber}</span>
          </div>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <strong>{t("Date of Booking")}</strong>
            <span>{rowData?.original?.bookingDate}</span>
          </div>
        </div> */}
      </div>
    </Modal>
  );
};

export default UpdateEPostStatus;
