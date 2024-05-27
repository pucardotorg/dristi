const complainantDetailsFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "complainantType",
        label: "CS_RESPONDENT_TYPE",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "INDIVIDUAL",
              name: "Individual",
              showCompanyDetails: false,
              commonFields: true,
              isEnabled: true,
            },
            {
              code: "REPRESENTATIVE",
              name: "Representative of an Entity",
              showCompanyDetails: true,
              commonFields: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
            },
          ],
          customStyle: {
            display: "flex",
            gap: 50,
          },
        },
      },
    ],
  },
  {
    dependentKey: { complainantType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "VerificationComponent",
        key: "complainantId",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "COMPLAINANT_ID",
              name: "complainantId",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { complainantType: ["commonFields"] },
    head: "CS_COMMON_COMPLAINANT_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "userDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              type: "text",
              name: "firstName",
              inputFieldClassName: "user-details-form-style",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "MIDDLE_NAME",
              type: "text",
              name: "middleName",
              inputFieldClassName: "user-details-form-style",
              showOptional: true,
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { complainantType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "VerifyPhoneNumber",
        key: "complainantVerification",
        withoutLabel: true,
        label: "PHONE_NUMBER",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        componentInFront: "+91",
        validation: {
          required: true,
          minLength: 10,
          maxLength: 10,
          pattern: /^[6-9]\d{9}$/,
        },
        populators: {},
      },
    ],
  },
  {
    dependentKey: { complainantType: ["commonFields"] },
    head: "CS_COMMON_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "addressDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            { label: "CS_PIN_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality", "uuid"] },
            {
              label: "PINCODE",
              type: "text",
              name: "pincode",
              inputFieldClassName: "user-details-form-style",
              validation: {
                minlength: 6,
                maxlength: 7,
                patternType: "Pincode",
                pattern: "[0-9]+",
                max: "9999999",
                errMsg: "ADDRESS_PINCODE_INVALID",
                isRequired: true,
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "STATE",
              type: "text",
              name: "state",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "district",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CITY/TOWN",
              type: "text",
              name: "city",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "ADDRESS",
              type: "text",
              name: "locality",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
];

export const complaintdetailconfig = {
  formconfig: complainantDetailsFormConfig,
  header: "CS_COMPLAINT_DETAIL_HEADING",
  subtext: "CS_RESPONDENT_DETAIL_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_Complaint",
  formItemName: "Complainant",
  className: "complainant",
};
