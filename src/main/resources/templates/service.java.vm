package ${package.Service};

import ${package.Entity}.${entity};
#if($cfg.conjInfoMap.get($table.name).get('resultStr'))
import ${package.Entity}.vo.PageData;
import java.util.List;
#end

/**
 * $!{table.comment} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} extends BaseService<${entity}> {
	#if($cfg.conjInfoMap.get($table.name).get('resultStr'))
	//联合查询接口
	List<${entity}> conjQueryList(${entity} condition);
	
	PageData<${entity}> conjQueryPage(PageData<${entity}> pageData,${entity} condition);
	
	int conjQueryCount(${entity} condition);
	
	${entity} conjQueryById(Object id);
	#end
	
}
