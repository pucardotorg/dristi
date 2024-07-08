export const ComplainantDummy = [
  {
    isenabled: true,
    data: {
      complainantType: {
        code: "INDIVIDUAL",
        name: "Individual",
        showCompanyDetails: false,
        commonFields: true,
        isEnabled: true,
      },
      "addressDetails-select": {
        pincode: "500084",
        state: "Telangana",
        district: "Rangareddy",
        city: "Kothaguda",
        locality: "Whitefields",
      },
      firstName: "Suresh",
      lastName: "Soren",
    },
    displayindex: 0,
  },
  {
    isenabled: true,
    data: {
      complainantType: {
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
      addressDetailsSelect: {
        pincode: "500032",
        state: "Telangana",
        district: "Rangareddy",
        city: "Hyderabad",
        locality: "Gachibowli, P Janardhan Reddy Nagar",
      },
      firstName: "Krishna",
      middleName: "",
      lastName: "Sanidhi",
    },
    displayindex: 1,
  },
];
