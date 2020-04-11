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

      // game will start with computer playing first
      setComputerWin(true);
      setHumanWin(false);

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

   public CardGameFramework getLowCardGame() {
      return LowCardGame;
   }

   public int getNumPlayers() {
      return numPlayers;
   }

   public void setNumPlayers(int numPlayers) {
      Model.numPlayers = numPlayers;
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

   public int getNumCardsInPlay() {
      return cardsInPlay.length;
   }

   public Card getCardInPlay(int index) {
      return cardsInPlay[index];
   }

   public void setCardInPlay(int index, Card card) {
      this.cardsInPlay[index] = card;
   }

   public Card[][] getCardWinningsPerPlayer() {
      return cardWinningsPerPlayer;
   }

   public void setCardWinningsPerPlayer(int playerIndex, int cardIndex, Card card) {
      cardWinningsPerPlayer[playerIndex][cardIndex] = new Card(card);
   }

   public int getNumWinningsByPlayerIndex(int playerIndex) {
      return numWinningsPerPlayer[playerIndex];
   }

   public void setNumWinningsPerPlayer(int playerIndex, int numWinnings) {
      Model.numWinningsPerPlayer[playerIndex] = numWinnings;
   }
}
