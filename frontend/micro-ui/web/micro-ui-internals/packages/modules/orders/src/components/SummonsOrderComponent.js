import React, { useCallback, useEffect, useMemo, useState } from "react";
import useSearchCaseService from "../../../dristi/src/hooks/dristi/useSearchCaseService";
import { Button, Dropdown } from "@egovernments/digit-ui-react-components";
import _ from "lodash";
import AddParty from "../../../hearings/src/pages/employee/AddParty";
import { DRISTIService } from "@egovernments/digit-ui-module-dristi/src/services";

const RenderDeliveryChannels = ({ partyDetails, deliveryChannels, handleCheckboxChange }) => {
  return (
    <div>
      <div style={{ display: "flex", flexDirection: "row", justifyContent: "space-between", alignItems: "center", height: 32 }}>
        <h1>Select Delivery Channels</h1>
        <p>
          {partyDetails?.length || 0} of {deliveryChannels.reduce((acc, channel) => acc + (channel.values?.length || 0), 0)} selected
        </p>
      </div>
      <form>
        {deliveryChannels.map((channel) => (
          <div key={channel?.type}>
            {Array.isArray(channel?.values) && channel?.values?.length > 0 && channel?.values[0] != null && (
              <div>
                <h2>
                  <strong>{channel.type} to </strong>
                </h2>

                {Array.isArray(channel?.values) &&
                  channel?.values?.map((value, index) => (
                    <div key={`${channel.type}-${index}`}>
                      <input
                        type="checkbox"
                        id={`${channel.type}-${index}`}
                        checked={
                          Array.isArray(partyDetails) &&
                          partyDetails.some((data) => data.type === channel.type && JSON.stringify(value) === JSON.stringify(data.value))
                        }
                        onChange={() => handleCheckboxChange(channel.type, channel.code, value)}
                      />
                      <label htmlFor={`${channel.type}-${index}`}>
                        {channel.type === "e-Post" || channel.type === "Via Police" || channel.type === "RPAD"
                          ? typeof value.address === "string"
                            ? value.address
                            : `${value.locality}, ${value.city}, ${value.district}, ${value.pincode}`
                          : value}
                      </label>
                    </div>
                  ))}
              </div>
            )}
          </div>
        ))}
      </form>
    </div>
  );
};

