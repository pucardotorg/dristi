import { Body, Loader } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { getI18n } from "react-i18next";
import { QueryClient, QueryClientProvider } from "react-query";
import { Provider } from "react-redux";
import { BrowserRouter as Router } from "react-router-dom";
import { DigitApp } from "./App";
import SelectOtp from "./pages/citizen/Login/SelectOtp";
import ChangeCity from "./components/ChangeCity";
import ChangeLanguage from "./components/ChangeLanguage";
import { useState } from "react";
import ErrorBoundary from "./components/ErrorBoundaries";
import getStore from "./redux/store";
import { useGetAccessToken } from "./hooks/useGetAccessToken";

const DigitUIWrapper = ({ stateCode, enabledModules, moduleReducers, defaultLanding }) => {
  const { isLoading, data: initData } = Digit.Hooks.useInitStore(stateCode, enabledModules);

  const moduleData = useMemo(() => {
    if (!initData?.modules) {
      return [];
    }
    if (
      initData?.modules?.some((item) => {
        return item && item.code === "DRISTI";
      })
    ) {
      return initData?.modules;
    }
    return [
      ...initData?.modules,
      {
        module: "DRISTI",
        code: "DRISTI",
        active: true,
        order: 11,
        tenants: [
          {
            code: `${stateCode}`,
          },
        ],
      },
    ];
  }, [initData?.modules, stateCode]);

  if (isLoading) {
    return <Loader page={true} />;
  }

  const i18n = getI18n();
  return (
    <Provider store={getStore(initData, moduleReducers(initData))}>
      <Router>
        <Body>
          <DigitApp
            initData={initData}
            stateCode={stateCode}
            modules={moduleData}
            appTenants={initData.tenants}
            logoUrl={initData?.stateInfo?.logoUrl}
            defaultLanding={defaultLanding}
          />
        </Body>
      </Router>
    </Provider>
  );
};

export const DigitUI = ({ stateCode, registry, enabledModules, moduleReducers, defaultLanding }) => {
  const [privacy, setPrivacy] = useState(Digit.Utils.getPrivacyObject() || {});

  const { isLoading: isGetAccessToken } = useGetAccessToken("refresh-token");

  if (isGetAccessToken) {
    return <Loader />;
  }

  const userType = Digit.UserService.getType();
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 15 * 60 * 1000,
        cacheTime: 50 * 60 * 1000,
        retry: false,
        retryDelay: (attemptIndex) => Infinity,
        /*
          enable this to have auto retry incase of failure
          retryDelay: attemptIndex => Math.min(1000 * 3 ** attemptIndex, 60000)
         */
      },
    },
  });

  const ComponentProvider = Digit.Contexts.ComponentProvider;
  const PrivacyProvider = Digit.Contexts.PrivacyProvider;

  const DSO = Digit.UserService.hasAccess(["FSM_DSO"]);

  return (
    <div>
      <ErrorBoundary>
        <QueryClientProvider client={queryClient}>
          <ComponentProvider.Provider value={registry}>
            <PrivacyProvider.Provider
              value={{
                privacy: privacy?.[window.location.pathname],
                resetPrivacy: (_data) => {
                  Digit.Utils.setPrivacyObject({});
                  setPrivacy({});
                },
                getPrivacy: () => {
                  const privacyObj = Digit.Utils.getPrivacyObject();
                  setPrivacy(privacyObj);
                  return privacyObj;
                },
                /*  Descoped method to update privacy object  */
                updatePrivacyDescoped: (_data) => {
                  const privacyObj = Digit.Utils.getAllPrivacyObject();
                  const newObj = { ...privacyObj, [window.location.pathname]: _data };
                  Digit.Utils.setPrivacyObject({ ...newObj });
                  setPrivacy(privacyObj?.[window.location.pathname] || {});
                },
                /**
                 * Main Method to update the privacy object anywhere in the application
                 *
                 * @author jagankumar-egov
                 *
                 * Feature :: Privacy
                 *
                 * @example
                 *    const { privacy , updatePrivacy } = Digit.Hooks.usePrivacyContext();
                 */
                updatePrivacy: (uuid, fieldName) => {
                  setPrivacy(Digit.Utils.updatePrivacy(uuid, fieldName) || {});
                },
              }}
            >
              <DigitUIWrapper stateCode={stateCode} enabledModules={enabledModules} moduleReducers={moduleReducers} defaultLanding={defaultLanding} />
            </PrivacyProvider.Provider>
          </ComponentProvider.Provider>
        </QueryClientProvider>
      </ErrorBoundary>
    </div>
  );
};

const componentsToRegister = {
  SelectOtp,
  ChangeCity,
  ChangeLanguage,
};

export const initCoreComponents = () => {
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
