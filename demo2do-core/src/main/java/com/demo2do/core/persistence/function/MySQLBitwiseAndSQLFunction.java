/**
 * 
 */
package com.demo2do.core.persistence.function;

import java.util.List;

import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * @author Downpour
 */
public class MySQLBitwiseAndSQLFunction extends StandardSQLFunction implements SQLFunction {
	
	/**
	 * 
	 * @param name
	 */
	public MySQLBitwiseAndSQLFunction(String name) {
		super(name);
	}

	/**
	 * 
	 * @param name
	 * @param typeValue
	 */
	public MySQLBitwiseAndSQLFunction(String name, Type typeValue) {
		super(name, typeValue);
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.dialect.function.StandardSQLFunction#render(org.hibernate.type.Type, java.util.List, org.hibernate.engine.spi.SessionFactoryImplementor)
	 */
	@SuppressWarnings("rawtypes")
	public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
		
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("The bitwise function must be passed 2 arguments");
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("(")
			  .append(arguments.get(0))
			  .append(" & ")
			  .append(arguments.get(1))
			  .append(" ) > 0");
		
		return buffer.toString();
	}

}
