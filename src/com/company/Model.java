package com.company;

public class Model {
   /**
    * booleans to track who goes first each turn
    */
   static boolean computerWin;
   static boolean humanWin;

   static int numPlayers;
   static int numCardsPerHand;

   static CardGameFramework LowCardGame; // CardGameFramework instance

   Model(int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand) {

      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;

      LowCardGame = new CardGameFramework(
            numPacks, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            numPlayers, numCardsPerHand);
   }

   public static CardGameFramework getLowCardGame() {
      return LowCardGame;
   }

   public static void setLowCardGame(CardGameFramework lowCardGame) {
      LowCardGame = lowCardGame;
   }

   public static int getNumPlayers() {
      return numPlayers;
   }

   public static void setNumPlayers(int numPlayers) {
      Model.numPlayers = numPlayers;
   }

   public static int getNumCardsPerHand() {
      return numCardsPerHand;
   }

   public static void setNumCardsPerHand(int numCardsPerHand) {
      Model.numCardsPerHand = numCardsPerHand;
   }

   public static boolean isComputerWin() {
      return computerWin;
   }

   public static void setComputerWin(boolean computerWin) {
      Model.computerWin = computerWin;
   }

   public static boolean isHumanWin() {
      return humanWin;
   }

   public static void setHumanWin(boolean humanWin) {
      Model.humanWin = humanWin;
   }
}
