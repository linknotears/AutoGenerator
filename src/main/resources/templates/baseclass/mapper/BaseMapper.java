package ${package.Mapper};

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ${package.Entity}.vo.Page;

@Repository("${package.Mapper}.BaseMapper")
public interface BaseMapper<T>#if($cfg.useMyBatitsPlus == 'true') extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>#end {
	int insertOrUpdate(T entity);
	
	List<T> findList(@Param("condition") T condition);
	
	List<T> findPage(Page<T> page);
	
	int findCount(@Param("condition") T condition);
	
	T findById(Object id);
	
	int removeBatchIds(Object... ids);
	//条件删除，条件不允许为空
	int removeByCondition(T condition);
	
	int saveByList(List<T> entities);

	int save(T entity);
	
	int updateByIdIgnoreNull(T entity);
	
	int removeById(Object id);
	
	int updateByIdSetNull(T entity);
	
	int updateByPartIdIgnoreNull(@Param("id") Object id,@Param("entity") T entity);
	
	int updateByPartId(@Param("id") Object id,@Param("entity") T entity);
	
	int updateByCondition(@Param("id") T entity, @Param("condition") T condition);
	
	String findMaxValue(@Param("columnName") String columnName);
	
	String findMaxValueByCondition(@Param("columnName") String columnName,@Param("condition") String condition);
	
	String findValue(@Param("columnName") String columnName,@Param("condition") String condition);
	
	T findOne(@Param("condition") T condition);
}
