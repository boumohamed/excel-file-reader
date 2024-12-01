package me.bouzri.poc_excel_reader.service;

import me.bouzri.poc_excel_reader.dto.ParcelTitle;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface FileUploadService {
    List<ParcelTitle> saveFile(MultipartFile file);

    ByteArrayOutputStream downloadFile();
}
