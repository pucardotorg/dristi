import React, { useState } from "react";
import { newConfig } from "./config";
import { FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

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
        if (
          input.isDependentOn &&
          data[curr.body[0].key][input.isDependentOn] &&
          !Boolean(data[curr.body[0].key][input.isDependentOn][input.dependentKey])
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
        userDetails: {
          username: Digit.UserService.getUser()?.info?.userName,
          roles: [
            {
              code: "USER_REGISTER",
              name: "USER_REGISTER",
              description: "USER_REGISTER",
            },
            {
              code: "CITIZEN",
              name: "Citizen",
            },
          ],
          type: Digit.UserService.getUser()?.info?.type,
        },
        mobileNumber: Digit.UserService.getUser()?.info?.mobileNumber,
        address: [
          {
            tenantId: tenantId,
            doorNo: "123",
            latitude: -19,
            longitude: 34,
            city: "Mumbai",
            pincode: "123456",
            district: "Apartment",
            buildingName: "Apartment",
            street: "Necklace Road",
            locality: "",
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
        history.push(`/digit-ui/citizen/dristi/home/response`, "success");
      })
      .catch((err) => {
        console.log(err);
        history.push(`/digit-ui/citizen/dristi/home/response`, "error");
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
