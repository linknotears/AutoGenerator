package ${package.Controller};


import java.util.List;
#if($cfg.loginTable==$table.name)
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
#end
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
#foreach($field in ${table.fields})
#if($cfg.propertyType.get($table.name).get($field.name) == 'file')
#set($isHaveFile = true)
#end
#if(${field.propertyName} == 'createTime')
#set($isHaveDate = true)
#end
#end

#if($isHaveFile)
import ${cfg.basePackage}.util.UploadFileSpringMVC;
import org.springframework.web.multipart.MultipartFile;
#end

#if($isHaveDate)
import java.util.Date;
#end

import ${package.Entity}.${entity};
import ${package.Entity}.vo.PageData;
import ${package.Entity}.vo.ResultData;
import ${package.Service}.${table.serviceName};
## 宏的第一个参数不能是#
## 首字母小写
#macro(tolowercase $str)$str.substring(0,1).toLowerCase()$str.substring(1)#end
## 后部分转换为小写
#macro(touppercase $str)$str.substring(0,1)$str.substring(1).toLowerCase()#end
## 首字母大写
#macro(toheaduppercase $str)$str.substring(0,1).toUpperCase()$str.substring(1)#end
## 判断查询方法
#macro(query)#if($cfg.conjInfoMap.get($table.name).get('resultStr'))conjQuery#else find#end#end
## 生成指定数目的制表符
#macro(tab $countArr)#foreach($i in $countArr)	#end#end
## 宏的参数有数据类型区别
#set( $columnType = ${table.fields[0].columnType.toString()})
/**
 * $!{table.comment} 控制器
 *
 * @author ${author}
 * @since ${date}
 */
@Controller("${package.Controller}.${table.controllerName}")
@RequestMapping("${cfg.suffixUri}/#tolowercase($entity)")
public class ${table.controllerName} extends BaseController{
	@Autowired
	private ${table.serviceName} #tolowercase($entity)Service;

