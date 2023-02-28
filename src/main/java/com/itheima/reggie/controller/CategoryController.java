package com.itheima.reggie.controller;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询
     * @param pageNum 查询页码
     * @param pageSize 每页大小
     * @return
     */
    @GetMapping("page")
    public R findByPage(@RequestParam("page") int pageNum, int pageSize){
        Page<Category> page = categoryService.findByPage(pageNum,pageSize);
        return R.success(page);
    }

    @PostMapping
    public R save(@RequestBody Category category, HttpSession session){
        //1.获取当前登陆者id
        Long userId = (Long) session.getAttribute("employee");

        //2.给category对象设置用户id
        category.setCreateUser(userId);
        category.setUpdateUser(userId);

        //3.调用service完成新增操作
        categoryService.save(category);

        //4.返回结果
        return R.success(null);
    }

    @PutMapping
    public R update(@RequestBody Category category,HttpSession session){
        //1.获取当前登陆者id
        Long userId = (Long) session.getAttribute("employee");

        //2.给category设置更新人id
        category.setUpdateUser(userId);

        //3.调用service完成更新
        categoryService.update(category);

        //4.返回成功
        return R.success(null);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R delete(Long id){
        //调用service完成删除操作
        categoryService.delete(id);

        return R.success(null);
    }

    @GetMapping("list")
    public R findByType(Integer type){
        List<Category> list = categoryService.findByType(type);
        return R.success(list);
    }

    /**
     * 导出Excel
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setHeader("content-disposition","attachment;filename=category.xlsx");

        //1. 读取模板的输入流
        InputStream inputStream = this.getClass().getResourceAsStream("/excel/category.xlsx");

        //2。 使用模板的输入流构建一个工作薄
        Workbook workbook = new XSSFWorkbook(inputStream);

        //3. 得到工作单
        Sheet sheet = workbook.getSheetAt(0);

        //4. 读取第一行,提取第一行的样式，并且存储到集合中
        Row row = sheet.getRow(1);
        List<CellStyle> cellStylesList= new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            //得到单元格的样式
            CellStyle cellStyle = row.getCell(i).getCellStyle();
            cellStylesList.add(cellStyle);
        }

        //5. 得到数据,遍历数据，把数据写入到excel
        List<Category> categoryList = categoryService.list();
        for (int i = 0; i < categoryList.size(); i++) {
            //一个类别就对应一行
            row =  sheet.createRow(i+1);
            Category category = categoryList.get(i);
            //把类别信息设置到单元格上

            //类别类型
            Cell cell = row.createCell(0);
            //设置单元格内容
            cell.setCellValue(category.getType());
            cell.setCellStyle(cellStylesList.get(0));

            //类别名字
            cell = row.createCell(1);
            //设置单元格内容
            cell.setCellValue(category.getName());
            cell.setCellStyle(cellStylesList.get(1));


            //类别排序
            cell = row.createCell(2);
            //设置单元格内容
            cell.setCellValue(category.getSort());
            cell.setCellStyle(cellStylesList.get(2));

            //创建时间
            cell = row.createCell(3);
            //设置单元格内容
            cell.setCellValue(category.getCreateTime().toLocalDate().toString());
            cell.setCellStyle(cellStylesList.get(3));


            //创建时间
            cell = row.createCell(4);
            //设置单元格内容
            cell.setCellValue(category.getUpdateTime().toLocalDate().toString());
            cell.setCellStyle(cellStylesList.get(4));
        }

        ServletOutputStream outputStream = response.getOutputStream();
        //6. 把工作薄写出
        workbook.write(outputStream);

        //关闭流
        inputStream.close();
        outputStream.close();
    }
}
