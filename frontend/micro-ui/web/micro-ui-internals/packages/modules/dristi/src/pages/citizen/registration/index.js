import React, { useMemo, useState } from "react";
import { newConfig } from "./config";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const Registration = () => {
  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);

  const validateFormData = (data) => {
    let isValid = true;
    newConfig.forEach((curr) => {
      if (!isValid) return;
      if (!(curr.body[0].key in data) || !data[curr.body[0].key]) {
        isValid = false;
      }
      curr.body[0].populators.inputs.forEach((input) => {
        if (!isValid) return;
        if (Array.isArray(input.name)) return;
        if (input?.isMandatory && !(input.name in data[curr.body[0].key])) {
          isValid = false;
        }
      });
    });
  };

  const onSubmit = (data) => {
    if (!validateFormData(data)) {
      setShowErrorToast(!validateFormData(data));
      return;
    }
    let Individual = {
      Individual: {
        tenantId: tenantId,
        name: {
          givenName: data?.userDetails?.firstName,
          familyName: data?.userDetails?.lastName,
          otherNames: data?.userDetails?.middleName,
        },
        mobileNumber: Digit.UserService.getUser()?.info?.mobileNumber,
        address: [
          {
            tenantId: tenantId,
            doorNo: "123",
            latitude: -19,
            longitude: 34,
            locationAccuracy: 14.27,
            type: "PERMANENT",
            addressLine1: "Apartment",
            addressLine2: "2nd Floor",
            landmark: "Near Juhu Beach",
            city: "Mumbai",
            pincode: "123456",
            district: "Apartment",
            buildingName: "Apartment",
            street: "Necklace Road",
            locality: {
              code: "test_9b31746b933d",
              name: "test_58630a388978",
              label: "test_7b7f928dcab8",
              latitude: "test_15d4a90d15a6",
              longitude: "test_e8854daed039",
              children: [],
            },
          },
        ],
        identifiers: [],
        isSystemUser: true,
        skills: [],
        additionalFields: {},
        clientAuditDetails: {},
        auditDetails: {},
      },
    };
    Digit.DRISTIService.postIndividualService(Individual, tenantId)
      .then((result, err) => {
        let getdata = { ...data, get: result };
        console.log(getdata);
        history.push(`/digit-ui/${defaultLandingUrl}/dristi/home/response`, "success");
      })
      .catch((err) => {
        console.log(err);
        history.push(`/digit-ui/${defaultLandingUrl}/dristi/home/response`, "error");
      });
  };

  const closeToast = () => {
    setShowErrorToast(false);
  };

  return (
    <div className="employee-card-wrapper">
      <div className="header-content">
        <Header>{t("CS_COMMON_REGISTRATION_DETAIL")}</Header>
      </div>
      <FormComposerV2
        label={t("CS_COMMON_SUBMIT")}
        config={newConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        onSubmit={(props) => {
          onSubmit(props);
        }}
        cardStyle={{ minWidth: "100%" }}
      />
      {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default Registration;
