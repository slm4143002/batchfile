package com.file.management;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.file.management.entity.MBatchNumber;
import com.file.management.mapper.MBatchNumberMapper;

@SpringBootTest
class ExcelTest {


	@Autowired
	private MBatchNumberMapper mBatchNumberMapper;

    @Test
    public void testAdd() throws Exception {
    	fileCutter();
    	excelRead();
    }


    public void excelRead() throws IOException {

        InputStream is = new FileInputStream("D:/platform/dl_workspace_ec/targetfile/m_batch_number.xlsx");

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        // 读取标题所有内容
        Row rowTitle = sheet.getRow(0);
        if (rowTitle != null) {// 行不为空
            // 读取cell
            int cellCount = rowTitle.getPhysicalNumberOfCells();
            for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                Cell cell = rowTitle.getCell(cellNum);
                if (cell != null) {
//                    int cellType = cell.getCellType();
                    String cellValue = cell.getStringCellValue();
                    System.out.print(cellValue + "|");
                }
            }
            System.out.println();
        }

        List<MBatchNumber> list = new ArrayList<>();
        // 读取商品列表数据 从第一行开始读取
        int rowCount = sheet.getPhysicalNumberOfRows();
        for (int rowNum = 1; rowNum < rowCount; rowNum++) {
            Row rowData = sheet.getRow(rowNum);
            if (rowData != null) {// 行不为空
            	MBatchNumber demoData = new MBatchNumber();
                // 读取cell
                //int cellCount = rowTitle.getPhysicalNumberOfCells();
                demoData.setBatchNumber(rowData.getCell(0).toString());
                demoData.setMachineCategoryName(rowData.getCell(1).toString());
//                Cell cell1 = rowData.getCell(1);
//                Date dateCellValue = cell1.getDateCellValue();
//                demoData.setDate(dateCellValue);
//                demoData.setDoubleData(Double.valueOf(rowData.getCell(2).toString()));

                list.add(demoData);
            }

        }
        System.out.println(list);
        //将list批量添加到数据库
        for (int i=0; i<list.size(); i++) {
            int prices = mBatchNumberMapper.insert((MBatchNumber)list.get(i));
        }
    }
    
    public void fileCutter() {
        String sourceFilePath = "D:/platform/dl_workspace_ec/srcfile/m_batch_number.xlsx";
        String destinationFolderPath = "D:/platform/dl_workspace_ec/targetfile";

        try {
            Path source = Paths.get(sourceFilePath);
            Path destination = Paths.get(destinationFolderPath, source.getFileName().toString());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件剪切成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
