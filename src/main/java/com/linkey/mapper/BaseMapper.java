package com.linkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkey.entity.vo.PageData;

public interface BaseMapper<T> {
	List<T> selectList(@Param("condition") T condition);
	
	List<T> selectPage(PageData<T> pageData);
	
	int selectCount(@Param("condition") T condition);
	
	int deleteBatchIds(Object... ids);
	//条件删除，条件不允许为空
	int deleteByCondition(T condition);
	
	int insertByList(List<T> entities);

	int insert(T entity);
	
	int updateByIdIgnoreNull(T entity);
	
	int deleteById(Object id);
	
	int updateById(T entity);
	
	T selectById(Object id);
	
	int updateByPartIdIgnoreNull(@Param("id") Object id,@Param("entity") T entity);
	
	int updateByPartId(@Param("id") Object id,@Param("entity") T entity);
}
