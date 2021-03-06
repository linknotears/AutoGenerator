package ${package.Entity};
## 首字母大写
#macro(toheaduppercase $str)$str.substring(0,1).toUpperCase()$str.substring(1)#end

import java.io.Serializable;
#foreach($pkg in ${table.importPackages})
#if($pkg.contains('java.util') || $pkg.contains('java.math') || $pkg.contains('java.time'))
import ${pkg};
#end
#end
/**
 * $!{table.comment}
 *
 * @author ${author}
 * @since ${date}
 */
public class ${entity} implements Serializable {

    private static final long serialVersionUID = 1L;
	
## ----------  BEGIN 字段循环遍历  ----------
#foreach($field in ${table.fields})
	private ${field.propertyType} ${field.propertyName};
#end
## ----------  END 字段循环遍历  ----------

	private String customCondition = "";
	
	private String orderby = "";
## ---------- BEGIN  遍历联合信息  ----------
#if($cfg.conjInfoMap.get($table.name))
	//联合所需数据
#foreach($conjEntityProp in $cfg.conjInfoMap.get($table.name).get('conjEntityProp'))
$conjEntityProp
#end
#end
## ----------  END  遍历联合信息  ----------

#if(!${entityLombokModel})
#foreach($field in ${table.fields})
#if(${field.propertyType.equals("boolean")})
#set($getprefix="get")
#else
#set($getprefix="get")
#end

##public ${field.propertyType} ${getprefix}${field.capitalName}() {
	public ${field.propertyType} ${getprefix}#toheaduppercase(${field.propertyName})() {
		return ${field.propertyName};
	}

#if(${entityBuilderModel})
	public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
#else
	public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
#end
		this.${field.propertyName} = ${field.propertyName};
#if(${entityBuilderModel})
		return this;
#end
	}
#end
#end

#if(${entityColumnConstant})
#foreach($field in ${table.fields})
	public static final String ${field.name.toUpperCase()} = "${field.name}";

#end
#end

#if(!${entityLombokModel})
	@Override
	public String toString() {
		return "${entity}{" +
#foreach($field in ${table.fields})
#if($!{velocityCount}==1)
			"${field.propertyName}=" + ${field.propertyName} +
#else
			", ${field.propertyName}=" + ${field.propertyName} +
#end
#end
			"}";
	}
#end
	
	public String getCustomCondition() {
		return customCondition;
	}

	public void setCustomCondition(String customCondition) {
		this.customCondition = "and " + customCondition;
	}
	
	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = "ORDER BY " + orderby;
	}
}
