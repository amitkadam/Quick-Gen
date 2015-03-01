package quick_gen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class ExpEvaluator
{
    private static final String ExpEvaluator = null;
	public double evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();
 
         // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();
 
        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();
 
        for (int i = 0; i < tokens.length; i++)
        {
             // Current token is a whitespace, skip it
            if (tokens[i] == ' ')
                continue;
 
            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.'))
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
            }
 

            // Current token is an opening brace, push it to 'ops'
            if (i < tokens.length && tokens[i] == '(')
                ops.push(tokens[i]);
 
            // Closing brace encountered, solve entire brace
            if (i < tokens.length && tokens[i] == ')')
            {
                while (ops.peek() != '(')
                  values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            }
 
            // Current token is an operator.
            if (i < tokens.length && (tokens[i] == '+' || tokens[i] == '-' ||
            		tokens[i] == '*' || tokens[i] == '/'))
            {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                  values.push(applyOp(ops.pop(), values.pop(), values.pop()));
 
                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }
 
        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
 
        // Top of 'values' contains result, return it
        return values.pop();
    }
 
    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }
 
    // A utility method to apply an operator 'op' on operands 'a' 
    // and 'b'. Return the result.
    public static double applyOp(char op, Double double1, Double double2)
    {
        switch (op)
        {
        case '+':
            return double2 + double1;
        case '-':
            return double2 - double1;
        case '*':
            return double2 * double1;
        case '%':
        	return double2 % double1;
        case '/':
            if (double1 == 0)
                throw new
                UnsupportedOperationException("Cannot divide by zero");
            return double2 / double1;
        }
        return 0;
    }
    
    public String replaceWithValues (String expression, Map<String, Double> values)
    {
    	Iterator<String> iterator = values.keySet().iterator();
    	while(iterator.hasNext()){
    	  Object key   = iterator.next();
    	  Object value = values.get(key);
    	  expression = expression.replaceAll((String) key, Double.toString((double) value));
    	}
		return expression;
    }
    
    public double evaluate (String expression, Map<String, Double> values)
    {
    	return evaluate(replaceWithValues (expression, values));
    }
}

