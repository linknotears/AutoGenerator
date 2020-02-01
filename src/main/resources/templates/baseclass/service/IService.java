package ${package.Service};

#if($cfg.useMyBatitsPlus == 'true')
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.conditions.Wrapper;


public interface IService<T> extends com.baomidou.mybatisplus.extension.service.IService<T> {
	@Override
	default boolean saveBatch(Collection<T> entityList, int batchSize) {
		boolean flag = false;
		if(entityList != null && entityList.size()>0) {
			try {
				int i = 0;
				for(T entity : entityList) {
					if(i < batchSize) {						
						getBaseMapper().insert(entity);
					}
					i++;
				}
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		return flag;
	}
	
	@Override
	default boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
		boolean flag = false;
		if(entityList != null && entityList.size()>0) {
			try {
				int i = 0;
				for(T entity : entityList) {
					if(i < batchSize) {						
						saveOrUpdate(entity);
					}
					i++;
				}
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		return flag;
	}
	
	@Override
	default boolean updateBatchById(Collection<T> entityList, int batchSize) {
		boolean flag = false;
		if(entityList != null && entityList.size()>0) {
			try {
				int i = 0;
				for(T entity : entityList) {
					if(i < batchSize) {
						getBaseMapper().updateById(entity);
					}
					i++;
				}
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		return flag;
	}

	@Override
	default T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
		T t = getBaseMapper().selectOne(queryWrapper);
		return t;
	}

	@Override
	@Deprecated
	default <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
		return null;
	}

	@Override
	@Deprecated
	default Map<String, Object> getMap(Wrapper<T> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}
}
#else
public interface IService<T> {

}
#end
