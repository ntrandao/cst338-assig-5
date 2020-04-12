package com.company;

import javax.swing.*;

public class View {
   /**
    * UI Labels
    */
   JLabel[] computerLabels;
   JButton[] humanLabels;
   JButton[] playedCardLabels;

   /**
    * Game Managers
    */
   static CardTable cardTable;  // CardTable instance

   public static void displayMessage(String message, String title) {
      JOptionPane.showMessageDialog(cardTable, message, title, JOptionPane.PLAIN_MESSAGE);
   }

   public JLabel[] getComputerLabels() {
      return computerLabels;
   }

   public JLabel getComputerLabelAtIndex(int index) {
      return computerLabels[index];
   }

   public JLabel setComputerLabelAtIndex(int index, JLabel label) {
      return computerLabels[index] = label;
   }

   public JButton getHumanLabelAtIndex(int index) {
      return humanLabels[index];
   }

   public void setHumanLabelAtIndex(int index, JButton button) {
      this.humanLabels[index] = button;
   }

   public void setComputerLabels(JLabel[] computerLabels) {
      this.computerLabels = computerLabels;
   }

   public JButton[] getHumanLabels() {
      return humanLabels;
   }

   public void setHumanLabels(JButton[] humanLabels) {
      this.humanLabels = humanLabels;
   }

   public JButton[] getPlayedCardLabels() {
      return playedCardLabels;
   }

   public void setPlayedCardLabels(JButton[] playedCardLabels) {
      this.playedCardLabels = playedCardLabels;
   }

   public CardTable getCardTable() {
      return cardTable;
   }

   public void setCardTable(CardTable cardTable) {
      this.cardTable = cardTable;
   }

}
