package generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
public class GeneratorCode {
	public static void main(String[] args) throws InterruptedException {
		//AutoGenerator.analyzeData(ConfigBuilder config)中有模板可用的一些内置属性
		//用来获取mybatis-plus.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("mybatis/mybatis-plus");
        AutoGenerator mpg = new AutoGenerator();
    	final String basePath = "/"+rb.getString("basePackage").replace(".", "/");
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(rb.getString("OutputDir"));
        gc.setFileOverride(true);
        gc.setOpen(false);//不打开目录
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor(rb.getString("author"));
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");//本人不习惯加I
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        //设置不覆盖文件
        if(!"true".equals(rb.getString("isOverFile"))){        	
        	gc.setFileOverride(false);
        }
        
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        
        dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
            	System.out.println("转换前类型：" + fieldType);
                if(fieldType.contains("decimal")){
                	fieldType = "double";
                }
                System.out.println("转换后类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });
        
        dsc.setDriverName(rb.getString("jdbc.driver"));
        dsc.setUrl(rb.getString("jdbc.url"));
        dsc.setUsername(rb.getString("jdbc.user"));
        dsc.setPassword(rb.getString("jdbc.pwd"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[] { "SYS_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        //自己加的，获取需要逆向的所有表
        String tablesStr = rb.getString("tables");
        String[] tables = null;
        if(tablesStr!=null){
        	tables = tablesStr.split(",");
            //去空格
            for(int i = 0; i < tables.length; i++){
            	tables[i] = tables[i].trim();
            }
        }
        
        strategy.setInclude(tables); // 需要生成的表
        //strategy.setExclude(new String[]{"test"}); // 排除生成的表
        mpg.setStrategy(strategy);

        // 包配置(java文件)
        PackageConfig pc = new PackageConfig();
        pc.setParent(rb.getString("basePackage"));//设置父包名
        // pc.setModuleName("tbldept");//模块名称，单独生成模块时使用！！！！！！！！！！！
        pc.setController("controller");//这里是父包名+controller
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);
        
        //属性注入用于传入模板
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-rb");
                
                //循环读取配置信息
                Enumeration<String> keys = rb.getKeys();
                System.out.println("===========configParameters=========");
                while(keys.hasMoreElements()){
                	String key = keys.nextElement();
                	//循环替换.有的字母转换成大写
                	String newkey = key;
                	int index = 0;
                	while((index = newkey.indexOf(".") )!= -1){
                		String head = newkey.substring(0,index);
                		String center = newkey.substring(index+1,index+2).toUpperCase();
                		String end = newkey.substring(index+2);
                		newkey = head + center + end;
                	}
                	System.out.println("newkey="+newkey);
                	
                	map.put(newkey, rb.getString(key));
                }
                
                //判断mysql版本
                String url = rb.getString("jdbc.url");
                String driver = rb.getString("jdbc.driver");
                if("5.0".equals(rb.getString("mysql.version"))){
                	String[] urltemp = url.split("\\?");
                	url = urltemp[0] + "?useUnicode=true&allowMultiQueries=true&characterEncoding=UTF-8";
                	driver = "com.mysql.jdbc.Driver";
                }
                map.put("jdbcUrl", url);
                map.put("jdbcDriver", driver);
                
                
                this.setMap(map);
            }
        };

        // 调整 xml 生成目录演示（这里既可以设置模板路径，也可以设置输出路径）
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return rb.getString("OutputDirConfig")+ "/mybatis/mappers/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        
        if(!"false".equals(rb.getString("isInit"))){
        	//生成基础类
	        {
	        	String templateDir = "baseclass";
	        	String outPath = rb.getString("OutputDir")+ basePath;
	        	templatesTo(focList,templateDir,outPath);
	        }
	        
	        //拷贝配置
	        {
	        	String templateDir = "config";
	        	String outPath = rb.getString("OutputDirConfig");
	        	templatesTo(focList,templateDir,outPath);
	        }
	        
	        //复制不需要解析的文件
	        //复制web文件
	        {
	        	templatesToNotAnalyze("web",rb.getString("OutputDirWeb"));
	        }
        }
        //更改Service输出名
        /*focList.add(new FileOutConfig("/templates/service.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return rb.getString("OutputDir")+ basePath+"/service/" + tableInfo.getEntityName() + "Service.java";
            }
        });*/
        //设置覆盖entity
        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return rb.getString("OutputDir")+ basePath+"/entity/" + tableInfo.getEntityName() + ".java";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        
        /**
         * 1.测试结果：首先查找本项目类路径有没有templates模板文件，再查找jar包类路径下的templates
         * 2.相比官方的逆向工具，mybatis-plus再次生成源文件的方法是覆盖
         */
        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();//可设置模板路径
        tc.setXml(null);
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
        System.out.println("执行完成!");
    }

	private static void templatesToNotAnalyze(String templateDir,String outDir){
		System.out.println("-----------------templateDir = " + templateDir + "-----------------");
		//遍历配置模板
        URL url = GeneratorCode.class.getClass().getResource("/templates/"+templateDir);
		String path = url.getPath();		//要遍历的路径
		File file = new File(path);		//获取其file对象
		List<String> shortPaths = getShortPath(file,new ArrayList<String>(),templateDir);
        //添加配置模板
		for(String shortPath : shortPaths){
			String templatePath = "/templates/"+templateDir+shortPath;    
            String outPath = outDir+shortPath;
            InputStream in = GeneratorCode.class.getClass().getResourceAsStream(templatePath);
            OutputStream out = null;
            
            File outFile = new File(outPath);
            //文件不存在就创建
            File parentFile = outFile.getParentFile();
            if(!parentFile.exists()){
            	parentFile.mkdirs();
            }
            
            byte[] buffer = new byte[1024*1024];
            int len = 0;
            try {
            	//read()一次读一个字节
            	out =new FileOutputStream(outFile);
            	while((len = in.read(buffer))!=-1){
            		out.write(buffer, 0, len);
            	}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
            
        }
	}
	
	private static void templatesTo(List<FileOutConfig> fileOutConfigList,String templateDir,final String outDir){
		System.out.println("-----------------templateDir = " + templateDir + "-----------------");
		//遍历配置模板
        URL url = GeneratorCode.class.getClass().getResource("/templates/"+templateDir);
		String path = url.getPath();		//要遍历的路径
		File file = new File(path);		//获取其file对象
		List<String> shortPaths = getShortPath(file,new ArrayList<String>(),templateDir);
        //添加配置模板
		for(final String shortPath : shortPaths){
			fileOutConfigList.add(new FileOutConfig("/templates/"+templateDir+shortPath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outDir+shortPath;
                }
            });
        }
	}
	
	private static List<String> getShortPath(File file,List<String> list,String splitName){
		File[] fs = file.listFiles();
		for(File f : fs){
			if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
				getShortPath(f,list,splitName);
			if(f.isFile()){		//若是文件，直接打印
				String fStr = f.toString();
				int index = f.toString().lastIndexOf("templates\\"+splitName+"\\");
				String shortPath = fStr.substring(index+splitName.length()+10);
				list.add(shortPath);
				System.out.println("shortPath:"+shortPath);
			}
		}
		return list;
	}
}

