package org.drishti.esign.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileValidationUtilTest {

    @Test
    public void testIsValidFile_Jpeg() {
        MultipartFile file = new MockMultipartFile("file", "filename.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Png() {
        MultipartFile file = new MockMultipartFile("file", "filename.png", MediaType.IMAGE_PNG_VALUE, new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Pdf() {
        MultipartFile file = new MockMultipartFile("file", "filename.pdf", MediaType.APPLICATION_PDF_VALUE, new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Odt() {
        MultipartFile file = new MockMultipartFile("file", "filename.odt", "application/vnd.oasis.opendocument.text", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Ods() {
        MultipartFile file = new MockMultipartFile("file", "filename.ods", "application/vnd.oasis.opendocument.spreadsheet", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Docx() {
        MultipartFile file = new MockMultipartFile("file", "filename.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Xlsx() {
        MultipartFile file = new MockMultipartFile("file", "filename.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Doc() {
        MultipartFile file = new MockMultipartFile("file", "filename.doc", "application/msword", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Xls() {
        MultipartFile file = new MockMultipartFile("file", "filename.xls", "application/vnd.ms-excel", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Dxf() {
        MultipartFile file = new MockMultipartFile("file", "filename.dxf", "application/dxf", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Csv() {
        MultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_Txt() {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", new byte[0]);
        assertTrue(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_InvalidType() {
        MultipartFile file = new MockMultipartFile("file", "filename.xyz", "application/xyz", new byte[0]);
        assertFalse(FileValidationUtil.isValidFile(file));
    }

    @Test
    public void testIsValidFile_NullContentType() {
        MultipartFile file = new MockMultipartFile("file", "filename.xyz", null, new byte[0]);
        assertFalse(FileValidationUtil.isValidFile(file));
    }
}
