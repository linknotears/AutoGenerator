package com.linkey.service.impl;

import java.util.List;

import com.linkey.entity.vo.PageData;
import com.linkey.mapper.BaseMapper;
import com.linkey.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T> {

	protected BaseMapper<T> baseMapper;
	
	@Override
	public List<T> findList(T condition) {
		return baseMapper.selectList(condition);
	}

	@Override
	public PageData<T> findPage(PageData<T> pageData,T condition) {
		if(pageData==null){
			pageData = new PageData<T>();
		}
		//设置条件
		pageData.setCondition(condition);
		//设置数据
		List<T> list = baseMapper.selectPage(pageData);
		pageData.setList(list);
		//设置记录
		int totalCount = baseMapper.selectCount(pageData.getCondition());
		pageData.setTotalCount(totalCount);
		return pageData;
	}

	@Override
	public int findCount(T condition) {
		return baseMapper.selectCount(condition);
	}

	@Override
	public int removeBatchIds(Object... ids) {
		return baseMapper.deleteBatchIds(ids);
	}

	@Override
	public int removeByCondition(T condition) {
		return baseMapper.deleteByCondition(condition);
	}

	@Override
	public int saveByList(List<T> entities) {
		return baseMapper.insertByList(entities);
	}

	@Override
	public int save(T entity) {
		return baseMapper.insert(entity);
	}

	@Override
	public int updateByIdIgnoreNull(T entity) {
		return baseMapper.updateByIdIgnoreNull(entity);
	}

	@Override
	public int removeById(Object id) {
		return baseMapper.deleteById(id);
	}

	@Override
	public int updateById(T entity) {
		return baseMapper.updateById(entity);
	}

	@Override
	public T findById(Object id) {
		return baseMapper.selectById(id);
	}
	
	@Override
	public int saveOrUpdate(Object id,T entity){
		int i = 0;
		if(id!=null){
			i = baseMapper.updateByPartIdIgnoreNull(id,entity);
		}else{
			i = baseMapper.insert(entity);
		}
		return i;
	}
}
