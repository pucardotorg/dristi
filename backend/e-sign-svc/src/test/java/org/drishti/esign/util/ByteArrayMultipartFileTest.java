package org.drishti.esign.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ByteArrayMultipartFileTest {

    private static final String FILE_NAME = "test.pdf";
    private static final byte[] FILE_CONTENT = "This is a test file".getBytes();
    private ByteArrayMultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        multipartFile = new ByteArrayMultipartFile(FILE_NAME, FILE_CONTENT);
    }

    @Test
    public void testGetName() {
        assertEquals(FILE_NAME, multipartFile.getName());
    }

    @Test
    public void testGetOriginalFilename() {
        assertEquals(FILE_NAME, multipartFile.getOriginalFilename());
    }

    @Test
    public void testGetContentType() {
        assertEquals("application/pdf", multipartFile.getContentType());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(multipartFile.isEmpty());
    }

    @Test
    public void testIsEmpty_EmptyContent() {
        ByteArrayMultipartFile emptyFile = new ByteArrayMultipartFile(FILE_NAME, new byte[0]);
        assertTrue(emptyFile.isEmpty());
    }

    @Test
    public void testGetSize() {
        assertEquals(FILE_CONTENT.length, multipartFile.getSize());
    }

    @Test
    public void testGetBytes() throws IOException {
        assertArrayEquals(FILE_CONTENT, multipartFile.getBytes());
    }

    @Test
    public void testGetInputStream() throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(FILE_CONTENT)) {
            assertArrayEquals(inputStream.readAllBytes(), multipartFile.getInputStream().readAllBytes());
        }
    }

    @Test
    public void testTransferTo_File() throws IOException {
        File dest = File.createTempFile("test", ".pdf");
        multipartFile.transferTo(dest);

        try (FileInputStream fis = new FileInputStream(dest)) {
            byte[] readContent = new byte[FILE_CONTENT.length];
            int bytesRead = fis.read(readContent);
            assertEquals(FILE_CONTENT.length, bytesRead);
            assertArrayEquals(FILE_CONTENT, readContent);
        } finally {
            dest.deleteOnExit();
        }
    }

    @Test
    public void testTransferTo_Path() throws IOException {
        Path dest = Files.createTempFile("test", ".pdf");
        multipartFile.transferTo(dest);

        byte[] readContent = Files.readAllBytes(dest);
        assertArrayEquals(FILE_CONTENT, readContent);

        Files.deleteIfExists(dest);
    }
}
