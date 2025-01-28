package org.drishti.esign.service;

import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.security.MakeSignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.drishti.esign.util.ByteArrayMultipartFile;
import org.drishti.esign.util.FileStoreUtil;
import org.drishti.esign.util.TextLocationFinder;
import org.drishti.esign.web.models.Coordinate;
import org.drishti.esign.web.models.ESignParameter;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import static org.drishti.esign.config.ServiceConstants.*;


@Component
@Slf4j
public class PdfEmbedder {

    private final FileStoreUtil fileStoreUtil;

    @Autowired
    public PdfEmbedder(FileStoreUtil fileStoreUtil) {
        this.fileStoreUtil = fileStoreUtil;
    }


    private Coordinate findLocationToSign(PdfReader reader, String signaturePlace) {

        log.info("Method=findLocationToSign ,Result=Inprogress ,placeholder={}", signaturePlace);

        Coordinate coordinate = new Coordinate();
        Rectangle cropBox = reader.getCropBox(1);
        coordinate.setX(cropBox.getLeft());
        coordinate.setY(cropBox.getBottom());
        coordinate.setFound(false);
        coordinate.setPageNumber(reader.getNumberOfPages());

        if (signaturePlace == null || signaturePlace.isBlank()) {
            return coordinate;
        }
        TextLocationFinder finder = new TextLocationFinder(signaturePlace);

        try {
            log.info("Method=findLocationToSign ,Result=Inprogress ,Reading pdf for placeholder={}", signaturePlace);

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
                    coordinate.setX(x - signaturePlace.length() * 5);
                    coordinate.setY(y);
                    coordinate.setFound(true);
                    coordinate.setPageNumber(i);
                    log.info("Method=findLocationToSign,Result=Success,Coordinate found for placeholder={}", signaturePlace);
                    return coordinate;

                }
            }
            log.info("Method=findLocationToSign,Result=Success,No Coordinate found for placeholder={}", signaturePlace);

        } catch (Exception e) {
            log.info("Method=findLocationToSign ,Result=Error,placeholder={}",signaturePlace);
            log.error("Method=findLocationToSign, Error:{}", e.toString());
            throw new CustomException("SIGNATURE_EMBED_EXCEPTION","Error occurred while finding coordinate for placeholder");
        }
        return coordinate;
    }


    public MultipartFile signPdfWithDSAndReturnMultipartFileV2(Resource resource, String response, ESignParameter eSignParameter) {
        log.info("Method=signPdfWithDSAndReturnMultipartFileV2 ,Result=Inprogress ,filestoreId={}", eSignParameter.getFileStoreId());
        try {
            int contentEstimated = 8192;

            PdfReader reader = new PdfReader(resource.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            String pkcsResponse = new XmlSigning().parseXml(response.trim());
            byte[] sigbytes = Base64.decodeBase64(pkcsResponse);
            byte[] paddedSig = new byte[contentEstimated];
            System.arraycopy(sigbytes, 0, paddedSig, 0, sigbytes.length);
            MyExternalSignatureContainer container = new MyExternalSignatureContainer(paddedSig, null, null);

            MakeSignature.signDeferred(reader, eSignParameter.getSignPlaceHolder(), baos, container);

            return new ByteArrayMultipartFile(FILE_NAME, baos.toByteArray());

        } catch (Exception e) {
            log.info("Method=signPdfWithDSAndReturnMultipartFileV2 ,Result=Error ,filestoreId={}", eSignParameter.getFileStoreId());
            log.error("Method=signPdfWithDSAndReturnMultipartFileV2, Error:{}", e.toString());
            throw new CustomException("SIGNATURE_EMBED_EXCEPTION", "Error Occurred while embedding signature");
        } finally {
            log.info("Deleting partially signed pdf in finally block, filestoreId={}", eSignParameter.getFileStoreId());
            fileStoreUtil.deleteFileFromFileStore(eSignParameter.getFileStoreId(), eSignParameter.getTenantId(), false);
            log.info("Method=signPdfWithDSAndReturnMultipartFileV2 ,Result=Success ,filestoreId={}", eSignParameter.getFileStoreId());

        }

    }

    public String pdfSignerV2(Resource resource, ESignParameter eSignParameter) {
        String hashDocument = null;
        log.info("Method=pdfSignerV2 ,Result=InProgress ,filestoreId={}", eSignParameter.getFileStoreId());

        try {
            PdfReader reader = new PdfReader(resource.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            String signPlaceHolder = eSignParameter.getSignPlaceHolder();

            PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\0', null, true);
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
            appearance.setAcro6Layers(false);// deprecated

            Coordinate locationToSign = findLocationToSign(reader, signPlaceHolder);
            Rectangle rectangle = new Rectangle(locationToSign.getX(), locationToSign.getY(), locationToSign.getX() + (100), locationToSign.getY() + (50));

            Font font = new Font();
            font.setSize(6);
            font.setFamily(FONT_FAMILY);
            font.setStyle(FONT_STYLE);

            appearance.setLayer2Font(font);
            Calendar currentDat = Calendar.getInstance();
            appearance.setSignDate(currentDat);
            appearance.setLayer2Text("Digitally Signed");

            appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            appearance.setImage(null);
            appearance.setVisibleSignature(rectangle,
                    locationToSign.getPageNumber(), signPlaceHolder);
            int contentEstimated = 8192;
            MyExternalSignatureContainer container = new MyExternalSignatureContainer(new byte[]{0}, PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);

            MakeSignature.signExternalContainer(appearance, container, contentEstimated);


            InputStream is = appearance.getRangeStream();
            hashDocument = DigestUtils.sha256Hex(is);

            MultipartFile dummySignedPdf = new ByteArrayMultipartFile(FILE_NAME, baos.toByteArray());

            String dummyFileStoreId = fileStoreUtil.storeFileInFileStore(dummySignedPdf, "kl");

            eSignParameter.setFileStoreId(dummyFileStoreId);
            stamper.close();

        } catch (Exception e) {
            log.info("Method=pdfSignerV2 ,Result=Error ,filestoreId={}", eSignParameter.getFileStoreId());
            log.error("Method=pdfSignerV2, Error:{}", e.toString());
            throw new CustomException("SIGNATURE_PLACEHOLDER_EXCEPTION","Error occurred while creating placeholder");
        }

        log.info("Method=pdfSignerV2 ,Result=Success ,filestoreId={}", eSignParameter.getFileStoreId());
        return hashDocument;

    }
}

