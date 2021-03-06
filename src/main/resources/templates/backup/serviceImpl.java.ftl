package ${package.ServiceImpl};

import org.springframework.beans.factory.annotation.Autowired;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
#if($cfg.conjInfoMap.get($table.name).get('resultStr'))
import ${package.Entity}.vo.PageData;
import java.util.List;
#end
#macro(tolowercase $str)$str.substring(0,1).toLowerCase()$str.substring(1)#end
/**
 * $!{table.comment} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service("${package.ServiceImpl}.${table.serviceImplName}")
public class ${table.serviceImplName} extends BaseServiceImpl<${entity}> implements ${table.serviceName} {
	protected ${table.mapperName} #tolowercase(${entity})Mapper;
	@Autowired
	public ${table.serviceImplName}(${table.mapperName} #tolowercase(${entity})Mapper){
		this.baseMapper = #tolowercase(${entity})Mapper;
		this.#tolowercase(${entity})Mapper = #tolowercase(${entity})Mapper;
	}
	
	#if($cfg.conjInfoMap.get($table.name).get('resultStr'))
	@Override
	public List<${entity}> conjQueryList(${entity} condition) {
		return #tolowercase(${entity})Mapper.conjQueryList(condition);
	}

	@Override
	public PageData<${entity}> conjQueryPage(PageData<${entity}> pageData,${entity} condition) {
		if(pageData==null){
			pageData = new PageData<${entity}>();
		}
		//设置条件
		pageData.setCondition(condition);
		//设置数据
		List<${entity}> list = #tolowercase(${entity})Mapper.conjQueryPage(pageData);
		pageData.setList(list);
		//设置记录
		int totalCount = #tolowercase(${entity})Mapper.conjQueryCount(pageData.getCondition());
		pageData.setTotalCount(totalCount);
		return pageData;
	}

	@Override
	public int conjQueryCount(${entity} condition) {
		return #tolowercase(${entity})Mapper.conjQueryCount(condition);
	}
	
	@Override
	public ${entity} conjQueryById(Object id) {
		return #tolowercase(${entity})Mapper.conjQueryById(id);
	}
	#end
}
