package ${package.ServiceImpl};

import java.util.List;

import ${package.Entity}.vo.PageData;
import ${package.Mapper}.BaseMapper;
import ${package.Service}.BaseService;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

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
		List<T> rows = baseMapper.selectPage(pageData);
		pageData.setRows(rows);
		//设置记录
		int total = baseMapper.selectCount(pageData.getCondition());
		pageData.setTotal(total);
		return pageData;
	}

	@Override
	public int findCount(T condition) {
		return baseMapper.selectCount(condition);
	}
	
	@Override
	public String findMaxValue(String columnName) {
		return baseMapper.selectMaxValue(columnName);
	}
	
	@Override
	public String findMaxValue(String columnName,String condition) {
		return baseMapper.selectMaxValueByCondition(columnName,condition);
	}
	
	@Override
	public String findValue(String columnName,String condition) {
		return baseMapper.selectValue(columnName,condition);
	}
	
	@Override
	public T findOne(T condition) {
		return baseMapper.selectOne(condition);
	}
	
	@Override
	public T findById(Object id) {
		return baseMapper.selectById(id);
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
	public int saveOrUpdate(Object id, T entity){
		int i = 0;
		if(id!=null){
			i = baseMapper.updateByPartIdIgnoreNull(id,entity);
		}else{
			i = baseMapper.insert(entity);
		}
		return i;
	}
	
	@Override
	public int saveOrUpdateByCheck(Object id, T entity){
		int i = 0;
		if(id!=null){
			Object testObj = baseMapper.selectById(id);
			if(testObj!=null) {
				i = baseMapper.updateByPartIdIgnoreNull(id,entity);
			}else {
				i = baseMapper.insert(entity);
			}
		}else{
			i = baseMapper.insert(entity);
		}
		return i;
	}
	
	@Override
	public int updateByCondition(T entity, T condition) {
		return baseMapper.updateByCondition(entity, condition);
	}
}
