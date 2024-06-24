const complainantDetailsFormConfig = [
  {
    dependentKey: { complainantVerification: ["isUserVerified"] },
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "personalDetailsNote",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              infoHeader: "CS_PLEASE_COMMON_NOTE",
              infoText: "CS_PLEASE_CONTACT_NYAY_MITRA_TEXT",
              infoTooltipMessage: "CS_NOTE_TOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    head: "SELECT_COMPLAINANT_TYPE",
    body: [
      {
        type: "radio",
        key: "complainantType",
        withoutLabel: true,
        isMandatory: true,
        name: "complainantType",
        populators: {
          label: "SELECT_COMPLAINANT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          options: [
            {
              code: "INDIVIDUAL",
              name: "Individual",
              showCompanyDetails: false,
              complainantLocation: true,
              commonFields: true,
              isEnabled: true,
              isIndividual: true,
            },
            {
              code: "REPRESENTATIVE",
              name: "Entity",
              showCompanyDetails: true,
              isIndividual: false,
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
          name: "complainantId",
          inputs: [
            {
              label: "COMPLAINANT_ID",
              updateLabelOn: "complainantType.showCompanyDetails",
              updateLabel: { key: "label", value: "CS_ENTITY_ID" },
              defaultLabel: { key: "label", value: "COMPLAINANT_ID" },
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
    updateLabelOn: "complainantType.showCompanyDetails",
    updateLabel: { key: "head", value: "CS_COMMON_ENTITY_DETAIL" },
    defaultLabel: { key: "head", value: "CS_COMMON_COMPLAINANT_DETAIL" },
    body: [
      {
        type: "text",
        label: "FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "firstName",
          error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
            },
            minLength: 2,
            // maxLength: 100,
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        labelChildren: "optional",
        populators: {
          name: "middleName",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
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
          error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
            },
            minLength: 2,
            // maxLength: 100,
            title: "",
            patternType: "Name",
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
        component: "VerifyPhoneNumber",
        key: "complainantVerification",
        withoutLabel: true,
        label: "PHONE_NUMBER",
        name: "mobileNumber",
        disableConfigFields: [
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
          "addressDetails",
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
              uploadGuidelines: "UPLOAD_DOC_50",
              fileTypes: ["JPG", "PDF"],
              isMultipleUpload: true,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { complainantType: ["complainantLocation"] },
    head: "CS_COMPLAINANT_LOCATION",
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
              label: "CS_LOCATION",
              type: "LocationSearch",
              name: ["pincode", "state", "district", "city", "coordinates", "locality", "uuid"],
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
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_STATE_INVALID",
                patternType: "Name",
                title: "",
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
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_DISTRICT_INVALID",
                patternType: "Name",
                title: "",
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
  {
    dependentKey: { complainantType: ["showCompanyDetails"] },
    head: "CS_COMPANY_LOCATION",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "addressCompanyDetails",
        addUUID: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_LOCATION",
              type: "LocationSearch",
              name: ["pincode", "state", "district", "city", "coordinates", "locality", "uuid"],
              key: "locationCompanySearch",
            },
            {
              label: "PINCODE",
              type: "text",
              name: "pincode",
              shouldBeEnabled: true,
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
              shouldBeEnabled: true,
              name: "state",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_STATE_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "district",
              shouldBeEnabled: true,
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_DISTRICT_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "CITY/TOWN",
              type: "text",
              name: "city",
              shouldBeEnabled: true,
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
              shouldBeEnabled: true,
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
  subtext: "CS_COMPLAINANT_DETAIL_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_COMPLAINANT",
  formItemName: "CS_COMPLAINANT",
  className: "complainant",
};
