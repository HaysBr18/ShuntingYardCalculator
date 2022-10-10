//Bryant R. Hays
//10/09/2022
//CS 480 Lab 2.

//Evaluation/Shunting Yard algorithm is a modified version of https://github.com/KylerMosich/Calculator/blob/master/src/Evaluator.java
package com.example.calculatorapp;

import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Stack;

public class Evaluator {
    /**
     * Evaluate infix expression and return the result.
     *
     * @param expression the infix expression to be evaluated.
     * @return a double containing the final result.
     */
    public static double calculate(char[] expression) {
        Queue<String> output = shunt(expression);
        return evalPfe(output);
    }

    // Operator having higher precedence value will be returned.
    static int getPrecedence(String ch) {

        if (ch.equals("+") || ch.equals("-")) {
            return 1;
        } else if (ch.equals("*") || ch.equals("/")) {
            return 2;
        } else if (ch.equals("^") || ch.equals("sin") || ch.equals("cos") || ch.equals("tan") || ch.equals("cot")
                || ch.equals("log") || ch.equals("ln")) {
            return 3;
        } else {
            return -1;
        }
    }


    /**
     * Evaluate postfix expression and return the result.
     *
     * @param output the postfix expression to be evaluated.
     * @return a double containing the final result.
     */
    private static double evalPfe(Queue<String> output) {
        Stack<Double> numbers = new Stack<>();
        final double MAX = Math.pow(2, 53); // Highest integer that can be stored with full precision in a double.

        // Handle each element in the output until it is empty.
        while (!output.isEmpty()) {
            String curr = output.remove();

            // If the element is a valid double within the legal range, push to stack.
            // Otherwise, check which operator it is.
            try {
                numbers.push(Double.parseDouble(curr));

            } catch (NumberFormatException e) {


                double a;
                double b;

                switch (curr) {
                    case "+":

                        b = numbers.pop();
                        a = numbers.pop();
                        numbers.push(a + b);
                        break;

                    case "-":

                        b = numbers.pop();
                        a = numbers.pop();
                        numbers.push(a - b);
                        break;

                    case "*":

                        numbers.push(numbers.pop() * numbers.pop());
                        break;

                    case "/":
                        b = numbers.pop();
                        a = numbers.pop();

                        if (b == 0)
                            throw new
                                    UnsupportedOperationException(
                                    "Error: Division by 0. Press [C]");
                        numbers.push(a / b);
                        break;

                    case "^":
                        b = numbers.pop();
                        a = numbers.pop();

                        numbers.push(Math.pow(a, b));
                        break;

                    case "sin":
                        numbers.push(Math.sin(numbers.pop()));
                        break;

                    case "cos":
                        numbers.push(Math.cos(numbers.pop()));
                        break;

                    case "tan":
                        numbers.push(Math.tan(numbers.pop()));
                        break;

                    case "cot":
                        numbers.push(1.0/Math.tan(numbers.pop()));
                        break;

                    case "log":
                        numbers.push(Math.log10(numbers.pop()));
                        break;

                    case "ln":
                        numbers.push(Math.log(numbers.pop()));
                        break;


                }
            }


        }

        // Throw exception if there are too many numbers in stack.
        if (numbers.size() > 1)
            throw new IllegalArgumentException("Invalid Input - Operand without operator.");

        return numbers.pop();

    }


