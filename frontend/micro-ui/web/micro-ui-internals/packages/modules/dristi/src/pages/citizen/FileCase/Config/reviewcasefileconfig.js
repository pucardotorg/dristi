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
        key: "userDetails",
        label: "1. Litigent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "complaintDetails",
              label: "CS_COMPLAINT_DETAILS",
              icon: "ComplainantDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"] },
                { type: "phonenumber", label: "Phone Number", value: "phone" },
                { type: "image", label: "ID Proof", value: "id" },
                { type: "address", label: "Address", value: "addressDetailsSelect" },
              ],
              data: ComplainantDummy,
            },
            {
              key: "respondentDetails",
              label: "CS_RESPONDENT_DETAILS",
              icon: "RespondentDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"] },
                { type: "phonenumber", label: "Phone Number", value: "phonenumbers.mobileNumber" },
                { type: "text", label: "Email", value: "emails.emailId" },
                { type: "address", label: "Address", value: "addressDetails" },
              ],
              data: RespondentDummy,
            },
          ],
        },
      },
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "userDetails",
        label: "2. Case Specific Details",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "chequeDetails",
              label: "CS_CHECKQUE_DETAILS",
              icon: "ChequeDetailsIcon",
              config: [
                { type: "title", value: "chequeNumber" },
                { type: "amount", label: "Cheque Amount", value: "chequeAmount" },
                { type: "text", label: "Cheque Issued to", value: "name" },
                { type: "text", label: "Payer bank", value: "payerbank" },
                { type: "text", label: "Payee bank", value: "bankName" },
                { type: "text", label: "Name of the signatory", value: "signatory" },
                { type: "text", label: "IFSC Code", value: "ifsc" },
                { type: "text", label: "Date of deposit", value: "depositDate" },
                { type: "text", label: "Date of issuance", value: "issuanceDate" },
              ],
              data: CheckqueDummy,
            },
            {
              key: "debtLiabilityDetails",
              label: "CS_DEBT_LIABILITY_DETAILS",
              icon: "DebtLiabilityIcon",
              config: [
                { type: "text", label: "Nature of Debt/Liability", value: "liabilityNature.name" },
                { type: "text", label: "Cheque Recieved for", value: "receivedfor" },
                { type: "image", label: "Documents", value: "document" },
              ],
              data: DebtDummy,
            },
            {
              key: "demandNoticeDetails",
              label: "CS_DEMAND_NOTICE_DETAILS",
              icon: "DemandDetailsNoticeIcon",
              config: [
                { type: "text", label: "Mode of Dispatch", value: "SelectUserTypeComponent.modeOfDispatchType.name" },
                { type: "text", label: "Dispatched on", value: "SelectUserTypeComponent.dateOfDispatch" },
                { type: "text", label: "Serviced on", value: "SelectUserTypeComponent.dateOfService" },
                { type: "text", label: "Recieved reply on", value: "recievedon" },
                { type: "text", label: "Cause of action on", value: "causeon" },
                { type: "image", label: "Documents", value: "document" },
              ],
              data: DemandDummy,
            },
          ],
        },
      },
      {
        type: "component",
        component: "SelectReviewAccordion",
        key: "userDetails",
        label: "3. Additional Details",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "witnessDetails",
              label: "CS_WITNESS_DETAILS",
              icon: "WitnessDetailsIcon",
              config: [
                { type: "title", value: ["firstName", "lastName"] },
                { type: "phonenumber", label: "Phone Number", value: "phonenumbers.mobileNumber" },
                { type: "text", label: "Email ID", value: "emails.emailId" },
                { type: "address", label: "Address", value: "addressDetails" },
              ],
              data: WitnessDummy,
            },
            {
              key: "prayerSwornStatement",
              label: "CS_PRAYER_SWORN_DETAILS",
              icon: "PrayerSwornIcon",
              config: [
                { type: "text", label: "Prayer of Relief", value: "prayer" },
                { type: "address", label: "Address", value: "addressDetails" },
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
              label: "CS_ADVOCATE_DETAILS",
              icon: "AdvocateDetailsIcon",
              config: [
                { type: "title", value: "name" },
                { type: "text", label: "BAR Registraion", value: "bar" },
                { type: "image", label: "ID Proof", value: "id" },
              ],
              data: AdvocateDummy,
            },
          ],
        },
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
