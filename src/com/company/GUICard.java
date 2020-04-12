package com.company;

import javax.swing.*;

/**
 * Class GUICard is designed to hold and return graphics for cards.
 */
public class GUICard {
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack = new ImageIcon("images/BK.gif");
   private static boolean iconsLoaded = false;

   static void loadCardIcons() {
      if (iconsLoaded) return; // only load icons once

      for (int i = 0; i < iconCards.length; i++) {
         for (int j = 0; j < iconCards[i].length; j++) {
            // i and j are value and suit indexes
            String filename = indexAsRank(i) + indexAsSuit(j) + ".gif";
            iconCards[i][j] = new ImageIcon("images/" + filename);
         }
      }

      iconsLoaded = true;
   }

   /**
    * Helper method to convert from IconCard array indexes to card values.
    * @param index
    * @return
    */
   static char indexAsRank(int index) {
      return Card.valuRanks[index];
   }

   /**
    * Helper method to convert from IconCard array indexes to card suits.
    * @param index
    * @return
    */
   private static String indexAsSuit(int index) {
      return Card.Suit.values()[index].toString().substring(0, 1);
   }

   /**
    * Get Card value as an integer.
    * @param card
    * @return
    */
   public static int valueAsInt(Card card) {
      int i;

      for (i = 0; i < Card.valuRanks.length; i++) {
         if (Card.valuRanks[i] == card.getValue()) break;
      }

      return i;
   }

   /**
    * Return card suit as an integer.
    * @param card
    * @return
    */
   public static int suitAsInt(Card card) {
      return card.getSuit().ordinal();
   }

   /**
    * Retrieve Icon representing given card.
    * @param card The card to get an Icon for.
    * @return     Icon representation of given card.
    */
   public static Icon getIcon(Card card) {
      loadCardIcons();

      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   public static Icon getBackCardIcon() {
      return iconBack;
   }
}
