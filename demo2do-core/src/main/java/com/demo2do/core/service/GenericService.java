/**
 * 
 */
package com.demo2do.core.service;

import java.io.Serializable;
import java.util.List;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.persistence.support.Order;
import com.demo2do.core.web.resolver.Page;

/**
 * @author Downpour
 */
public interface GenericService<T> {
	
	public List<T> loadAll(Class<T> persistentClass);
	
	public T load(Class<T> persistentClass, Serializable id);
	
	public List<T> list(Class<T> persistentClass, Page page);
	
	public List<T> list(Class<T> persistentClass, Filter filter);
	
	public List<T> list(Class<T> persistentClass, Filter filter, Page page);
	
	public List<T> list(Class<T> persistentClass, Order order);
	
	public List<T> list(Class<T> persistentClass, Order order, Page page);
	
	public List<T> list(Class<T> persistentClass, Filter filter, Order order);
	
	public List<T> list(Class<T> persistentClass, Filter filter, Order order, Page page);

	public Serializable save(Object entity);
	
	public void saveOrUpdate(Object entity);
	
	public void update(Object entity);
	
	public void delete(Object entity);
	
	public void delete(Class<T> persistentClass, Serializable id);
	
	
	
}
