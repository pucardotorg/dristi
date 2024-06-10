export const DebtDummy = [
  {
    isenabled: true,
    data: {
      liabilityNature: {
        code: "SALES",
        name: "Sales Agreement",
        showAmountCovered: true,
        isVerified: true,
        hasBarRegistrationNo: true,
        isEnabled: true,
        apiDetails: {
          serviceName: "/advocate/advocate/v1/_create",
          requestKey: "advocates",
          AdditionalFields: ["barRegistrationNumber"],
        },
      },
      liabilityType: {
        code: "PARTIAL_LIABILITY",
        name: "Partial Liability",
        showAmountCovered: true,
        isVerified: true,
        hasBarRegistrationNo: true,
        isEnabled: true,
        apiDetails: {
          serviceName: "/advocate/advocate/v1/_create",
          requestKey: "advocates",
          AdditionalFields: ["barRegistrationNumber"],
        },
      },
      Terms_Conditions: "342",
      condonationFileUpload: {
        document: [{}],
      },
      delayApplicationReason: {
        undefined: "asdfasdfasdfasdf",
      },
    },
    displayindex: 0,
  },
];
