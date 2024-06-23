package org.example.file_storage_system.Message;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseFile {
    private String name;
    private String url;
    private String type;
    private long size;

    public ResponseFile(String name, String fileDownloadUri, String type, int length) {

    }
}
