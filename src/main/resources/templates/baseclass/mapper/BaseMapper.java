package ${package.Mapper};

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ${package.Entity}.vo.PageData;

@Repository("${package.Mapper}.BaseMapper")
public interface BaseMapper<T> {
	List<T> selectList(@Param("condition") T condition);
	
	List<T> selectPage(PageData<T> pageData);
	
	int selectCount(@Param("condition") T condition);
	
	T selectById(Object id);
	
	int deleteBatchIds(Object... ids);
	//条件删除，条件不允许为空
	int deleteByCondition(T condition);
	
	int insertByList(List<T> entities);

	int insert(T entity);
	
	int updateByIdIgnoreNull(T entity);
	
	int deleteById(Object id);
	
	int updateById(T entity);
	
	int updateByPartIdIgnoreNull(@Param("id") Object id,@Param("entity") T entity);
	
	int updateByPartId(@Param("id") Object id,@Param("entity") T entity);
	
	int updateByCondition(@Param("id") T entity, @Param("condition") T condition);
	
	String selectMaxValue(@Param("columnName") String columnName);
	
	String selectMaxValueByCondition(@Param("columnName") String columnName,@Param("condition") String condition);
}
