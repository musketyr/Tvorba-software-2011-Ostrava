package eu.appsatori.ts2011;

public interface Operator {
	
	static final Operator DISPLAY = new Operator(){
		public int operate(int registry, int display) {
			return display;
		}
	};
	
	static final Operator PLUS = new Operator(){
		public int operate(int registry, int display) {
			return registry + display;
		}
	};
	
	static final Operator MINUS = new Operator(){
		public int operate(int registry, int display) {
			return registry - display;
		}
	};
	
	static final Operator DIV = new Operator(){
		public int operate(int registry, int display) {
			return registry / display;
		}
	};
	
	static final Operator POWER = new Operator(){
		public int operate(int registry, int display) {
			return registry * display;
		}
	};
	
	int operate(int registry, int display);

}
