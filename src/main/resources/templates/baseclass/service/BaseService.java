package ${package.Service};

import java.util.List;

import ${package.Entity}.vo.PageData;

public interface BaseService<T> {
List<T> findList(T condition);
	
	PageData<T> findPage(PageData<T> pageData,T condition);
	
	int findCount(T condition);
	
	int removeBatchIds(Object... ids);
	//条件删除，条件不允许为空
	int removeByCondition(T condition);
	
	int saveByList(List<T> entities);

	int save(T entity);
	
	int updateByIdIgnoreNull(T entity);
	
	int removeById(Object id);
	
	int updateById(T entity);
	
	T findById(Object id);
	
	int saveOrUpdate(Object id,T entity);
}
