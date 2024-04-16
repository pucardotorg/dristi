import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React from "react";

const newConfig = [
  {
    head: "CS_ID_VERIFICATION",
    body: [
      {
        type: "component",
        route: "property-type",
        isMandatory: true,
        component: "CPTPropertyAssemblyDetails",
        texts: {
          headerCaption: "",
          header: "PT_ASSEMBLY_DET",
          cardText: "",
          submitBarLabel: "PT_COMMONS_NEXT",
        },
        nextStep: {
          COMMON_PROPTYPE_BUILTUP_INDEPENDENTPROPERTY: "landarea",
          COMMON_PROPTYPE_BUILTUP_SHAREDPROPERTY: "PtUnits",
          COMMON_PROPTYPE_VACANT: "area",
        },
        key: "assemblyDet",
        withoutLabel: true,
      },
    ],
  },
];

function SelectId({ config, t }) {
  return (
    <FormComposerV2
      config={config}
      t={t}
      onSubmit={(props) => {
        console.log(props);
      }}
      noBoxShadow
      inline
      submitInForm
      label={"Next"}
      onSecondayActionClick={() => {}}
      description={"Description"}
      headingStyle={{ textAlign: "center" }}
      cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column" }}
    ></FormComposerV2>
  );
}

export default SelectId;
