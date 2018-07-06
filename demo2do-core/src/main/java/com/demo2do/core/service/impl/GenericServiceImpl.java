/**
 * 
 */
package com.demo2do.core.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.GenericJdbcSupport;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.persistence.support.Order;
import com.demo2do.core.service.GenericService;
import com.demo2do.core.web.resolver.Page;

/**
 * @author Downpour
 * @param <T>
 */
public abstract class GenericServiceImpl<T> implements GenericService<T> {
	
	@Autowired
	private GenericJdbcSupport genericJdbcSupport;
	
	@Autowired
	protected GenericDaoSupport genericDaoSupport;

	/**
	 * @param genericDaoSupport the genericDaoSupport to set
	 */
	public void setGenericDaoSupport(GenericDaoSupport genericDaoSupport) {
		this.genericDaoSupport = genericDaoSupport;
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#loadAll(java.lang.Class)
	 */
	public List<T> loadAll(Class<T> persistentClass) {
		return genericDaoSupport.loadAll(persistentClass);
	}

	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#load(java.lang.Class, java.io.Serializable)
	 */
	public T load(Class<T> persistentClass, Serializable id) {
		return genericDaoSupport.load(persistentClass, id);
	}

	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.web.resolver.Page)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Page page) {
		List<T> result = genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass), page.getBeginIndex(), page.getMaxResultRecords());
		page.calculate(result.size());
		return result.size() > page.getEveryPage() ? result.subList(0, page.getEveryPage()) : result;
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.persistence.support.Filter)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Filter filter) {
		return genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass) + filter.getWhereSentence(), filter.getParameters());
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.persistence.support.Filter, com.demo2do.core.web.resolver.Page)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Filter filter, Page page) {
		List<T> result = genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass) + filter.getWhereSentence(), filter.getParameters(), page.getBeginIndex(), page.getMaxResultRecords());
		page.calculate(result.size());
		return result.size() > page.getEveryPage() ? result.subList(0, page.getEveryPage()) : result; 
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.persistence.support.Order)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Order order) {
		return genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass) + order.getSentence());
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.persistence.support.Order, com.demo2do.core.web.resolver.Page)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Order order, Page page) {
		List<T> result = genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass) + order.getSentence(), page.getBeginIndex(), page.getMaxResultRecords());
		page.calculate(result.size());
		return result.size() > page.getEveryPage() ? result.subList(0, page.getEveryPage()) : result; 
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.persistence.support.Filter, com.demo2do.core.persistence.support.Order)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Filter filter, Order order) {
		return genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass) + filter.getWhereSentence() + order.getSentence(), filter.getParameters());
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#list(java.lang.Class, com.demo2do.core.persistence.support.Filter, com.demo2do.core.persistence.support.Order, com.demo2do.core.web.resolver.Page)
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass, Filter filter, Order order, Page page) {
		List<T> result = genericDaoSupport.searchForList("FROM " + ClassUtils.getShortClassName(persistentClass) + filter.getWhereSentence() + order.getSentence(), filter.getParameters(), page.getBeginIndex(), page.getMaxResultRecords());
		page.calculate(result.size());
		return result.size() > page.getEveryPage() ? result.subList(0, page.getEveryPage()) : result; 
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#save(java.lang.Object)
	 */
	public Serializable save(Object entity) {
		return genericDaoSupport.save(entity);
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#update(java.lang.Object)
	 */
	public void update(Object entity) {
		genericDaoSupport.update(entity);
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Object entity) {
		genericDaoSupport.saveOrUpdate(entity);
	}

	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#delete(java.lang.Object)
	 */
	public void delete(Object entity) {
		genericDaoSupport.delete(entity);
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.service.GenericService#delete(java.lang.Class, java.io.Serializable)
	 */
	public void delete(Class<T> persistentClass, Serializable id) {
		genericDaoSupport.delete(persistentClass, id);
	}
}
