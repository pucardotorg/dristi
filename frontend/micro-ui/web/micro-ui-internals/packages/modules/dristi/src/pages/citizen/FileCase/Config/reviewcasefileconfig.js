import { AdvocateDummy } from "../AdvocateDummy";
import { CheckqueDummy } from "../ChequeDummy";
import { ComplainantDummy } from "../ComplainantDummy";
import { DebtDummy } from "../DebtDummy";
import { DemandDummy } from "../DemandDummy";
import { RespondentDummy } from "../RespondentDummy";
import { WitnessDummy } from "../WitnessDummy";

export const reviewCaseFileFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "litigentDetails",
        label: "CS_LITIGENT_HEADER",
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
                { type: "phonenumber", label: "CS_PHONE_NUMBER", value: "phone" },
                { type: "image", label: "CS_ID_PROOF", value: "id" },
                { type: "address", label: "CS_ADDRESS", value: "addressDetailsSelect" },
              ],
              data: ComplainantDummy,
            },
            {
              key: "respondentDetails",
              name: "respondentDetails",
              label: "CS_RESPONDENT_DETAILS",
              icon: "RespondentDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"], badgeType: ["respondentType.name"] },
                { type: "phonenumber", label: "CS_PHONE_NUMBER", value: "phonenumbers.mobileNumber" },
                { type: "text", label: "CS_EMAIL", value: "emails.emailId" },
                { type: "address", label: "CS_ADDRESS", value: "addressDetails" },
              ],
              data: RespondentDummy,
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
                { type: "title", value: "chequeNumber" },
                { type: "amount", label: "CS_CHEQUE_AMOUNT", value: "chequeAmount" },
                { type: "text", label: "CS_CHEQUE_ISSUED_TO", value: "name" },
                { type: "text", label: "CS_PAYER_BANK", value: "payerbank" },
                { type: "text", label: "CS_PAYEE_BANK", value: "bankName" },
                { type: "text", label: "CS_NAME_SIGNATORY", value: "signatory" },
                { type: "text", label: "CS_IFSC_CODE", value: "ifsc" },
                { type: "text", label: "CS_DATE_DEPOSITE", value: "depositDate" },
                { type: "text", label: "CS_DATE_ISSUANCE", value: "issuanceDate" },
              ],
              data: CheckqueDummy,
            },
            {
              key: "debtLiabilityDetails",
              name: "debtLiabilityDetails",
              label: "CS_DEBT_LIABILITY_DETAILS",
              icon: "DebtLiabilityIcon",
              config: [
                { type: "text", label: "CS_NATURE_OF_DEBT", value: "liabilityNature.name" },
                { type: "text", label: "CS_CHEQUE_RECIEVED_FOR", value: "receivedfor" },
                { type: "image", label: "Documents", value: "document" },
              ],
              data: DebtDummy,
            },
            {
              key: "demandNoticeDetails",
              name: "demandNoticeDetails",
              label: "CS_DEMAND_NOTICE_DETAILS",
              icon: "DemandDetailsNoticeIcon",
              config: [
                { type: "text", label: "CS_MODE_OF_DISPATCH", value: "SelectUserTypeComponent.modeOfDispatchType.name" },
                { type: "text", label: "CS_DISPATCHED_ON", value: "SelectUserTypeComponent.dateOfDispatch" },
                { type: "text", label: "CS_SERVICES_ON", value: "SelectUserTypeComponent.dateOfService" },
                { type: "text", label: "CS_RECIEVED_REPLY_ON", value: "recievedon" },
                { type: "text", label: "CS_CAUSE_ACTION_ON", value: "causeon" },
                { type: "image", label: "CS_DOCUMENT", value: "document" },
              ],
              data: DemandDummy,
            },
          ],
        },
      },
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "additionalDetails",
        label: "CS_ADDITIONAL_DETAILS_HEADER",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "witnessDetails",
              name: "witnessDetails",
              label: "CS_WITNESS_DETAILS",
              icon: "WitnessDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"] },
                { type: "phonenumber", label: "CS_PHONE_NUMBER", value: "phonenumbers.mobileNumber" },
                { type: "text", label: "CS_EMAIL_ID", value: "emails.emailId" },
                { type: "address", label: "CS_ADDRESS", value: "addressDetails" },
              ],
              data: WitnessDummy,
            },
            {
              key: "prayerSwornStatement",
              name: "prayerSwornStatement",
              label: "CS_PRAYER_SWORN_DETAname",
              icon: "PrayerSwornIcon",
              config: [
                { type: "text", label: "CS_PRAYER_RELIEF", value: "prayer" },
                { type: "address", label: "CS_ADDRESS", value: "addressDetails" },
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
                { type: "title", value: "name" },
                { type: "text", label: "CS_BAR_REGISTRATION", value: "bar" },
                { type: "image", label: "CS_ID_PROOF", value: "id" },
              ],
              data: AdvocateDummy,
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
