/**
 Created by Jun Yuan on 2016-01-07.
 This program asks the user for a secret number and attempts to guess the number
 by getting the user to perform operations on the secret number and giving the program its result.
 */

import javax.swing.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GuessingGame {
    //Global variable to keep track of game state
    public static Boolean keepPlaying = true;

    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null, "Lets play a little mind game.", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        //do while loop to keep the game going if keepPlaying is true
        do {
            int min = 2;
            int max = 10;
            String opLabel = "";
            String difficultyLabel = "";

            int difficulty = JOptionPane.showOptionDialog(null, "Choose a difficulty: ", "I am CEO", 0, JOptionPane.QUESTION_MESSAGE, null, Difficulty.values(), 0);
            switch (difficulty) {
                case 0:
                    difficultyLabel += "You are hot-shot CTO!";
                    max = 9999;
                    min = 1000;
                    System.out.println("Mod");
                    break;
                case 1:
                    opLabel += "You are full-stack DEVOPS!";
                    max = 99;
                    min = 10;
                    System.out.println("DEVOPS");
                    break;
                case 2:
                    difficultyLabel += "You are bug-ridden Junior!";
                    max = 99;
                    min = 10;
                    System.out.println("JUNIOR");
                    break;
                case 3:
                    difficultyLabel += "You are nothing but a n00b!";
                    System.out.println("N00B");
                    break;
            }
            int randNum = generateRandomInt(min, max);
            JOptionPane.showMessageDialog(null, "Think of a number.", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);
            int op;
            if (min == 2) {
                op = JOptionPane.showOptionDialog(null, "Perform operation", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationNoob.values(), 0);
                switch (op) {
                    case 0:
                        opLabel = "Mod";
                        System.out.println("Mod");
                        break;
                }
            } else if (min == 1000) {
                op = JOptionPane.showOptionDialog(null, "Perform operation", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationHard.values(), 0);
                switch (op) {
                    case 0:
                        opLabel = "Divide";
                        System.out.println("Divide");
                        break;
                    case 1:
                        opLabel = "Multiply";
                        System.out.println("Multiply");
                        break;
                }
            } else {
                op = JOptionPane.showOptionDialog(null, "Perform operation", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationEasy.values(), 0);
                switch (op) {
                    case 0:
                        opLabel = "Subtract";
                        System.out.println("Subtract");
                        break;
                    case 1:
                        opLabel = "Add";
                        System.out.println("Add");
                        break;
                }

            }

            System.out.println(min);
            int ansVerified;
            if (min > 3) {
                String ansString = JOptionPane.showInputDialog(null, opLabel + " your secret number by " + randNum + ". Submit your answer: ", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);
                ansVerified = inputValidationForAns(ansString);
            } else {
                //n00b check
                do {
                    String ansString = JOptionPane.showInputDialog(null, opLabel + " your secret number by " + randNum + ". Submit your answer: ", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);
                    ansVerified = inputValidationForAns(ansString);
                    System.out.println("yo");
                } while (!(randNum > ansVerified));
                //a % b = c, c is strictly smaller than b
            }

            getSecretNum(ansVerified, randNum, opLabel);
            int play = JOptionPane.showConfirmDialog(null, "Play again?",null, JOptionPane.YES_NO_OPTION);
            if (play == 1) {
                keepPlaying = false;
            }
        } while (keepPlaying);
    }
    public enum Difficulty {
        CTO,
        DEVOPS,
        JUNIOR,
        N00B
    }

    public enum OperationHard {
        Divide,
        Multiply
    }

    public enum OperationEasy {
        Subtract,
        Add
    }

    public enum OperationNoob {
        Modulos
    }

    static int inputValidationForAns(String input) {
        //regex
        Pattern numOnly = Pattern.compile("[+-]?\\d+");
        Matcher m = numOnly.matcher(input);
        boolean match = m.matches();

        if (!match) {
            do {
                input = JOptionPane.showInputDialog("Enter a numeric value: ");
                numOnly = Pattern.compile("[+-]?\\d+");
                m = numOnly.matcher(input);
                match = m.matches();
            } while (!match);
        }


        return Integer.parseInt(input);
    }


    public static int generateRandomInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rng = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rng.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void getSecretNum(int ansVerified, int randomNum, String opLabel) {
        int answer = 0;

        switch (opLabel) {
            case "Mod":
                JOptionPane.showMessageDialog(null, "Cannot inverse mod to get your secret number.\nFriggin n00b, You win! ", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            case "Divide":
                answer = ansVerified * randomNum;
                break;
            case "Multiply":
                answer = ansVerified / randomNum;
                break;
            case "Subtract":
                answer = ansVerified + randomNum;
                break;
            case "Add":
                answer = ansVerified - randomNum;
                break;
        }

        JOptionPane.showMessageDialog(null, "Your secret number is " + String.valueOf(answer), "", JOptionPane.INFORMATION_MESSAGE);
    }

}



