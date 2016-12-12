package com.cydroid.coreframe.db.base;

import com.cydroid.coreframe.db.Pager;

import java.io.Serializable;
import java.util.List;


public interface BaseOperate<T, PK extends Serializable> {

	/**
	 *  保存
	 * @param entity
	 * @return
	 */
	public Long insert(T entity);
	public Long insertAll(List<T> entitys);

	public Long getNextPrimaryKey();

	/**
	 *  更新
	 * @param entity
	 * @return
	 */
	public Long update(T entity);

	/**
	 *  更新属性值
	 * @param id
	 * @param property
	 * @param value
	 * @return
	 */
	public int updateByProperty(Serializable id, String property, Serializable value);

	/**
	 *  根据id删除某个对象
	 * @param id
	 * @return
	 */
	public int deleteById(PK id);

	/**
	 *  根据id批量删除对象
	 * @param ids
	 * @return
	 */
	public int delete(List<PK> ids);

	/**
	 *  根据属性批量删除对象
	 * @param property
	 * @param value
	 * @return
	 */
	public int deleteByProperty(String property, Serializable value);

	/**
	 *  根据id获取某个对象
	 * @param id
	 * @return
	 */
	public T getById(PK id);
	
	/**
	 * 根据属性查找对象
	 * @param property
	 * @param value
	 * @return
	 */
	public List<T> getByProperty(String property, Serializable value);

	public List<T> getByLikeProperty(String property, Serializable value);

	/**
	 *  根据样例对象
	 * @param id
	 * @return
	 */
	public List<T> getByExample(T entity);

	/**
	 * 分页查找所有对象
	 * @return
	 */
	public List<T> findAllByPage(Pager mPager, BaseDAO.SortType sortType);
	
	public List<T> findAll(String orderby);
	public List<T> findAll();
	public void close();
	public void clear();
}