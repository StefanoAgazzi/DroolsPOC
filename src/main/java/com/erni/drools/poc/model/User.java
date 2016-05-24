package com.erni.drools.poc.model;



public class User implements java.io.Serializable
{

	static final long serialVersionUID = 1L;

	private java.lang.Long id;
	private java.lang.String name;

	private java.lang.Boolean suspended;

	public User()
	{
	}

	public java.lang.Long getId()
	{
		return this.id;
	}

	public void setId(java.lang.Long id)
	{
		this.id = id;
	}

	public java.lang.String getName()
	{
		return this.name;
	}

	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	public java.lang.Boolean getSuspended()
	{
		return this.suspended;
	}

	public void setSuspended(java.lang.Boolean suspended)
	{
		this.suspended = suspended;
	}

	public User(java.lang.Long id, java.lang.String name,
				java.lang.Boolean suspended)
	{
		this.id = id;
		this.name = name;
		this.suspended = suspended;
	}

}