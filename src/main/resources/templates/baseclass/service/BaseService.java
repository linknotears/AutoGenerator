package ${package.Service};

import java.io.Serializable;
import java.util.List;

import ${package.Entity}.vo.Page;

public interface BaseService<T>#if($cfg.useMyBatitsPlus == 'true') extends IService<T>#end {
	//适应mybatisplus
	boolean saveOrUpdate(T entity);
	
	List<T> findList(T condition);
	
	Page<T> findPage(Page<T> page,T condition);
	
	int findCount(T condition);
	
	int removeBatchIds(Object... ids);
	//条件删除，条件不允许为空
	int removeByCondition(T condition);
	
	int saveByList(List<T> entities);

	//int save(T entity);
	
	int updateByIdIgnoreNull(T entity);
	
	boolean removeById(Serializable id);
	
	int updateByIdSetNull(T entity);
	
	T findById(Object id);
	
	int saveOrUpdate(Object id,T entity);
	
	int saveOrUpdateByCheck(Object id,T entity);
	
	int updateByCondition(T entity, T condition);
	
	String findMaxValue(String columnName);
	
	String findMaxValue(String columnName,String condition);
	
	String findValue(String columnName,String condition);
	
	T findOne(T condition);
}
