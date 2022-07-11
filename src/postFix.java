import java.util.StringTokenizer;
/**
 * Methods for parsing and conversion
 * This method exports methods for dealing with expression parsing and conversion.
 * 
 * Queue objects are used for input and output.
 * Required methods:
 * <ul>
 * <li> public Queue parse (String arg)
 * <li> public Queue In2Post(Queue Qin)
 *
 * @author antoinephan
 */
public class postFix
{
    /**
     * Take a string containing an Infix or Postfix
     * expression and parses it into a set of tokens
     * (numbers & operators). The tokens are returned
     * in a queue.

     * The implementation for parse is given on the
     * instruction of the assignment by prof. Ferrie.
     *
     * @param arg input String
     * @return a queue contains tokens
     */
    public Queue parse(String arg) //tokenizer, parse the input String
    {
        Queue tokenList = new Queue();

        //The below part was given by TA Katrina during the tutorial
        arg = arg.replaceAll("\\s+", "");

        /*
        PARSING A NEGATIVE NUMBER: temporary make it "_" then later handle it in In2Post method
        - if a negative sign at the first position in line
        - if a negative sign right after another operand or an open parenthesis
        (a - sign after a close parenthesis would not make sense)
         */
        if(arg.charAt(0) == '-')
            arg = "_" + arg.substring(1);

        for(int i = 1; i < arg.length() - 1; i++) //search the string
        {
            if(arg.charAt(i) == '-')
            {
                String tempArg = Character.toString(arg.charAt(i-1));
                if(isOperand(tempArg) || tempArg.equals("(")) //previous character is either another operand, or an open parenthesis
                {
                    arg = arg.substring(0,i) + "_" + arg.substring(i+1);
                }
            }

        }

        StringTokenizer st = new StringTokenizer(arg, "+-*x/^()", true);
        while (st.hasMoreElements())
        {
            tokenList.enqueue(st.nextToken());
        }
        return tokenList; //return tokenList as Queue object
    }

    /**
     * <p>
     * Helper method to get precedence value of a String. The method is
     * enhanced by IntelliJ IDEA.
     * </p>
     *
     * @param operator can be an operator, a number, or a parenthesis
     * @return an integer representing the character's precedence value
     */
    private int precedence(String operator)
    {
        return switch (operator)
                {
                    case "sin", "cos", "tan", "cot", "sec", "csc" -> 5;
                    case "^" -> 4;
                    case "*", "x", "/" -> 3;
                    case "+", "-" -> 2;
                    case "(", ")" -> -1;
                    default -> 0;
                };
    }

    /**
     * <p>
     * Takes a queue containing a tokenized Infix expression
     * and returns a queue containing the corresponding
     * Postfix expression.
     * </p>
     * <p>
     * The following implementation was influenced by Creel on YouTube:
     * <b>
     * <a href="https://youtu.be/QzVVjboyb0s">Shunting Yard Algorithm - Intro and Reverse Polish Notation</a>
     * </b>
     * and the given pseudocode.
     * </p>
     *
     * @param Qin the input Queue with Infix expression || Qin is this.parse(String arg)
     * @return a Queue that contains tokens in Postfix expression
     */
    public Queue In2Post(Queue Qin)
    {
        Queue outputQueue = new Queue();
        Stack operatorStack = new Stack();

        while (Qin.size != 0)
        {
            String temp = Qin.dequeue(); //Read a token

            if (precedence(temp) == 0)      //if token is a number (either positive & negative)
            {
                if(temp.charAt(0)=='_') //a negative number
                    temp = "-" + temp.substring(1);

                outputQueue.enqueue(temp);  //add it to the outputQueue
            }


            if (precedence(temp) > 0)       //if temp is operator
            {
                //check if on top of the operatorStack there is a higher precedence operator
                while (operatorStack.size != 0 && precedence(operatorStack.top.data) >= precedence(temp))
                {
                    //pop all higher precedence operator(s) to outputQueue
                    outputQueue.enqueue(operatorStack.pop());
                }
                //[then] push the operator to the operatorStack
                operatorStack.push(temp);
            }

            //cases with parenthesis
            if (temp.equals("(")) //opening parenthesis: push parenthesis to operatorStack
                operatorStack.push(temp);

            if (temp.equals(")"))
            {
                while (!operatorStack.top.data.equals("("))
                {
                    outputQueue.enqueue(operatorStack.pop());
                }
                operatorStack.pop(); //discard the open parenthesis
            }
        }

        //If there remains operator(s) in the operatorStack, "pop" them to the queue
        while (operatorStack.size != 0)
        {
            outputQueue.enqueue(operatorStack.pop());
        }

        return outputQueue;
    }

    /**
     * <p>
     * Check if the current argument is an operand.
     * </p>
     *
     * @param arg the current argument type String
     * @return true if the argument is an operand
     */
    private boolean isOperand(String arg)
    {
        return precedence(arg) > 0;
    }

    /**
     * <p>
     * The method template and pseudocode is given by prof. Ferrie
     * in the assignment 4 instruction.
     * </p>
     * <p>
     * NOTE: I implemented this method before
     * Katrina introduced them during the tutorial,
     * so please do not flag me for plagiarism here.
     * </p>
     *
     * @param PostFix the Queue that holds the postFix expression
     * @return the result of the postFix expression
     */
    public Double PostEval(Queue PostFix)
    {
        if (PostFix.size == 0) //if the PostFix queue is empty
            return null;

        Stack eval = new Stack();
        while (PostFix.size > 0) //while the PostFix queue is not empty
        {
            String token = PostFix.dequeue();
            if (isOperand(token))
            {
                if (precedence(token) == 5) // trigonometry
                {
                    double ans = Double.parseDouble(eval.pop());
                    ans = switch (token)
                            {
                                case "sin" -> Math.sin(Math.toRadians(ans));
                                case "cos" -> Math.cos(Math.toRadians(ans));
                                case "tan" -> Math.tan(Math.toRadians(ans));
                                case "cot" -> 1 / Math.tan(Math.toRadians(ans));
                                case "sec" -> 1 / Math.cos(Math.toRadians(ans));
                                case "csc" -> 1 / Math.sin(Math.toRadians(ans));
                                default -> Double.parseDouble(eval.pop());
                            };
                    eval.push(String.valueOf(ans));
                }
                else
                {
                    double b = Double.parseDouble(eval.pop());
                    double a = Double.parseDouble(eval.pop());
                    switch (token)
                    {
                        case "+" -> {
                            double ans = a + b;
                            eval.push(String.valueOf(ans));
                        }
                        case "-" -> {
                            double ans = a - b;
                            eval.push(String.valueOf(ans));
                        }
                        case "*", "x" -> {
                            double ans = a * b;
                            eval.push(String.valueOf(ans));
                        }
                        case "/" -> {
                            double ans = a / b;
                            eval.push(String.valueOf(ans));
                        }
                        case "^" -> {
                            double ans = Math.pow(a, b);
                            eval.push(String.valueOf(ans));
                        }
                    }
                }
            }
            else
            {
                eval.push(token);
            }
        }

        return Double.parseDouble(eval.pop()); //return the double value of the postfix expression
    }
}