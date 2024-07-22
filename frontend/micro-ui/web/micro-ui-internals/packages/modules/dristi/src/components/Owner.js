import React from "react";
import { FactCheckIcon } from "../icons/svgIndex";

export const Owner = ({ value = "", column }) => {
  console.log(column.parties);
  return (
    <React.Fragment>
      <div>{value}</div>
    </React.Fragment>
  );
};
