/**
 * 
 */
package com.demo2do.core.security.details;


/**
 * @author Downpour
 */
public class Role {

	private String name;
	
	private String alias;
	
	private String description;
	
	private boolean display;
	
	/**
	 * The default constructor
	 */
	public Role() {
		
	}
	
	/**
	 * The full constructor
	 * 
	 * @param name
	 * @param alias
	 * @param description
	 */
	public Role(String name, String alias, String description, boolean display) {
		this.name = name;
		this.alias = alias;
		this.description = description;
		this.display = display;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
 	}
	
	/**
	 * @return the display
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param display the display to set
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}
	
}
