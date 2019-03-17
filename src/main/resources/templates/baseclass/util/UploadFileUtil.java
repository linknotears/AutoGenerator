package ${basePackage}.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadFileUtil {
	/**
	 * 
	 * @param imagefile
	 * @param servletContext
	 * @return 返回文件成功上传的位置，上传不成功返回null
	 */
	public static String getUploadPath(CommonsMultipartFile cmfile,ServletContext servletContext,String dir){
		String sqlPath = null;
		if(cmfile!=null){
			if(!cmfile.isEmpty()){
				String imagename = cmfile.getOriginalFilename();
				String uuid = UUID.randomUUID().toString().replace("-", "");
				String path = servletContext.getRealPath("/");
				sqlPath = "upload/+"+dir+"+/"+uuid+"_"+imagename;
				File newfile = new File(path+"/"+sqlPath);
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
