import React from "react";
import { CardSubHeader, FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";

const CustomCaseInfoDiv = ({ data }) => {
  // Function to partition the data into rows of three items each
  const partitionData = (data) => {
    const result = [];
    for (let i = 0; i < data.length; i += 3) {
      result.push(data.slice(i, i + 3));
    }
    return result;
  };

  return (
    <div style={{ borderRadius: "10px", backgroundColor: "#F7F5F3", padding: "10px", width: "100%" }}>
      <table style={{ width: "100%" }}>
        <tbody>
          {partitionData(data).map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map(({ key, value }, cellIndex) => (
                <td key={cellIndex} style={{ borderRight: "1px solid gray", padding: "10px" }}>
                  <CardSubHeader>{key}</CardSubHeader>
                  <div>{value}</div>
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CustomCaseInfoDiv;
