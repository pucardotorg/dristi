import SelectName from "./components/SelectName";

// import { config as complaintConfig } from "./complaintConfig";

const pgrCustomizations = {
  // complaintConfig,
  getcomplainantDetailsTableRows: ({ id, service, role, t }) => {
    return {};
  },
};

const pgrComponents = {
  SelectName: SelectName,
};
export { pgrCustomizations, pgrComponents };
