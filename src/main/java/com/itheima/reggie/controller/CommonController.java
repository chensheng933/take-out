package com.itheima.reggie.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.itheima.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("common")
public class CommonController {

    @Value("${reggie.path}")
    String dirPath;

    /**
     * 上传图片
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    /*参数名字必须和提交过来的数据的name属性保持一致*/
    public R upload(MultipartFile file) throws IOException {
        //1.判断目录文件是否存在,若不存在就创建
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        //2.获取文件的原始名称
        String filename = file.getOriginalFilename();
        //2.1 获取文件的后缀名
        String extName = FileUtil.extName(filename); // jpg

        //2.2 为了防止同名文件被覆盖,给文件起个唯一的名字
        filename = IdUtil.fastUUID()+"."+extName;

        //3.将文件保存在指定目录中
        file.transferTo(new File(dirFile,filename));

        //4.返回文件的名称
        return R.success(filename);
    }

    /**
     * 下载图片
     * @param name
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //1.创建输入流
        FileInputStream is = new FileInputStream(dirPath + name);

        //2.获取输出流
        ServletOutputStream os = response.getOutputStream();

        //3.流的对拷
        IoUtil.copy(is,os);

        os.close();
        is.close();
    }
}
