package com.antlr4.demo;

public class Pojo {
	
	private Grammars[] grammars;

    public Grammars[] getGrammars ()
    {
        return grammars;
    }

    public void setGrammars (Grammars[] grammars)
    {
        this.grammars = grammars;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [grammars = "+grammars+"]";
    }
    
}
