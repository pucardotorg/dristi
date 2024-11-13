const CustomCaseInfoDiv = ({ data }) => {
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
                <td key={cellIndex} style={{ borderRight: "1px solid gray", padding: "10px", textAlign: "left" }}>
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
