/**
 * 
 */
package com.demo2do.core.persistence.dialect;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.type.StandardBasicTypes;

import com.demo2do.core.persistence.function.MySQLBitwiseAndSQLFunction;

/**
 * @author Downpour
 */
public class Demo2doHibernateDialect extends MySQL5InnoDBDialect {
	
	/**
	 * The default constructor
	 */
	public Demo2doHibernateDialect() {
		super();
		registerFunction("bitand", new MySQLBitwiseAndSQLFunction("bitand", StandardBasicTypes.INTEGER));
	}

}
