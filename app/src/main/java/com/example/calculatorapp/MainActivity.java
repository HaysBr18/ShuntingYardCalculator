//Bryant R. Hays
//10/09/2022
//CS 480 Lab 2.
//For help building this interface, I followed along with this tutorial: https://www.youtube.com/watch?v=B5b-7uDtUp4&ab_channel=PracticalCoding
// and added my own features to it.
package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Grabs the view from the xml and assigns it to display.
        display = findViewById(R.id.input);

        //prevents the keyboard from popping up in teh display.
        display.setShowSoftInputOnFocus(false);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getString(R.string.display).equals(display.getText().toString())){
                    display.setText("");
                }
            }
        });
    }

    //Updates the string that's on the display
    private void updateInput (String stringToAdd){
        //Grabs the string that's already on the display
        String currString = display.getText().toString();

        //Grabs the cursor position
        int cursorPosition = display.getSelectionStart();

        //Grabs the left substring up to the cursor
        String leftSubStr = currString.substring(0, cursorPosition);

        //Grabs the right substring from the cursor position up to the end of the string
        String rightSubStr = currString.substring(cursorPosition);

        //If the string equals the default text, set the display to the updated text.
        if (getString(R.string.display).equals(display.getText().toString())){
            display.setText(stringToAdd);

            //Keeps the cursor at the end of the string instead of the front.
            display.setSelection(cursorPosition + 1);
        } else {
            //Else, update the existing text.

            //Updates the display
            display.setText(String.format("%s%s%s", leftSubStr, stringToAdd, rightSubStr));

            //Keeps the cursor at the end of the string instead of the front.
            display.setSelection(cursorPosition + 1);
        }

    }

    //Numbers
    public void nineBTN(View view){

        updateInput("9");
    }

    public void eightBTN(View view){

        updateInput("8");
    }
    public void sevenBTN(View view){
        updateInput("7");

    }public void sixBTN(View view){
        updateInput("6");

    }public void fiveBTN(View view){
        updateInput("5");

    }public void fourBTN(View view){
        updateInput("4");

    }public void threeBTN(View view){
        updateInput("3");

    }public void twoBTN(View view){
        updateInput("2");

    }public void oneBTN(View view){
        updateInput("1");

    }public void zeroBTN(View view){
        updateInput("0");

    }

    //Functions
    public void exponentBTN(View view){

        if (display.getText().length() != 0 && display.getSelectionStart() !=0) {
            updateInput("^");
        }
    }
    public void sineBTN(View view){

        int cursorPosition = display.getSelectionStart();

        updateInput("sin");

        display.setSelection(cursorPosition+3);

    }
    public void cosineBTN(View view){
        int cursorPosition = display.getSelectionStart();

        updateInput("cos");

        display.setSelection(cursorPosition+3);

    }
    public void tangentBTN(View view){
        int cursorPosition = display.getSelectionStart();

        updateInput("tan");

        display.setSelection(cursorPosition+3);


    }

    public void cotangentBTN(View view){
        int cursorPosition = display.getSelectionStart();

        updateInput("cot");

        display.setSelection(cursorPosition+3);


    }

    public void logBTN(View view){
        int cursorPosition = display.getSelectionStart();

        updateInput("log");

        display.setSelection(cursorPosition+3);


    }
    public void naturalLogBTN(View view){
        int cursorPosition = display.getSelectionStart();

        updateInput("ln");

        display.setSelection(cursorPosition+2);


    }

    public void clearBTN(View view){
        display.setText("");

    }
    public void plusMinusBTN(View view){

        updateInput("-");

    }
    public void percentBTN(View view){


    }
    public void divisionBTN(View view){
        if(display.getText().length() != 0 && display.getSelectionStart() !=0){
            updateInput("/");
        }

    }
    public void multiplicationBTN(View view){

        if(display.getText().length() != 0 && display.getSelectionStart() !=0) {
            updateInput("*");
        }

    }
    public void subtractionBTN(View view){
        if(display.getText().length() != 0 && display.getSelectionStart() !=0) {
            updateInput("-");
        }
    }
    public void additionBTN(View view){
        if(display.getText().length() != 0 && display.getSelectionStart() !=0) {
            updateInput("+");
        }
    }

    public void equalsBTN(View view){
        Evaluator exp = new Evaluator();

        char[] expression1 = display.getText().toString().toCharArray();
        try {
            double result = exp.calculate(expression1);
            String StrResult = String.valueOf(result);

            display.setText(StrResult);
            display.setSelection(StrResult.length());

        }catch(Exception e){
            display.setText(e.getMessage());
        }


    }
    public void openParenthesesBTN(View view){

        updateInput("(");
        /*
        //Grabs the cursor position
        int cursorPosition = display.getSelectionStart();
        int strLength = display.getText().toString().length();

        int numOpenPars = 0;
        int numClosedPars = 0;

        for (int i = 0; i < cursorPosition; i++){

            if(display.getText().toString().substring(i, i+1).equals("(")) {
                numOpenPars++;
            }
            else if(display.getText().toString().substring(i,i+1).equals(")")) {
                numClosedPars++;
            }
        }

        if (numClosedPars == numOpenPars || display.getText().toString().substring(strLength-1, strLength).equals("(")){
            updateInput("(");
        }

        else if (numClosedPars < numOpenPars && !display.getText().toString().substring(strLength-1, strLength).equals("(")){
            updateInput(")");
        }

        display.setSelection(cursorPosition + 1);
         */

    }

    public void closedParenthesesBTN(View view){
        updateInput(")");
    }
    public void pointBTN(View view){
        updateInput(".");
    }

    public void backSpaceBTN(View view){

        //Grabs the string to edit
        String textToEdit = display.getText().toString();

        //Length of the string on display.
        int length = textToEdit.length();

        //Grabs the cursor position
        int cursorPosition = display.getSelectionStart();


        if (length != 0 && cursorPosition !=0){
            SpannableStringBuilder position = (SpannableStringBuilder) display.getText();
            position.replace(cursorPosition-1, cursorPosition, "");
            display.setText(position);
            display.setSelection(cursorPosition-1);
        }



    }

    public void curlyBracesBTN(View view){
        //Grabs the cursor position
        int cursorPosition = display.getSelectionStart();
        int strLength = display.getText().toString().length();

        int numOpenPars = 0;
        int numClosedPars = 0;

        for (int i = 0; i < cursorPosition; i++){

            if(display.getText().toString().substring(i, i+1).equals("{")) {
                numOpenPars++;
            }
            else if(display.getText().toString().substring(i,i+1).equals("}")) {
                numClosedPars++;
            }
        }

        if (numClosedPars == numOpenPars || display.getText().toString().substring(strLength-1, strLength).equals("{")){
            updateInput("{");
        }

        else if (numClosedPars < numOpenPars && !display.getText().toString().substring(strLength-1, strLength).equals("}")){
            updateInput("}");
        }

        display.setSelection(cursorPosition + 1);


    }


}