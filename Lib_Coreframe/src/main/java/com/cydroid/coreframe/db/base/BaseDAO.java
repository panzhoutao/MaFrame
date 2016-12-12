package com.cydroid.coreframe.db.base;

import com.cydroid.coreframe.db.Pager;

import java.io.Serializable;
import java.util.List;


public interface BaseDAO<T, PK extends Serializable> {
	public enum SortType{
		asc,desc
	}
	T save(T entity);
	void saveAll(List<T> entitys);

	Long update(T entity);


	int updatePropertyById(Serializable id, String property, Serializable value);


	int deleteById(PK id);


	int delete(List<PK> ids);


	int deleteByProperty(String property, Serializable value);


	T getById(PK id);


	List<T> getByExample(T entity);

	List<T> getByProperty(String property, Serializable value);
	List<T> getByLikeProperty(String property, Serializable value);


	List<T> findAllByPage(Pager mPager, SortType sortType);

	BaseOperate<T, PK> getOperate();
	int getLanguage();
	
	int getDataType();

	List<T> findAll();
	void close();
	void clear();
}