import { scanFile } from "@openhealthnz-credentials/pdf-image-qr-scanner";
import {decodeData} from '@mosip/pixelpass';
import {HEADER_DELIMITER, SUPPORTED_QR_HEADERS} from "./config";
import JSZip from "jszip";

export const CERTIFICATE_FILE = "certificate.json";

export const scanFilesForQr = async (selectedFile) => {
    let scanResult = { data: null, error: null };
    try {
        scanResult.data = await scanFile(selectedFile);
    } catch (e) {
        // Example Error Handling
        if (e?.name === "InvalidPDFException") {
            scanResult.error = "Invalid PDF";
        } else if (e instanceof Event) {
            scanResult.error = "Invalid Image";
        } else {
            scanResult.error = "Unknown error:" + e;
        }
    }
    return scanResult;
}

export const decodeQrData = async (qrData) => {
    if (qrData) {
        const zip = new JSZip();
        return await zip.loadAsync(qrData).then((contents) => {
            return contents.files[CERTIFICATE_FILE].async("text")
        }).then(function (contents) {
            return contents
        }).catch(err => {
            console.error("decodeQrData error", err);
            return  { error: "Failed to decode QR data", details: err.message };
          }
        );
    }
    return qrData

}
