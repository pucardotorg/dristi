export type QrScanResult = {
    data: string | null,
    error: string | null
}

export type ScanStatus = "Success" | "NotScanned" | "Failed";

export type VcStatus = {
    status: "OK" | "NOK" | "Verifying";
    checks: {
        active: string | null;
        revoked: "OK" | "NOK";
        expired: "OK" | "NOK";
        proof: "OK" | "NOK";
    }[];
}

export type VerificationStep = {
    label: string,
    description: string
}

export type CardPositioning = {
    top?: number,
    right?: number,
    bottom?: number,
    left?: number
}

export type AlertSeverity = "success" | "info" | "warning" | "error" | undefined;

export type AlertInfo = {
    message?: string,
    severity?: AlertSeverity,
    open?: boolean
}

export type Vc = {
    credentialSubject: {
      [key: string]: any;
      caseName?: string;
      caseTypes?: string;
      issuedBy?: string;
      filingDate?: string;
      caseNumber?: string;
    };
  } | undefined