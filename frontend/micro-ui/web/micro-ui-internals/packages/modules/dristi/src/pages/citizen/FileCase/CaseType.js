import { Loader } from "@egovernments/digit-ui-components";
import { CitizenInfoLabel, CloseSvg } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useHistory, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import Button from "../../../components/Button";
import CustomDetailsCard from "../../../components/CustomDetailsCard";
import Modal from "../../../components/Modal";
import { FileDownloadIcon } from "../../../icons/svgIndex";
import { DRISTIService } from "../../../services";
import { userTypeOptions } from "../registration/config";
import SelectCustomNote from "../../../components/SelectCustomNote";
import _ from "lodash";

const customNoteConfig = {
  populators: {
    inputs: [
      {
        infoHeader: "CS_COMMON_NOTE",
        infoText: "ES_BANNER_LABEL",
        infoTooltipMessage: "CS_NOTE_TOOLTIP_CASE_TYPE",
      },
    ],
  },
};

export const formatDate = (date) => {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
};

function CaseType({ t }) {
  const { path } = useRouteMatch();
  const history = useHistory();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [page, setPage] = useState(0);
  const [isDisabled, setIsDisabled] = useState(false);
  const onCancel = () => {
    history.push("/digit-ui/citizen/home/home-pending-task");
  };
  const onSelect = () => {
    setPage(1);
  };
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const Submitbar = () => {
    const token = window.localStorage.getItem("token");
    const isUserLoggedIn = Boolean(token);
    const moduleCode = "DRISTI";
    const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
    const roles = userInfo?.roles;
    const { data: individualData, isLoading, refetch, isFetching } = window?.Digit.Hooks.dristi.useGetIndividualUser(
      {
        Individual: {
          userUuid: [userInfo?.uuid],
        },
      },
      { tenantId, limit: 1000, offset: 0 },
      moduleCode,
      "",
      userInfo?.uuid && isUserLoggedIn
    );
    const individualId = individualData?.Individual?.[0]?.individualId;

    const addressLine1 = individualData?.Individual?.[0]?.address[0]?.addressLine1 || "Telangana";
    const addressLine2 = individualData?.Individual?.[0]?.address[0]?.addressLine2 || "Rangareddy";
    const buildingName = individualData?.Individual?.[0]?.address[0]?.buildingName || "";
    const street = individualData?.Individual?.[0]?.address[0]?.street || "";
    const city = individualData?.Individual?.[0]?.address[0]?.city || "";
    const pincode = individualData?.Individual?.[0]?.address[0]?.pincode || "";
    const latitude = individualData?.Individual?.[0]?.address[0]?.latitude || "";
    const longitude = individualData?.Individual?.[0]?.address[0]?.longitude || "";
    const doorNo = individualData?.Individual?.[0]?.address[0]?.doorNo || "";
    const idType = individualData?.Individual?.[0]?.identifiers[0]?.identifierType || "";
    const identifierIdDetails = JSON.parse(
      individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "identifierIdDetails")?.value || "{}"
    );
    const address = `${doorNo ? doorNo + "," : ""} ${buildingName ? buildingName + "," : ""} ${street}`.trim();

    const givenName = individualData?.Individual?.[0]?.name?.givenName || "";
    const otherNames = individualData?.Individual?.[0]?.name?.otherNames || "";
    const familyName = individualData?.Individual?.[0]?.name?.familyName || "";

    const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
      individualData?.Individual,
    ]);
    const { data: searchData, isLoading: isSearchLoading } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
      {
        criteria: [{ individualId }],
        tenantId,
      },
      {},
      individualId,
      userType,
      "/advocate/advocate/v1/_search"
    );

    if (userType === "ADVOCATE" && searchData) {
      const advocateBarRegNumber = searchData?.advocates?.[0]?.responseList?.[0]?.barRegistrationNumber;
      if (advocateBarRegNumber) {
        window?.Digit.SessionStorage.set("isAdvocateAndApproved", true);
      } else {
        window?.Digit.SessionStorage.set("isAdvocateAndApproved", false);
      }
    }

    const userTypeDetail = useMemo(() => {
      return userTypeOptions.find((item) => item.code === userType) || {};
    }, [userType]);

    const searchResult = useMemo(() => {
      return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`];
    }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

    const advocateId = useMemo(() => {
      return searchResult?.[0]?.responseList?.[0]?.id;
    }, [searchResult]);

    const { isLoading: mdmsLoading, data: statuteData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "case", [{ name: "Statute" }], {
      select: (data) => {
        const optionsData = _.get(data, `${"case"}.${"Statute"}`, []);
        return optionsData.filter((opt) => (opt?.hasOwnProperty("active") ? opt.active : true)).map((opt) => ({ ...opt }));
      },
    });

    if (isLoading || isFetching || isSearchLoading || mdmsLoading) {
      return <Loader />;
    }
    return (
      <div className="submit-bar-div">
        <Button icon={<FileDownloadIcon />} className="download-button" label={t("CS_COMMON_DOWNLOAD")} />
        <div className="right-div">
          <Button
            className="cancel-button"
            label={t("CS_COMMON_BACK")}
            onButtonClick={() => {
              setPage(0);
            }}
          />
          <Button
            className="start-filling-button"
            label={t("CS_START_FILLING")}
            isDisabled={isDisabled}
            onButtonClick={() => {
              setIsDisabled(true);
              const cases = {
                tenantId,
                resolutionMechanism: "COURT",
                caseDescription: "Case description",
                linkedCases: [],
                filingDate: formatDate(new Date()),
                caseDetails: {},
                caseCategory: "CRIMINAL",
                statutesAndSections: [
                  {
                    tenantId,
                    statute: statuteData?.name,
                    sections: ["Negotiable Instrument Act", "02."],
                    subsections: ["138", "03."],
                  },
                ],
                litigants: [],
                representatives: advocateId
                  ? [
                      {
                        advocateId: advocateId,
                        tenantId,
                        representing: [],
                      },
                    ]
                  : [],
                documents: [],
                workflow: {
                  action: "SAVE_DRAFT",
                  comments: null,
                  assignes: null,
                  documents: [
                    {
                      documentType: null,
                      fileStore: null,
                      documentUid: null,
                      additionalDetails: null,
                    },
                  ],
                },
                additionalDetails: {
                  payerMobileNo: individualData?.Individual?.[0]?.mobileNumber,
                  payerName: `${givenName} ${familyName}`,
                  ...(advocateId
                    ? {
                        advocateDetails: {
                          formdata: [
                            {
                              isenabled: true,
                              displayindex: 0,
                              data: {
                                isAdvocateRepresenting: {
                                  code: "YES",
                                  name: "Yes",
                                  showForm: true,
                                  isEnabled: true,
                                },
                              },
                            },
                          ],
                        },
                      }
                    : {
                        complainantDetails: {
                          formdata: [
                            {
                              isenabled: true,
                              data: {
                                complainantType: {
                                  code: "INDIVIDUAL",
                                  name: "Individual",
                                  showCompanyDetails: false,
                                  complainantLocation: true,
                                  commonFields: true,
                                  isEnabled: true,
                                  isIndividual: true,
                                },
                                "addressDetails-select": {
                                  pincode: pincode,
                                  district: addressLine2,
                                  city: city,
                                  state: addressLine1,
                                  locality: address,
                                },
                                complainantId: { complainantId: true },
                                firstName: givenName,
                                middleName: otherNames,
                                lastName: familyName,
                                complainantVerification: {
                                  mobileNumber: userInfo?.userName,
                                  otpNumber: "123456",
                                  individualDetails: {
                                    individualId: individualId,
                                    document: identifierIdDetails?.fileStoreId
                                      ? [{ name: idType, fileStore: identifierIdDetails?.fileStoreId, documentName: identifierIdDetails?.filename }]
                                      : null,
                                    "addressDetails-select": {
                                      pincode: pincode,
                                      district: addressLine2,
                                      city: city,
                                      state: addressLine1,
                                      locality: address,
                                    },
                                    addressDetails: {
                                      pincode: pincode,
                                      district: addressLine2,
                                      city: city,
                                      state: addressLine1,
                                      coordinates: {
                                        longitude: latitude,
                                        latitude: longitude,
                                      },
                                      locality: address,
                                    },
                                  },
                                  isUserVerified: true,
                                },
                                addressDetails: {
                                  pincode: pincode,
                                  district: addressLine2,
                                  city: city,
                                  state: addressLine1,
                                  coordinates: {
                                    longitude: latitude,
                                    latitude: longitude,
                                  },
                                  locality: address,
                                },
                              },
                              displayindex: 0,
                            },
                          ],
                        },
                      }),
                },
              };
              DRISTIService.caseCreateService({ cases, tenantId })
                .then((res) => {
                  history.push(`${path}/case?caseId=${res?.cases[0]?.id}`);
                })
                .finally(() => setIsDisabled(false));
            }}
          />
        </div>
      </div>
    );
  };

  const detailsCardList = useMemo(() => {
    const caseTypeDetails = [
      { header: "Case Category", subtext: "Criminal" },
      {
        header: "Status / Act",
        subtext: "Negotiable Instrument Act",
      },
      { header: "Section", subtext: "138" },
    ];
    const listDocumentDetails = [
      {
        header: "Proof of Identity",
        subtext: "PAN Card, Aadhar card, Passport, Driving license, Voter ID, Ration card or Bank passbook",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "01.",
      },
      {
        header: "Bounced Cheque",
        subtext: "A copy of the bounced chequeon the  basis which this case is being filed",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "02.",
      },
      {
        header: "Cheque Return Memo",
        subtext: "The document received from the bank that has the information that the cheque has bounced",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "03.",
      },
      {
        header: "Proof of Debt/ Liability",
        subtext: "Anything to prove some sort of agreement between you and the respondent",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "04.",
      },
      {
        header: "Legal Demand Notice",
        subtext:
          "Any intimation you provided to the respondent to informing them that their cheque had bounced and they still owed you the cheque amount",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "05",
      },
      {
        header: "Notarised Affidavit",
        subtext:
          "This is a replacement for your sworn statement which reduces the chances that an admission hearing is needed for the court to take cognisance of your case.",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "06",
      },
    ];
    return page === 0 ? caseTypeDetails : listDocumentDetails;
  }, [page]);

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={page === 0 ? t("CORE_LOGOUT_CANCEL") : null}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_CORE_WEB_PROCEED")}
      hideSubmit={page !== 0}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
      headerBarMain={<Heading label={page === 0 ? t("CS_SELECT_CASETYPE_HEADER") : t("CS_LIST_DOCUMENTS") + ` (${detailsCardList.length})`} />}
      // style={{ height: "3vh" }}
      className="case-types"
    >
      <div className="case-types-main-div">
        {detailsCardList.map((item) => (
          <CustomDetailsCard
            header={item.header}
            subtext={item.subtext}
            serialNumber={item.serialNumber}
            subnote={item.subnote}
            style={{ width: "100%" }}
          />
        ))}
        {page === 0 && <SelectCustomNote t={t} config={customNoteConfig}></SelectCustomNote>}
      </div>

      {page === 1 && <Submitbar />}
    </Modal>
  );
}

export default CaseType;
