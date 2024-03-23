package com.file.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.file.management.entity.MBatchNumber;
import com.file.management.entity.TWarningMessage;
import com.file.management.mapper.MBatchNumberMapper;
import com.file.management.mapper.TWarningMessageMapper;

import jakarta.annotation.Resource;


@Component 
@EnableScheduling
public class SaticScheduleTask {

    Logger logger = LoggerFactory.getLogger(SaticScheduleTask.class);

    @Value("${my.dirPath}")
    private String dirPath;
    @Value("${my.filename}")
    private String filename;
    @Value("${my.targetPath}")
    private String targetPath;
    @Value("${my.finishPath}")
    private String finishPath;

    @Resource
	private TWarningMessageMapper tWarningMessageMapper;

    @Resource
	private MBatchNumberMapper mBatchNumberMapper;

    @Scheduled(fixedRate = 10000)
//	@Scheduled(fixedRate = 180000)
    private void configureTasks() throws IOException {
//        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        fileCutter(dirPath, filename, targetPath);
    }

    public void excelRead() throws IOException {
    	try {
            InputStream is = new FileInputStream(targetPath + filename);

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
                        String cellValue = cell.getStringCellValue();
//                        System.out.print(cellValue + "|");
                    }
                }
//                System.out.println();
            }

            List<MBatchNumber> list = new ArrayList<>();
            // 读取商品列表数据 从第一行开始读取
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int rowNum = 3; rowNum < rowCount; rowNum++) {
                Row rowData = sheet.getRow(rowNum);
                if (rowData != null) {// 行不为空
                	MBatchNumber number = new MBatchNumber();
                    // 读取cell
                    //int cellCount = rowTitle.getPhysicalNumberOfCells();
                	if (rowData.getCell(2)!=null && !"".equals(removeZeros(rowData.getCell(2).toString()))) {
                    	number.setBatchNumber(removeZeros(rowData.getCell(2).toString()));
                    	number.setMachineCategoryName(rowData.getCell(3).toString());
                    	if (rowData.getCell(4)!=null && !"".equals(removeZeros(rowData.getCell(4).toString()))) {
                        	number.setMachineCount(Integer.valueOf(removeZeros(rowData.getCell(4).toString())));
                    	}
                    	if (rowData.getCell(10)!=null && !"".equals(removeZeros(rowData.getCell(10).toString()))) {
                        	number.setCarCount(Integer.valueOf(removeZeros(rowData.getCell(10).toString())));
                    	}
                        list.add(number);
                	}
//                    Cell cell1 = rowData.getCell(1);
//                    Date dateCellValue = cell1.getDateCellValue();
//                    demoData.setDate(dateCellValue);
//                    demoData.setDoubleData(Double.valueOf(rowData.getCell(2).toString()));
                }
            }
//            System.out.println(list.size());
            //将list批量添加到数据库
            List<MBatchNumber> listNum = new ArrayList<>();
            for (int i=0; i<list.size(); i++) {
            	List<MBatchNumber> num = mBatchNumberMapper.selectNum(list.get(i).getBatchNumber().toString());
            	if(num.size() > 0) {
//                	System.err.println("批量号已使用： " + list.get(i).getBatchNumber().toString());
                	logger.warn("批量号已使用： " + list.get(i).getBatchNumber().toString());
                	Date date = new Date();
                	TWarningMessage message = new TWarningMessage();
                	message.setBatchNumber(list.get(i).getBatchNumber().toString());
                	message.setWarningMessage(list.get(i).getMachineCategoryName().toString());
                	message.setWriteDate(date);
                	message.setCreateDate(date);
                	tWarningMessageMapper.insert(message);
            	} else {
            		MBatchNumber bean = mBatchNumberMapper.selectRepeat(list.get(i).getBatchNumber().toString());
            		if (bean!=null) {
                    	Date date = new Date();
                    	MBatchNumber update = new MBatchNumber();
                    	update.setMachineCategoryName(list.get(i).getMachineCategoryName().toString());
                    	update.setMachineCount(Integer.parseInt(list.get(i).getMachineCount().toString()));
                    	update.setCarCount(Integer.parseInt(list.get(i).getCarCount().toString()));
                    	update.setWriteDate(date);
                    	update.setCreateDate(date);
                        mBatchNumberMapper.update(update, new QueryWrapper<MBatchNumber>().eq("batch_number", list.get(i).getBatchNumber().toString()));
            		} else {
                    	Date date = new Date();
                    	MBatchNumber entity = new MBatchNumber();
                    	entity.setBatchNumber(list.get(i).getBatchNumber().toString());
                    	entity.setMachineCategoryName(list.get(i).getMachineCategoryName().toString());
                    	entity.setMachineCount(Integer.parseInt(list.get(i).getMachineCount().toString()));
                    	entity.setCarCount(Integer.parseInt(list.get(i).getCarCount().toString()));
                    	entity.setWriteDate(date);
                    	entity.setCreateDate(date);
                        mBatchNumberMapper.insert(entity);
            		}
    			}
            }
            workbook.close();
            fileCutter1(targetPath, filename, finishPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void fileCutter(String dirPath, String filename, String targetPath) {
        String sourceFilePath = dirPath + filename;
        String destinationFolderPath = targetPath;
        try {
            Path source = Paths.get(sourceFilePath);
            if (!Files.exists(source)) {
//                System.out.println("文件不存在");
            	logger.warn("文件不存在");
            	return;
            }
            File directory = new File(destinationFolderPath);
            if (!directory.exists()) {
            	directory.mkdirs();
            }
            Path destination = Paths.get(destinationFolderPath, source.getFileName().toString());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
//            System.out.println("文件移动成功！");
        	logger.warn("文件移动成功！" );
            excelRead();// 读取excel
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    public void fileCutter1(String dirPath, String filename, String targetPath) {
        String sourceFilePath = dirPath + filename;
        String destinationFolderPath = targetPath;
        try {
            Path source = Paths.get(sourceFilePath);
            if (!Files.exists(source)) {
//                System.out.println("文件不存在");
            	logger.warn("文件移动成功！" );
            	return;
            }
            File directory = new File(destinationFolderPath);
            if (!directory.exists()) {
            	directory.mkdirs();
            }
            Path destination = Paths.get(destinationFolderPath, source.getFileName().toString());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
//            System.out.println("文件移动成功！");
        	logger.warn("文件移动成功！" );
//            excelRead();// 读取excel
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    /**
     * 正则去除.或多余的0
     *
     * @param num 需要去除的参数
     */
    public static String removeZeros(String num) {
        if (num.indexOf(".") > 0) {
            // 去掉多余的0
            num = num.replaceAll("0+?$", "");
            // 如果最后一位是. 则去掉
            num = num.replaceAll("[.]$", "");
        }
        return num;
    }
}
