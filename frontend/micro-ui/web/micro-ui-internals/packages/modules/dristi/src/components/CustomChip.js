import React from "react";

const CustomChip = ({ text, shade }) => {
  const shades = {
    green: { textColor: "#00703C", backgroundColor: "#E4F2E4" },
    red: { textColor: "#BB2C2F", backgroundColor: "#FCE8E8" },
    orange: { textColor: "#9E400A", backgroundColor: "#FFF6E8" },
    blue: { textColor: "#0F3B8C", backgroundColor: "#ECF3FD" },
  };

  const { textColor, backgroundColor } = shades[shade] || { textColor: "#3d3c3c", backgroundColor: "#E8E8E8" };

  return <div style={{ ...styles.chip, backgroundColor, color: textColor }}>{text}</div>;
};

const styles = {
  chip: {
    padding: "5px 10px",
    borderRadius: "15px",
    display: "inline-block",
    fontSize: "0.9rem",
    textAlign: "center",
  },
};

export default CustomChip;
