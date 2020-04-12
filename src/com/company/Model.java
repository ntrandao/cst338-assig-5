package com.company;

public class Model {
   /**
    * booleans to track who goes first each turn
    */
   static boolean computerWin;
   static boolean humanWin;

   static int numPlayers;
   static int numCardsPerHand;
   static int numStacks;

   static CardGameFramework LowCardGame; // CardGameFramework instance

   /**
    * Keep track of which cards are in play at any time
    */
   static Card[] cardsInPlay;

   /**
    * Keep track of winnings in 2D array
    */

   Model(int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand, int numStacks) {

      setNumPlayers(numPlayers);
      setNumCardsPerHand(numCardsPerHand);
      setNumStacks(numStacks);

      LowCardGame = new CardGameFramework(
            numPacks, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            numPlayers, numCardsPerHand, numStacks);

      cardsInPlay = new Card[this.numStacks];
   }

   public CardGameFramework getLowCardGame() {
      return LowCardGame;
   }

   public void setLowCardGame(CardGameFramework lowCardGame) {
      LowCardGame = lowCardGame;
   }

   public int getNumPlayers() {
      return numPlayers;
   }

   public void setNumPlayers(int numPlayers) {
      Model.numPlayers = numPlayers;
   }

   public void setNumStacks(int numStacks) {
      Model.numStacks = numStacks;
   }

   public int getNumCardsPerHand() {
      return numCardsPerHand;
   }

   public void setNumCardsPerHand(int numCardsPerHand) {
      Model.numCardsPerHand = numCardsPerHand;
   }

   public boolean isComputerWin() {
      return computerWin;
   }

   public void setComputerWin(boolean computerWin) {
      Model.computerWin = computerWin;
   }

   public boolean isHumanWin() {
      return humanWin;
   }

   public void setHumanWin(boolean humanWin) {
      Model.humanWin = humanWin;
   }

   public Card[] getCardsInPlay() {
      return cardsInPlay;
   }

   public void setCardsInPlay(Card[] cardsInPlay) {
      Model.cardsInPlay = cardsInPlay;
   }
}
