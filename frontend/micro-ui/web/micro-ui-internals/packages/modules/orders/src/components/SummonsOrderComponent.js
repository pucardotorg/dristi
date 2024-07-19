import React, { useMemo, useState, useEffect } from "react";
import useSearchCaseService from "../../../dristi/src/hooks/dristi/useSearchCaseService";
import { Button, Dropdown } from "@egovernments/digit-ui-react-components";
import _ from "lodash";
import { DRISTIService } from "../../../dristi/src/services";
import AddParty from "../../../hearings/src/pages/employee/AddParty";
const SummonsOrderComponent = ({ t, config, formData, onSelect }) => {
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedChannels, setSelectedChannels] = useState(formData[config.key]?.["selectedChannels"] || []);
  const inputs = useMemo(() => config?.populators?.inputs || [], [config?.populators?.inputs, t]);

  const { data: caseData } = useSearchCaseService(
    {
      criteria: [{ filingNumber: filingNumber }],
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    filingNumber
  );

  const userList = useMemo(() => {
    let users = [];
    if (caseData?.criteria?.[0]?.responseList?.[0]?.additionalDetails) {
      const respondentData = caseData.criteria[0].responseList[0]?.additionalDetails?.respondentDetails?.formdata || [];
      const updatedRespondentData = respondentData.map((item) => ({
        ...item,
        data: {
          ...item.data,
          firstName: item.data.respondentFirstName,
          lastName: item.data.respondentLastName,
          address: item.data.addressDetails.map((address) => ({
            locality: address.addressDetails.locality,
            city: address.addressDetails.city,
            district: address.addressDetails.district,
            pincode: address.addressDetails.pincode,
          })),
          phone_numbers: item.data.phonenumbers?.mobileNumber || [],
          email: item.data.emails?.emailId,
        },
      }));

      users = [...updatedRespondentData];
    }
    return users;
  }, [caseData]);

  const handleDropdownChange = (selectedOption) => {
    const isEqual = _.isEqual(selectedOption.value.data, formData[config.key]?.party.data);
    if (!isEqual) {
      setSelectedChannels([]);
      onSelect(config.key, { ...formData[config.key], party: selectedOption.value, selectedChannels: [] });
    } 
  };

  const handleCheckboxChange = (channelType, value) => {
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
        ? [...partyDetails, { type: channelType, value: value }]
        : [{ type: channelType, value: value }];
    }
    setSelectedChannels(updatedSelectedChannels);
    onSelect(config.key, { ...formData[config.key], selectedChannels: updatedSelectedChannels });
  };
  const renderDeliveryChannels = () => {
    const party = formData[config.key]?.party;
    if (!party) return null;
    const partyDetails = selectedChannels.length === 0 ? formData[config.key]?.selectedChannels : selectedChannels;
    const { address, phone_numbers, email } = party.data || {};
    const clean_data = (values) =>{
      if(Array.isArray(values) && JSON.stringify(values)!=JSON.stringify([]))
      return values.reduce((value) => value!=null); 
    return values;
    }
    const deliveryChannels = [
      { type: "SMS", values: clean_data(phone_numbers) || [] },
      { type: "E-mail", values: email || [] },
      { type: "Post", values: address || [] },
      { type: "Via Police", values: address || [] },
    ];
    return (
      <div>
        <div style={{ display: "flex", flexDirection: "row", justifyContent: "space-between" }}>
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
                      <div key={index}>
                        <input
                          type="checkbox"
                          id={`${channel.type}-${index}`}
                          checked={
                            Array.isArray(partyDetails) &&
                            partyDetails.some((data) => data.type === channel.type && JSON.stringify(value) === JSON.stringify(data.value))
                          }
                          onChange={() => handleCheckboxChange(channel.type, value)}
                        />
                        <label htmlFor={`${channel.type}-${index}`}>
                          {channel.type === "Post" || channel.type === "Via Police"
                            ? `${value.locality}, ${value.city}, ${value.district}, ${value.pincode}`
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

  const selectedParty = useMemo(() => {
    return formData[config.key]?.party != null
      ? {
          label: `${formData[config.key]?.party?.data?.firstName} ${formData[config.key]?.party?.data?.lastName}`,
          value: formData[config.key]?.party,
        }
      : null;
  }, [formData]);
  const [isPartyModalOpen, setIsPartyModalOpen] = useState(false);
  const handleAddParty = () => {
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
                option={userList.map((user) => ({
                  label: `${user.data.firstName} ${user.data.lastName}`,
                  value: user,
                }))}
                optionKey="label"
                selected={selectedParty}
                select={handleDropdownChange}
              />
              <Button
                onButtonClick={handleAddParty}
                className="add-party-btn"
                style={{
                  backgroundColor: "transparent",
                  color: "blue",
                  border: "none",
                  textDecoration: "underline",
                  cursor: "pointer",
                  padding: 0,
                  WebkitBoxShadow: 'none', 
                  boxShadow: 'none',
                }}
                label={t("+ Add new witness")}
              />
            </div>
          )}
          {input.type !== "dropdown" && renderDeliveryChannels()}
        </div>
      ))}
      {isPartyModalOpen && <AddParty onCancel={handleAddParty} onDismiss={handleAddParty} tenantId={tenantId} caseData={caseData}></AddParty>}
    </div>
  );
};

export default SummonsOrderComponent;
