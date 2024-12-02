package me.bouzri.poc_excel_reader.service.impl;

import me.bouzri.poc_excel_reader.dto.ParcelTitle;
import me.bouzri.poc_excel_reader.service.FileUploadService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public List<ParcelTitle> saveFile(MultipartFile file) {
        List<ParcelTitle> titles = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                ParcelTitle title = new ParcelTitle();
                title.setName(row.getCell(0).getStringCellValue());
                title.setAction(row.getCell(1).getStringCellValue());
                System.out.println(title);
                titles.add(title);
            }
            return titles;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public ByteArrayOutputStream downloadFile() {
        try (Workbook workbook = WorkbookFactory.create(true)) {
            Sheet sheet = workbook.createSheet("Choices");

            // Add headers
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Action");
            headerRow.createCell(2).setCellValue("Action 2");
            headerRow.createCell(3).setCellValue("Action 3");

            // Define the list of valid choices
            String[] validChoices = {"decision", "Notaire", "Autre"};

            // Apply validation to columns B (index 1) and D (index 3)
            int startRow = 1;
            int endRow = 100;

            // Create validation helper
            DataValidationHelper validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

            // Define constraints for valid choices
            DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(validChoices);

            // Apply validation to column 1 (B)
            CellRangeAddressList addressList1 = new CellRangeAddressList(startRow, endRow, 1, 1);
            DataValidation dataValidation1 = validationHelper.createValidation(constraint, addressList1);

            // Apply validation to column 3 (D)
            CellRangeAddressList addressList3 = new CellRangeAddressList(startRow, endRow, 3, 3);
            DataValidation dataValidation3 = validationHelper.createValidation(constraint, addressList3);

            // Set properties for both validations
            dataValidation1.setSuppressDropDownArrow(true);
            dataValidation1.setShowErrorBox(true);

            dataValidation3.setSuppressDropDownArrow(true);
            dataValidation3.setShowErrorBox(true);

            // Add validations to the sheet
            sheet.addValidationData(dataValidation1);
            sheet.addValidationData(dataValidation3);

            // Write the Excel file to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Prepare the response
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return new ByteArrayOutputStream();
        }

    }
}
