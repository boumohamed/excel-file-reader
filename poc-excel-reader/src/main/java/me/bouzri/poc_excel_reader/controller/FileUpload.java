package me.bouzri.poc_excel_reader.controller;

import lombok.AllArgsConstructor;
import me.bouzri.poc_excel_reader.dto.ParcelTitle;
import me.bouzri.poc_excel_reader.service.FileUploadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class FileUpload {
    private final FileUploadService fileUploadService;

    @PostMapping("/upload-file")
    public ResponseEntity<List<ParcelTitle>> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<List<ParcelTitle>>(fileUploadService.saveFile(file), HttpStatus.OK);
    }

    @GetMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile() {
        System.out.println();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileUploadService.downloadFile().toByteArray());
    }
}
