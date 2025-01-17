package org.egov.filestore.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.egov.filestore.config.FileStoreConfig;
import org.egov.filestore.domain.model.Artifact;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageValidator {

	private FileStoreConfig fileStoreConfig;

	
	@Autowired
	public StorageValidator(FileStoreConfig fileStoreConfig) {
		super();
		this.fileStoreConfig = fileStoreConfig;
	}

	private static final Map<String, byte[]> MAGIC_NUMBERS = new HashMap<>();

	static {
		MAGIC_NUMBERS.put("pdf", new byte[]{0x25, 0x50, 0x44, 0x46});
		MAGIC_NUMBERS.put("jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0});
		MAGIC_NUMBERS.put("png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47});
		MAGIC_NUMBERS.put("gif", new byte[]{0x47, 0x49, 0x46, 0x38});
		MAGIC_NUMBERS.put("zip", new byte[]{0x50, 0x4B, 0x03, 0x04});
		MAGIC_NUMBERS.put("mp4", new byte[]{0x00, 0x00, 0x00, 0x18, 0x66, 0x74, 0x79, 0x70, 0x6D, 0x70, 0x34, 0x32});
		MAGIC_NUMBERS.put("mov", new byte[]{0x00, 0x00, 0x00, 0x14, 0x66, 0x74, 0x79, 0x70, 0x71, 0x74});
	}

	public void validate(Artifact artifact) {
			
		String extension = (FilenameUtils.getExtension(artifact.getMultipartFile().getOriginalFilename())).toLowerCase();
		validateFileExtention(extension);
		validateContentType(artifact.getFileContentInString(), extension);
		validateInputContentType(artifact);
		validateMagicNumber(artifact.getMultipartFile(), extension);
		validateFileSize(artifact.getMultipartFile());
	}
	
	private void validateFileExtention(String extension) {
		if(!fileStoreConfig.getAllowedFormatsMap().containsKey(extension)) {
			throw new CustomException("EG_FILESTORE_INVALID_INPUT","Inalvid input provided for file : " + extension + ", please upload any of the allowed formats : " + fileStoreConfig.getAllowedKeySet());
		}
	}
	
	private void validateContentType(String inputStreamAsString, String extension) {
		
		String inputFormat = null;
		Tika tika = new Tika();
		try {
			
			InputStream ipStreamForValidation = IOUtils.toInputStream(inputStreamAsString, fileStoreConfig.getImageCharsetType());
			inputFormat = tika.detect(ipStreamForValidation);
			ipStreamForValidation.close();
		} catch (IOException e) {
			throw new CustomException("EG_FILESTORE_PARSING_ERROR","not able to parse the input please upload a proper file of allowed type : " + e.getMessage());
		}
		
		if (!fileStoreConfig.getAllowedFormatsMap().get(extension).contains(inputFormat)) {
			throw new CustomException("EG_FILESTORE_INVALID_INPUT", "Inalvid input provided for file, the extension does not match the file format. Please upload any of the allowed formats : "
							+ fileStoreConfig.getAllowedKeySet());
		}
	}

	private void validateInputContentType(Artifact artifact){

		MultipartFile file =  artifact.getMultipartFile();
		String contentType = file.getContentType();
		String extension = (FilenameUtils.getExtension(artifact.getMultipartFile().getOriginalFilename())).toLowerCase();


		if (!fileStoreConfig.getAllowedFormatsMap().get(extension).contains(contentType)) {
			throw new CustomException("EG_FILESTORE_INVALID_INPUT", "Invalid Content Type");
		}
	}
	/*private void validateFilesToUpload(List<MultipartFile> filesToStore, String module, String tag, String tenantId) {
		if (CollectionUtils.isEmpty(filesToStore)) {
			throw new EmptyFileUploadRequestException(module, tag, tenantId);
		}
	}*/
	private void validateMagicNumber(MultipartFile file, String extension) {
		byte[] magicNumber = MAGIC_NUMBERS.get(extension);
		if (magicNumber == null) {
			return;
		}
		try (InputStream is = file.getInputStream()) {
			byte[] fileHeader = new byte[magicNumber.length];
			if (is.read(fileHeader) != fileHeader.length) {
				throw new CustomException("EG_FILESTORE_INVALID_INPUT", "File content does not match the expected format for " + extension);
			}
			for(int i=0; i<magicNumber.length; i++) {
				if(fileHeader[i] != magicNumber[i]) {
					throw new CustomException("EG_FILESTORE_INVALID_INPUT", "File content does not match the expected format for " + extension);
				}
			}
		} catch (IOException e) {
			throw new CustomException("EG_FILESTORE_IO_ERROR", "Error reading file: " + e.getMessage());
		}
	}

	private void validateFileSize(MultipartFile file) {
		if (file.getSize() > fileStoreConfig.getFileSizeMax()) {
			throw new CustomException("EG_FILESTORE_INVALID_INPUT", "File size exceeds the maximum allowed size of " + fileStoreConfig.getFileSizeMax() + " bytes");
		}
	}

	public void validateDeleteFiles(List<String> fileStoreIds, String tenantId){
		if (fileStoreIds == null || fileStoreIds.isEmpty()) {
			throw new CustomException("EG_FILESTORE_INVALID_INPUT", "fileStoreIds cannot be null or empty");
		}
		if (tenantId == null || tenantId.isEmpty()) {
			throw new CustomException("EG_FILESTORE_INVALID_INPUT", "tenantId cannot be null or empty");
		}
	}
}
