package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
		
	
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays in the expression.
     * For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays)
    {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	// Delete all Whitespaces --> they do not affect code
    	expr = expr.replaceAll(" ", "");
    	System.out.println(expr);
    	
    	Stack<String> name = new Stack<>(); // Stores all letters of the Array or variable
    	boolean isArray = false; // Keeps track of whether I've parsed through the array name
    	boolean isVar = false; // Keeps track of whether I've parsed through the variable name
    	
    	for(int i=expr.length()-1; i>=0; i--)
    	{
    		if(expr.charAt(i)=='0'||expr.charAt(i)=='1'||expr.charAt(i)=='2'|| expr.charAt(i)=='3'||expr.charAt(i)=='4'||expr.charAt(i)=='5'||
    	       expr.charAt(i)=='6'||expr.charAt(i)=='7'||expr.charAt(i)=='8'||expr.charAt(i)=='9'||expr.charAt(i) == ']')
    			continue;
    		
    		// if I've been parsing the Array name and delimiter has been met, then I am no longer adding to arrayname.
    		else if(expr.charAt(i)=='(' || expr.charAt(i)==')'|| expr.charAt(i) == '[' ||
    				expr.charAt(i)=='+' || expr.charAt(i)=='-'|| expr.charAt(i)=='*' ||expr.charAt(i)=='/')
    		{
    			if(isArray)
    			{
    				isArray = false;
    				// Result in helper is going to be an Array
    				Array temp = new Array(helper(name));
    				if(arrays.indexOf(temp)== -1)
    	    			arrays.add(temp);
    			}
    			else if(isVar) 
    			{
    				isVar = false;
    				// Result in helper is going to be a Variable
    				Variable temp = new Variable(helper(name));
    				if(vars.indexOf(temp)== -1)
    	    			vars.add(temp);
    			}
    		}
    		// if it is not an array, but it is an letter then add it to name (which holds var name);
    		// has to be else if because both can be false 
    		else if(!isArray)
    		{
    			isVar = true;
    			name.push(expr.substring(i,i+1));
    		}
    		// if isArray is true, then add the letter to the stack corresponding to array name
    		else if(isArray)
    			name.push(expr.substring(i,i+1));
    		
    		
    		// mandatory check for [
    		if(expr.charAt(i)=='[')
    			isArray = true;
    	}
    	
    	if(isArray)
		{
			isArray = false;
			// Result in helper is going to be an Array
			Array temp = new Array(helper(name));
			if(arrays.indexOf(temp)== -1)
    			arrays.add(temp);
		}
		else if(isVar) 
		{
			isVar = false;
			// Result in helper is going to be a Variable
			Variable temp = new Variable(helper(name));
			if(vars.indexOf(temp)== -1)
    			vars.add(temp);
		}
    }
   
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays)
    {
    	expr = expr.replaceAll(" ", ""); // Remove all whitespace
    	
    	//System.out.println("\nExpression before sub -->  " + expr);
    	expr = rewrite(expr, vars); // Replace all variables with their names
    	//System.out.println("Expression after sub -->  " + expr + "\n\n");
    	
    	// Keep track of number of opening brackets;
    	// If we know how many opening brackets, we also know how many pairs of brackets.
    	int numBrackets = 0;
    	for(int i=0; i<expr.length(); i++)
    		if(expr.charAt(i) == '[')
    			++numBrackets;
    	
    	while(numBrackets != 0)
    	{	
    		// Contains the [ matching the first ] in the expression
    		int indexOfMatchingBracket = indexOfMatchingBracket(expr, expr.indexOf("]"));
        	
    		// This passes the interior of the first pair of [] and stores the result in betweenBrackets. 
        	int betweenBrackets = evaluate(expr.substring(indexOfMatchingBracket+1, expr.indexOf("]")));
        	
        	// Substitute interior of first pair of []=
        	//System.out.println("Expression Before -->  " + expr);
        	expr = rewrite(expr, arrays, betweenBrackets, indexOfMatchingBracket(expr, expr.indexOf("]")));
        	//System.out.println("Expression after -->  " + expr);
        	
        	// After this process has finished, we know there is one less pair of brackets
        	--numBrackets;
    	}
    	//Evaluate the entire thing.
    	return evaluate2(expr);
    }
    
    
    // evaluate and evalute2 pass the things to computeInteger and computeFloat respectively
    
    
    //This method is used solely to compute the solution inside an Array as it will be an integer
    private static int evaluate(String expr)
    {
    	// Base case: execute if there are no more parenthesis
    	if(expr.indexOf(')') == -1)
    		return computeInteger(expr);
    	
    	// Only executed if there are still more parenthesis. 
    	int indexOfClosingParenthesis = expr.indexOf(")");
    	int indexOfMatchingParenthesis = indexOfMatchingParenthesis(expr, indexOfClosingParenthesis);
    	
    	return evaluate(expr.substring(0, indexOfMatchingParenthesis) 
    			+ evaluate(expr.substring(indexOfMatchingParenthesis+1,indexOfClosingParenthesis)) 
    			+ expr.substring(indexOfClosingParenthesis+1));
    }
    
    // Used to compute final expression
    private static float evaluate2(String expr)
    {
    	if(expr.indexOf(')') == -1)
	    	return computeFloat(expr);
    		
    	// Only executed if there are still more parenthesis. 
    	int indexOfClosingParenthesis = expr.indexOf(")");
    	int indexOfMatchingParenthesis = indexOfMatchingParenthesis(expr, indexOfClosingParenthesis);
    	
    	return evaluate2(expr.substring(0, indexOfMatchingParenthesis) 
    			+ evaluate2(expr.substring(indexOfMatchingParenthesis+1,indexOfClosingParenthesis)) 
    			+ expr.substring(indexOfClosingParenthesis+1));
    }
    
    
    // these two methods actually compute
    
    private static int computeInteger(String expr)
    {
    	// If the expression only has no operands, parameter will presumably be an int
        if((expr.indexOf('+')== -1 && expr.indexOf('-')== -1 && expr.indexOf('*')== -1 && expr.indexOf('/')== -1))
            return Integer.parseInt(expr);
        
        Stack<Integer> operand = new Stack<>();
        Stack<Character> operator = new Stack<>();
        
        String numberString = "";
        while(expr.length() > 0)
        {
            //================================================================================ GOLDEN
            char ch = expr.charAt(0);
            if(ch=='+' || ch=='-' ||ch=='*' ||ch=='/')
            {
    			// Reset String and add it to operand
    			if(!numberString.equals(""))
    			{
    				int k = Integer.parseInt(numberString);
    				operand.push(k);
    				numberString = "";
    			}
    			operator.push(ch);
    			
    			// checks if next character starts has unary - before it
    			// it will never have another binary operator because if the opening if evaluated that means there has to be number after
    			if(expr.substring(1,2).equals("-"))
    			{
    				numberString = "-";
    				expr = expr.substring(1);
    			}
    		}
            
            if(ch=='0' || ch=='1' ||ch=='2' ||ch=='3' ||ch=='4' ||ch=='5' ||ch=='6' ||ch=='7' ||ch=='8' ||ch=='9' || ch=='.')
            {    
                numberString += expr.substring(0,1);
            }
            // Put in last number
            if(expr.length()==1)
            {
                int k = Integer.parseInt(numberString);
                operand.push(k);
                numberString = "";
            }
            //============================================================================== = GOLDEN
            /*
             * Size of Operand = 2
             * Size of Operator = 1
             * If they are mult or div then only compute
             *
             * We still need to handle the final case
             */
            if(operand.size()==2 && operator.size()==1)
            {
                char sign = operator.pop();
                if(sign == '+' || sign == '-')
                    operator.push(sign);
                else
                {
                    int second = operand.pop();
                    int first = operand.pop();
                    
                    if(sign=='*')
                        operand.push(first*second);
                    else
                        operand.push(first/second);
                }
            }
            else if(operand.size()==2 && operator.size()==2)
            {
                char sign = operator.pop();
                if(sign == '+' || sign == '-')
                {
                    // + or - last and second to last in operand stack
                    char temp = operator.pop();
                    
                    int second = operand.pop();
                    int first = operand.pop();
                    
                    if(temp=='*')
                        operand.push(first*second);
                    else if(temp=='/')
                        operand.push(first/second);
                    else if(temp=='+')
                        operand.push(first+second);
                    else if(temp=='-')
                        operand.push(first-second);
                }
                else // sign is * or /
                {
                    char temp = operator.pop();
                    
                    if(temp=='+' || temp=='-')
                        operator.push(temp);
                    
                    else
                    {
                        int second = operand.pop();
                        int first = operand.pop();
                        
                        if(temp=='*')
                            operand.push(first*second);
                        else if(temp=='/')
                            operand.push(first/second);
                    }
                }
                operator.push(sign);
            }
            /*
             * Size of Operand = 3
             * Size of Operator = 3
             * This means bottom of stack is +- and top of stack is either * or /
             */
            else if(operand.size()==3 && operator.size()==3)
            {
                char sign = operator.pop();
                if(sign == '+' || sign == '-')
                {
                    // + or - last and second to last in operand stack
                    char temp = operator.pop();
                    
                    int second = operand.pop();
                    int first = operand.pop();
                    
                    if(temp=='+')
                        operand.push(first+second);
                    else if(temp=='-')
                        operand.push(first-second);
                    else if(temp=='*')
                        operand.push(first*second);
                    else if(temp=='/')
                        operand.push(first/second);
                }
                else // sign will be * or /
                {
                    char temp = operator.pop();
                    if(temp=='*'||temp=='/')
                    {
                        int second = operand.pop();
                        int first = operand.pop();
                        
                        if(temp=='*')
                            operand.push(first*second);
                        else
                            operand.push(first/second);
                    }
                }
                operator.push(sign);
            }
            else if(operand.size()==3 && operator.size()==2)
            {
                char sign = operator.pop();
                if(sign == '*' || sign == '/')
                {
                    int third = operand.pop();
                    int second = operand.pop();
                    
                    if(sign=='*')
                        operand.push(second*third);
                    else
                        operand.push(second/third);
                }
                else // sign will be + or -
                {
                    char temp = operator.pop();
                    
                    int third = operand.pop();
                    int second = operand.pop();
                    int first = operand.pop();
                    
                    if(temp=='+')
                        operand.push(first+second);
                    else if(temp=='-')
                        operand.push(first-second);
                    else if(temp=='*')
                        operand.push(first*second);
                    else if(temp=='/')
                        operand.push(first/second);
                    
                    operand.push(third);
                    
                    operator.push(sign);
                }
            }
            expr = expr.substring(1);
        }
        
        if(operand.size()==2 && operator.size()==1)
        {
            char sign = operator.pop();
            int second = operand.pop();
            int first = operand.pop();
            
            if(sign=='+')
                operand.push(first+second);
            else
                operand.push(first-second);
        }
        
        return operand.pop();


    }
    
    private static float computeFloat(String expr)
    {
    	// If the expression only has no operands, parameter will presumably be an integer
    	if((expr.indexOf('+')== -1 && expr.indexOf('-')== -1 && expr.indexOf('*')== -1 && expr.indexOf('/')== -1))
    		return Float.parseFloat(expr);
    	
    	Stack<Float> operand = new Stack<>();
    	Stack<Character> operator = new Stack<>();
    	
    	String numberString = "";
    	while(expr.length() > 0)
    	{
    		//================================================================================ GOLDEN
    		char ch = expr.charAt(0);
    		if(ch=='+' || ch=='-' ||ch=='*' ||ch=='/')
    		{
    			// Reset String and add it to operand
    			if(!numberString.equals(""))
    			{
    				float k = Float.parseFloat(numberString);
    				operand.push(k);
    				numberString = "";
    			}
    			operator.push(ch);
    			
    			// checks if next character starts has unary - before it
    			// it will never have another binary operator because if the opening if evaluated that means there has to be number after
    			if(expr.substring(1,2).equals("-"))
    			{
    				numberString = "-";
    				expr = expr.substring(1);
    			}
    		}
    		
    		if(ch=='0' || ch=='1' ||ch=='2' ||ch=='3' ||ch=='4' ||ch=='5' ||ch=='6' ||ch=='7' ||ch=='8' ||ch=='9' || ch=='.')
    		{	
    			numberString += expr.substring(0,1);
    		}
    		// Put in last number
    		if(expr.length()==1)
    		{
    			float k = Float.parseFloat(numberString);
    			operand.push(k);
    			numberString = "";
    		}
    		//============================================================================== = GOLDEN
    		/*
        	 * Size of Operand = 2
        	 * Size of Operator = 1
        	 * If they are mult or div then only compute
        	 * 
        	 * We still need to handle the final case
        	 */
        	if(operand.size()==2 && operator.size()==1)
        	{
        		char sign = operator.pop();
        		if(sign == '+' || sign == '-')
        			operator.push(sign);
        		else
        		{
        			float second = operand.pop();
            		float first = operand.pop();
            		
            		if(sign=='*')
                		operand.push(first*second);
            		else
            			operand.push(first/second);
        		}
        	}
        	else if(operand.size()==2 && operator.size()==2)
        	{
        		char sign = operator.pop();
        		if(sign == '+' || sign == '-') 
        		{
        			// + or - last and second to last in operand stack
        			char temp = operator.pop();
        			
        			float second = operand.pop();
        			float first = operand.pop();
        			
        			if(temp=='*')
            			operand.push(first*second);
            		else if(temp=='/')
            			operand.push(first/second);
            		else if(temp=='+')
            			operand.push(first+second);
            		else if(temp=='-')
            			operand.push(first-second);
        		}
        		else // sign is * or /
        		{
        			char temp = operator.pop();
        			
        			if(temp=='+' || temp=='-')
        				operator.push(temp);
        			
        			else
        			{
	        			float second = operand.pop();
	        			float first = operand.pop();
	        			
	        			if(temp=='*')
	        				operand.push(first*second);
	        			else if(temp=='/')
	            			operand.push(first/second);
        			}
        		}
        		operator.push(sign);
        	}
        	/*
        	 * Size of Operand = 3
        	 * Size of Operator = 3
        	 * This means bottom of stack is +- and top of stack is either * or /
        	 */
    		else if(operand.size()==3 && operator.size()==3)
    		{
    			char sign = operator.pop();
        		if(sign == '+' || sign == '-') 
        		{
        			// + or - last and second to last in operand stack
        			char temp = operator.pop();
        			
        			float second = operand.pop();
        			float first = operand.pop();
        			
        			if(temp=='+')
            			operand.push(first+second);
            		else if(temp=='-')
            			operand.push(first-second);
            		else if(temp=='*')
            			operand.push(first*second);
            		else if(temp=='/')
            			operand.push(first/second);
        		}
        		else // sign will be * or /
        		{
        			char temp = operator.pop();
        			if(temp=='*'||temp=='/')
        			{
        				float second = operand.pop();
            			float first = operand.pop();
            			
            			if(temp=='*')
            				operand.push(first*second);
            			else
            				operand.push(first/second);
        			}
        		}
        		operator.push(sign);
    		}
    		else if(operand.size()==3 && operator.size()==2)
    		{
    			char sign = operator.pop();
        		if(sign == '*' || sign == '/') 
        		{
        			float third = operand.pop();
        			float second = operand.pop();
        			
        			if(sign=='*')
        				operand.push(second*third);
        			else
        				operand.push(second/third);
        		}
        		else // sign will be + or -
        		{
        			char temp = operator.pop();
        			
    				float third = operand.pop();
    				float second = operand.pop();
        			float first = operand.pop();
        			
        			if(temp=='+')
        				operand.push(first+second);
        			else if(temp=='-')
        				operand.push(first-second);
        			else if(temp=='*')
        				operand.push(first*second);
        			else if(temp=='/')
        				operand.push(first/second);
        			
        			operand.push(third);
        			
        			operator.push(sign);
        		}
    		}
    		expr = expr.substring(1);
    	}
    	
    	if(operand.size()==2 && operator.size()==1)
    	{
    		char sign = operator.pop();
    		float second = operand.pop();
    		float first = operand.pop();
    		
    		if(sign=='+')
    			operand.push(first+second);
    		else
    			operand.push(first-second);
    	}
    	
    	return operand.pop(); 
    }

    
    /*
     * Rewrite Method
     *               |
     * Turns a-(b+A[B[2]])*d + 3 into
     *       a-(b+A[0])*d + 3
     * Where suppose B = {0,0,1};
     */
    private static String rewrite(String expr, ArrayList<Array> arrays, int betweenBrackets, int openingBracket)
    {
    	Stack<String> name = new Stack<>(); // Stores all letters of the Array
    	
    	// Populate the name stack with array corresponding to openingBracket
    	int count = openingBracket-1;
    	char ch = expr.charAt(count);
    	
    	while(count!=0 && !((ch== '(') || (ch== '[') || (ch== '+') || (ch== '-')|| (ch== '*') || (ch== '/')))
    	{
    		name.push(Character.toString(ch));
    		--count;
    		ch = expr.charAt(count);
    	}
    	
    	if(!((ch== '(') || (ch== '[') || (ch== '+') || (ch== '-')|| (ch== '*') || (ch== '/')))
    	{
    		name.push(Character.toString(ch));
    		--count;
    	}
    	
    	String arrayName = helper(name);
    	
    	Array temp = null;
    	for(Array n : arrays)
    		if(n.name.equals(arrayName))
    			temp = n;
    	
    	// Holds value of say, B[2]
    	int reWrittenPart = temp.values[betweenBrackets];

    	return expr.substring(0,count+1) + reWrittenPart + expr.substring(expr.indexOf(']')+1);
    }
    /*
     *  Substitutes all Variables with their Integer equivalents
     */
    private static String rewrite(String expr, ArrayList<Variable> vars)
    {	
    	for(Variable temp : vars)
    		do
    		{
    			int i = expr.indexOf(temp.name);
    			
    			if(i == -1)
    				break;
    			
    			if(i == 0)
    				expr = temp.value + expr.substring(temp.name.length());
    			else
    				expr = expr.substring(0, i) + temp.value + expr.substring(i+ temp.name.length());
    		
    		} while(expr.indexOf(temp.name) != -1);
    	
    	return expr;
    }
    
    private static int indexOfMatchingBracket(String expr, int indexOfFirstClosingBracket)
    {
    	// Finds the index of the [ matching the passed ].
    	for(int i= indexOfFirstClosingBracket-1; i>=0; i--)
    		if(expr.charAt(i) == '[')
    			return i;
    	
    	return -1;
    }
    private static int indexOfMatchingParenthesis(String expr, int indexOfFirstClosingParenthesis)
    {
    	// Finds the index of the [ matching the passed ].
    	for(int i= indexOfFirstClosingParenthesis-1; i>=0; i--)
    		if(expr.charAt(i) == '(')
    			return i;
    	
    	return -1;
    }

    private static String helper(Stack<String> stack)
    {
    	String result = "";
    	while(stack.size() != 0)
	    	result += stack.pop();
    	
    	stack.clear();
    	
    	return result;
    }

}
