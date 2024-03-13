import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import useCustomAPIMutationHook from "../../hooks/useCustomAPIMutationHook";
import { newConfig } from "../../configs/IndividualCreateConfig";



const IndividualCreate = () => {
  const [toastMessage, setToastMessage] = useState("");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const [gender, setGender] = useState("");
  const reqCreate = {
    url: `/individual/v1/_create`,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };

  

  const mutation = useCustomAPIMutationHook(reqCreate);

  const handleGenderChange = (e) => {
    setGender(e.target.value);
  };


  const onSubmit = async(data) => {
    console.log(data, "data");
    setToastMessage("Form submitted successfully!");
    await mutation.mutate(
      {
        url: `/individual/v1/_create`,
        params: { tenantId: "pg.citya" },
        body: {
          Individual: {
            tenantId: "pg.citya",
            name: {
              givenName: data.applicantname,
            },
            dateOfBirth: null,
            gender: data.genders.code,
            mobileNumber: data.phno,
            address: [
              {
                tenantId: "pg.citya",
                pincode: data.pincode,
                city: data.city,
                street: data.street,
                doorNo: data.doorno,
                "locality":
                {
                  "code" : data.locality.code,
                },
                landmark: data.landmark,
                "type": "PERMANENT"
              },
            ],
            identifiers: null,
            skills: [
                {
                    "type": "DRIVING",
                    "level": "UNSKILLED"
                }
            ],
            "photograph": null,
            additionalFields: {
                "fields": [
                    {
                        "key": "EMPLOYER",
                        "value": "ULB"
                    }
                ]
            },
            isSystemUser: null,
            userDetails: {
                "username": "8821243212",
                "tenantId": "pg.citya",
                "roles": [
                    {
                        "code": "SANITATION_WORKER",
                        "tenantId": "pg.citya"
                    }
                ],
                "type": "CITIZEN"
            },
        },
      },
        config: {
          enable: true,
        },
      },
    );

    const configs = newConfig;

  };

  return (
    <div>
      <h1> Create Individual</h1>
      <FormComposerV2
        label={t("Submit")}
        config={newConfig.map((config) => {
          return {
            ...config,


          };
        })}
        defaultValues={{}}
        onSubmit={(data,) => onSubmit(data, )}
        fieldStyle={{ marginRight: 0 }}

      />
        {/* Toast Component */}
        {toastMessage && (
        <div style={{ backgroundColor: "lightblue", padding: "10px", borderRadius: "5px", marginTop: "10px" }}>
          <div>{toastMessage}</div>
        </div>
      )}
    </div>
  );
}

export default IndividualCreate;