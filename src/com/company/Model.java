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

   /**
    * Keep track of which cards are in play at any time
    */
   static Card[] cardsInPlay;

   /**
    * Keep track of winnings in 2D array
    */
   static Card[][] cardWinningsPerPlayer;
   static int[] numWinningsPerPlayer; // so we know index to add cards for each win

   Model(int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand) {

      setNumPlayers(numPlayers);
      setNumCardsPerHand(numCardsPerHand);

      LowCardGame = new CardGameFramework(
            numPacks, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            numPlayers, numCardsPerHand);

      cardsInPlay = new Card[this.numPlayers];

      cardWinningsPerPlayer = new Card[this.numPlayers][Deck.MAX_CARDS];
      numWinningsPerPlayer = new int[this.numPlayers]; // so we know index to add cards for each win
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

   public static Card[] getCardsInPlay() {
      return cardsInPlay;
   }

   public static void setCardsInPlay(Card[] cardsInPlay) {
      Model.cardsInPlay = cardsInPlay;
   }

   public static Card[][] getCardWinningsPerPlayer() {
      return cardWinningsPerPlayer;
   }

   public static void setCardWinningsPerPlayer(int playerIndex, int cardIndex, Card card) {
      cardWinningsPerPlayer[playerIndex][cardIndex] = new Card(card);
   }

   public static int getNumWinningsPerPlayer(int playerIndex) {
      return numWinningsPerPlayer[playerIndex];
   }

   public static void setNumWinningsPerPlayer(int playerIndex, int numWinnings) {
      Model.numWinningsPerPlayer[playerIndex] = numWinnings;
   }
}
