import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
/**
 * основной класс
 */
public class Main {
    /**
     * HashMap для переменных
     */
    private static final Map<String, Double> variables = new HashMap<>();

    /**
     * добавляет переменную в variables
     * @param name название переменной
     * @param value значение переменной
     */
    public static void setVariable(String name, double value) {
        variables.put(name, value);
    }

    /**
     * Запрашивает у пользователя математическое выражение
     * @param args аргументы
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите выражение:");
        String expression = scanner.nextLine();

        try {
            double result = evaluateExpression(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        scanner.close();
    }

    /**
     * Запрос значений для переменных и вычисление выражения
     * @param expression выражение
     * @return вычисление выражения
     * @throws Exception ошибка
     */
    public static double evaluateExpression(String expression) throws Exception {
        expression = expression.replaceAll("\s+", "");
        for (char c : expression.toCharArray()) {
            if (Character.isLetter(c) && !variables.containsKey(String.valueOf(c))) {
                System.out.print("Введите значение для переменной '" + c + "': ");
                Scanner scanner = new Scanner(System.in);
                double value = scanner.nextDouble();
                variables.put(String.valueOf(c), value);
            }
        }
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    /**
     * Конвертирует инфиксное выражение в постфиксное
     * @param expression инфиксное выражение
     * @return постфиксное выражение
     * @throws Exception ошибка
     */
    private static String infixToPostfix(String expression) throws Exception {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        int precedence;

        for (char token : expression.toCharArray()) {
            if (Character.isDigit(token)) {
                output.append(token);
            } else if (Character.isLetter(token)) {
                output.append(token);
            } else if (token == '(') {
                stack.push(token);
            } else if (token == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(' ').append(stack.pop());
                }
                stack.pop(); // Удаляем '('
            } else { // оператор
                precedence = getPrecedence(token);
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= precedence) {
                    output.append(' ').append(stack.pop());
                }
                output.append(' ');
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            output.append(' ').append(stack.pop());
        }

        return output.toString().trim();
    }

    /**
     * Определяет приоритет операторов
     * @param operator оператор
     * @return приоритет оператора
     */
    private static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    /**
     * Вычисляет результат постфиксного выражения
     * @param postfix постфиксное выражение
     * @return результат постфиксного выражения
     * @throws Exception ошибка
     */
    private static double evaluatePostfix(String postfix) throws Exception {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix.split(" ")) {
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Double.parseDouble(token));
            } else if (Character.isLetter(token.charAt(0))) {
                if (variables.containsKey(token)) {
                    stack.push(variables.get(token));
                } else {
                    throw new Exception("Переменная '" + token + "' не определена.");
                }
            } else { // оператор
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(a - b);
                        break;
                    case '*':
                        stack.push(a * b);
                        break;
                    case '/':
                        if (b == 0) throw new Exception("Деление на ноль.");
                        stack.push(a / b);
                        break;
                    case '^':
                        stack.push(Math.pow(a, b));
                        break;
                    default:
                        throw new Exception("Неизвестный оператор: " + token);
                }
            }
        }
        return stack.pop();
    }
}