const SummonsOrderComponent = ({ t, config, formData, onSelect }) => {
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedChannels, setSelectedChannels] = useState(formData[config.key]?.["selectedChannels"] || []);
  const inputs = useMemo(() => config?.populators?.inputs || [], [config?.populators?.inputs]);
  const orderType = useMemo(() => formData?.orderType?.code, [formData?.orderType?.code]);
  const [userList, setUserList] = useState([]);
  const [deliveryChannels, setDeliveryChannels] = useState([
    { type: "SMS", code: "SMS", values: [] },
    { type: "E-mail", code: "EMAIL", values: [] },
    {
      type: "e-Post",
      code: "EPOST",
      values: [],
    },
    {
      type: "RPAD",
      code: "RPAD",
      values: [],
    },
    { type: "Via Police", code: "POLICE", values: [] },
  ]);

  const { data: caseData, refetch } = useSearchCaseService(
    {
      criteria: [{ filingNumber: filingNumber }],
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    filingNumber
  );
  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );
  // caseDetails?.litigants
  // ?.filter((item) => item?.partyType?.includes("respondent"))
  // .map((item) => {
  //   return {
  //     code: item?.additionalDetails?.fullName,
  //     name: item?.additionalDetails?.fullName,
  //     uuid: allAdvocates[item?.additionalDetails?.uuid],
  //   };
  // }) || []

  const mapAddressDetails = (addressDetails, isIndividualData = false) => {
    return addressDetails?.map((address) => ({
      locality: address?.addressDetails?.locality || address?.street || "",
      city: address?.addressDetails?.city || address?.city || "",
      district: address?.addressDetails?.district || address?.addressLine2 || "",
      pincode: address?.addressDetails?.pincode || address?.pincode || "",
      address: isIndividualData ? undefined : address?.addressDetails,
    }));
  };

  useEffect(() => {
    const fetchUsers = async () => {
      let users = [];
      if (caseDetails?.additionalDetails) {
        const respondentData = caseDetails?.additionalDetails?.respondentDetails?.formdata || [];
        const witnessData = caseDetails?.additionalDetails?.witnessDetails?.formdata || [];
        const updatedRespondentData = await Promise.all(
          respondentData.map(async (item) => {
            const individualId = item?.data?.respondentVerification?.individualDetails?.individualId;
            let individualData = undefined;
            if (individualId) {
              try {
                const response = await window?.Digit.DRISTIService.searchIndividualUser(
                  {
                    Individual: {
                      individualId: individualId,
                    },
                  },
                  { tenantId, limit: 1000, offset: 0 }
                );
                individualData = response?.Individual?.[0];
              } catch (error) {
                console.error("error :>> ", error);
              }
            }
            return {
              ...item,
              data: {
                ...item?.data,
                firstName: individualData ? individualData?.name?.givenName : item?.data?.respondentFirstName || "",
                lastName: individualData ? individualData?.name?.familyName : item?.data?.respondentLastName || "",
                ...(individualData && {
                  respondentFirstName: individualData?.name.givenName,
                  respondentMiddleName: individualData?.name?.otherNames,
                  respondentLastName: individualData?.name?.familyName,
                }),
                address: individualData ? mapAddressDetails(individualData?.address, true) : mapAddressDetails(item?.data?.addressDetails),
                partyType: "Respondent",
                phone_numbers: (individualData ? [individualData?.mobileNumber] : [])
                  .concat(item?.data?.phonenumbers?.mobileNumber || [])
                  .filter(Boolean),
                email: (individualData ? [individualData?.email] : []).concat(item?.data?.emails?.emailId || []).filter(Boolean),
              },
            };
          })
        );
        const updatedWitnessData = witnessData.map((item) => ({
          ...item,
          data: {
            ...item?.data,
            firstName: item?.data?.firstName,
            lastName: item?.data?.lastName,
            address: mapAddressDetails(item?.data?.addressDetails),
            partyType: "Witness",
            phone_numbers: item?.data?.phonenumbers?.mobileNumber || [],
            email: item?.data?.emails?.emailId || [],
          },
        }));
        users = [...updatedRespondentData, ...updatedWitnessData];
      }
      setUserList(users);
    };

    fetchUsers();
  }, [caseDetails?.additionalDetails, tenantId]);

  const handleDropdownChange = (selectedOption) => {
    const isEqual = _.isEqual(selectedOption.value.data, formData?.[config.key]?.party?.data);
    if (!isEqual) {
      setSelectedChannels([]);
      onSelect(config.key, { ...formData[config.key], party: selectedOption.value, selectedChannels: [] });
    }
  };

  const handleCheckboxChange = (channelType, code, value) => {
    const partyDetails = selectedChannels.length === 0 ? formData[config.key]?.selectedChannels : selectedChannels;
    const isPresent =
      Array.isArray(partyDetails) && partyDetails.some((data) => data.type === channelType && JSON.stringify(value) === JSON.stringify(data.value));
    let updatedSelectedChannels;

    if (isPresent) {
      updatedSelectedChannels = partyDetails.filter(
        (channel) => !(channel.type === channelType && JSON.stringify(value) === JSON.stringify(channel.value))
      );
    } else {
      updatedSelectedChannels = Array.isArray(partyDetails)
        ? [...partyDetails, { type: channelType, code: code, value: value }]
        : [{ type: channelType, code: code, value: value }];
    }
    setSelectedChannels(updatedSelectedChannels);
    onSelect(config.key, { ...formData[config.key], selectedChannels: updatedSelectedChannels });
  };
  // const clean_data = (values) => {
  //   return values?.reduce((value, curr) => {
  //     if (value != null) {
  //       values.push(curr);
  //     }
  //     return values;
  //   }, []);
  // };

  const selectedParty = useMemo(() => {
    const partyData = formData?.[config.key]?.party?.data || {};
    const { firstName = "", lastName = "", partyType } = partyData;
    const label = [firstName, lastName, partyType ? `(${partyType})` : ""].filter(Boolean).join(" ");
    return formData[config.key]?.party
      ? {
          label,
          value: formData[config.key]?.party,
        }
      : null;
  }, [config.key, formData]);

  const [isPartyModalOpen, setIsPartyModalOpen] = useState(false);
  const party = useMemo(() => formData[config.key]?.party, [config.key, formData]);
  const partyDetails = selectedChannels.length === 0 ? formData[config.key]?.selectedChannels : selectedChannels;
  const { address, phone_numbers, email } = useMemo(() => party?.data || {}, [party?.data]);

  const getRespondentPincodeDetails = useCallback(
    async (pincode) => {
      const pincodeData = await DRISTIService.getrepondentPincodeDetails(
        {
          Criteria: {
            pincode: [pincode],
          },

          tenantId,
        },
        {}
      );
      if (pincodeData?.PostalHubs && Array.isArray(pincodeData.PostalHubs) && Boolean(pincodeData.PostalHubs.length)) {
        return pincodeData.PostalHubs?.[0];
      }
      return null;
    },
    [tenantId]
  );

  const getEPostAddress = useCallback(
    async (address = []) => {
      const addressList = await Promise.all(
        address.map(async (item) => {
          if (item?.pincode) {
            const verifiedPincode = await getRespondentPincodeDetails(item.pincode);
            if (Boolean(verifiedPincode)) {
              return item;
            }
            return null;
          }
          return null;
        })
      );
      const ePostAddresses = addressList?.filter((item) => Boolean(item));
      setDeliveryChannels(
        [
          { type: "SMS", code: "SMS", values: phone_numbers || [] },
          { type: "E-mail", code: "EMAIL", values: email || [] },
          {
            type: "e-Post",
            code: "EPOST",
            values: ePostAddresses,
          },
          {
            type: "RPAD",
            code: "RPAD",
            values: address || [],
          },
          orderType === "SUMMONS" && { type: "Via Police", code: "POLICE", values: address || [] },
        ]
          .filter((item) => Boolean(item))
          .map((item) => item)
      );
    },
    [email, getRespondentPincodeDetails, orderType, phone_numbers]
  );

  // console.log("selectedParty", selectedParty);

  // const summonsPincode = useMemo(() => filteredTasks?.[0]?.taskDetails?.respondentDetails?.address?.pincode, [filteredTasks]);

  // const { data: respondentPincodeDetails, isLoading: isRespondentPincodeLoading } = Digit.Hooks.dristi.useRepondentPincodeDetails(
  //   {
  //     Criteria: [
  //       {
  //         pincode: [summonsPincode],
  //       },
  //     ],
  //   },
  //   {},
  //   "dristi",
  //   Boolean(filteredTasks)
  // );

  useEffect(() => {
    if (address && Array.isArray(address)) {
      getEPostAddress(address);
    }
  }, [address, email, getEPostAddress, orderType, phone_numbers]);

  const handleAddParty = (e) => {
    e?.stopPropagation();
    e?.preventDefault();
    setIsPartyModalOpen(!isPartyModalOpen);
  };

  return (
    <div>
      {inputs.map((input, index) => (
        <div key={index}>
          {input.type === "dropdown" && (
            <div>
              <Dropdown
                t={t}
                option={userList?.map((user) => ({
                  label: [user?.data?.firstName, user?.data?.lastName, `(${user?.data?.partyType})`].filter(Boolean).join(" "),
                  value: user,
                }))}
                optionKey="label"
                selected={selectedParty}
                select={handleDropdownChange}
                style={{ maxWidth: "100%", marginBottom: 8 }}
              />
              {
                <Button
                  onButtonClick={handleAddParty}
                  className="add-party-btn"
                  style={{
                    backgroundColor: "transparent",
                    border: "none",
                    cursor: "pointer",
                    padding: 0,
                    WebkitBoxShadow: "none",
                    boxShadow: "none",
                    height: "auto",
                  }}
                  textStyles={{
                    marginTop: 0,
                    fontFamily: "Roboto",
                    fontSize: "16px",
                    fontWeight: 700,
                    lineHeight: "18.75px",
                    textAlign: "center",
                    color: "#007E7E",
                  }}
                  label={t("+ Add new witness")}
                />
              }
            </div>
          )}
          {input.type !== "dropdown" && selectedParty && (
            <RenderDeliveryChannels deliveryChannels={deliveryChannels} handleCheckboxChange={handleCheckboxChange} partyDetails={partyDetails} />
          )}
        </div>
      ))}
      {isPartyModalOpen && (
        <AddParty
          onCancel={handleAddParty}
          onDismiss={handleAddParty}
          tenantId={tenantId}
          caseData={caseData}
          onAddSuccess={() => {
            handleAddParty();
            refetch();
          }}
        ></AddParty>
      )}
    </div>
  );
};

export default SummonsOrderComponent;
