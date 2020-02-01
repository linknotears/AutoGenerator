package ${package.ServiceImpl};

import java.io.Serializable;
import java.util.List;

import ${package.Entity}.vo.Page;
import ${package.Mapper}.BaseMapper;
import ${package.Service}.BaseService;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected BaseMapper<T> baseMapper;
	
	
	@Override
	public boolean saveOrUpdate(T entity) {
		return baseMapper.insertOrUpdate(entity) > 0;
	}
	
	@Override
	public BaseMapper<T> getBaseMapper() {
		return baseMapper;
	}
	
	@Override
	public List<T> findList(T condition) {
		return baseMapper.findList(condition);
	}

	@Override
	public Page<T> findPage(Page<T> page,T condition) {
		if(page==null){
			page = new Page<T>();
		}
		//设置条件
		page.setCondition(condition);
		//设置数据
		List<T> rows = baseMapper.findPage(page);
		page.setRows(rows);
		//设置记录
		int total = baseMapper.findCount(page.getCondition());
		page.setTotal(total);
		return page;
	}

	@Override
	public int findCount(T condition) {
		return baseMapper.findCount(condition);
	}
	
	@Override
	public String findMaxValue(String columnName) {
		return baseMapper.findMaxValue(columnName);
	}
	
	@Override
	public String findMaxValue(String columnName,String condition) {
		return baseMapper.findMaxValueByCondition(columnName,condition);
	}
	
	@Override
	public String findValue(String columnName,String condition) {
		return baseMapper.findValue(columnName,condition);
	}
	
	@Override
	public T findOne(T condition) {
		return baseMapper.findOne(condition);
	}
	
	@Override
	public T findById(Object id) {
		return baseMapper.findById(id);
	}

	@Override
	public int removeBatchIds(Object... ids) {
		return baseMapper.removeBatchIds(ids);
	}

	@Override
	public int removeByCondition(T condition) {
		return baseMapper.removeByCondition(condition);
	}

	@Override
	public int saveByList(List<T> entities) {
		return baseMapper.saveByList(entities);
	}

	@Override
	public int updateByIdIgnoreNull(T entity) {
		return baseMapper.updateByIdIgnoreNull(entity);
	}

	@Override
	public boolean removeById(Serializable id) {
		return baseMapper.removeById(id) > 0;
	}

	@Override
	public int updateByIdSetNull(T entity) {
		return baseMapper.updateByIdSetNull(entity);
	}
	
	@Override
	public int saveOrUpdate(Object id, T entity){
		int i = 0;
		if(id!=null){
			i = baseMapper.updateByPartIdIgnoreNull(id,entity);
		}else{
			i = baseMapper.save(entity);
		}
		return i;
	}
	
	@Override
	public int saveOrUpdateByCheck(Object id, T entity){
		int i = 0;
		if(id!=null){
			Object testObj = baseMapper.findById(id);
			if(testObj!=null) {
				i = baseMapper.updateByPartIdIgnoreNull(id,entity);
			}else {
				i = baseMapper.save(entity);
			}
		}else{
			i = baseMapper.save(entity);
		}
		return i;
	}
	
	@Override
	public int updateByCondition(T entity, T condition) {
		return baseMapper.updateByCondition(entity, condition);
	}
}
