package eu.appsatori.ts2011;

public class Calc {
	
	private String registry = "0";
	private String display = "0";
	private Operator operator = Operator.DISPLAY;
	private Operators operators = new Operators();
	private boolean clearDisplay = false;
	
	public void push(String button){
		if("0".equals(display) || clearDisplay){
			display = button;
			clearDisplay = false;
		} else if(button.matches("\\d+")){
			display += button;
		} else if("=".equals(button)){
			display = "" + operator.operate(
					Integer.parseInt(registry), Integer.parseInt(display)
			);
			registry = display;
		} else {
			operator = operators.get(button);
			registry = display;
			clearDisplay = true;
		}
	}
	
	public String getDisplay(){
		return display;
	}
	
	public void setOperators(Operators ops){
		this.operators = ops;
	}

}
