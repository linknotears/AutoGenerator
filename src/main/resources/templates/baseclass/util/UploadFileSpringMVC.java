package ${cfg.basePackage}.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import ${cfg.basePackage}.util.CommonParams;
import org.springframework.web.multipart.MultipartFile;
import java.util.Random;

public class UploadFileSpringMVC {
	public static String getUploadPath(MultipartFile mtfile,String outDir,String oldFilePath,ServletContext servletContext){
		String sqlPath = null;
		if(mtfile!=null){
			if(!mtfile.isEmpty()){
				String imagename = mtfile.getOriginalFilename();
				String suffix = imagename.substring(imagename.lastIndexOf("."));
				String filename = verifyCode(8);
				String webPath = CommonParams.UPLOAD_PATH;//servletContext.getRealPath("/");
				SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sf2 = new SimpleDateFormat("HHmmssSS");
				sqlPath = "/" + outDir + "/" + sf1.format(new Date()) + "/" + sf2.format(new Date()) + filename + suffix;
				File newfile = new File(webPath + "/" + sqlPath);
				//如果父目录不存在就创建
				File parentFile = newfile.getParentFile();
				if(!parentFile.exists()){
					parentFile.mkdirs();
				}
				//删除老文件
				if(oldFilePath!=null){
					File oldFile = new File(webPath+"/"+oldFilePath);
					oldFile.delete();
				}
				log.info("webPath=" + webPath + ",sqlPath=" + sqlPath);
				//运输文件
				try {
					mtfile.transferTo(newfile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sqlPath;
	}


	public static String verifyCode(int length){
		char[] c= "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm_".toCharArray();//获取包含26个字母大小写和数字的字符数组
		Random rd = new Random();
		StringBuilder code = new StringBuilder();
		for (int k = 0; k < length; k++) {
			int index = rd.nextInt(c.length);//随机获取数组长度作为索引
			code.append(c[index]);//循环添加到字符串后面
		}
		return code.toString();
	}
}
