package eu.appsatori.ts2011;

import static org.duckapter.Duck.type;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class CalcTest {

	private Calc calc;

	private int[] numbers;
	private int display;
	private int x;
	private String operator;
	private int y;
	private int result;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ new int[] { 1, 2, 3 }, 123, 4, "+", 2, 6 },
				{ new int[] { 2, 2, 4 }, 224, 4, "-", 2, 2 },
				{ new int[] { 3, 5, 1 }, 351, 4, "/", 2, 2 },
				{ new int[] { 1, 4, 5 }, 145, 4, "*", 2, 8 } };
		return Arrays.asList(data);
	}

	public CalcTest(int[] numbers, int display, int x, String operator, int y,
			int result) {
		this.numbers = numbers;
		this.display = display;
		this.x = x;
		this.operator = operator;
		this.y = y;
		this.result = result;
	}

	@After
	public void tearDown() throws Exception {
		calc = null;
	}

	@Before
	public void setUp() throws Exception {
		calc = new Calc();
	}

	@Test
	public void testEmptyDisplay() throws Exception {
		assertEquals("0", calc.getDisplay());
	}

	@Test
	public void testPushingButtons() throws Exception {
		pushButtons(numbers);

		assertEquals("" + display, calc.getDisplay());
	}

	@Test
	public void testBasicOperators() throws Exception {
		calc.push("" + x);
		calc.push(operator);
		calc.push("" + y);
		calc.push("=");

		assertEquals("" + result, calc.getDisplay());
	}

	@Test
	public void testRegistry() throws Exception {
		pushButtons(new int[] { 3, 2 });
		assertEquals("0", getCalcRegistry());
		assertEquals("32", calc.getDisplay());

		calc.push("+");

		assertEquals(getCalcRegistry(), calc.getDisplay());

		pushButtons(new int[] { 6, 4 });
		calc.push("=");

		assertEquals("96", calc.getDisplay());
		assertEquals(getCalcRegistry(), calc.getDisplay());
	}
	
	@Test
	public void testOperators() throws Exception {
		Operator plus = mock(Operator.class);
		Operators operators = mock(Operators.class);
		calc.setOperators(operators);
		
		pushButtons(new int[]{1,2});
		verify(operators, never()).get("+");
		reset(operators);
		
		when(operators.get("+")).thenReturn(plus);
		calc.push("+");
		verify(operators).get("+");
		verify(plus, never()).operate(12, 12);
		reset(operators);
		
		pushButtons(new int[]{1,2});
		verify(operators, never()).get("+");
		verify(plus, never()).operate(12, 12);
		reset(operators);
		
		when(plus.operate(12, 12)).thenReturn(24);
		calc.push("=");
		verify(plus).operate(12, 12);
	}
	
	

	private static interface HasRegistry {
		@Private @Field String getRegistry();
	}

	private String getCalcRegistry() {
		return type(calc, HasRegistry.class).getRegistry();
	}

	private void pushButtons(int[] numbers) {
		for (int i : numbers) {
			calc.push("" + i);
		}
	}

}
