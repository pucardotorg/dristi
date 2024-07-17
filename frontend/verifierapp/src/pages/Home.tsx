import React, { createContext, useContext, useState } from "react";
import { Grid, Container, Box, CssBaseline } from "@mui/material";
import { VerificationSteps } from "../utils/config";
import { AlertInfo } from "../types/data-types";
import AlertMessage from "../components/commons/AlertMessage";
import VerificationSection from "../components/Home/VerificationSection";

// Step useContext
let activeStep: number = 0;
const setActiveStep: React.Dispatch<React.SetStateAction<number>> = (value) => {};
export const ActiveStepContext = createContext({ activeStep, setActiveStep });
export const useActiveStepContext = () => useContext(ActiveStepContext);

// Alert Box useContext
let alert: AlertInfo = { open: false };
let setAlertInfo: React.Dispatch<React.SetStateAction<AlertInfo>> = (value) => {};
const AlertsContext = createContext({ alertInfo: alert, setAlertInfo });
export const useAlertMessages = () => useContext(AlertsContext);

function Home(_props: any) {

  const [activeStep, setActiveStep] = useState<number>(VerificationSteps.ScanQrCodePrompt);
  const [alertInfo, setAlertInfo] = useState<AlertInfo>({
    open: false,
    severity: "success",
    message: "",
  });

  return (
    <AlertsContext.Provider value={{ alertInfo, setAlertInfo }}>
      <ActiveStepContext.Provider value={{ activeStep, setActiveStep }}>
        <CssBaseline />
        <Container maxWidth="lg" className="home-container">
          <Grid container spacing={4} justifyContent="center">
            <Grid item xs={12} md={6} className="tracker-container">
              <VerificationSection />
            </Grid>
          </Grid>

          <AlertMessage
            alertInfo={alertInfo}
            handleClose={() => setAlertInfo({ ...alertInfo, open: false })}
          />
        </Container>
      </ActiveStepContext.Provider>
    </AlertsContext.Provider>
  );
}

export default Home;
