package com.company;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * CardTable is designed to display cards in player hands and common play area.
 */
public class CardTable extends JFrame {
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2; // for now, we only allow 2 person game

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlTurnActions;

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

   public JPanel getPnlTurnActions() {
      return pnlTurnActions;
   }

   /**
    * Helper method to setup UI panels.
    */
   private void setUpPanels() {
      pnlComputerHand = new JPanel(new GridLayout(1, numCardsPerHand));
      pnlHumanHand = new JPanel(new GridLayout(2, numCardsPerHand));
      pnlPlayArea = new JPanel(new GridLayout(1, numPlayers));
      pnlTurnActions = new JPanel(new FlowLayout());

      JPanel nestedPanel = new JPanel();
      nestedPanel.setLayout(new BoxLayout(nestedPanel, BoxLayout.PAGE_AXIS));

      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlHumanHand.setBorder(new TitledBorder("Human Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));

      nestedPanel.add(pnlHumanHand);
      nestedPanel.add(pnlTurnActions);

      add(pnlComputerHand, BorderLayout.NORTH);
      add(nestedPanel, BorderLayout.SOUTH);
      add(pnlPlayArea, BorderLayout.CENTER);
   }
}