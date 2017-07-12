package com.antlr4.demo;

public class Grammars {

	
	    private String name;

	    private Properties properties;

	    public String getName ()
	    {
	        return name;
	    }

	    public void setName (String name)
	    {
	        this.name = name;
	    }

	    public Properties getProperties ()
	    {
	        return properties;
	    }

	    public void setProperties (Properties properties)
	    {
	        this.properties = properties;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [name = "+name+", properties = "+properties+"]";
	    }
	
}
