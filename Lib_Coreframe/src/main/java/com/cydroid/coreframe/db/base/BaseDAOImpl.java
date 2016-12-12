package com.cydroid.coreframe.db.base;

import com.cydroid.coreframe.db.Pager;

import java.io.Serializable;
import java.util.List;

public abstract class BaseDAOImpl<T extends Serializable, PK> implements
		BaseDAO<T, Integer> {
	protected Class<?> entityClass;
	public boolean beforeSave(T entity) {
		return true;
	}
	/**
	 * 保存对象
	 */
	public T save(T entity) {
		beforeSave(entity);
		this.getOperate().insert(entity);
		afterSave(entity);
		return entity;
	}

	public boolean afterSave(T entity) {
		return true;
	}

	/**
	 * 更新对象
	 */
	@Override
	public Long update(T entity) {
		return this.getOperate().update(entity);
	}

	/**
	 * 通过id修改属性值
	 */
	@Override
	public int updatePropertyById(Serializable id, String property,
			Serializable value) {
		return this.getOperate().updateByProperty(id, property, value);
	}

	/**
	 * 根据id删除对象
	 */
	@Override
	public int deleteById(Integer id) {
		return this.getOperate().deleteById(id);

	}

	/**
	 * 根据id列表删除对象
	 */
	@Override
	public int delete(List<Integer> ids) {
		return this.getOperate().delete(ids);
	}

	/**
	 * 根据属性值删除对象
	 */
	@Override
	public int deleteByProperty(String property, Serializable value) {
		return this.getOperate().deleteByProperty(property, value);
	}

	/**
	 * 根据id查找对象
	 */
	@Override
	public T getById(Integer id) {
		return this.getOperate().getById(id);
	}


	public List<T> getByExample(T entity) {
		return this.getOperate().getByExample(entity);
	}

	/**
	 * 根据属性值查找对象
	 */
	@Override
	public List<T> getByProperty(String property, Serializable value) {
		return this.getOperate().getByProperty(property, value);
	}
	public List<T> getByLikeProperty(String property, Serializable value) {
		return this.getOperate().getByLikeProperty(property, value);
	}


	public List<T> findAllByPage(Pager mPager,SortType sortType) {
		return this.getOperate().findAllByPage(mPager,sortType);
	}

	public List<T> findAll() {
		return this.getOperate().findAll();
	}

	public int getLanguage() {
		return 0;
	}

	public int getDataType() {
		return 0;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		this.getOperate().close();
	}
	
	@Override
	public void saveAll(List<T> entitys) {
		// TODO Auto-generated method stub
		this.getOperate().insertAll(entitys);
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.getOperate().clear();
	}
}