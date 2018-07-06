/**
 * 
 */
package com.demo2do.core.cache;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.core.ReflectUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


/**
 * @author Downpour
 */
public class CompositeCacheAccessor implements CacheAccessor {
	
	private ExpressionParser expressionParser = new SpelExpressionParser();
	
	private Set<String> keys;
	
	private EvaluationContext evaluationContext;

	/**
	 * Generate evaluationContext and available keys by setting cacheRoot
	 * 
	 * @param cacheRoot
	 */
	public void setCacheRoot(Object cacheRoot) {
		this.evaluationContext = new StandardEvaluationContext(cacheRoot);
		this.keys = this.generateKeys(cacheRoot.getClass());
	}

	/**
	 * 
	 * @return
	 */
	protected Set<String> generateKeys(Class<?> cacheRootClass) {
		PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanGetters(cacheRootClass);
		Set<String> keys = new HashSet<String>(propertyDescriptors.length);
		
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			keys.add(propertyDescriptor.getName());
		}
		
		return keys;
	}
	
	/* (non-Javadoc)
	 * @see com.demo2do.core.cache.CacheAccessor#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		return keys.contains(key);
	}

	/* (non-Javadoc)
	 * @see com.demo2do.core.cache.CacheAccessor#evaluate(java.lang.String)
	 */
	public Object evaluate(String key) {
		try {
			Expression expression = this.expressionParser.parseExpression(key);
			return expression.getValue(evaluationContext);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
