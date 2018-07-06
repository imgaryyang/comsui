package com.suidifu.dowjones.utils;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Downpour
 */
@Component
public class GenericJdbcSupport implements Serializable{
    private static final Dialect DEFAULT_DIALECT = new Demo2doJdbcDialect();

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private Dialect dialect = DEFAULT_DIALECT;

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * @param dialect the dialect to set
     */
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    /**
     * @return
     */
    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }


    /**
     * Get limited SQL Sentence
     *
     * @param sentence
     * @param beginIndex
     * @param maxResult
     * @return
     */
    private String getLimitedSentence(String sentence, int beginIndex, int maxResult) {
        return this.dialect.getSearchLimitString(sentence, beginIndex, maxResult, false);
    }

    /**
     * Query by JDBC for int result, with no parameters
     *
     * @param sentence
     * @return
     */
    @SuppressWarnings("unchecked")
    public int queryForInt(String sentence) {
        Number number = getJdbcTemplate().queryForObject(sentence, Collections.emptyMap(), Integer.class);
        return (number != null ? number.intValue() : 0);
    }

    /**
     * Query by JDBC for int result, using Map as basic parameter.
     *
     * @param sentence
     * @param parameters
     * @return
     */
    public int queryForInt(String sentence, Map<String, Object> parameters) {
        Number number = getJdbcTemplate().queryForObject(sentence, parameters, Integer.class);
        return (number != null ? number.intValue() : 0);
    }

    /**
     * Query by JDBC for int result, using single parameter.
     *
     * @param sentence
     * @param parameter
     * @return
     */
    @SuppressWarnings("serial")
    public int queryForInt(String sentence, final String key, final Object parameter) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(key, parameter);

        Number number = getJdbcTemplate().queryForObject(sentence, param, Integer.class);
        return (number != null ? number.intValue() : 0);
    }


    /**
     * Query by JDBC for int result, using a typical JavaBean as basic
     * parameters.
     *
     * @param sentence
     * @param properties
     * @return
     */
    public int queryForInt(String sentence, Object properties) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(properties);
        Number number = getJdbcTemplate().queryForObject(sentence, namedParameters, Integer.class);
        return (number != null ? number.intValue() : 0);
    }

    /**
     * Query by JDBC for List result, with no parameters.
     *
     * @param sentence
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryForList(String sentence) {
        return getJdbcTemplate().queryForList(sentence, Collections.emptyMap());
    }

    /**
     * Query by JDBC for List result, using Map as basic parameter.
     *
     * @param sentence
     * @param parameters
     * @return
     */
    public List<Map<String, Object>> queryForList(String sentence, Map<String, Object> parameters) {
        return getJdbcTemplate().queryForList(sentence, parameters);
    }

    /**
     * Query by JDBC for List result, using a typical JavaBean as basic
     * parameters.
     *
     * @param sentence
     * @param properties
     * @return
     */
    public List<Map<String, Object>> queryForList(String sentence, Object properties) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(properties);
        return getJdbcTemplate().queryForList(sentence, namedParameters);
    }

    /**
     * Query by JDBC for List result, using single parameter.
     *
     * @param sentence
     * @param parameters
     * @return
     */
    @SuppressWarnings("serial")
    public List<Map<String, Object>> queryForList(String sentence, final String key, final Object parameter) {
        return getJdbcTemplate().queryForList(sentence, new HashMap<String, Object>() {{
            put(key, parameter);
        }});
    }

    /**
     * @param sentence
     * @param parameters
     * @param beginIndex
     * @param maxResult
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryForList(String sentence, final int beginIndex, final int maxResult) {
        return this.queryForList(sentence, Collections.EMPTY_MAP, beginIndex, maxResult);
    }

    /**
     * @param sentence
     * @param parameters
     * @param beginIndex
     * @param maxResult
     * @return
     */
    public List<Map<String, Object>> queryForList(String sentence, Map<String, Object> parameters, final int beginIndex, final int maxResult) {
        parameters.put("beginIndex", beginIndex);
        parameters.put("maxResult", maxResult);
        return getJdbcTemplate().queryForList(this.getLimitedSentence(sentence, beginIndex, maxResult), parameters);
    }

    /**
     * @param sentence
     * @param key
     * @param parameter
     * @param beginIndex
     * @param maxResult
     * @return
     */
    public List<Map<String, Object>> queryForList(String sentence, final String key, final Object parameter, final int beginIndex, final int maxResult) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(key, parameter);
        parameters.put("beginIndex", beginIndex);
        parameters.put("maxResult", maxResult);
        return getJdbcTemplate().queryForList(this.getLimitedSentence(sentence, beginIndex, maxResult), parameters);
    }

    /**
     * Query by JDBC for list of resultClass (single column result) as result, using Map as basic parameter,
     *
     * @param <T>
     * @param sentence
     * @param parameters
     * @param resultClass
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> List<T> queryForSingleColumnList(String sentence, Map<String, Object> parameters, Class<T> resultClass) {
        return getJdbcTemplate().query(sentence, parameters, new SingleColumnRowMapper(resultClass));
    }


    /**
     * Query by JDBC for list of resultClass (single column result) as result, using single parameter
     *
     * @param <T>
     * @param sentence
     * @param parameters
     * @param resultClass
     * @return
     */
    @SuppressWarnings({"serial", "unchecked", "rawtypes"})
    public <T> List<T> queryForSingleColumnList(String sentence, final String key, final Object parameter, Class<T> resultClass) {
        return getJdbcTemplate().query(sentence, new HashMap<String, Object>() {{
            put(key, parameter);
        }}, new SingleColumnRowMapper(resultClass));
    }

    /**
     * Query by JDBC for list of resultClass as result, using Map as basic parameter,
     *
     * @param <T>
     * @param sentence
     * @param parameters
     * @param resultClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryForList(String sentence, Class<T> resultClass) {
        return getJdbcTemplate().query(sentence, Collections.EMPTY_MAP, BeanPropertyRowMapper.newInstance(resultClass));
    }

    /**
     * Query by JDBC for list of resultClass as result, using Map as basic parameter,
     *
     * @param <T>
     * @param sentence
     * @param parameters
     * @param resultClass
     * @return
     */
    public <T> List<T> queryForList(String sentence, Map<String, Object> parameters, Class<T> resultClass) {
        return getJdbcTemplate().query(sentence, parameters, BeanPropertyRowMapper.newInstance(resultClass));
    }

    /**
     * Query by JDBC for list of resultClass as result, using single parameter
     *
     * @param <T>
     * @param sentence
     * @param parameters
     * @param resultClass
     * @return
     */
    @SuppressWarnings("serial")
    public <T> List<T> queryForList(String sentence, final String key, final Object parameter, Class<T> resultClass) {
        return getJdbcTemplate().query(sentence, new HashMap<String, Object>() {{
            put(key, parameter);
        }}, BeanPropertyRowMapper.newInstance(resultClass));
    }

    /**
     * Query by JDBC for list result, using a typical JavaBean as parameter.
     *
     * @param <T>
     * @param sentence
     * @param properties
     * @param resultClass
     * @return
     */
    public <T> List<T> queryForList(String sentence, Object properties, Class<T> resultClass) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(properties);
        return getJdbcTemplate().query(sentence, namedParameters, BeanPropertyRowMapper.newInstance(resultClass));
    }

    /**
     * @param sentence
     * @param parameters
     * @param resultClass
     * @param beginIndex
     * @param maxResult
     * @return
     */
    public <T> List<T> queryForList(String sentence, Map<String, Object> parameters, Class<T> resultClass, final int beginIndex, final int maxResult) {
        parameters.put("beginIndex", beginIndex);
        parameters.put("maxResult", maxResult);
        return this.queryForList(this.getLimitedSentence(sentence, beginIndex, maxResult), parameters, resultClass);
    }

    /**
     * @param sentence
     * @param parameters
     * @param resultClass
     * @param beginIndex
     * @param maxResult
     * @return
     */
    public <T> List<T> queryForList(String sentence, final String key, final Object parameter, Class<T> resultClass, final int beginIndex, final int maxResult) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("beginIndex", beginIndex);
        parameters.put("maxResult", maxResult);
        return this.queryForList(this.getLimitedSentence(sentence, beginIndex, maxResult), parameters, resultClass);
    }

    /**
     * Execute SQL according to parameters
     *
     * @param sql
     * @param parameters
     */
    @SuppressWarnings("serial")
    public void executeSQL(String sql, final String key, final Object parameter) {
        getJdbcTemplate().update(sql, new HashMap<String, Object>() {{
            put(key, parameter);
        }});
    }

    /**
     * Execute SQL according to parameters
     *
     * @param sql
     * @param parameters
     */
    public void executeSQL(String sql, Map<String, Object> parameters) {
        getJdbcTemplate().update(sql, parameters);
    }

    /**
     * Execute SQL according to bean
     *
     * @param sql
     * @param bean
     */
    public void executeSQL(String sql, Object bean) {
        getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(bean));
    }

    /**
     * Query by JDBC for SqlRowSet, using Map as basic parameter.
     *
     * @param sql
     * @param paramMap
     * @return
     */
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) {
        return getJdbcTemplate().queryForRowSet(sql, paramMap);
    }

    /**
     * Query by JDBC for SqlRowSet, using a typical JavaBean as basic parameters.
     *
     * @param sql
     * @param properties
     * @return
     */
    public SqlRowSet queryForRowSet(String sql, Object properties) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(properties);
        return getJdbcTemplate().queryForRowSet(sql, paramSource);
    }

    /**
     * ProcessRow after query, using Map as basic parameter.
     *
     * @param sql
     * @param paramMap
     * @param rch
     * @throws DataAccessException
     */
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch) {
        getJdbcTemplate().query(sql, new MapSqlParameterSource(paramMap), rch);
    }

    /**
     * ProcessRow after query, using single parameter.
     *
     * @param sentence
     * @param parameters
     * @return
     */
    @SuppressWarnings("serial")
    public void query(String sql, final String key, final Object parameter, RowCallbackHandler rch) {
        getJdbcTemplate().query(sql, new HashMap<String, Object>() {{
            put(key, parameter);
        }}, rch);
    }

    /**
     * ProcessRow after query, using a typical JavaBean as basic parameters.
     *
     * @param sql
     * @param properties
     * @param rch
     * @throws DataAccessException
     */
    public void query(String sql, Object properties, RowCallbackHandler rch) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(properties);
        getJdbcTemplate().query(sql, paramSource, rch);
    }
}