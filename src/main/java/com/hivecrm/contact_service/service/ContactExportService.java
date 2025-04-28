package com.hivecrm.contact_service.service;

import com.hivecrm.contact_service.model.Contact;
import com.hivecrm.contact_service.repository.ContactRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ContactExportService {

    private final ContactService contactService;
    private final ContactRepository contactRepository;

    public ContactExportService(ContactService contactService, ContactRepository contactRepository) {
        this.contactService = contactService;
        this.contactRepository = contactRepository;
    }

    public byte[] exportToExcel(String userId) throws IOException {
        List<Contact> allContacts = contactService.getAllByUser(userId);

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Contacts");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("First Name");
        headerRow.createCell(1).setCellValue("Last Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Phone");
        headerRow.createCell(4).setCellValue("Company");
        headerRow.createCell(5).setCellValue("Tags");
        headerRow.createCell(6).setCellValue("Address");
        headerRow.createCell(7).setCellValue("City");
        headerRow.createCell(8).setCellValue("Zip Code");
        headerRow.createCell(9).setCellValue("Gender");
        headerRow.createCell(10).setCellValue("Website");

        int rowIdx = 1;
        for (Contact contact : allContacts) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(contact.getFirstName());
            row.createCell(1).setCellValue(contact.getLastName());
            row.createCell(2).setCellValue(contact.getEmail());
            row.createCell(3).setCellValue(contact.getPhone());
            row.createCell(4).setCellValue(contact.getCompany());
            row.createCell(5).setCellValue(String.join(",", contact.getTags()));
            row.createCell(6).setCellValue(contact.getAddress());
            row.createCell(7).setCellValue(contact.getCity());
            row.createCell(8).setCellValue(contact.getZipCode());
            row.createCell(9).setCellValue(contact.getGender());
            row.createCell(10).setCellValue(contact.getWebsite());
        }

        workbook.write(dataStream);
        workbook.close();

        return dataStream.toByteArray();


    }

    public void uploadContacts(String userId, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Contact> contacts = new ArrayList<>();

            // Skip the header (start from row 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Contact contact = new Contact();
                contact.setFirstName(getCellValue(row.getCell(0)));
                contact.setLastName(getCellValue(row.getCell(1)));
                contact.setEmail(getCellValue(row.getCell(2)));
                contact.setPhone(getCellValue(row.getCell(3)));
                contact.setCompany(getCellValue(row.getCell(4)));
                contact.setTags(Arrays.stream(getCellValue(row.getCell(5)).split("\\.")).toList());
                contact.setAddress(getCellValue(row.getCell(6)));
                contact.setCity(getCellValue(row.getCell(7)));
                contact.setZipCode(getCellValue(row.getCell(8)));
                contact.setGender(getCellValue(row.getCell(9)));
                contact.setWebsite(getCellValue(row.getCell(10)));
                contact.setUserId(userId);

                contacts.add(contact);
            }

            // Bulk insert
            contactRepository.saveAll(contacts);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload contacts", e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            return "";
        }
    }
}
