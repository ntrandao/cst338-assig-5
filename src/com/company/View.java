package com.company;

import javax.swing.*;

public class View {
   /**
    * UI Labels
    */
   JLabel[] computerLabels;
   JButton[] humanLabels;
   JLabel[] playedCardLabels;

   /**
    * Game Managers
    */
   static CardTable cardTable;  // CardTable instance

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

   public JLabel[] getPlayedCardLabels() {
      return playedCardLabels;
   }

   public void setPlayedCardLabels(JLabel[] playedCardLabels) {
      this.playedCardLabels = playedCardLabels;
   }

   public CardTable getCardTable() {
      return cardTable;
   }

   public void setCardTable(CardTable cardTable) {
      this.cardTable = cardTable;
   }

}
