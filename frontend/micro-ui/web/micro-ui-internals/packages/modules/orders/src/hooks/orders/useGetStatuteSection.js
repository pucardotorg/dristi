import _ from "lodash";

const useGetStatuteSection = (moduleName = "case", masterName = [{ name: "Statute" }, { name: "Section" }]) => {
  return Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), moduleName, masterName, {
    select: (data) => {
      let newData = {};
      masterName?.forEach((master) => {
        const optionsData = _.get(data, `${moduleName}.${master?.name}`, []);
        newData = {
          ...newData,
          [master.name]: optionsData.filter((opt) => (opt?.hasOwnProperty("active") ? opt.active : true)).map((opt) => ({ ...opt })),
        };
      });
      return newData;
    },
  });
};

export default useGetStatuteSection;
