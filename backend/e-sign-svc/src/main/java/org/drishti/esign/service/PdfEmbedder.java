package org.drishti.esign.service;

import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.drishti.esign.util.ByteArrayMultipartFile;
import org.drishti.esign.util.TextLocationFinder;
import org.drishti.esign.web.models.Coordinate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static org.drishti.esign.config.ServiceConstants.*;


@Component
public class PdfEmbedder {

    PdfSignatureAppearance appearance;


    public MultipartFile signPdfWithDSAndReturnMultipartFile(Resource resource, String response, String signaturePlaceHolder) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            InputStream inputStream = resource.getInputStream();
            PdfReader reader = new PdfReader(inputStream);

            PdfStamper stamper = PdfStamper.createSignature(reader, bos, '\0', null, true);

            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance = stamper.getSignatureAppearance();
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
            appearance.setAcro6Layers(false);

            Coordinate locationToSign = findLocationToSign(reader, signaturePlaceHolder);
            Rectangle rectangle = new Rectangle(locationToSign.getX(), locationToSign.getY(), locationToSign.getX() + (100), locationToSign.getY() + (90));
            appearance.setVisibleSignature(rectangle, locationToSign.getPageNumber(), signaturePlaceHolder);

            Font font = new Font();
            font.setSize(6);
            font.setFamily(FONT_FAMILY);
            font.setStyle(FONT_STYLE);
            appearance.setLayer2Font(font);
            Calendar currentDat = Calendar.getInstance();
            appearance.setSignDate(currentDat);

            PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
            appearance.setCryptoDictionary(dic);
            appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            dic.setReason(appearance.getReason());
            dic.setLocation(appearance.getLocation());
            dic.setDate(new PdfDate(appearance.getSignDate()));

            HashMap<PdfName, Integer> exc = new HashMap<>();
            exc.put(PdfName.CONTENTS, 8192 * 2 + 2);

            String certString = response.substring(response.indexOf(User_X509_START_TAG), response.indexOf(User_X509_END_TAG))
                    .replaceAll(User_X509_START_TAG, "").replaceAll(User_X509_END_TAG, "");
            byte[] certBytes = Base64.decodeBase64(certString);
            ByteArrayInputStream stream = new ByteArrayInputStream(certBytes);
            CertificateFactory factory = CertificateFactory.getInstance(X_509);
            Certificate cert = factory.generateCertificate(stream);
            List<Certificate> certificates = List.of(cert);
            appearance.setCrypto(null, certificates.toArray(new Certificate[0]), null, null);


            int contentEstimated = 8192;
            String errorCode = response.substring(response.indexOf(ERR_CODE), response.indexOf(ERR_MSG));
            errorCode = errorCode.trim();
            if (errorCode.contains("NA")) {
                String pkcsResponse = new XmlSigning().parseXml(response.trim());
                byte[] sigbytes = Base64.decodeBase64(pkcsResponse);
                byte[] paddedSig = new byte[contentEstimated];
                System.arraycopy(sigbytes, 0, paddedSig, 0, sigbytes.length);
                PdfDictionary dic2 = new PdfDictionary();
                dic2.put(PdfName.CONTENTS,
                        new PdfString(paddedSig).setHexWriting(true));
                appearance.preClose(exc);
                appearance.close(dic2);
            } else {
                // handle error case
            }


            bos.close();

            return new ByteArrayMultipartFile(FILE_NAME, bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateHash(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return DigestUtils.sha256Hex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Coordinate findLocationToSign(PdfReader reader, String signaturePlace) {
        Coordinate coordinate = new Coordinate();
        Rectangle cropBox = reader.getCropBox(1);
        coordinate.setX(cropBox.getLeft());
        coordinate.setY(cropBox.getBottom());
        coordinate.setFound(false);
        coordinate.setPageNumber(reader.getNumberOfPages());

        if (signaturePlace == null || signaturePlace.isEmpty() || signaturePlace.isBlank()) {
            return coordinate;
        }
        TextLocationFinder finder = new TextLocationFinder(signaturePlace);

        try {
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                PdfContentStreamProcessor processor = new PdfContentStreamProcessor(finder);
                PdfDictionary pageDic = reader.getPageN(i);
                if (pageDic == null) {
                    continue;
                }
                PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
                if (resourcesDic == null) {
                    continue;
                }
                // Use the raw content stream instead of PdfTextExtractor
                byte[] contentBytes = reader.getPageContent(i);
                processor.processContent(contentBytes, resourcesDic);

                if (finder.getKeywordFound()) {
                    // Once found, use the coordinates of the keyword
                    float x = finder.getKeywordX();
                    float y = finder.getKeywordY();
                    coordinate.setX(x);
                    coordinate.setY(y);
                    coordinate.setFound(true);
                    coordinate.setPageNumber(i);
                    return coordinate;

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return coordinate;
    }
}

