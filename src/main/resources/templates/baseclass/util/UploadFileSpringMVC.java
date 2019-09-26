package ${cfg.basePackage}.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileSpringMVC {
	/**
	 * 
	 * @param imagefile
	 * @param servletContext
	 * @return 返回文件成功上传的位置，上传不成功返回null 
	 */
	public static String getUploadPath(MultipartFile cmfile,String outDir,String oldFilePath,ServletContext servletContext){
		String sqlPath = null;
		if(cmfile!=null){
			if(!cmfile.isEmpty()){
				String imagename = cmfile.getOriginalFilename();
				String suffix = imagename.substring(imagename.lastIndexOf("."));
				String uuid = UUID.randomUUID().toString().replace("-", "");
				String webPath = servletContext.getRealPath("/");
				sqlPath = "/upload/" + outDir + "/" + uuid + "_" + (new Date().getTime()) + suffix;
				File newfile = new File(webPath+"/"+sqlPath);
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
				
				//运输文件
				try {
					cmfile.transferTo(newfile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sqlPath;
	}
}
