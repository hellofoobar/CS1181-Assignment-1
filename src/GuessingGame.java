/**
 Created by Jun Yuan on 2016-01-07 in IntelliJ.
 This program asks the user for a secret number and attempts to guess the number
 by getting the user to perform operations on the secret number and giving the program its result.
 The user can choose between one of four difficulty and play multiple rounds.
 Please read the accompanying README to dive in the details and find the easter egg.
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
            //min max initialized to be used as random generator's range
            int min = 2;
            int max = 10;
            //label to display info to user
            String opLabel = "";
            String difficultyLabel = "";
            //higher level difficulty will need to perform operation more than once
            int numOfOperations = 1;

            //prompt user to select one of four difficulty and set min max accordingly
            int difficulty = JOptionPane.showOptionDialog(null, "Choose a difficulty: ", "I am CEO", 0, JOptionPane.QUESTION_MESSAGE, null, Difficulty.values(), 0);
            switch (difficulty) {
                //most difficult, 4 digit calculations for 4 times
                case 0:
                    difficultyLabel += "You are hot-shot CTO!";
                    max = 9999;
                    min = 1000;
                    numOfOperations = 4;
                    break;
                case 1:
                    difficultyLabel += "You are full-stack DEVOPS!";
                    max = 99;
                    min = 10;
                    numOfOperations = 3;
                    break;
                case 2:
                    difficultyLabel += "You are bug-ridden Junior!";
                    max = 99;
                    min = 10;
                    numOfOperations = 2;
                    break;
                // easiest and cheesiest (guarantee to keep secret number's secrecy)
                case 3:
                    difficultyLabel += "You are nothing but a n00b!";
                    break;
            }
            //randNum stores a number from rng, range depends on user's chosen difficulty
            int randNum = generateRandomInt(min, max);
            JOptionPane.showMessageDialog(null, "Think of a number.", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);

            int op;
            //prompt user to select an operation to perform
            //min is used to determine what difficulty the user chose
            //operations displayed will depend on difficulty, eg. CTO can only chose to do multiplication or division
            if (min == 2) {
                //for teh n00b, only %
                op = JOptionPane.showOptionDialog(null, "Perform an operation", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationNoob.values(), 0);
                switch (op) {
                    case 0:
                        opLabel = "Mod";
                        break;
                }
            } else if (min == 1000) {
                //for the CTO, * and /
                op = JOptionPane.showOptionDialog(null, "Perform an operation", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationHard.values(), 0);
                switch (op) {
                    case 0:
                        opLabel = "Divide";
                        break;
                    case 1:
                        opLabel = "Multiply";
                        break;
                }
            } else {
                //for Junior and DEVOPS, + and -
                op = JOptionPane.showOptionDialog(null, "Perform an operation", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationEasy.values(), 0);
                switch (op) {
                    case 0:
                        opLabel = "Subtract";
                        break;
                    case 1:
                        opLabel = "Add";
                        break;
                }

            }

            int




            int ansVerified;
            if (min > 3) {
                //present user with a randomly generated number to do operation on and prompt for the answer as input (stored as string to be parsed later)
                String ansString = JOptionPane.showInputDialog(null, opLabel + " your secret number by " + randNum + ". Submit your answer: ", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);
                //call function to parse for valid input for non modulo case
                ansVerified = inputValidationForAns(ansString);
            } else {
                //special check for % modulo's case
                do {
                    String ansString = JOptionPane.showInputDialog(null, opLabel + " your secret number by " + randNum + ". Submit your answer: ", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);
                    ansVerified = inputValidationForAns(ansString);
                    //System.out.println("no wonder you are a n00b");
                } while (!(randNum > ansVerified));
                //a % b = c, c cannot be equal or greater than b
            }
            //call function to guess secret number
            getSecretNum(ansVerified, randNum, opLabel);
            //prompt user to play again
            int play = JOptionPane.showConfirmDialog(null, "Play again?",null, JOptionPane.YES_NO_OPTION);
            if (play == 1) {
                //if user press no program terminates
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
    //just for teh n00bs
    public enum OperationNoob {
        Modulos
    }

    static int inputValidationForAns(String input) {
        //use regex to parse for valid input
        Pattern numOnly = Pattern.compile("[+-]?\\d+");
        Matcher m = numOnly.matcher(input);
        boolean match = m.matches();
        //prompt user for input until valid
        if (!match) {
            do {
                input = JOptionPane.showInputDialog("Enter a numeric value: ");
                numOnly = Pattern.compile("[+-]?\\d+");
                m = numOnly.matcher(input);
                match = m.matches();
            } while (!match);
        }
        //cast valid input to int
        return Integer.parseInt(input);
    }

    public static int generateRandomInt(int min, int max) {
        //instantiate rng
        Random rng = new Random();
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



