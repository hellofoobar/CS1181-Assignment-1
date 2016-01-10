/**
 Created by Jun Yuan on 2016-01-07 in IntelliJ.
 This program asks the user for a secret number and attempts to guess the number
 by getting the user to perform calculation/s on the secret number and getting the resulting answer from user.
 The user can choose between one of four difficulty levels and play multiple rounds.
 Difficulty level will determine what operations are selectable.

 IMPORTANT: This program assumes the user does his/her calculation/s correctly and only checks for
 answer's format using regular expressions. Answer's correctness to calculations will not be checked
 since user's secret number is never disclosed in accordance with assignment's specs.

 Choose the CTO difficulty and do division if feeling brave.
 Choose the n00b difficulty to find the easter egg :)
 */

import javax.swing.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GuessingGame {

    //boolean var to keep track of game state
    static Boolean keepPlaying = true;
    //min max initialized to be used as random generator's range
    static int min = 2;
    static int max = 10;
    static int randNum;
    //AL to store of randNums
    static ArrayList<Integer> randNumArrayList = new ArrayList<>();
    //text labels to display info to user
    static String opLabel = "";
    static String difficultyLabel = "";
    //number of operations to be performed
    static int numOfOperations = 1;

    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null, "Lets play a little mind game.\nThink of a secret number.", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        //do while loop to keep the game going if keepPlaying is true
        do {
            //prompt user to select one of four difficulty and call function to configure  min, max, and numOfOperations accordingly
            int difficulty = JOptionPane.showOptionDialog(null, "Choose a difficulty: ", "I am CEO", 0, JOptionPane.QUESTION_MESSAGE, null, Difficulty.values(), 0);
            setDifficulty(difficulty);

            //function call to prompt user for an operation
            setOperation();

            //game logic
            //user's answer input must be double due to division
            Double ansVerified = -1.0;
            for (int i = 1; i <= numOfOperations; i++) {
                //randNum stores a number from rng, range depends on user's chosen difficulty
                randNum = generateRandomInt(min, max);
                randNumArrayList.add(randNum);

                if (numOfOperations == 1) {
                    //for n00b's % modulo case, do while loop to parse for valid input
                    do {
                        String ansString = JOptionPane.showInputDialog(null, opLabel + " your secret number by " + randNum + " Submit your answer: ", difficultyLabel, JOptionPane.INFORMATION_MESSAGE);
                        if (ansString == null) {
                            //handle Cancel gracefully
                            return;
                        }
                        //call function to parse for valid input
                        ansVerified = inputValidationForAns(ansString);
                    } while (!(randNum > ansVerified));
                    //a % b = c, c cannot be equal or greater than b
                    //System.out.println("no wonder you are a n00b");

                //for all other operations
                } else {
                    if (i == 1) {
                        //first calculation
                        JOptionPane.showMessageDialog(null, opLabel + " your secret number by " + randNum, "Calculation #" + i, JOptionPane.INFORMATION_MESSAGE);
                    } else if (!(i == numOfOperations)) {
                        //subsequent calculations before last
                        JOptionPane.showMessageDialog(null, opLabel + " your previous answer by " + randNum, "Calculation #" + i, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        //last calculation, ask user for final answer and store it as a String to parse later
                        String ansString = JOptionPane.showInputDialog(null, opLabel + " your previous answer by " + randNum + " Submit your answer: ", "Calculation #" + i, JOptionPane.INFORMATION_MESSAGE);
                        if (ansString == null) {
                            //handle Cancel gracefully
                            return;
                        }
                        //call function to parse for valid input
                        ansVerified = inputValidationForAns(ansString);
                    }
                }
            }

            //call function to guess secret number
            getSecretNum(ansVerified, randNumArrayList, opLabel);

            //prompt user to play again
            play();

            //clear out arrayList
            randNumArrayList.clear();

        } while (keepPlaying);
        return;
    }

    enum Difficulty {
        CTO,
        DEVOPS,
        JUNIOR,
        N00B
    }

    enum OperationHard {
        Divide,
        Multiply
    }

    enum OperationEasy {
        Subtract,
        Add
    }
    //just for teh n00bs
    enum OperationNoob {
        Modulo
    }

    static void setDifficulty(int difficulty) {
        switch (difficulty) {
            //most difficult, do 3 digit calculations for 2 times
            case 0:
                difficultyLabel += "You are hot-shot CTO!";
                max = 999;
                min = 101;
                numOfOperations = 2;
                break;
            case 1:
                difficultyLabel += "You are full-stack DEVOPS!";
                max = 99;
                min = 11;
                numOfOperations = 2;
                break;
            case 2:
                difficultyLabel += "You are bug-ridden Junior!";
                max = 99;
                min = 10;
                numOfOperations = 2;
                break;
            //easiest and cheesiest (guarantee to keep secret number's secrecy)
            case 3:
                difficultyLabel += "You are nothing but a n00b!";
                break;
        }
    }

    static void setOperation() {
        //prompt user to think of a secret number and select an operation to perform
        //min is used to determine what difficulty the user chose
        //operations displayed will depend on difficulty, eg. CTO can only chose to do multiplication or division
        //op for operation
        int op;
        if (min == 2) {
            //for teh n00b, only %
            op = JOptionPane.showOptionDialog(null,
                    "Select an operation to perform", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationNoob.values(), 0);
            switch (op) {
                case 0:
                    opLabel = "Mod";
                    break;
            }
        } else if (min == 10) {
            //for the Junior, + and -
            op = JOptionPane.showOptionDialog(null,
                    "Select an operation to perform", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationEasy.values(), 0);
            switch (op) {
                case 0:
                    opLabel = "Subtract";
                    break;
                case 1:
                    opLabel = "Add";
                    break;
            }
        } else if (min > 10) {
            //for the CTO and the DEVOPS, * and /
            op = JOptionPane.showOptionDialog(null,
                    "Select an operation to perform", difficultyLabel, 0, JOptionPane.QUESTION_MESSAGE, null, OperationHard.values(), 0);
            switch (op) {
                case 0:
                    opLabel = "Divide";
                    break;
                case 1:
                    opLabel = "Multiply";
                    break;
            }
        }
    }

    static Double inputValidationForAns(String input) {
        //use regex to parse for valid input
        Pattern numOnly = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+$");
        Matcher m = numOnly.matcher(input);
        boolean match = m.matches();
        //prompt user for input until valid
        if (!match) {
            do {
                input = JOptionPane.showInputDialog("Please enter a number: ");
                numOnly = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+$");
                m = numOnly.matcher(input);
                match = m.matches();
            } while (!match);
        }
        //cast valid input to Double
        return Double.parseDouble(input);
    }

    static int generateRandomInt(int min, int max) {
        //instantiate and create randomNum
        Random rng = new Random();
        int randomNum = rng.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    static void getSecretNum(Double ansVerified, ArrayList<Integer> randomNumArrayList, String opLabel) {
        //reverse engineer user's secret number
        Double secretNum = -1.0;

        switch (opLabel) {
            case "Mod":
                JOptionPane.showMessageDialog(null, "Cannot inverse mod to get your secret number.\nFriggin n00b, You win! ", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            case "Divide":
                for (int i = 0; i < randomNumArrayList.size(); i++) {
                    System.out.println(randomNumArrayList.get(i));
                    ansVerified *= randomNumArrayList.get(i);
                }
                secretNum = ansVerified;
                break;
            case "Multiply":
                for (int i = 0; i < randomNumArrayList.size(); i++) {
                    System.out.println(randomNumArrayList.get(i));
                    ansVerified /= randomNumArrayList.get(i);
                }
                secretNum = ansVerified;
                break;
            case "Subtract":
                for (int i = 0; i < randomNumArrayList.size(); i++) {
                    System.out.println(randomNumArrayList.get(i));
                    ansVerified += randomNumArrayList.get(i);
                }
                secretNum = ansVerified;
                break;
            case "Add":
                for (int i = 0; i < randomNumArrayList.size(); i++) {
                    System.out.println(randomNumArrayList.get(i));
                    ansVerified -= randomNumArrayList.get(i);
                }
                secretNum = ansVerified;
                break;
        }
        //round to nearest whole number
        long longSecretNum = Math.round(secretNum);
        System.out.println(secretNum);
        JOptionPane.showMessageDialog(null, "Your secret number is " + String.valueOf(longSecretNum), "", JOptionPane.INFORMATION_MESSAGE);
    }

    static void play() {
        int play = JOptionPane.showConfirmDialog(null, "Play again?",null, JOptionPane.YES_NO_OPTION);
        if (play == 1) {
            //if user press "No" program terminates
            keepPlaying = false;
        }
    }

}