	@RequestMapping("/saveOrUpdate")//更新不置空
	public @ResponseBody ResultData saveOrUpdate(@RequestParam(value="id",required=false) #touppercase(${columnType}) id,${entity} #tolowercase($entity)#if($cfg.loginTable==$table.name),HttpSession session#end
	#foreach($field in ${table.fields})
	#if($cfg.propertyType.get($table.name).get($field.name) == 'file' ||  ${field.propertyName.contains('image')})
	,@RequestParam(value="${field.propertyName}File",required=false) MultipartFile ${field.propertyName}file
	#end
	#end
	) { 
		ResultData resultData = new ResultData();
		int i = 0;
		try {
			#foreach($field in ${table.fields})
			#if($cfg.propertyType.get($table.name).get($field.name) == 'file')			
			if(${field.propertyName}file != null){
				if(!${field.propertyName}file.isEmpty()){
					#tab([1..5])#tolowercase($entity).set#toheaduppercase(${field.propertyName})(
						UploadFileSpringMVC.getUploadPath(${field.propertyName}file,"image",#tolowercase($entity).get#toheaduppercase(${field.propertyName})(),servletContext)
					);
				}
			}
			#end
			#if(${field.propertyName} == 'createTime')
			if(id == null) {
				#tab([1..4])#tolowercase($entity).set#toheaduppercase(${field.propertyName})(new Date());
			}
			#end
			#end
			i = #tolowercase($entity)Service.saveOrUpdate(id, #tolowercase($entity));
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		#if($cfg.loginTable==$table.name)
		//更新session
		if(id != null){
			if(resultData.getCode()!=-1){
				if(session.getAttribute("existUser")!=null){
					#tolowercase($entity) = #tolowercase($entity)Service.#query()ById(id);
					session.setAttribute("existUser",#tolowercase($entity));
				}
			}
		}
		#end
		resultData
		.setData("count", i)
		.setData("id",#tolowercase($entity).get#toheaduppercase(${table.fields[0].propertyName})());;
		return resultData;
	}
	
	@RequestMapping("/update")//允许置空
	public @ResponseBody ResultData update(${entity} #tolowercase($entity)){
		ResultData resultData = new ResultData();
		int i = 0;
		try {
			i = #tolowercase($entity)Service.updateById(#tolowercase($entity));
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		resultData.setData("count", i);
		return resultData;
	}
	
	@RequestMapping("/remove")
	public @ResponseBody ResultData remove(#touppercase(${columnType}) id){
		ResultData resultData = new ResultData();
		int i = 0;
		try {
			i = #tolowercase($entity)Service.removeById(id);
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		resultData.setData("count", i);
		return resultData;
	}
	
	@RequestMapping("/findList")
	public @ResponseBody ResultData #query()List($entity entity){
		ResultData resultData = new ResultData();
		List<${entity}> #tolowercase($entity)s = null;
		try {
			#tab([1..3])#tolowercase($entity)s = #tolowercase($entity)Service.#query()List(entity);
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		resultData.setData("#tolowercase($entity)s", #tolowercase($entity)s);
		return resultData;
	}

	@RequestMapping("/findById")
	public @ResponseBody ResultData #query()ById(#touppercase(${columnType}) id){
		ResultData resultData = new ResultData();
		${entity} #tolowercase($entity) = null;
		try {
			#tab([1..3])#tolowercase($entity) = #tolowercase($entity)Service.#query()ById(id);
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		resultData.setData("#tolowercase($entity)", #tolowercase($entity));
		return resultData;
	}
	
	@RequestMapping("/findPage")
	public @ResponseBody ResultData #query()Page(PageData<${entity}> pageData,${entity} condition){
		ResultData resultData = new ResultData();
		
		List<${entity}> #tolowercase($entity)s = null;
		try {
			pageData = #tolowercase($entity)Service.#query()Page(pageData, condition);
			#tab([1..3])#tolowercase($entity)s = pageData.getList();
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		
		resultData.setData("#tolowercase($entity)s", #tolowercase($entity)s);
		resultData.setData("pageData", pageData);
		return resultData;
	}
	#if($cfg.loginTable==$table.name)
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		$entity exist$entity = ($entity) request.getSession().getAttribute("existUser");
		if (exist$entity!=null) {
			return "admin/index";
		}
		return "login";
	}
	
	@RequestMapping("/regist")
	public @ResponseBody ResultData regist(${entity} #tolowercase($entity),HttpServletRequest request){
		//判断是否已注册
		${entity} condition = new ${entity}();
		condition.set#toheaduppercase($cfg.loginUsercolumn)(#tolowercase($entity).get#toheaduppercase($cfg.loginUsercolumn)());
		List<$entity> #tolowercase($entity)s = #tolowercase($entity)Service.#query()List(condition);
		if(#tolowercase($entity)s.size()!=0){
			return new ResultData()
					.setCode(-1)
					.setData("message","用户已存在！");
		}
		ResultData resultData = saveOrUpdate(null, #tolowercase($entity),null#foreach($field in ${table.fields})#if($cfg.propertyType.get($table.name).get($field.name) == 'file'),null#end#end);
		if(resultData.getCode()==0){
			request.getSession().setAttribute("existUser", #tolowercase($entity));
			resultData.setData("existUser", #tolowercase($entity));
		}
		return resultData;
	}
	
	@RequestMapping("/login")
	public @ResponseBody ResultData login($entity #tolowercase($entity),HttpServletRequest request){
		if(#tolowercase($entity)!=null){
			if (#tolowercase($entity).get#toheaduppercase($cfg.loginPwdcolumn)()!=null&&#tolowercase($entity).get#toheaduppercase($cfg.loginUsercolumn)()!=null) {				
				List<$entity> #tolowercase($entity)s = #tolowercase($entity)Service.#query()List(#tolowercase($entity));
				if(#tolowercase($entity)s.size()!=0){
					request.getSession().setAttribute("existUser",#tolowercase($entity)s.get(0));
					return new ResultData().setData("existUser",#tolowercase($entity)s.get(0));
				}
			}
		}
		return new ResultData()
				.setCode(-1)
				.setData("message", "用户名或密码错误！");
	}
	#end
}
