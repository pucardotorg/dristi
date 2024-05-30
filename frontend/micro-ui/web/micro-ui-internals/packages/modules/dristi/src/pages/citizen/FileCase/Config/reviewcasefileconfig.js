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
                { type: "title", value: "name" },
                { type: "phonenumber", label: "Phone Number", value: "phone" },
                { type: "image", label: "ID Proof", value: "id" },
                { type: "address", label: "Address", value: "address" },
              ],
              data: [{ name: "Sheetal Arora", phone: "9834178901", id: "uri", address: "4601E, Gatade Plot, Pandharpur" }],
            },
            {
              key: "respondentDetails",
              label: "CS_RESPONDENT_DETAILS",
              icon: "RespondentDetailsIcon",
              config: [
                { type: "title", value: "name" },
                { type: "phonenumber", label: "Phone Number", value: "phone" },
                { type: "image", label: "ID Proof", value: "id" },
                { type: "address", label: "Address", value: "address" },
              ],
              data: [{ name: "N. S. Prasad", phone: "9894178901", id: "uri", address: "4601E, Gatade Plot, Pandharpur" }],
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
                { type: "title", value: "checkno" },
                { type: "amount", label: "Cheque Amount", value: "amount" },
                { type: "text", label: "Checque Issued to", value: "issuedto" },
                { type: "text", label: "Payer bank", value: "payerbank" },
                { type: "text", label: "Payee bank", value: "payeebank" },
                { type: "text", label: "Name of the signatory", value: "signatory" },
                { type: "text", label: "IFSC Code", value: "ifsc" },
                { type: "text", label: "Date of deposit", value: "depositdate" },
                { type: "text", label: "Date of issuance", value: "issuancedate" },
              ],
              data: [
                {
                  checkno: "Cheque No. 134582",
                  payerbank: "HDFC Bank",
                  issuedto: "Aparna Subramhanyam",
                  payeebank: "State Bank of India",
                  amount: "50000",
                  signatory: "Name of the signatory",
                  ifsc: "ABC12345",
                  depositdate: "10/02/2024",
                  issuancedate: "10/02/2024",
                },
                {
                  checkno: "Cheque No. 134582",
                  payerbank: "HDFC Bank",
                  issuedto: "Aparna Subramhanyam",
                  payeebank: "State Bank of India",
                  amount: "50000",
                  signatory: "Name of the signatory",
                  ifsc: "ABC12345",
                  depositdate: "10/02/2024",
                  issuancedate: "10/02/2024",
                },
              ],
            },
            {
              key: "debtLiabilityDetails",
              label: "CS_DEBT_LIABILITY_DETAILS",
              icon: "DebtLiabilityIcon",
              config: [
                { type: "text", label: "Nature of Debt/Liability", value: "nature" },
                { type: "text", label: "Cheque Recieved for", value: "receivedfor" },
                { type: "image", label: "Documents", value: "document" },
              ],
              data: [{ nature: "Loan", receivedfor: "Partial Liability", document: "ui" }],
            },
            {
              key: "demandNoticeDetails",
              label: "CS_DEMAND_NOTICE_DETAILS",
              icon: "DemandDetailsNoticeIcon",
              config: [
                { type: "text", label: "Mode of Dispatch", value: "dispatch" },
                { type: "text", label: "Dispatched on", value: "dispatchon" },
                { type: "text", label: "Serviced on", value: "servicedon" },
                { type: "text", label: "Recieved reply on", value: "recievedon" },
                { type: "text", label: "Cause of action on", value: "causeon" },
                { type: "image", label: "Documents", value: "document" },
              ],
              data: [{ dispatch: "Post", dispatchon: "02/01/2024", servicedon: "03/01/2024", causeon: "21/01/2024" }],
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
                { type: "title", value: "name" },
                { type: "phonenumber", label: "Phone Number", value: "phone" },
                { type: "text", label: "Email ID", value: "email" },
                { type: "address", label: "Address", value: "address" },
              ],
              data: [
                { name: "Sheetal Arora", phone: "9834178901", id: "uri", address: "4601E, Gatade Plot, Pandharpur", email: "vaibhav.c@gmail.com" },
              ],
            },
            {
              key: "prayerSwornStatement",
              label: "CS_PRAYER_SWORN_DETAILS",
              icon: "PrayerSwornIcon",
              config: [
                { type: "text", label: "Prayer of Relief", value: "prayer" },
                { type: "address", label: "Address", value: "address" },
              ],
              data: [
                {
                  prayer: `The Petitioner respectfully requests the Court to:Summon the Respondent to appear before this Court and answer the charges.Find the Respondent guilty of the offence under Section 138 of the Negotiable Instruments Act. Grant an order directing the Respondent to pay the sum of [amount] (cheque amount) along with any accrued interest and legal fees.Grant such other and further relief as this Court deems fit and just in the circumstances.`,
                  adress: "1469 feakl jfdsakl jkl asdj f",
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
              data: [{ name: "Soumya Arora", bar: "9834178901", id: "uri" }],
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
