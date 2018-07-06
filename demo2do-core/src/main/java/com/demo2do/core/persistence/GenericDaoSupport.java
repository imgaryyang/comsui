/**
 * 
 */
package com.demo2do.core.persistence;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Downpour
 */
public class GenericDaoSupport extends GenericJdbcSupport {

	@Autowired
	private SessionFactory sessionFactory;

    /**
	 * The default constructor
	 */
	public GenericDaoSupport() {
		
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Get current session if exists, otherwise open a new session
	 * 
	 * @return
	 */
	private Session getCurrentSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			return this.sessionFactory.openSession();
		}
	}

	/**
	 * Save entity using current session
	 * 
	 * @param entity
	 * @return
	 */
	public Serializable save(Object entity) {
		return this.getCurrentSession().save(entity);
	}

	/**
	 * Update entity using current session
	 * 
	 * @param entity
	 */
	public void update(Object entity) {
		this.getCurrentSession().update(entity);
	}

	/**
	 * 
	 * @param entity
	 */
	public void persist(Object entity) {
		this.getCurrentSession().persist(entity);
	}

	/**
	 * Save or update entity using current session
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(Object entity) {
		this.getCurrentSession().saveOrUpdate(entity);
	}

	/**
	 * Delete entity using current session
	 * 
	 * @param entity
	 */
	public void delete(Object entity) {
		this.getCurrentSession().delete(entity);
	}

	/**
	 * Delete entity according to persistentClass and primary key using current
	 * session
	 * 
	 * @param <T>
	 * @param persistentClass
	 * @param id
	 */
	public <T> void delete(Class<T> persistentClass, Serializable id) {
		delete(load(persistentClass, id));
	}

	/**
	 * Load entity according to persistentClass and primary key using current
	 * session
	 * 
	 * @param <T>
	 * @param persistentClass
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T load(Class<T> persistentClass, Serializable id) {
		return (T) this.getCurrentSession().load(persistentClass, id);
	}

	/**
	 * Load all the entities according to persistentClass using current session
	 * 
	 * @param <T>
	 * @param persistentClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> loadAll(Class<T> persistentClass) {
		Criteria criteria = this.getCurrentSession().createCriteria(persistentClass);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		criteria.setCacheable(true);
		return criteria.list();
	}

	/**
	 * Get entity according to persistentClass and primary key using current
	 * session
	 * 
	 * @param <T>
	 * @param persistentClass
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> persistentClass, Serializable id) {
		return (T) this.getCurrentSession().get(persistentClass, id);
	}
	
	/**
	 * Search by hibernate for int result. Mostly
	 * used for HQL like: 'SELECT count(*) ...'
	 * 
	 * @param sentence
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int searchForInt(final String sentence) {

		Query query = this.getCurrentSession().createQuery(sentence);
		List result = query.list();

		// deal with the condition when hql contains group by
		return result.size() > 1 ? result.size() : ((Long) result.get(0)).intValue();
	}

	/**
	 * Search by hibernate for int result, using Map basic parameters. Mostly
	 * used for HQL like: 'SELECT count(*) ...'
	 * 
	 * @param sentence
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int searchForInt(final String sentence, final Map<String, Object> parameters) {

		Query query = this.getCurrentSession().createQuery(sentence);
		query.setProperties(parameters);

		List result = query.list();

		// deal with the condition when hql contains group by
		return result.size() > 1 ? result.size() : ((Long) result.get(0)).intValue();
	}
	
	/**
	 * Search by hibernate for count, adding prefix: 'SELECT count(*) ...'
	 * 
	 * @param sentence
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int count(final String sentence, final Map<String, Object> parameters) {
		Query query = this.getCurrentSession().createQuery(this.getHQLCountSentence(sentence));
		query.setProperties(parameters);

		List result = query.list();

		// deal with the condition when hql contains group by
		return result.size() > 1 ? result.size() : ((Long) result.get(0)).intValue();

	}

	/**
	 * Generate HQL Count sentence
	 * 
	 * @param sentence
	 * @return
	 */
	private String getHQLCountSentence(String sentence) {
		StringBuffer sb = new StringBuffer("SELECT count(*) ");
		String tempSentence = StringUtils.trim(sentence).toUpperCase();
		if (tempSentence.startsWith("FROM")) {
			return sb.append(sentence).toString();
		} else {
			int index = tempSentence.indexOf(" FROM ");
			return sb.append(sentence.substring(index)).toString();
		}
	}

	/**
	 * Search by hibernate for list result, using single parameter
	 * 
	 * @param sentence
	 * @param key
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "serial" })
	public List searchForList(final String sentence, final String key, final Object parameter) {
		return searchForList(sentence, new HashMap<String, Object>(){{put(key, parameter);}});
	}
	
	/**
	 * Search by hibernate for list result, using single parameter and query cache
	 * 
	 * @param sentence
	 * @param key
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "serial" })
	public List searchForCacheableList(final String sentence, final String key, final Object parameter) {
		return searchForCacheableList(sentence, new HashMap<String, Object>(){{put(key, parameter);}});
	}

	/**
	 * Search by hibernate for list result
	 * 
	 * @param sentence
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List searchForList(final String sentence) {
		return searchForList(sentence, Collections.EMPTY_MAP);
	}
	
	/**
	 * Search by hibernate for list result, using Map as basic parameter.
	 * 
	 * @param sentence
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List searchForList(final String sentence, final Map<String, Object> parameters) {
		Query query = this.getCurrentSession().createQuery(sentence);
		query.setProperties(parameters);
		return query.list();
	}

	/**
	 * Search by hibernate for list result, using bean as basic parameter
	 * 
	 * @param sentence
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List searchForList(final String sentence, final Object bean) {
		Query query = this.getCurrentSession().createQuery(sentence);
		query.setProperties(bean);
		return query.list();
	}
	
	/**
	 * Search by hibernate for list result, using Map basic parameter and query cache.
	 * 
	 * @param sentence
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List searchForCacheableList(final String sentence, final Map<String, Object> parameters) {
		Query query = this.getCurrentSession().createQuery(sentence);
		query.setProperties(parameters);
		query.setCacheable(true);
		return query.list();
	}
	
	/**
	 * Search by hibernate for list result from beginIndex to the number of maxResult
	 * 
	 * @param sentence
	 * @param beginIndex
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List searchForList(final String sentence, final int beginIndex, final int maxResult) {
		return searchForList(sentence, Collections.EMPTY_MAP, beginIndex, maxResult);
	}
	
	/**
	 * Search by hibernate for list result from beginIndex to the number of
	 * maxResult, using Map as basic parameter.
	 * 
	 * @param sentence
	 * @param parameters
	 * @param beginIndex
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List searchForList(final String sentence, final Map<String, Object> parameters, final int beginIndex, final int maxResult) {
		Query query = this.getCurrentSession().createQuery(sentence);
		query.setFirstResult(beginIndex);
		query.setMaxResults(maxResult);
		query.setProperties(parameters);
		return query.list();
	}

	/**
	 * Search by hibernate for list result, using single parameter
	 * 
	 * @param sentence
	 * @param key
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "serial" })
	public List searchForList(final String sentence, final String key, final Object parameter, final int beginIndex, final int maxResult) {
		return searchForList(sentence, new HashMap<String, Object>(){{put(key, parameter);}}, beginIndex, maxResult);
	}
	
	/**
	 * Execute HQL
	 * 
	 * @param sentence
	 * @param key
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("serial")
	public int executeHQL(final String sentence, final String key, final Object parameter) {
		return this.executeHQL(sentence, new HashMap<String, Object>(){{put(key, parameter);}});
	}
	
	/**
	 * Execute HQL
	 * 
	 * @param sentence
	 * @param parameters
	 */
	public int executeHQL(final String sentence, final Map<String, Object> parameters) {
		Query query = this.getCurrentSession().createQuery(sentence);
		query.setProperties(parameters);
		return query.executeUpdate();
	}


	public <T> List searchForListUseSql(final String sentence, final Map<String, Object> parameters, Class<T> resultClass) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(sentence).addEntity(resultClass);
		query.setProperties(parameters);
		return query.list();
	}

    public <T> List searchForListUseSql(final String sentence, final Map<String, Object> parameters,String tableAlias, Class<T> resultClass) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(sentence).addEntity(tableAlias, resultClass);
        query.setProperties(parameters);
        return query.list();
    }


    public <T> List searchForListUseSql(final String sentence, final Map<String, Object> parameters,String tableAlias, Class<T> resultClass, Map<String, String> tableAliasWithPath) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(sentence).addEntity(tableAlias, resultClass);
        for (Map.Entry<String,String> tp : tableAliasWithPath.entrySet()){
            query.addJoin(tp.getKey(),tp.getValue());
        }
        query.setProperties(parameters);
        return query.list();
    }


	public  <T> List searchForListUseSql(final String sentence, final Map<String, Object> parameters, final int beginIndex, final int maxResult,String tableAlias,  Class<T> resultClass ) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sentence).addEntity(tableAlias, resultClass);
		query.setFirstResult(beginIndex);
		query.setMaxResults(maxResult);
		query.setProperties(parameters);
		return query.list();
	}

	public Long countUseSql(final String sentence, final Map<String, Object> parameters,String countField ) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sentence).addScalar(countField, StandardBasicTypes.LONG);
		query.setProperties(parameters);
		Object no = query.uniqueResult();
		return no == null ? 0L : Long.parseLong(no.toString());
	}
}
