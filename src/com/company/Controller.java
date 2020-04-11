package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   /**
    * booleans to track who goes first each turn
    */
   static boolean computerWin;
   static boolean humanWin;
   /**
    * UI Labels
    */
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JButton[] humanLabels = new JButton[NUM_CARDS_PER_HAND];
   static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
   /**
    * Game Managers
    */
   static CardGameFramework LowCardGame; // CardGameFramework instance
   static CardTable myCardTable;  // CardTable instance
   /**
    * CardGameFramework config
    */
   static int numPacksPerDeck;
   static int numJokersPerPack;
   static int numUnusedCardsPerPack;
   static Card[] unusedCardsPerPack;
   /**
    * Constants to keep track of Human and Computer hand indexes in CardGameFramework
    */
   static int COMPUTER_HAND_INDEX = 0;
   static int HUMAN_HAND_INDEX = 1;
   /**
    * Keep track of winnings in 2D array
    */
   static Card[][] cardWinningsPerPlayer = new Card[NUM_PLAYERS][Deck.MAX_CARDS];
   static int[] numWinningsPerPlayer = new int[NUM_PLAYERS]; // so we know index to add cards for each win
   /**
    * Keep track of which cards are in play at any time
    */
   static Card[] cardsInPlay = new Card[NUM_PLAYERS];


   private Model model;
   private View view;

   Controller(Model m, View v) {
      model = m;
      view = v;
   }

   public void initController() {
      myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      numPacksPerDeck = 1;
      numJokersPerPack = 2;
      numUnusedCardsPerPack = 0;
      unusedCardsPerPack = null;
      LowCardGame = new CardGameFramework(
            numPacksPerDeck, numJokersPerPack,
            numUnusedCardsPerPack, unusedCardsPerPack,
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      LowCardGame.deal(); // deal to players
      myCardTable.getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER); // put placeholders in play area
      myCardTable.getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER);
      myCardTable.getPnlPlayArea().add(new JLabel("Computer", JLabel.CENTER));
      myCardTable.getPnlPlayArea().add(new JLabel("You", JLabel.CENTER));
      renderHands();
      // show everything to the user
      myCardTable.setVisible(true);
      // game will start with computer playing first
      computerWin = true;
      humanWin = false;
      /**
       * Start Playing Game
       */
      playCards();
   }

   /**
    * Show a dialog with Game Results.
    */
   private static void handleEndGame() {
      String resultText = "";
      if (numWinningsPerPlayer[HUMAN_HAND_INDEX] == numWinningsPerPlayer[COMPUTER_HAND_INDEX]) {
         resultText = "You tied!";
      }
      else if (numWinningsPerPlayer[HUMAN_HAND_INDEX] > numWinningsPerPlayer[COMPUTER_HAND_INDEX]) {
         resultText = "You win!";
      } else {
         resultText = "Computer wins!";
      }
      String displayText =
            "Game is Over. Final Scores: \n" + "Computer: " + numWinningsPerPlayer[COMPUTER_HAND_INDEX] + " Cards\n"
                  + "You: " + numWinningsPerPlayer[HUMAN_HAND_INDEX] + " Cards\n" + resultText;
      JOptionPane.showMessageDialog(myCardTable, displayText, "Round Results", JOptionPane.PLAIN_MESSAGE);
   }
   /**
    * Debugging Method: Print out each player's Card winnings.
    */
   private static void printPlayerWinnings(int playerIndex) {
      Card[] cardWinnings = cardWinningsPerPlayer[playerIndex];
      for (int i = 0; i < numWinningsPerPlayer[playerIndex]; i++) {
         System.out.println(cardWinnings[i].toString());
      }
   }
   /**
    * Play cards from each hand to playing area
    */
   private static void playCards() {
      if(computerWin)
      {
         cardsInPlay[0] = computerPlayCard();
      }
   }
   /**
    * Reset for next round
    */
   private static void resetForNewRound() {
      Component[] playAreaLabels = myCardTable.getPnlPlayArea().getComponents();
      for (int i = 0; i < playAreaLabels.length; i++) {
         ((JLabel) playAreaLabels[i]).setIcon(null); // set the play area icons to null
      }
      // Deal out new cards
      for (int i = 0; i < NUM_PLAYERS; i++) {
         LowCardGame.takeCard(i); // replenish hand
      }
      myCardTable.getPnlHumanHand().removeAll();
      myCardTable.getPnlComputerHand().removeAll();
      if(LowCardGame.getNumCardsRemainingInDeck() == 0) NUM_CARDS_PER_HAND--;
      renderHands();
      myCardTable.revalidate();
      myCardTable.repaint();
      // End Game if either player is out of cards
      if(LowCardGame.getHand(COMPUTER_HAND_INDEX).getNumCards() == 1  || LowCardGame.getHand(HUMAN_HAND_INDEX).getNumCards() == 1) {
         handleEndGame();
      }
      // Otherwise check if computer starts new round
      else if(computerWin) {
         cardsInPlay[0] = computerPlayCard();
      }
   }
   /**
    * Calculate and Display Results
    */
   private static void handleRoundResults() {
      String resultText = "";
      int winnerIndex = 0; // start with index: 0 as winner
      for (int i = 1; i < cardsInPlay.length; i++) {
         int cardValue = GUICard.valueAsInt(cardsInPlay[i]);
         int currentLowest = GUICard.valueAsInt(cardsInPlay[winnerIndex]);
         if (cardValue < currentLowest) {
            winnerIndex = i;
         } else if (cardValue == currentLowest) {
            if (GUICard.suitAsInt(cardsInPlay[i]) < GUICard.suitAsInt(cardsInPlay[winnerIndex])) { // break tie on suit
               winnerIndex = i;
            }
         }
      }
      // save winnings
      int numCardsWon = numWinningsPerPlayer[winnerIndex];
      cardWinningsPerPlayer[winnerIndex][numCardsWon] = cardsInPlay[0]; // actually place cards in winnings
      cardWinningsPerPlayer[winnerIndex][numCardsWon + 1] = cardsInPlay[1];
      numWinningsPerPlayer[winnerIndex] += 2; // increment num of cards won
      if (winnerIndex == HUMAN_HAND_INDEX) {
         humanWin = true;
         computerWin = false;
         resultText = "You Won";
      } else {
         humanWin = false;
         computerWin = true;
         resultText = "You Lost";
      }
      JOptionPane.showMessageDialog(myCardTable,
            resultText, "Round Results",  JOptionPane.PLAIN_MESSAGE); // Display dialog with results
      myCardTable.getPnlPlayArea().revalidate();
   }
   private static void renderHands() {
      computerLabels = new JLabel[NUM_CARDS_PER_HAND];
      humanLabels = new JButton[NUM_CARDS_PER_HAND];
      // CREATE COMP LABELS ----------------------------------------------------
      for (int i = 0; i < computerLabels.length; i++) {
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         // ADD COMP LABELS TO PANELS -----------------------------------------
         myCardTable.getPnlComputerHand().add(computerLabels[i]);
      }
      // CREATE HUMAN LABELS ----------------------------------------------------
      for (int i = 0; i < humanLabels.length; i++) {
         // player hand should be buttons
         JButton playCardButton = new JButton(GUICard.getIcon(LowCardGame.getHand(HUMAN_HAND_INDEX).inspectCard(i)));
         playCardButton.setActionCommand(String.valueOf(i));
         playCardButton.addActionListener(new CardButtonListener());
         humanLabels[i] = playCardButton;
         // ADD HUMAN LABELS TO PANELS -----------------------------------------
         myCardTable.getPnlHumanHand().add(humanLabels[i]);
      }
   }
   private static Card computerPlayCard() {
      Hand hand = LowCardGame.getHand(COMPUTER_HAND_INDEX);
      Card cardToPlay = null;
      hand.sort();  //want to sort out the hand to get 1 card lower than humanCardToPlay
      if (playedCardLabels[HUMAN_HAND_INDEX] == null) {  //if no display on humanCard, then play at index 0.
         cardToPlay = LowCardGame.playCard(COMPUTER_HAND_INDEX, 0); //which if is sorted then would be lowest card
      }
      else {
         for(int i = hand.getNumCards() - 1; i > 0; i--) { // find highest card that is sufficient to win round
            if(cardsInPlay[HUMAN_HAND_INDEX].getValue() < hand.inspectCard(i).getValue()) {
               cardToPlay = LowCardGame.playCard(COMPUTER_HAND_INDEX, i);
               break;
            }
         }
      }
      if (cardToPlay == null) { // if still null then computer can't win round, discard highest
         cardToPlay = hand.playCard(hand.getNumCards() - 1);
      }
      // update ui
      JLabel playArea = (JLabel) myCardTable.getPnlPlayArea().getComponent(COMPUTER_HAND_INDEX);
      playArea.setIcon(GUICard.getIcon(cardToPlay));
      return cardToPlay;
   }
   private static Card humanPlayCard(int handIndex) {
      Hand hand = LowCardGame.getHand(HUMAN_HAND_INDEX);
      Card cardToPlay = LowCardGame.playCard(HUMAN_HAND_INDEX, handIndex);
      // update ui
      JLabel playArea = (JLabel) myCardTable.getPnlPlayArea().getComponent(HUMAN_HAND_INDEX);
      playArea.setIcon(GUICard.getIcon(cardToPlay));
      return cardToPlay;
   }
   /**
    * Inner button listener class
    */
   private static class CardButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         int slotNumber = Integer.valueOf(e.getActionCommand()); // get slot number played
         JButton button = (JButton)e.getSource();
         cardsInPlay[1] = humanPlayCard(slotNumber);
         button.setIcon(null);
         button.setEnabled(false);
         //human is playing first this round
         if(humanWin) {
            cardsInPlay[0] = computerPlayCard();
         }
         handleRoundResults();
         resetForNewRound();
      }
   }
}
