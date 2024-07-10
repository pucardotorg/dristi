package org.pucar.dristi.service;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class FileDeleteService {

    public void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}