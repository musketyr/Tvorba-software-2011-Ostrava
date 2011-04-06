package eu.appsatori.ts2011;

public class Operators {
	
	public Operator get(String literal){
		if("+".equals(literal)){
			return Operator.PLUS;
		} else if("-".equals(literal)){
			return Operator.MINUS;
		} else if("/".equals(literal)){
			return Operator.DIV;
		} else if("*".equals(literal)){
			return Operator.POWER;
		}
		return Operator.DISPLAY;
	}
}
