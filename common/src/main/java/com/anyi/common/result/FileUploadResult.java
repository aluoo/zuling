package com.anyi.common.result;

import lombok.Data;

@Data
public class FileUploadResult {

    private String url;
    private String fileName;
    private String newFileName;
    private String originalFilename;
}
