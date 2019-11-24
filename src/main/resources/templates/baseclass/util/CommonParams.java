package ${cfg.basePackage}.util;

public class CommonParams {
    public static final String UPLOAD_PATH;
    static{
    	//获取jar包所在路径(不打jar包情况下显示的是项目路径)
		String rootPath = System.getProperty("user.dir");
		String uploadPath = rootPath + "/SpringBootUpload/";
		UPLOAD_PATH = uploadPath.replace("\\", "/");
        System.out.println("UPLOAD_PATH="+UPLOAD_PATH);
		/*
		 * String os = System.getProperty("os.name");
		 * if(os.toLowerCase().startsWith("win")){ IMG_PATH = "D:/WebUpload/"; }else{
		 * IMG_PATH = "/root/web_upload/"; }
		 */

    }
}
