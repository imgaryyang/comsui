/**
 * 
 */
package com.demo2do.core.persistence.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author downpour
 *
 */
public class Filter {
	
	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();
	
	private List<String> sentences = new ArrayList<String>();
	
	/**
	 * The default construtor
	 */
	public Filter() {
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public Filter addEquals(String key, Object value) {
		
		if (value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("eq", null, key);

			this.sentences.add(" " + key + " = :" + parameterKey + " ");
			this.parameters.put(parameterKey, value);
		}

		return this;
	}
	
	/**
	 * @param alias
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addEquals(String alias, String key, Object value) {
		
		if(value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("eq", alias, key);
			
			if(StringUtils.isEmpty(alias)) {
				this.sentences.add(" " + key + " = :" + parameterKey + " ");
				this.parameters.put(parameterKey, value);
			} else {
				this.sentences.add(" " + alias + "." + key + " = :" + parameterKey + " ");
				this.parameters.put(parameterKey, value);
				
			}
			
		}
		
		return this;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addLike(String key, Object value) {

		if (value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("like", null, key);

			this.sentences.add(" " + key + " LIKE :" + parameterKey + " ");
			this.parameters.put(parameterKey, "%" + value.toString() + "%");
		}

		return this;
	}
	
	/**
	 * 
	 * @param alias
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addLike(String alias, String key, Object value) {
		
		if(value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("like", alias, key);
			
			if(StringUtils.isEmpty(alias)) {
				this.sentences.add(" " + key + " LIKE :" + parameterKey + " ");
				this.parameters.put(parameterKey, "%" + value.toString() + "%");
			} else {
				this.sentences.add(" " + alias + "." + key + " LIKE :" + parameterKey + " ");
				this.parameters.put(parameterKey, "%" + value.toString() + "%");
				
			}
			
		}
		
		return this;
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addGreaterThan(String key, Object value) {
		
		if (value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("gt", null, key);

			this.sentences.add(" " + key + " > :" + parameterKey + " ");
			this.parameters.put(parameterKey, value);
		}

		return this;
	}
	
	/**
	 * 
	 * @param alias
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addGreaterThan(String alias, String key, Object value) {
		
		if(value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("gt", alias, key);
			
			if(StringUtils.isEmpty(alias)) {
				this.sentences.add(" " + key + " > :" + parameterKey + " ");
				this.parameters.put(parameterKey, value);
			} else {
				this.sentences.add(" " + alias + "." + key + " > :" + parameterKey + " ");
				this.parameters.put(parameterKey, value);
				
			}
			
		}
		
		return this;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addLessThan(String key, Object value) {
		
		if (value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("lt", null, key);

			this.sentences.add(" " + key + " < :" + parameterKey + " ");
			this.parameters.put(parameterKey, value);
		}

		return this;
	}
	
	/**
	 * 
	 * @param alias
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addLessThan(String alias, String key, Object value) {
		
		if(value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("lt", alias, key);
			
			if(StringUtils.isEmpty(alias)) {
				this.sentences.add(" " + key + " < :" + parameterKey + " ");
				this.parameters.put(parameterKey, value);
			} else {
				this.sentences.add(" " + alias + "." + key + " < :" + parameterKey + " ");
				this.parameters.put(parameterKey, value);
				
			}
			
		}
		
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addBitandEquals(String key, Object value) {
		
		if (value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("baeq", null, key);

			this.sentences.add(" bitand(" + key + ", :" + parameterKey + ") > 0 ");
			this.parameters.put(parameterKey, value);
		}

		return this;
	}
	
	/**
	 * 
	 * @param alias
	 * @param key
	 * @param value
	 * @return
	 */
	public Filter addBitandEquals(String alias, String key, Object value) {
		
		if(value != null && this.checkStatus(value)) {
			
			if(value instanceof String && StringUtils.isBlank((String) value)) {
				return this;
			}
			
			String parameterKey = this.generateParameterKey("baeq", alias, key);
			
			if(StringUtils.isEmpty(alias)) {
				this.sentences.add(" bitand(" + key + ", :" + parameterKey + ") > 0 ");
				this.parameters.put(parameterKey, value);
			} else {
				this.sentences.add(" bitand(" + alias + "." + key + ", :" + parameterKey + ") > 0 ");
				this.parameters.put(parameterKey, value);
				
			}
			
		}
		
		return this;
		
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private boolean checkStatus(Object value) {
		
		if(value instanceof EntityStatus) {
			EntityStatus entityStatus = (EntityStatus) value;
			return !entityStatus.isEmpty();
		}
		
		return true;
	}
	
	/**
	 * Generate parameter key
	 * 
	 * @param operation
	 * @param key
	 * @return
	 */
	private String generateParameterKey(String operation, String alias, String key) {
		
		StringBuffer sb = new StringBuffer(operation);
		
		if(!StringUtils.isEmpty(alias)) {
			sb.append(alias.substring(0, 1).toUpperCase() + alias.substring(1));
		}
		
		String[] temp = StringUtils.split(key, ".");
		for (int i = 0; i < temp.length; i++) {
            sb.append(temp[i].substring(0, 1).toUpperCase() + temp[i].substring(1));
        }
		
        return sb.toString();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWhereSentence() {
		
		if(sentences.isEmpty()) {
			return "";
		}
		
		return " WHERE" + StringUtils.join(sentences, "AND");
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClauseSentence() {
		
		if(sentences.isEmpty()) {
			return "";
		}
		
		return " AND" + StringUtils.join(this.sentences, "AND");
	}
	
	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
}
