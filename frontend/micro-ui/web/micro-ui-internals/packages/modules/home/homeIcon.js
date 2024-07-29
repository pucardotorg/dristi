import React from "react";

const InboxIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path
      d="M22 8.98V18C22 19.1 21.1 20 20 20H4C2.9 20 2 19.1 2 18V6C2 4.9 2.9 4 4 4H14.1C14.04 4.32 14 4.66 14 5C14 6.48 14.65 7.79 15.67 8.71L12 11L4 6V8L12 13L17.3 9.68C17.84 9.88 18.4 10 19 10C20.13 10 21.16 9.61 22 8.98ZM16 5C16 6.66 17.34 8 19 8C20.66 8 22 6.66 22 5C22 3.34 20.66 2 19 2C17.34 2 16 3.34 16 5Z"
      fill="#3D3C3C"
    />
  </svg>
);

const DocumentIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <g clip-path="url(#clip0_7437_113106)">
      <path
        d="M14 2H6C4.9 2 4.01 2.9 4.01 4L4 20C4 21.1 4.89 22 5.99 22H18C19.1 22 20 21.1 20 20V8L14 2ZM16 18H8V16H16V18ZM16 14H8V12H16V14ZM13 9V3.5L18.5 9H13Z"
        fill="#3D3C3C"
      />
    </g>
    <defs>
      <clipPath id="clip0_7437_113106">
        <rect width="24" height="24" fill="white" />
      </clipPath>
    </defs>
  </svg>
);

export { InboxIcon, DocumentIcon };