    /**
     * Convert infix expression to postfix expression.
     *
     * @param expression the infix expression to be converted.
     * @return a Queue of Strings representing each token of the postfix expression.
     */
    private static Queue<String> shunt(char[] expression) {

        // Parse expression with (modified) Shunting-Yard algorithm.
        Stack<String> operators = new Stack<>();
        Queue<String> output = new LinkedList<>();

        //Set of valid operators.
        HashSet<Character> ops = new HashSet<Character>();
        ops.add('+');
        ops.add('-');
        ops.add('*');
        ops.add('/');
        ops.add('^');


        //Checks the expression for curly braces and replaces each occurrence with a parenthesis.
        for(int i = 0; i < expression.length; i++){
            if(expression[i] == '{'){
                expression[i] = '(';
            }

            if (expression[i] == '}'){
                expression[i] = ')';
            }
        }

        for (int i = 0; i < expression.length; i++) {




            // Throw exception if ) is found before (.
            if (expression[i] == ')') {
                throw new IllegalArgumentException("Invalid Input - ')' without matching '(' press [C]");
            }


            // While char is '0'-'9' or '.', add to token, then push all to output.
            if (Character.isDigit(expression[i]) || expression[i] == '.') {
                String token = "";
                int decCount = 0;

                while (i < expression.length && (Character.isDigit(expression[i]) || expression[i] == '.')) {
                    if (expression[i] == '.') decCount++;
                    token += expression[i];
                    i++;
                }

                // Throw error if last digit is decimal point.
                if (expression[i - 1] == '.') {
                    throw new IllegalArgumentException("Invalid Input - Number ends with '.' press [C]");
                }
                // Throw error if token has multiple decimal points.
                if (decCount > 1) {
                    throw new IllegalArgumentException("Invalid Input - Number has more than one '.' press [C]");
                }

                output.add(token);
                i--;
                continue;
            }


            if (expression[i] == '(') {
                // Check for implicit multiplication before parentheses.
                if (i != 0 && Character.isDigit(expression[i - 1])) {
                    operators.add("*");
                }

                // Evaluate everything within the parenthesis and push result to output.
                i++;
                String token = "";
                int parenCount = 1;

                while (true) {
                    try {
                        if (expression[i] == '(') parenCount++;
                        else if (expression[i] == ')') parenCount--;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Throw exception if whole expression is parsed without finding ).
                        throw new IllegalArgumentException("Invalid Input - '(' without matching ')' press [C]");
                    }

                    // Stop iterating when all ( are matched with ).
                    if (parenCount == 0) break;

                    token += expression[i];
                    i++;
                }

                // Add solution to sub-expression in place of the parentheses.
                output.add(calculate(token.toCharArray()) + "");

                // Check for implicit multiplication after parentheses.
                if (i + 1 < expression.length && (Character.isDigit(expression[i + 1]) || expression[i + 1] == '.')) {
                    operators.add("*");
                }
                continue;
            }

            // Handle unary negation.
            if (expression[i] == '-') {
                // Check for unary negation, then treat it as multiplying by -1.
                if (i == 0 || expression[i - 1] == '(' || expression[i - 1] == '+' || expression[i - 1] == '-'
                        || expression[i - 1] == '*' || expression[i - 1] == '/' || expression[i - 1] == '^') {
                    operators.push("*");
                    output.add("-1");
                }else { // Handle subtraction like normal.
                    if (!operators.isEmpty()) {
                        String o = operators.peek();
                        while (getPrecedence(o) >= getPrecedence(String.valueOf(expression[i]))) {
                            output.add(operators.pop());
                            if (operators.isEmpty()) break;
                            o = operators.peek();
                        }
                    }
                    operators.push("-");
                }
                continue;
            }



                //Handle all other arithmetic operations.
                if (ops.contains(expression[i])) {
                    //If operators stack is not empty, check the precedence of the peek operator from the stack
                    //..compared to the current operator.
                    if (!operators.isEmpty()) {
                        String o = operators.peek();
                        //If the peek element has greater precedence of current element.
                        while (getPrecedence(o) >= getPrecedence(String.valueOf(expression[i]))) {
                            //Pop the peek operator from the stack and add it to the output.
                            output.add(operators.pop());
                            if (operators.isEmpty()) break;
                            o = operators.peek();

                        }
                    }
                    //Push the current operator to the top of the stack.
                    operators.push(String.valueOf(expression[i]));
                    continue;
                }





                // Handle trigonometric and other math functions.
                if (Character.isLetter(expression[i])) {
                    String function = "";

                    function = function.copyValueOf(expression, i, 3);

                    switch (function.toLowerCase()) {
                        case "sin":
                            operators.push("sin");
                            break;
                        case "cos":
                            operators.push("cos");
                            break;
                        case "tan":
                            operators.push("tan");
                            break;
                        case "cot":
                            operators.push("cot");
                            break;
                        case "log":
                            operators.push("log");
                            break;
                        case "ln(":
                            operators.push("ln");
                            i--;
                            break;
                        default:
                            throw new IllegalArgumentException("Incorrect function name at end of expression. press [C]");
                    }

                    //Increment following the function name.
                    i += 2;


                    if (Character.isLetter(expression[i + 1])) {

                        throw new IllegalArgumentException("Incorrect function name at end of expression. press [C]");

                    }

                    String token = "";

                }

            }

            // Move all remaining operators to output, then return.
            while (!operators.isEmpty()) output.add(operators.pop());

                return output;
            }
        }

