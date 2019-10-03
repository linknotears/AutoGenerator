package generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class GeneratorCode {
	public static String mapperSuffix;
	public static String entitySuffix;
	public static String serviceSuffix;
	public static String serviceImplSuffix;
	public static String controllerSuffix;
	public static String xmlSuffix;
	
	public static void main(String[] args) throws InterruptedException {
		//AutoGenerator.analyzeData(ConfigBuilder config)中有模板可用的一些内置属性
		//用来获取mybatis-plus.properties文件的配置信息
        //配置文件改为yaml
        YamlMapFactoryBean yaml = new YamlMapFactoryBean();
        yaml.setResources(new ClassPathResource("mybatis/mybatis-plus.yml"));
        Map<String,Object> yamlMap = yaml.getObject();
        for(Entry<String, Object> entry : yamlMap.entrySet()) {
        	Object value = entry.getValue();
			if(value instanceof String) {
        		String val = value.toString();
        		Pattern pattern = Pattern.compile("\\*\\{.+\\}");
        		Matcher matcher = pattern.matcher(val);
        		while(matcher.find()) {
        			String group = matcher.group();
        			String searchStr =  group.replace("*", "").replace("{", "").replace("}", "").trim();
        			String replaceStr = yamlMap.get(searchStr).toString();
        			val = val.replace(group, replaceStr);
        		}
        		entry.setValue(val);
        	}
        }
        
        AutoGenerator mpg = new AutoGenerator();
    	String basePath = "/"+((String)yamlMap.get("basePackage")).replace(".", "/");
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir((String)yamlMap.get("OutputDir"));
        gc.setFileOverride(true);
        gc.setOpen(false);//不打开目录
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor((String)yamlMap.get("author"));
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");//本人不习惯加I
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        //在注入时做处理
        gc.setFileOverride(true);
        //设置为date,而不是LocalDateTime
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);
        //mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        
        dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
        	/*@Override
        	public DbColumnType processTypeConvert(String fieldType) {
        		System.out.println("转换前类型：" + fieldType);
                if(fieldType.contains("decimal")){
                	fieldType = "double";
                }
                System.out.println("转换后类型：" + fieldType);
        		return super.processTypeConvert(fieldType);
        	}*/
            //3.1版本用
        	@Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
            	System.out.println("转换前类型：" + fieldType);
                if(fieldType.contains("decimal")){
                	fieldType = "double";
                }
                System.out.println("转换后类型：" + fieldType);
            	return super.processTypeConvert(globalConfig, fieldType);
            }
        });
        
        dsc.setDriverName((String)yamlMap.get("jdbc.driver"));
        dsc.setUrl((String)yamlMap.get("jdbc.url"));
        dsc.setUsername((String)yamlMap.get("jdbc.user"));
        dsc.setPassword((String)yamlMap.get("jdbc.pwd"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[] { "SYS_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        //自己加的，获取需要逆向的所有表
        String tablesStr = (String)yamlMap.get("tables");
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
        pc.setParent((String)yamlMap.get("basePackage"));//设置父包名
        // pc.setModuleName("tbldept");//模块名称，单独生成模块时使用！！！！！！！！！！！
        pc.setController((String)yamlMap.get("package.suffix.controller"));//这里是父包名+controller
        pc.setService((String)yamlMap.get("package.suffix.service"));
        pc.setServiceImpl((String)yamlMap.get("package.suffix.serviceImpl"));
        pc.setEntity((String)yamlMap.get("package.suffix.entity"));
        pc.setMapper((String)yamlMap.get("package.suffix.mapper"));
        mpg.setPackageInfo(pc);

        //储存包后缀路径
        mapperSuffix = ((String)yamlMap.get("package.suffix.mapper")).replace(".", "/");
        entitySuffix = ((String)yamlMap.get("package.suffix.entity")).replace(".", "/");
        serviceSuffix = ((String)yamlMap.get("package.suffix.service")).replace(".", "/");
        serviceImplSuffix = ((String)yamlMap.get("package.suffix.serviceImpl")).replace(".", "/");
        controllerSuffix = ((String)yamlMap.get("package.suffix.controller")).replace(".", "/");
        xmlSuffix = ((String)yamlMap.get("package.suffix.xml")).replace(".", "/");
        
        // 调整 xml 生成目录演示（这里既可以设置模板路径，也可以设置输出路径）
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
            	return (String)yamlMap.get("OutputDirConfig")+ "/" + xmlSuffix + "/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        //设置覆盖entity
        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return (String)yamlMap.get("OutputDir")+ basePath + "/" + entitySuffix + "/" + tableInfo.getEntityName() + ".java";
            }
        });
        
        //初始化
        //生成基础类（生成的类对基础类有依赖，不作为初始化模板）
        {
        	String templateDir = "baseclass";
        	String outPath = (String) yamlMap.get("OutputDir") + basePath;
        	templatesTo(focList,templateDir,outPath);
        }
        
        Map<String,List<String>> colExcludeMap = new HashMap<String,List<String>>();        
        {
        	//创建模板输出信息
        	if(!"false".equals((String)yamlMap.get("createWebtemplates"))) {
        		//模板配置
        		List<Map<String,Object>> webtemplates = (List<Map<String, Object>>) yamlMap.get("webtemplates");
            	//自定义web模板
            	if(webtemplates!=null){
    	        	for(Map<String,Object> tempInfo : webtemplates){
    	        		//记录排除输出字段
    	        		String excludeStr = (String) tempInfo.get("exclude");
    	        		if(excludeStr != null) { 
    	        			List<String> excludes = new ArrayList<>(Arrays.asList(excludeStr.split(",")));
    	        			//判断排除所有表
    	        			for(int i = 0; i < excludes.size(); i++) {
    	        				if(excludes.get(i).matches("^all\\..+")) {
    	        					String excludeCol = excludes.get(i).replace("all.", "");
    	        					excludes.remove(i);
    	        					i--;
    	        					for( String table : (List<String>)tempInfo.get("tables")) {
    	        						String tableAndCol = table + "." + excludeCol;
    	        						if(!excludes.contains(tableAndCol)) {
        	        						excludes.add(tableAndCol);
    	        						}
    	        					}
    	        				}
    	        			}
    	        			//判断include
    	        			String includeStr = (String) tempInfo.get("include");
    	        			if(includeStr != null) { 
    	        				List<String> includes = Arrays.asList(excludeStr.split(","));
    	        				for(int i = 0; i < includes.size(); i++) {
    	        					String tableAndCol = includes.get(i);
    	        					if(excludes.contains(tableAndCol)) {
    	        						excludes.remove(i);
    	        					}
    	        				}
    	        			}
    	        			String templateStr = (String) tempInfo.get("template");
    	        			//截掉后缀
    	        			int index = templateStr.lastIndexOf(".");
    	        			if(index != -1){
    	        				templateStr = templateStr.substring(0, index);
    	        			}
    	        			//文件名换成正则
    	        			templateStr = templateStr.replace("${entity}", "[a-zA-Z]+") + "\\.[a-zA-Z]+";
    	        			colExcludeMap.put(templateStr, excludes);
    	        		}
    	        		//判断输出模板
    		        	focList.add(new FileOutConfig() {
    		                @Override
    		                public String outputFile(TableInfo tableInfo) {
    		                	List<String> outTableList = (List<String>) tempInfo.get("tables");
    		                	for (int i = 0; i < outTableList.size(); i++) {
    		                		String yamlTableName = outTableList.get(i);
    		                		String sqlTableName = tableInfo.getName();
    		                		if(sqlTableName.equals(yamlTableName)){
    		                			this.setTemplatePath("/templates/web/assign/" + tempInfo.get("template"));
    		                			//首字母转小写
    		                			String tip = tableInfo.getEntityName().substring(0, 1).toLowerCase() + tableInfo.getEntityName().substring(1);
    		                			String filePath = ((String) tempInfo.get("outpath")).replace("${entity}", tip);
    		                			return (String)yamlMap.get("OutputDirTemplates") + "/" + filePath;
    		                		}
    							}
    		                	//置空
    		                	//避免因之前数据引起空指针
    		                	this.setTemplatePath(null);
    		                	return null;
    		                }
    		            });
    	        	}
            	}
        	}
        	

        	//拿共有参数时适用，非共用不适用
	        //创建spring等配置拷贝配置
        	if(!"false".equals((String)yamlMap.get("createConfig"))){
	        	String templateDir = "config";
	        	String outPath = (String)yamlMap.get("OutputDirConfig");
	        	templatesTo(focList,templateDir,outPath);
        	}
        	
	        
	        //复制不需要解析的文件
	        //复制web文件
        	if(!"false".equals((String)yamlMap.get("createNotAnalyze"))){
	        	templatesToNotAnalyze("web/not-analyze",(String)yamlMap.get("OutputDirStatic"));
	        }
	        
	        //拷贝解析的web
        	if(!"false".equals((String)yamlMap.get("createAnalyze"))){
	        	String templateDir = "web/analyze";
	        	String outPath = (String)yamlMap.get("OutputDirTemplates");
	        	templatesTo(focList,templateDir,outPath);
	        }
        }
        //属性注入用于传入模板
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
			@Override
            public void initMap() {
				//获取备注组建者
				ConfigBuilder config = this.getConfig();
            	//获取全局配置
            	GlobalConfig globalConfig = config.getGlobalConfig();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", globalConfig.getAuthor() + "-rb");
                
				//指定view显示类型
				map.put("propertyType", (Map<String,Object>) yamlMap.get("propertyType"));
                
                //判断是否覆盖文件
                this.setFileCreate(new IFileCreate() {
					@Override
					public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
						//判断文件路径是否为空
						if(filePath==null || "".equals(filePath)){
							return false;
						}
						//mapper.java、entity.java、xxxMapper.xml覆盖，其他不覆盖
						if(fileType==FileType.MAPPER||fileType==FileType.ENTITY||fileType==FileType.XML){
							return true;
						}
						//不存在才创建
						File file = new File(filePath);
						if(!file.exists()){
							//创建父文件夹
							this.checkDir(filePath);
							//模板排除字段
							//截取文件名
							int index = filePath.lastIndexOf("/");
							String fileName = filePath.substring(index + 1);
							//重置排除信息
							configBuilder.getInjectionConfig().getMap().put("colExclude", null);
							colExcludeMap.forEach(new BiConsumer<String,List<String>>(){
								@Override
								public void accept(String key, List<String> value) {
									if(fileName.matches(key)){
										Map<String,Map<String,Boolean>> colExcludeMap = new HashMap<String,Map<String,Boolean>>();
										value.forEach(colStr ->{
											String[] tableAndCol = colStr.split("\\.");
											Map<String,Boolean> colMap = null;
											if(colExcludeMap.get(tableAndCol[0]) != null){
												colMap = colExcludeMap.get(tableAndCol[0]);
											}else{
												colMap = new HashMap<String,Boolean>();
												colExcludeMap.put(tableAndCol[0], colMap);
											}

											colMap.put(tableAndCol[1], true);
										});
										//注入排除信息
										configBuilder.getInjectionConfig().getMap().put("colExclude", colExcludeMap);
									}
								}});
							return true;
						}
						return false;
					}
				});
                
                //循环读取配置信息
                System.out.println("===========configParameters=========");
                for(String key : yamlMap.keySet()){
                	//循环替换.有的字母转换成大写
                	if(yamlMap.get(key) instanceof String){
                		String newkey = key;
                    	int index = 0;
                    	while((index = newkey.indexOf(".") )!= -1){
                    		String head = newkey.substring(0,index);
                    		String center = newkey.substring(index+1,index+2).toUpperCase();
                    		String end = newkey.substring(index+2);
                    		newkey = head + center + end;
                    	}
                    	System.out.println("newkey="+newkey);
                    	
                    	map.put(newkey, (String)yamlMap.get(key));
                	}
                }
                
                //判断mysql版本
                String url = (String)yamlMap.get("jdbc.url");
                String driver = (String)yamlMap.get("jdbc.driver");
                if(!"8.0".equals((String)yamlMap.get("mysql.version"))){
                	String[] urltemp = url.split("\\?");
                	url = urltemp[0] + "?useUnicode=true&allowMultiQueries=true&characterEncoding=UTF-8";
                	driver = "com.mysql.jdbc.Driver";
                }
                map.put("jdbcUrl", url);
                map.put("jdbcDriver", driver);
                
                List<TableInfo> tableInfoList = config.getTableInfoList();
                HashMap<String,String> absoluteNameStrMap = new HashMap<String,String>();
                //使结果集的column变得绝对不一样
                for(TableInfo tableInfo : tableInfoList){
                	//用来存放绝对字段名
                	String tableName = tableInfo.getName();
                	StringBuffer fieldNamesBuf = new StringBuffer(); 
                	for(TableField field  : tableInfo.getFields()){
                		String fieldName = field.getName();
                		String absoluteName = tableName + "_" + fieldName;
                		//传入模板
                		fieldNamesBuf
                		.append("`")
                		.append(tableName)
                		.append("`");
                		fieldNamesBuf.append(".");
                		fieldNamesBuf.append(fieldName);
                		fieldNamesBuf.append(" AS ");
                		fieldNamesBuf.append(absoluteName);
                		fieldNamesBuf.append(",");
                	}
                	String fieldNamesStr = fieldNamesBuf.toString();
                	fieldNamesStr = fieldNamesStr.substring(0,fieldNamesStr.length()-1);
                	//替换字段绝对别名字符串
                	absoluteNameStrMap.put(tableName, fieldNamesStr);
                }
                map.put("absoluteNameStrMap", absoluteNameStrMap);
                
                //设置联合表数据
                List<Map<String,Object>> yamlConjInforMap = (List<Map<String,Object>>)yamlMap.get("conj.infor");
                if(yamlConjInforMap != null){
	                Map<String, Object> conjInfoMap = getConjunctiveInfo(
	                		(String)yamlMap.get("basePackage")+".entity",
	            			tableInfoList,
	            			yamlConjInforMap,
	            			absoluteNameStrMap,
	            			null,null,null,null,null,null,null,
	            			0,0);
	                map.put("conjInfoMap", conjInfoMap);
                }

                //web模板配置信息
                map.put("webtemplates", yamlMap.get("webtemplates"));
                //设置表信息map（表名对应表信息）
                HashMap<String,TableInfo> tableInfoMap = new HashMap<String,TableInfo>();
                tableInfoList.forEach(tableInfo -> {
                	tableInfoMap.put(tableInfo.getName(), tableInfo);
                });
                map.put("tableInfoMap", tableInfoMap);
                this.setMap(map);
            }
        };
        
        
        
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
            	out = new FileOutputStream(outFile);
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
	
	private static void templatesTo(List<FileOutConfig> fileOutConfigList,String templateDir,String outDir){
		System.out.println("-----------------templateDir = " + templateDir + "-----------------");
		//遍历配置模板
        URL url = GeneratorCode.class.getClass().getResource("/templates/"+templateDir);
		String path = url.getPath();		//要遍历的路径
		File file = new File(path);		//获取其file对象
		List<String> shortPaths = getShortPath(file,new ArrayList<String>(),templateDir);
        //添加配置模板
		for(String shortPath : shortPaths){
			fileOutConfigList.add(new FileOutConfig("/templates/"+templateDir+shortPath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                	String shortPathTemp = shortPath;
                	if("baseclass".equals(templateDir)) {
                		shortPathTemp = shortPath
    							.replace("controller", controllerSuffix)
    							.replace("entity", entitySuffix)
    							.replace("mapper", mapperSuffix)
    							.replace("service", serviceSuffix)
    							.replace( serviceSuffix + "/impl", serviceImplSuffix);
    							
    				}
                    return outDir + shortPathTemp;
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
				String fStr = f.toString().replace("\\","/");
				int index = fStr.lastIndexOf("templates/"+splitName+"/");
				String shortPath = fStr.substring(index + splitName.length() + 10);
				list.add(shortPath);
				System.out.println("shortPath:" + shortPath);
			}
		}
		return list;
	}
	/**
	 * @param tableInfoList
	 * @param conjInfoList
	 * @return
	 */
	//解析联合信息
	private static Map<String,Object> getConjunctiveInfo(
			String basePackage,
			List<TableInfo> tableInfoList,
			List<Map<String,Object>> conjInfoList,
			Map<String,String> absoluteNameStrMap,
			StringBuilder resultBuilder,
			StringBuilder conjTablesBuilder,
			StringBuilder resultColumnBuilder,
			StringBuilder whereBuilder,
			Map<String,Object> conjInfoMap,
			String conjtype,
			String condition,
			int count,
			Integer conjCount){
		if(conjInfoMap==null){
			conjInfoMap = new HashMap<String,Object>();
			count = 0;
			conjtype = (String) conjInfoList.get(0).get("conjtype");
			conjCount = 0;
		}

		for(Map<String,Object> ymlMap : conjInfoList){
			for(TableInfo tableInfo : tableInfoList){
				String ymltable = (String) ymlMap.get("table");
				if(ymltable.equals(tableInfo.getName())){
					//记录每一层的表名
					conjInfoMap.put("layer"+count, tableInfo);
					//每进入一层根据表名，新建一个映射
					if(conjInfoMap.get(tableInfo.getName()) == null){
						Map<String,Object> tableMap = new HashMap<String,Object>();
						conjInfoMap.put(tableInfo.getName(),tableMap);
						tableMap.put("conjEntityProp", new HashSet<String>());
					}else{
						Map<String,Object> tableMap = (Map<String, Object>) conjInfoMap.get(tableInfo.getName());
						HashSet<String> conjEntityProp = (HashSet<String>) tableMap.get("conjEntityProp");
						if(conjEntityProp==null){
							tableMap.put("conjEntityProp", new HashSet<String>());
						}
					}
					//ConjResultMap
					//开始
					if(count == 0){
						condition = (String)ymlMap.get("condition");
						//置零
						conjCount = new Integer(0);
						//重新建立一个StringBuilder
						resultBuilder = new StringBuilder();
						resultBuilder
						.append("\t<resultMap id=\"ConjResultMap\" type=\"")
						.append(basePackage)
						.append(".")
						.append(tableInfo.getEntityName())
						.append("\" >\n");
						
						//储存结果列
						resultColumnBuilder = new StringBuilder();
						resultColumnBuilder
						.append("\n\n\t<sql id=\"Conj_Column_List\">\n")
						.append("\t\t")
						.append(absoluteNameStrMap.get(tableInfo.getName()));
						
						//默认条件
						whereBuilder = new StringBuilder();
						
						//连接查询集合
						conjTablesBuilder = new StringBuilder();
						conjTablesBuilder
						.append("`")
						.append(tableInfo.getName())
						.append("`")
						.append("\n\t\t\t");
						
					}else{
						//添加结果列
						resultColumnBuilder
						.append(",")
						.append(absoluteNameStrMap.get(tableInfo.getName()));
						
						//之前的表
						TableInfo beforeTable = (TableInfo) conjInfoMap.get("layer"+(count-1));
						Set<String> set = (Set<String>) ((Map<String,Object>)conjInfoMap.get(beforeTable.getName())).get("conjEntityProp");
						String lowEntityname = tableInfo.getEntityName().substring(0,1).toLowerCase() + tableInfo.getEntityName().substring(1);
						if("many".equals(ymlMap.get("relation"))) {
							StringBuilder sb = new StringBuilder();
							sb
							.append("\tprivate List<")
							.append(tableInfo.getEntityName())
							.append("> ")
							.append(lowEntityname)
							.append("List;\n\n");
							//设置List包
							if(!beforeTable.getImportPackages().contains("java.util.List")){
								beforeTable.getImportPackages().add("java.util.List");
							}
							//get
							sb
							.append("\tpublic ")
							.append("List")
							.append("<")
							.append(tableInfo.getEntityName())
							.append(">")
							.append(" get")
							.append(tableInfo.getEntityName())
							.append("List() {\n")
							.append("\t\t")
							.append("return ")
							.append(lowEntityname)
							.append("List;\n")
							.append("\t}\n\n");
							//set
							sb
							.append("\tpublic ")
							.append("void ")
							.append("set")
							.append(tableInfo.getEntityName())
							.append("List(")
							.append("List<")
							.append(tableInfo.getEntityName())
							.append("> ")
							.append(lowEntityname)
							.append("List")
							.append(") {\n")
							.append("\t\t")
							.append("this.")
							.append(lowEntityname)
							.append("List = ")
							.append(lowEntityname)
							.append("List;\n")
							.append("\t}\n");
							
							set.add(sb.toString());
						}else{
							StringBuilder sb = new StringBuilder();
							sb
							.append("\tprivate ")
							.append(tableInfo.getEntityName())
							.append(" ")
							.append(lowEntityname)
							.append(";\n\n");
							
							//get
							sb
							.append("\tpublic ")
							.append(tableInfo.getEntityName())
							.append(" get")
							.append(tableInfo.getEntityName())
							.append("() {\n")
							.append("\t\t")
							.append("return ")
							.append(lowEntityname)
							.append(";\n")
							.append("\t}\n\n");
							//set
							sb
							.append("\tpublic ")
							.append("void ")
							.append("set")
							.append(tableInfo.getEntityName())
							.append("(")
							.append(tableInfo.getEntityName())
							.append(" ")
							.append(lowEntityname)
							.append("")
							.append(") {\n")
							.append("\t\t")
							.append("this.")
							.append(lowEntityname)
							.append(" = ")
							.append(lowEntityname)
							.append(";\n")
							.append("\t}\n");
							
							set.add(sb.toString());
						}
						String conjtypeEach = (String) ymlMap.get("conjtype");
						String ownCondition = (String)ymlMap.get("condition");
						if(ownCondition != null) {
							ownCondition = ownCondition.replace(" and ", "\n\t\t\tAND ");
						}
						//搜集联合表
						if(!"left".equals(conjtypeEach)){
							//对齐
							if(conjCount != 0){
								conjTablesBuilder.append("\t\t\t");
							}
							conjTablesBuilder
							.append("INNER JOIN `")
							.append(tableInfo.getName())
							.append("`")
							.append("\n");
							if(ownCondition != null) {
								conjTablesBuilder.append("\t\t\tON ")
								.append(ownCondition)
								.append("\n");
							}
							
							conjCount++;
							/*
							 * try { Field field = Integer.class.getDeclaredField("value");
							 * field.setAccessible(true); field.setInt(conjCount, conjCount + 1); } catch
							 * (Exception e) { e.printStackTrace(); }
							 */
						}else{
							//对齐
							if(conjCount != 0){
								conjTablesBuilder.append("\t\t\t");
							}
							conjTablesBuilder
							.append("LEFT JOIN `")
							.append(tableInfo.getName())
							.append("`")
							.append("\n");
							if(ownCondition != null) {
								conjTablesBuilder.append("\t\t\tON ")
								.append(ownCondition)
								.append("\n");
							}
							

							conjCount++;
						}
						
						//打印层级
						for(int i = 0; i < count + 1; i++){							
							resultBuilder.append("\t");
						}
						/**
						 * <collection property="optionsList" ofType="com.query.question.entity.Options">
						 * <association property="options" javaType="com.query.question.entity.Options">
						 */
						if("many".equals(ymlMap.get("relation"))){
							resultBuilder
							.append("<collection property=\"")
							.append(tableInfo.getEntityName().substring(0,1).toLowerCase())
							.append(tableInfo.getEntityName().substring(1))
							.append("List")
							.append("\" ofType=\"")
							.append(basePackage)
							.append(".")
							.append(tableInfo.getEntityName())
							.append("\">\n");
						}else{
							resultBuilder
							.append("<association property=\"")
							.append(tableInfo.getEntityName().substring(0,1).toLowerCase())
							.append(tableInfo.getEntityName().substring(1))
							.append("\" javaType=\"")
							.append(basePackage)
							.append(".")
							.append(tableInfo.getEntityName())
							.append("\">\n");
						}
					}
					//插入
					//生成结果集
					//id
					for(TableField field : tableInfo.getFields()){
						if(field.isKeyFlag()){
							//打印层级
							for(int i = 0; i < count + 1; i++){							
								resultBuilder.append("\t");
							}
							resultBuilder
							.append("\t<id column=\"")
							.append(tableInfo.getName())
							.append("_")
							.append(field.getName())
							.append("\" property=\"")
							.append(field.getPropertyName())
							.append("\" />\n");
						}
					}
					//普通字段
					for(TableField field : tableInfo.getFields()){
						if(!field.isKeyFlag()){
							//打印层级
							for(int i = 0; i < count + 1; i++){							
								resultBuilder.append("\t");
							}
							resultBuilder
							.append("\t<result column=\"")
							.append(tableInfo.getName())
							.append("_")
							.append(field.getName())
							.append("\" property=\"")
							.append(field.getPropertyName())
							.append("\" />\n");
						}
					}
					
					if(ymlMap.get("innertables")!=null){
						getConjunctiveInfo(
								basePackage,
								tableInfoList,
								(List<Map<String,Object>>)ymlMap.get("innertables"),
								absoluteNameStrMap,
								resultBuilder,
								conjTablesBuilder,
								resultColumnBuilder,
								whereBuilder,
								conjInfoMap,
								conjtype,
								condition,
								count + 1,
								conjCount);
					}
					
					//结束
					if(count == 0){
						//结束结果列
						resultColumnBuilder.append("\n\t</sql>\n");
						//结束结果集
						resultBuilder.append("\n\t</resultMap>\n");
						//合并结果集，结果列
						resultBuilder.append(resultColumnBuilder.toString());
						List<TableField> fields = tableInfo.getFields();
						//条件conjQueryList
						whereBuilder.append("\t\t\t<if test=\"condition!=null\">");
						String idFieldName = "";
						String idPropertyName = "";
						String idType = "";
						for(int i = 0; i < fields.size(); i++){
							TableField f = fields.get(i);
							if(f.isKeyFlag()){
								idFieldName = "`"+tableInfo.getName() +"`."+ f.getName();
								idPropertyName = f.getPropertyName();
								idType = f.getPropertyType();
							}
							whereBuilder
							.append("\n\t\t\t\t<if test=\"condition.")
							.append(f.getPropertyName())
							.append("!=null\">\n")
							.append("\t\t\t\t\t and `")
							.append(tableInfo.getName())
							.append("`")
							.append(".")
							.append(f.getName())
							.append(" = #{condition.")
							.append(f.getPropertyName())
							.append(",")
							.append("javaType=")
							.append(f.getPropertyType())
							.append("}\n")
							.append("\t\t\t\t</if>");
						}
						whereBuilder.append("\n\t\t\t</if>");
						
						//用来连接四个查询的字符串（conjQueryList）
						StringBuilder conjBuilder = new StringBuilder();
						conjBuilder
						.append("\n\t<select id=\"conjQueryList\" resultMap=\"ConjResultMap\" parameterType=\"")
						.append(basePackage)
						.append(".")
						.append(tableInfo.getEntityName())
						.append("\">\n")
						.append("\t\tselect \n\t\t<include refid=\"Conj_Column_List\" />\n\t\tfrom ")
						.append(conjTablesBuilder.toString());
						//添加连接条件
						String[] conditionArr = new String[0];
						if(condition != null) {
							conditionArr = condition.split(" and ");
						}
						conjBuilder
						.append("\t\t<where>\n");
						for(int i = 0; i < conditionArr.length; i++) {
							conjBuilder
							.append("\t\t\t")
							.append("and ")
							.append(conditionArr[i])
							.append("\n");
						}
						//实体条件
						conjBuilder
						.append(whereBuilder.toString());
						//条件
						conjBuilder
						.append("\n\t\t\t${condition.customCondition}\n")
						.append("\t\t</where>\n")
						.append("\t\t${condition.orderby}")
						.append("\n\t</select>");
						
						//conjQueryCount
						conjBuilder
						.append("\n\n\t<select id=\"conjQueryCount\" parameterType=\"")
						.append(basePackage)
						.append(".")
						.append(tableInfo.getEntityName())
						.append("\" resultType=\"java.lang.Integer\">\n")
						.append("\t\tselect \n\t\tcount(")
						.append("distinct ")
						.append(idFieldName)
						.append(")\n\t\tfrom ")
						.append(conjTablesBuilder.toString());
						//添加连接条件
						conjBuilder
						.append("\t\t<where>\n");
						for(int i = 0; i < conditionArr.length; i++) {
							conjBuilder
							.append("\t\t\t")
							.append("and ")
							.append(conditionArr[i])
							.append("\n");
						}
						//实体条件
						conjBuilder
						.append(whereBuilder.toString());
						//条件
						conjBuilder
						.append("\n\t\t\t${condition.customCondition}\n")
						.append("\t\t</where>\n")
						.append("\t\t${condition.orderby}")
						.append("\n\t</select>");
						
						//conjQueryPage
						conjBuilder
						.append("\n\n\t<select id=\"conjQueryPage\" resultMap=\"ConjResultMap\" parameterType=\"")
						.append(basePackage)
						.append(".")
						.append("vo.PageData")
						.append("\">\n")
						.append("\t\tselect \n\t\t<include refid=\"Conj_Column_List\" />\n\t\tfrom ")
						.append("(select *\n\t\t\tfrom ")
						.append("`")
						.append(tableInfo.getName())
						.append("`")
						.append("\n\t\t\tlimit #{offset },#{limit }) ")
						.append(conjTablesBuilder.toString());
						//添加连接条件
						conjBuilder
						.append("\t\t<where>\n");
						for(int i = 0; i < conditionArr.length; i++) {
							conjBuilder
							.append("\t\t\t")
							.append("and ")
							.append(conditionArr[i])
							.append("\n");
						}
						//实体条件
						conjBuilder
						.append(whereBuilder.toString());
						//条件
						conjBuilder
						.append("\n\t\t\t${condition.customCondition}\n")
						.append("\t\t</where>\n")
						.append("\t\t${orderby}")
						.append("\nlimit #{offset },#{limit }")
						.append("\n\t</select>");
						
						//conjQueryById
						conjBuilder
						.append("\n\n\t<select id=\"conjQueryById\" resultMap=\"ConjResultMap\" parameterType=\"")
						.append(basePackage)
						.append(".")
						.append(tableInfo.getEntityName())
						.append("\">\n")
						.append("\t\tselect \n\t\t<include refid=\"Conj_Column_List\" />\n\t\tfrom ")
						.append(conjTablesBuilder.toString());
						//添加连接条件
						conjBuilder
						.append("\t\t<where>\n");
						for(int i = 0; i < conditionArr.length; i++) {
							conjBuilder
							.append("\t\t\t")
							.append("and ")
							.append(conditionArr[i])
							.append("\n");
						}
						//条件
						conjBuilder
						.append("\t\t\tand ")
						.append(idFieldName)
						.append(" = ")
						.append("#{")
						.append(idPropertyName)
						.append(",javaType=")
						.append(idType)
						.append("}\n")
						.append("\t\t</where>\n")
						.append("\n\t</select>");
						
						
						

						//连接次数置零
						//利用反射赋值改变的是缓冲区中的包装Integer的对象的值0
						//下次使用0给包装类型赋值时0就变成了改变的值
						Map<String, Object> tableMap = (Map<String, Object>) conjInfoMap.get(tableInfo.getName());
						tableMap.put("resultStr", resultBuilder.toString());
						tableMap.put("conjQueryStr", conjBuilder.toString());
					}else{
						//打印层级缩进
						for(int i = 0; i < count + 1; i++){							
							resultBuilder.append("\t");
						}
						//打印结束标识
						if("many".equals(ymlMap.get("relation"))){
							resultBuilder.append("</collection>\n");
						}else{
							resultBuilder.append("</association>\n");
						}
					}
				}
			}
			
		}
		
		return conjInfoMap;
	}

}
