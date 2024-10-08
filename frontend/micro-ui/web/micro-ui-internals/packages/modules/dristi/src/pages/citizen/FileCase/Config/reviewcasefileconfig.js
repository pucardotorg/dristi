export const reviewCaseFileFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "litigentDetails",
        label: "CS_LITIGENT_DETAILS",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "complaintDetails",
              name: "complaintDetails",
              label: "CS_COMPLAINT_DETAILS",
              icon: "ComplainantDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"], badgeType: "complainantType.name" },
                { type: "phonenumber", label: "PHONE_NUMBER", value: "complainantVerification.mobileNumber" },
                {
                  type: "image",
                  label: "CS_ID_PROOF",
                  value: ["individualDetails.document", "companyDetailsUpload.document"],
                },
                { type: "address", label: "ADDRESS", value: "addressDetails" },
              ],
              data: {},
            },
            {
              key: "respondentDetails",
              name: "respondentDetails",
              label: "CS_RESPONDENT_DETAILS",
              icon: "RespondentDetailsIcon",
              config: [
                { type: "title", value: ["respondentFirstName", "respondentLastName"], badgeType: "respondentType.name" },
                { type: "phonenumber", label: "PHONE_NUMBER", value: "phonenumbers.mobileNumber" },
                { type: "text", label: "CS_EMAIL_ID", value: "emails.emailId" },
                { type: "address", label: "ADDRESS", value: "addressDetails" },
              ],
              data: {},
            },
          ],
        },
      },
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "caseSpecificDetails",
        label: "CS_CASE_SPECIFIC_DETAILS",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "chequeDetails",
              name: "chequeDetails",
              label: "CS_CHECKQUE_DETAILS",
              icon: "ChequeDetailsIcon",
              config: [
                { type: "title", label: "CS_CHEQUE_NO", value: "chequeNumber" },
                { type: "amount", label: "CS_CHEQUE_AMOUNT", value: "chequeAmount" },
                { type: "text", label: "CS_CHEQUE_ISSUED_TO", value: "name" },
                { type: "text", label: "CS_PAYER_BANK", value: "payerbank" },
                { type: "text", label: "CS_PAYEE_BANK", value: "bankName" },
                { type: "text", label: "CS_NAME_SIGNATORY", value: "signatory" },
                { type: "text", label: "CS_IFSC_CODE", value: "ifsc" },
                { type: "text", label: "CS_DATE_DEPOSITE", value: "depositDate" },
                { type: "text", label: "CS_DATE_ISSUANCE", value: "issuanceDate" },
                { type: "infoBox", value: "infoBoxData" },
                {
                  type: "image",
                  label: "CS_DOCUMENT",
                  value: ["bouncedChequeFileUpload.document", "depositChequeFileUpload.document", "returnMemoFileUpload.document"],
                },
              ],
              data: {},
            },
            {
              key: "debtLiabilityDetails",
              name: "debtLiabilityDetails",
              label: "CS_DEBT_LIABILITY_DETAILS",
              icon: "DebtLiabilityIcon",
              config: [
                { type: "text", label: "CS_NATURE_OF_DEBT", value: "liabilityNature.name" },
                { type: "text", label: "CS_CHEQUE_RECIEVED_FOR", value: "liabilityType.name" },
                {
                  type: "image",
                  label: "CS_DOCUMENT",
                  value: ["debtLiabilityFileUpload.document"],
                },
              ],
              data: {},
            },
            {
              key: "demandNoticeDetails",
              name: "demandNoticeDetails",
              label: "CS_DEMAND_NOTICE_DETAILS",
              icon: "DemandDetailsNoticeIcon",
              config: [
                { type: "text", label: "CS_MODE_OF_DISPATCH", value: "SelectUserTypeComponent.modeOfDispatchType.name" },
                { type: "text", label: "CS_DISPATCHED_ON", value: "dateOfDispatch" },
                { type: "text", label: "CS_SERVICES_ON", value: "dateOfService" },
                { type: "text", label: "CS_RECIEVED_REPLY_ON", value: "dateOfReply" },
                { type: "text", label: "CS_CAUSE_ACTION_ON", value: "dateOfAccrual" },
                {
                  type: "image",
                  label: "CS_DOCUMENT",
                  value: [
                    "SelectCustomDragDrop.legalDemandNoticeFileUpload",
                    "SelectCustomDragDrop.proofOfAcknowledgmentFileUpload",
                    "SelectCustomDragDrop.proofOfDispatchFileUpload",
                    "SelectCustomDragDrop.proofOfReplyFileUpload",
                  ],
                },
              ],
              data: {},
            },
            {
              key: "delayApplications",
              name: "delayApplications",
              label: "CS_DELAY_CONDONATION_APPLICATION",
              icon: "DemandDetailsNoticeIcon",
              config: [
                { type: "text", label: "CS_QUESTION_DELAY_APPLICATION", value: "delayApplicationType.name" },
                { type: "text", label: "CS_TEXTAREA_HEADER_DELAY_REASON", value: "delayApplicationReason.reasonForDelay" },
                {
                  type: "image",
                  label: "CS_DOCUMENT",
                  value: ["condonationFileUpload.document"],
                },
              ],
              data: {},
            },
          ],
        },
      },
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "additionalDetails",
        label: "CS_ADDITIONAL_DETAILS",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "witnessDetails",
              name: "witnessDetails",
              label: "CS_WITNESS_DETAIL_HEADING",
              icon: "WitnessDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"] },
                { type: "phonenumber", label: "PHONE_NUMBER", value: "phonenumbers.mobileNumber" },
                { type: "text", label: "CS_EMAIL_ID", value: "emails.emailId" },
                { type: "address", label: "ADDRESS", value: "addressDetails" },
              ],
              data: {},
            },
            {
              key: "prayerSwornStatement",
              name: "prayerSwornStatement",
              label: "CS_PRAYER_AND_SWORN_STATEMENT_HEADING",
              icon: "PrayerSwornIcon",
              config: [
                { type: "infoBox", value: "infoBoxData" },
                { type: "text", label: "CS_PRAYER_FOR_RELIEF_HEADER", value: "prayerForRelief.text" },
                {
                  type: "image",
                  label: "CS_DOCUMENT",
                  value: [
                    "SelectCustomDragDrop.swornStatement",
                    "memorandumOfComplaint.document",
                    "prayerForRelief.document",
                    "SelectUploadDocWithName.document",
                  ],
                },
              ],
              data: [
                {
                  data: {
                    prayer: `The Petitioner respectfully requests the Court to:Summon the Respondent to appear before this Court and answer the charges.Find the Respondent guilty of the offence under Section 138 of the Negotiable Instruments Act. Grant an order directing the Respondent to pay the sum of [amount] (cheque amount) along with any accrued interest and legal fees.Grant such other and further relief as this Court deems fit and just in the circumstances.`,
                    addressDetails: {
                      pincode: "500032",
                      state: "Telangana",
                      district: "Rangareddy",
                      city: "Kondapur",
                      coordinates: {
                        longitude: 78.3500765,
                        latitude: 17.4549784,
                      },
                      locality: "F84X+6P6",
                      doorNo: "dfhdghhf",
                    },
                  },
                },
              ],
            },
            {
              key: "advocateDetails",
              name: "advocateDetails",
              label: "CS_ADVOCATE_DETAILS",
              icon: "AdvocateDetailsIcon",
              config: [
                { type: "title", value: "advocateName" },
                { type: "text", label: "CS_BAR_REGISTRATION", value: "barRegistrationNumber" },
                {
                  type: "image",
                  label: "CS_ID_PROOF",
                  value: ["vakalatnamaFileUpload.document"],
                },
              ],
              data: {},
            },
          ],
        },
      },
      {
        key: "scrutinyMessage",
        type: "component",
        withoutLabel: true,
        component: "SelectEmptyComponent",
        populators: {},
      },
    ],
  },
];

export const reviewcasefileconfig = {
  formconfig: reviewCaseFileFormConfig,
  header: "CS_REVIEW_CASE_FILE_HEADING",
  subtext: "CS_REVIEW_CASE_FILE_SUBTEXT",
  className: "review-case-file",
};
