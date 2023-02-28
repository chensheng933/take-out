package com.itheima.reggie.test;

import com.itheima.reggie.entity.User;
import com.itheima.reggie.mapper.UserMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class PoiTest {

    @Autowired
    UserMapper userMapper;

    /**
     * 07版本写入工作蒲
     * @throws IOException
     */
    @Test
    public void testCreateExcel() throws IOException {
        //1.创建一个工作薄
        Workbook wk = new XSSFWorkbook();
        
        //2.创建表sheet
        Sheet sheet = wk.createSheet("用户");

        //3.创建标题行
        Row row = sheet.createRow(0);

        //4.在标题行中创建若干单元格
        Cell cell = null;
        //CellStyle css = wk.createCellStyle();
        String[] arr = {"id","名字","电话","性别","身份证号","头像","状态"};
        for (int i = 0; i < 7 ; i++) {
            cell = row.createCell(i);
            //设置单元格中放置的内容
            cell.setCellValue(arr[i]);
        }

        //5.查询所有用户数据 list
        List<User> list = userMapper.findAll();

        //6.遍历list,获取每个用户数据
        for (int i = 0; i < list.size() ; i++) {
            User user = list.get(i);

            //7.每个用户的数据占一行
            row = sheet.createRow(i+1);

            cell = row.createCell(0);
            cell.setCellValue(user.getId());

            cell = row.createCell(1);
            cell.setCellValue(user.getName());

            cell = row.createCell(2);
            cell.setCellValue(user.getPhone());

            cell = row.createCell(3);
            cell.setCellValue(user.getSex());

            cell = row.createCell(4);
            cell.setCellValue(user.getIdNumber());

            cell = row.createCell(5);
            cell.setCellValue(user.getAvatar());

            cell = row.createCell(6);
            cell.setCellValue(user.getStatus());
        }


        //8.将工作薄保存到磁盘上
        FileOutputStream pos = new FileOutputStream("e:/user.xlsx");
        wk.write(pos);
        pos.close();
    }


    /**
     * 07版本读取所有行
     * @throws IOException
     * @throws InvalidFormatException
     */
    @Test
    public void testReadExcel() throws IOException, InvalidFormatException {
        //1.加载excel形成工作薄对象
        Workbook wk = new XSSFWorkbook(new File("e:/user.xlsx"));

        //2.读取工作薄中sheet表
        Sheet sheet = wk.getSheetAt(0);

        //3.获取表中的标题行
        Row row = sheet.getRow(0);

        //4.获取标题行的所以单元格,输出内容
        int num = row.getPhysicalNumberOfCells();
        //System.out.println(num);//7
        Cell cell = null;
        for (int i = 0; i < num; i++) {
            cell = row.getCell(i);
            String value = cell.getStringCellValue();

            System.out.print(value+"\t");
        }
        System.out.println();

        int num2 = sheet.getPhysicalNumberOfRows();
        //System.out.println(num2);//4

        //5.获取数据行内容
        for (int i = 1; i < num2; i++) {
            row = sheet.getRow(i);

            double id = row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String phone = row.getCell(2).getStringCellValue();
            String sex = row.getCell(3).getStringCellValue();
            String idNum = row.getCell(4).getStringCellValue();
            String avater = row.getCell(5).getStringCellValue();
            double status = row.getCell(6).getNumericCellValue();

            System.out.println(id+"\t"+name+"\t"+phone+"\t"+sex+"\t"+idNum+"\t"+avater+"\t"+status);

        }
    }
}
