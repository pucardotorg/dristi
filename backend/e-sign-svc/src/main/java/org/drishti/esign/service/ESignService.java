package org.drishti.esign.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.drishti.esign.cipher.Encryption;
import org.drishti.esign.util.FileStoreUtil;
import org.drishti.esign.util.XmlFormDataSetter;
import org.drishti.esign.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;


@Service
@Slf4j
public class ESignService {


    private final PdfEmbedder pdfEmbedder;
    private final XmlSigning xmlSigning;
    private final Encryption encryption;
    private final XmlFormDataSetter formDataSetter;
    private final XmlGenerator xmlGenerator;
    private final HttpServletRequest servletRequest;
    private final FileStoreUtil fileStoreUtil;

    @Autowired
    public ESignService(PdfEmbedder pdfEmbedder, XmlSigning xmlSigning, Encryption encryption, XmlFormDataSetter formDataSetter, XmlGenerator xmlGenerator, HttpServletRequest servletRequest, FileStoreUtil fileStoreUtil) {
        this.pdfEmbedder = pdfEmbedder;
        this.xmlSigning = xmlSigning;
        this.encryption = encryption;
        this.formDataSetter = formDataSetter;
        this.xmlGenerator = xmlGenerator;
        this.servletRequest = servletRequest;
        this.fileStoreUtil = fileStoreUtil;
    }

    public ESignXmlForm signDoc(ESignRequest request) {

        ESignParameter eSignParameter = request.getESignParameter();
        String fileStoreId = eSignParameter.getFileStoreId();
        String tenantId = eSignParameter.getTenantId();
        String pageModule = eSignParameter.getPageModule();
        Resource resource = fileStoreUtil.fetchFileStoreObjectById(fileStoreId, eSignParameter.getTenantId());
        String fileHash = pdfEmbedder.generateHash(resource);
        ESignXmlData eSignXmlData = formDataSetter.setFormXmlData(fileHash, new ESignXmlData());
        eSignXmlData.setTxn(tenantId + "-" +  pageModule + "-" + fileStoreId);
        String strToEncrypt = xmlGenerator.generateXml(eSignXmlData);  // this method is writing in testing.xml
        log.info(strToEncrypt);
        String xmlData = "";

        try {
            PrivateKey rsaPrivateKey = encryption.getPrivateKey("privateKey.pem");
            xmlData = xmlSigning.signXmlStringNew(servletRequest.getServletContext().getRealPath("upload") + File.separator + "Testing.xml", rsaPrivateKey);
            log.info(xmlData);
            xmlGenerator.writeToXmlFile(xmlData, servletRequest.getServletContext().getRealPath("upload") + File.separator + "Testing.xml");
        } catch (Exception e) {
            log.error("");

        }

        ESignXmlForm myRequestXmlForm = new ESignXmlForm();
        myRequestXmlForm.setId("");
        myRequestXmlForm.setType("A");
        myRequestXmlForm.setDescription("Y");
        myRequestXmlForm.setESignRequest(xmlData);
        myRequestXmlForm.setAspTxnID(eSignXmlData.getTxn());
        myRequestXmlForm.setContentType("application/xml");
        return myRequestXmlForm;

    }

    public String signDocWithDigitalSignature(SignDocRequest request) {

        SignDocParameter eSignParameter = request.getESignParameter();
        String fileStoreId = eSignParameter.getFileStoreId();
        String tenantId = eSignParameter.getTenantId();
        String response = eSignParameter.getResponse();

        Resource resource = fileStoreUtil.fetchFileStoreObjectById(fileStoreId, eSignParameter.getTenantId());
        MultipartFile multipartFile;
        try {
            //fixme: get the multipart file and upload into fileStore
            multipartFile = pdfEmbedder.signPdfWithDSAndReturnMultipartFile(resource, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String sigendFileStoreId = null;

            sigendFileStoreId = fileStoreUtil.storeFileInFileStore(multipartFile, tenantId);



        return sigendFileStoreId;
    }
}
