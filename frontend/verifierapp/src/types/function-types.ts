import {AlertInfo, QrScanResult, Vc} from "./data-types";

export type SetActiveStepFunction = (activeStep: number) => void;
export type SetVcStatusFunction = (vc: Vc) => void;
export type SetQrDataFunction = (qrData: string) => void;
export type SetScanResultFunction = (result: QrScanResult) => void;
export type SetAlertInfoFunction = ((alert: AlertInfo) => void) | ((setUsingCurrentAlert: ((alert: AlertInfo) => AlertInfo)) => void);