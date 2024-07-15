import React, { useState } from "react";
import { CardSubHeader } from "@egovernments/digit-ui-react-components";
import { CopyIcon } from "../icons/svgIndex";

const copyIcon = () => {
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M4 2.85714H2.4C1.7632 2.85714 1.1528 3.08286 0.703198 3.485C0.252798 3.88643 0 4.43143 0 5C0 7.98857 0 14.8686 0 17.8571C0 18.4257 0.252798 18.9707 0.703198 19.3721C1.1528 19.7743 1.7632 20 2.4 20C5.1704 20 10.8296 20 13.6 20C14.2368 20 14.8472 19.7743 15.2968 19.3721C15.7472 18.9707 16 18.4257 16 17.8571V17.1429H17.6C18.2368 17.1429 18.8472 16.9171 19.2968 16.515C19.7472 16.1136 20 15.5686 20 15C20 12.0114 20 5.13143 20 2.14286C20 1.57429 19.7472 1.02928 19.2968 0.627856C18.8472 0.225713 18.2368 0 17.6 0C14.8296 0 9.1704 0 6.4 0C5.7632 0 5.1528 0.225713 4.7032 0.627856C4.2528 1.02928 4 1.57429 4 2.14286V2.85714ZM16 15.7143H17.6C17.812 15.7143 18.016 15.6393 18.1656 15.505C18.316 15.3714 18.4 15.1893 18.4 15V2.14286C18.4 1.95357 18.316 1.77143 18.1656 1.63786C18.016 1.50357 17.812 1.42857 17.6 1.42857H6.4C6.188 1.42857 5.984 1.50357 5.8344 1.63786C5.684 1.77143 5.6 1.95357 5.6 2.14286V2.85714H13.6C14.2368 2.85714 14.8472 3.08286 15.2968 3.485C15.7472 3.88643 16 4.43143 16 5V15.7143Z"
      fill="#007E7E"
    />
  </svg>;
};
function fallbackCopyTextToClipboard(text) {
  const textArea = document.createElement("textarea");
  textArea.value = text;

  // Avoid scrolling to bottom
  textArea.style.top = "0";
  textArea.style.left = "0";
  textArea.style.position = "fixed";

  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();

  try {
    document.execCommand("copy");
  } catch (err) {
    throw err;
  }

  document.body.removeChild(textArea);
}

const copyToClipboard = (text) => {
  if (!navigator.clipboard) {
    fallbackCopyTextToClipboard(text);
    return;
  }
  navigator.clipboard.writeText(text);
};

const CustomCaseInfoDiv = ({ t, data, column = 3, children, style, ...props }) => {
  const [copied, setCopied] = useState({ isCopied: false, text: "Copied" });
  // Function to partition the data into rows of three items each
  const partitionData = (data) => {
    const result = [];
    for (let i = 0; i < data.length; i += column) {
      result.push(data.slice(i, i + column));
    }
    return result;
  };

  const dataCopy = (isCopied, message, duration = 3000) => {
    setCopied({ isCopied: isCopied, text: message });
    setTimeout(() => {
      setCopied({ isCopied: false, text: "Copied" });
    }, duration);
  };

  return (
    <React.Fragment>
      {data && data.length > 0 && (
        <div className="custom-case-info-div">
          <table>
            <tbody>
              {partitionData(data).map((row, rowIndex) => (
                <tr key={rowIndex}>
                  {row.map(({ key, value, copyData }, cellIndex) => (
                    <td key={cellIndex} className={`${props?.tableDataClassName} column-${column}`}>
                      <h2 className="case-info-title">{t(key)}</h2>
                      <div className={"case-info-value"}>
                        <span className={props?.tableValueClassName}>{value}</span>{" "}
                        {copyData && (
                          <button
                            className="case-info-button"
                            onClick={() => {
                              copyToClipboard(value);
                              dataCopy(true, "Copied");
                            }}
                          >
                            <CopyIcon />
                            {copied.isCopied ? copied.text : "Copy"}
                          </button>
                        )}
                      </div>
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
          {children}
        </div>
      )}
    </React.Fragment>
  );
};

export default CustomCaseInfoDiv;
