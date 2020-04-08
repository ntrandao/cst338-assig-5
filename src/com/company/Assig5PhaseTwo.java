package com.company;

/**
 * Project Members: Ericka Koyama, Holly Stephens, Ngoc Tran Do
 * CST 338 Software Design Assignment 5 - Low Card Game
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Main Phase 2 Client
 */
public class Assig5PhaseTwo {
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];

   public static void main(String[] args) {
      CardTable myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // CREATE LABELS ----------------------------------------------------
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         humanLabels[i] = new JLabel(GUICard.getIcon(generateRandomCard()));
      }

      // ADD LABELS TO PANELS -----------------------------------------
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
         myCardTable.getPnlComputerHand().add(computerLabels[i]);
         myCardTable.getPnlHumanHand().add(humanLabels[i]);
      }

      // add two random cards in the play region (simulating a computer/hum ply)
      for (int i = 0; i < NUM_PLAYERS; i++) {
         playedCardLabels[i] = new JLabel(GUICard.getIcon(generateRandomCard()));
         myCardTable.getPnlPlayArea().add(playedCardLabels[i]);

      }

      // Add played card text labels
      myCardTable.getPnlPlayArea().add(new JLabel("Computer", JLabel.CENTER));
      myCardTable.getPnlPlayArea().add(new JLabel("You", JLabel.CENTER));

      // show everything to the user
      myCardTable.setVisible(true);
   }

   static Card generateRandomCard() {
      Deck deck = new Deck();
      int randomIndex = (int) (Math.random() * (deck.getNumCards() - 1));
      return deck.inspectCard(randomIndex);
   }
}

/**
 * CardTable is designed to display cards in player hands and common play area.
 */
class CardTable extends JFrame {
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2; // for now, we only allow 2 person game

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   CardTable(String title, int numCardsPerHand, int numPlayers) {
      super(title);

      setLayout(new BorderLayout(10, 10));

      this.numCardsPerHand = numCardsPerHand > 0 && numCardsPerHand <= MAX_CARDS_PER_HAND ? numCardsPerHand :
            MAX_CARDS_PER_HAND;
      this.numPlayers = numPlayers > 0 && numPlayers <= MAX_PLAYERS ? numPlayers : MAX_PLAYERS;

      setUpPanels();
   }

   public JPanel getPnlComputerHand() {
      return pnlComputerHand;
   }

   public JPanel getPnlHumanHand() {
      return pnlHumanHand;
   }

   public JPanel getPnlPlayArea() {
      return pnlPlayArea;
   }

   private void setUpPanels() {
      pnlComputerHand = new JPanel(new GridLayout(1, numCardsPerHand));
      pnlHumanHand = new JPanel(new GridLayout(1, numCardsPerHand));
      pnlPlayArea = new JPanel(new GridLayout(2, numPlayers));

      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlHumanHand.setBorder(new TitledBorder("Human Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));

      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlHumanHand, BorderLayout.SOUTH);
      add(pnlPlayArea, BorderLayout.CENTER);
   }
}

/**
 * Class GUICard is designed to hold and return graphics for cards.
 */
class GUICard {
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack = new ImageIcon("images/BK.gif");
   private static boolean iconsLoaded = false;

   static void loadCardIcons() {
      if (iconsLoaded) return;

      for (int i = 0; i < iconCards.length; i++) {
         for (int j = 0; j < iconCards[i].length; j++) {
            // i and j are value and suit indexes
            String filename = indexAsRank(i) + indexAsSuit(j) + ".gif";
            iconCards[i][j] = new ImageIcon("images/" + filename);
         }
      }

      iconsLoaded = true;
   }

   static char indexAsRank(int index) {
      return Card.valuRanks[index];
   }

   private static String indexAsSuit(int index) {
      return Card.Suit.values()[index].toString().substring(0, 1);
   }

   public static int valueAsInt(Card card) {
      int i;

      for (i = 0; i < Card.valuRanks.length; i++) {
         if (Card.valuRanks[i] == card.getValue()) break;
      }

      return i;
   }

   public static int suitAsInt(Card card) {
      return card.getSuit().ordinal();
   }

   public static Icon getIcon(Card card) {
      loadCardIcons();

      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   public static Icon getBackCardIcon() {
      return iconBack;
   }
}
