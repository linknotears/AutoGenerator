package ${cfg.basePackage}.util;

public class CommonParams {
    public static final String IMG_PATH;
    static{
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            IMG_PATH = "D:/WebUpload/";
        }else{
            IMG_PATH = "/root/web_upload/";
        }

    }
}
