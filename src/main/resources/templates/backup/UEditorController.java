package com.colin.teaching_aid_sys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.colin.teaching_aid_sys.util.CommonParams;

@RequestMapping("ueditor")
@RestController
public class UEditorController {
    private String imgUploadDir = CommonParams.UPLOAD_PATH;
    private static final Logger log = LoggerFactory.getLogger(UEditorController.class);

    /**
     * @throws JSONException 
     * @Description 获取config.json配置文件
     */
    @RequestMapping("getConfig")
    public void getConfig(HttpServletResponse response) throws JSONException {
        org.springframework.core.io.Resource res = new ClassPathResource("config.json");
        InputStream is = null;
        response.setHeader("Content-Type", "text/html");
        try {
            is = new FileInputStream(res.getFile());
            StringBuffer sb = new StringBuffer();
            byte[] b = new byte[1024];
            int length;
            while (-1 != (length = is.read(b))) {
                sb.append(new String(b, 0, length, "utf-8"));
            }
            String result = sb.toString().replaceAll("/\\*(.|[\\r\\n])*?\\*/", "");
            JSONObject json = new JSONObject(result);
            PrintWriter out = response.getWriter();
            out.print(json.toString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 富文本上传的文件地址
     *
     * @param files
     * @throws JSONException 
     */
    @PostMapping("upload")
    public Map<String, Object> upload(@RequestParam("upfile") MultipartFile upfile, HttpServletResponse response) throws IOException, JSONException {
    	Map<String, Object> params = new HashMap<String, Object>();
        try{
             String basePath = CommonParams.UPLOAD_PATH;  //与properties文件中lyz.uploading.url相同，未读取到文件数据时为basePath赋默认值
             String  visitUrl = "/ueditor/file/"; //与properties文件中lyz.visit.url相同，未读取到文件数据时为visitUrl赋默认值
             String ext = upfile.getOriginalFilename();
             ext = ext.substring(ext.lastIndexOf("."));
             String fileName = verifyCode(6).concat("_").concat(String.valueOf(System.currentTimeMillis())).concat(ext);
             StringBuilder sb = new StringBuilder();
             //拼接保存路径
             sb.append(basePath).append(visitUrl).append("/").append(fileName);
             visitUrl = visitUrl.concat(fileName);
             File f = new File(sb.toString());
             if(!f.exists()){
                 f.getParentFile().mkdirs();
             }
             OutputStream out = new FileOutputStream(f);
             FileCopyUtils.copy(upfile.getInputStream(), out);
             params.put("state", "SUCCESS");
             params.put("url", visitUrl);
             params.put("size", upfile.getSize());
             params.put("original", fileName);
             params.put("type", upfile.getContentType());
        } catch (Exception e){
             params.put("state", "ERROR");
        }
         return params;

    }
    
    public String verifyCode(int length){
        char[] c= "1234567890qwertyuiopasdfghjklzxcvbnm_".toCharArray();//获取包含26个字母大小写和数字的字符数组
        Random rd = new Random();
        StringBuilder code = new StringBuilder();
        for (int k = 0; k < length; k++) {
            int index = rd.nextInt(c.length);//随机获取数组长度作为索引
            code.append(c[index]);//循环添加到字符串后面
        } 
        System.out.println(code);
        return code.toString();
    }
    
    
    /*
    <script type="text/javascript" src="/js/plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="/js/plugins/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" src="/js/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript">
		$(function(){
			var editor = UE.getEditor('container');
			UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
		    UE.Editor.prototype.getActionUrl = function (action) {
		        if (action == 'uploadimage' || action == 'uploadfile'|| action == 'uploadvideo' || action == 'uploadscrawl') {
		            return '/ueditor/upload';
		        } else {
		            return this._bkGetActionUrl.call(this, action);
		        }
		    };
		});
	</script>
    */
}
