const complainantDetailsFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "complainantType",
        label: "CS_RESPONDENT_TYPE",
        isMandatory: true,
        name: "complainantType",
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
            gap: "40px",
            flexDirection: "row",
            alignItems: "center",
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
        isMandatory: true,
        populators: {
          inputs: [
            {
              label: "COMPLAINANT_ID",
              name: "complainantId",
              verificationOn: "complainantVerification.isUserVerified",
            },
          ],
          customStyle: {
            marginTop: 20,
          },
        },
      },
    ],
  },
  {
    dependentKey: { complainantType: ["commonFields"] },
    head: "CS_COMMON_COMPLAINANT_DETAIL",
    body: [
      {
        type: "text",
        label: "FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "firstName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            minLength: 2,
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        populators: {
          name: "middleName",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "LAST_NAME",
        isMandatory: true,
        populators: {
          name: "lastName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            minLength: 2,
            title: "",
            patternType: "Name",
          },
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_COMPANY_DETAIL",
    dependentKey: { complainantType: ["showCompanyDetails"] },
    body: [
      {
        type: "text",
        key: "company_Name",
        label: "company_Name",
        isMandatory: true,
        populators: {
          name: "companyName",
          styles: { minWidth: "100%" },
          labelStyles: { padding: "8px" },
          customStyle: { minWidth: "100%" },
        },
      },
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "companyDetailsUpload",
        populators: {
          inputs: [
            {
              isMandatory: true,
              name: "document",
              documentHeader: "COMPANY_DOCUMENT_DETAILS",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: true,
            },
          ],
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
        disableConfigFields: [
          "complainantType",
          "firstName",
          "middleName",
          "lastName",
          "pincode",
          "locationSearch",
          "pincode",
          "state",
          "district",
          "city",
          "locality",
          'addressDetails'
        ],
        error: "ERR_HRMS_INVALID_MOB_NO",
        componentInFront: "+91",
        disableConfigKey: "isUserVerified",
        isMandatory: true,
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
        addUUID: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_PIN_LOCATION",
              type: "LocationSearch",
              name: ["pincode", "state", "district", "city", "coordinates", "locality", "uuid", ],
              key: "locationSearch",
            },
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
  addFormText: "ADD_COMPLAINANT",
  formItemName: "Complainant",
  className: "complainant",
};
