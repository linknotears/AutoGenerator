package ${cfg.basePackage}.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFileUniversal {
		// 上传配置
		private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
		private static final int MAX_FILE_SIZE      = 1024 * 1024 * 500; // 500MB
		private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 500; // 500MB

		public static <T> T copyProperties(HttpServletRequest request,Map<String,String> propAndDirMap,Class<T> entityClazz){
			// 检测是否为多媒体上传
	        if (!ServletFileUpload.isMultipartContent(request)) {
	        	System.err.println("Error: 表单必须包含 enctype=multipart/form-data");
	            return null;
	        }
	 
	        // 配置上传参数
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
	        factory.setSizeThreshold(MEMORY_THRESHOLD);
	        // 设置临时存储目录
	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	 
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        
	        // 设置最大文件上传值
	        upload.setFileSizeMax(MAX_FILE_SIZE);
	        // 设置最大请求值 (包含文件和表单数据)
	        upload.setSizeMax(MAX_REQUEST_SIZE);
	        // 中文处理
	        upload.setHeaderEncoding("UTF-8"); 

	        String uploadPath = request.getServletContext().getRealPath("/");
	        Map<String,String> pmap = new HashMap<String,String>();
	        //获取映射参数
	        Set<String> propSet = new HashSet<String>();
	        if(propAndDirMap != null){	        	
	        	propSet = propAndDirMap.keySet();
	        }
	        T entity = null;
			try {
				entity = entityClazz.newInstance();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
	        try {
	            // 解析请求的内容提取文件数据
	            @SuppressWarnings("unchecked")
	            List<FileItem> formItems = upload.parseRequest(request);
	 
	            if (formItems != null && formItems.size() > 0) {
	                // 迭代表单数据
	                for (FileItem item : formItems) { 
	                    // 处理在表单中的字段
	                	if(item.isFormField()) {
	                		pmap.put(item.getFieldName(), item.getString("utf-8"));
	                	}
	                	// 处理不在表单中的字段
	                    if (!item.isFormField()) {
	                    	if(item.getSize() == 0){
	                    		continue;
	                    	} 
	                    	String fieldName = item.getFieldName();
	                    	String outDir = null;
	                    	String prop = null;
                    		for(String propTemp : propSet){
	                    		if(propTemp.equals(fieldName)){
	                    			outDir = propAndDirMap.get(propTemp);
	                    			prop = propTemp;
	                    		}
	                    	}
                    		//非设置好的的文件要
                    		if(outDir==null){
                    			continue;
                    		}
	                    	
	                        String fileName = new File(item.getName()).getName();
	                        String uuid = UUID.randomUUID().toString().replace("-","");
	                        String urlPath = "upload/" + outDir + "/" +uuid+"_"+fileName;
	                        String filePath = uploadPath + File.separator + urlPath;
	                        
	                        File storeFile = new File(filePath);
	                        
	                        // 如果目录不存在则创建
	            	        File uploadDir = storeFile.getParentFile();
	            	        if (!uploadDir.exists()) {
	            	            uploadDir.mkdirs();
	            	        }
	                        
	                        // 在控制台输出文件的上传路径
	                        System.out.println("输出路径："+filePath);
	                        // 保存文件到硬盘
	                        item.write(storeFile);
	                        String methodStr = prop.substring(0,1).toUpperCase()+prop.substring(1);
	                        //设置文件url
	                        Method setmethod = entity.getClass().getDeclaredMethod("set"+methodStr, String.class);
	                        Method getmethod = entity.getClass().getDeclaredMethod("get"+methodStr);
	                        //备份旧文件url
	                        Object obj = getmethod.invoke(entity);
	                        
	                        setmethod.invoke(entity, urlPath);
	                        //删除旧文件
	                        if(obj != null){
		                        filePath = uploadPath + File.separator + obj;
		                        
		                        File oldFile = new File(filePath);
		                        if(oldFile.exists()){
		                        	oldFile.delete();
		                        }
	                        }
	                        System.out.println("文件上传成功!");
	                    }
	                }
	            }
	        } catch (Exception ex) {
	        	System.err.println( "错误信息: " + ex.getMessage());
	        }
			try {
				BeanUtils.copyProperties(entity, pmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return entity;
		}
}
