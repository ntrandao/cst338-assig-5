package com.company;
import javax.swing.*;
import java.awt.*;

public class Assig5PhaseOne {

    static final int NUM_CARD_IMAGES = 57; // 52 + 4 jokers + 1 back-of-card image
    static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];

    static void loadCardIcons() {
        int index = 0;
        for(int i = 0; i <= 3; i++) {
            for(int j = 0; j <= 13; j++) {
                String fileName = "images/" + turnIntIntoCardValue(j) + turnIntIntoCardSuit(i) + ".gif";
                // DEBUGGING- DELETE AFTER TESTING
                System.out.println("File Name: " + fileName);
                icon[index] = new ImageIcon(fileName);
                System.out.println(icon[index] );
                index ++;
            }
        }
        // add back of card image
        icon[index] = new ImageIcon("BK.gif");
    }

    // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
    static String turnIntIntoCardValue(int k) {
        switch (k) {
            case 0:
                return "A";
            case 1:
                return "2";
            case 2:
                return "3";
            case 3:
                return "4";
            case 4:
                return "5";
            case 5:
                return "6";
            case 6:
                return "7";
            case 7:
                return "8";
            case 8:
                return "9";
            case 9:
                return "T";
            case 10:
                return "J";
            case 11:
                return "Q";
            case 12:
                return "K";
            default:
                return "X";
        }
    }

    // turns 0 - 3 into "C", "D", "H", "S"
    static String turnIntIntoCardSuit(int j) {
        switch (j) {
            case 0:
                return "C";
            case 1:
                return "D";
            case 2:
                return "H";
            default:
                return "S";
        }
    }

    // a simple main to throw all the JLabels out there for the world to see
    public static void main(String[] args) {
        int k;
        
        // prepare the image icon array
        loadCardIcons();

        // establish main frame in which program will run
        JFrame frmMyWindow = new JFrame("Card Room");
        frmMyWindow.setSize(1150, 650);
        frmMyWindow.setLocationRelativeTo(null);
        frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up layout which will control placement of buttons, etc.
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
        frmMyWindow.setLayout(layout);

        // prepare the image label array
        JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
        for (k = 0; k < NUM_CARD_IMAGES; k++)
            labels[k] = new JLabel(icon[k]);

        // place your 3 controls into frame
        for (k = 0; k < NUM_CARD_IMAGES; k++)
            frmMyWindow.add(labels[k]);


        // show everything to the user
        frmMyWindow.setVisible(true);
    }
}
