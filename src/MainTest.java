import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class MainTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testEvaluateExpressionSimple() throws Exception {
        assertEquals("2 + 2", 4.0, Main.evaluateExpression("2 + 2"), 0.01);
    }

    @Test
    public void testEvaluateExpressionWithVariables() throws Exception {
        Main.setVariable("x", 5);
        assertEquals("x + 2", 7.0, Main.evaluateExpression("x + 2"), 0.01);
    }

    @Test
    public void testEvaluateExpressionDivisionByZero() {
        Exception exception = assertThrows(Exception.class, () -> {
            Main.evaluateExpression("5 / 0");
        });

        assertEquals("Деление на ноль.", exception.getMessage());
    }
}