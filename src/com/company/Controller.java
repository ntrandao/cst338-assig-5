package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

   /**
    * UI Labels
    */
   static JLabel[] computerLabels;
   static JButton[] humanLabels;
   static JLabel[] playedCardLabels;

   /**
    * Game Managers
    */
   static CardTable myCardTable;  // CardTable instance

   /**
    * Constants to keep track of Human and Computer hand indexes in CardGameFramework
    */
   static int COMPUTER_HAND_INDEX = 0;
   static int HUMAN_HAND_INDEX = 1;

   /**
    * Keep track of winnings in 2D array
    */
   static Card[][] cardWinningsPerPlayer;
   static int[] numWinningsPerPlayer; // so we know index to add cards for each win

   /**
    * Keep track of which cards are in play at any time
    */
   static Card[] cardsInPlay;


   private Model model;
   private View view;

   Controller(Model m, View v) {
      model = m;
      view = v;
   }

   public void initController() {
      computerLabels = new JLabel[model.getNumCardsPerHand()];
      humanLabels = new JButton[model.getNumCardsPerHand()];
      playedCardLabels = new JLabel[model.getNumPlayers()];

      cardsInPlay = new Card[model.getNumPlayers()];

      /**
       * Keep track of winnings in 2D array
       */
      cardWinningsPerPlayer = new Card[model.getNumPlayers()][Deck.MAX_CARDS];
      numWinningsPerPlayer = new int[model.getNumPlayers()]; // so we know index to add cards for each win

      myCardTable = new CardTable("CardTable", model.getNumCardsPerHand(), model.getNumPlayers());
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      model.getLowCardGame().deal(); // deal to players

      myCardTable.getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER); // put placeholders in play area
      myCardTable.getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER);
      myCardTable.getPnlPlayArea().add(new JLabel("Computer", JLabel.CENTER));
      myCardTable.getPnlPlayArea().add(new JLabel("You", JLabel.CENTER));
      renderHands();
      // show everything to the user
      myCardTable.setVisible(true);
      // game will start with computer playing first
      model.setComputerWin(true);
      model.setHumanWin(false);

      /**
       * Start Playing Game
       */
      playCards();
   }

   /**
    * Show a dialog with Game Results.
    */
   private void handleEndGame() {
      String resultText = "";
      if (numWinningsPerPlayer[HUMAN_HAND_INDEX] == numWinningsPerPlayer[COMPUTER_HAND_INDEX]) {
         resultText = "You tied!";
      } else if (numWinningsPerPlayer[HUMAN_HAND_INDEX] > numWinningsPerPlayer[COMPUTER_HAND_INDEX]) {
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
    * Play cards from each hand to playing area
    */
   private void playCards() {
      if (model.isComputerWin()) {
         cardsInPlay[0] = computerPlayCard();
      }
   }

   /**
    * Reset for next round
    */
   private void resetForNewRound() {
      Component[] playAreaLabels = myCardTable.getPnlPlayArea().getComponents();
      for (int i = 0; i < playAreaLabels.length; i++) {
         ((JLabel) playAreaLabels[i]).setIcon(null); // set the play area icons to null
      }
      // Deal out new cards
      for (int i = 0; i < model.getNumPlayers(); i++) {
         model.getLowCardGame().takeCard(i); // replenish hand
      }
      myCardTable.getPnlHumanHand().removeAll();
      myCardTable.getPnlComputerHand().removeAll();

      if (model.getLowCardGame().getNumCardsRemainingInDeck() == 0)
         model.setNumCardsPerHand(model.getNumCardsPerHand() - 1);
      renderHands();
      myCardTable.revalidate();
      myCardTable.repaint();
      // End Game if either player is out of cards
      if (model.getLowCardGame().getHand(COMPUTER_HAND_INDEX).getNumCards() == 1 || model.getLowCardGame().getHand(HUMAN_HAND_INDEX).getNumCards() == 1) {
         handleEndGame();
      }
      // Otherwise check if computer starts new round
      else if (model.isComputerWin()) {
         cardsInPlay[0] = computerPlayCard();
      }
   }

   /**
    * Calculate and Display Results
    */
   private void handleRoundResults() {
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
         model.setHumanWin(true);
         model.setComputerWin(false);
         resultText = "You Won";
      } else {
         model.setHumanWin(false);
         model.setComputerWin(true);
         resultText = "You Lost";
      }
      JOptionPane.showMessageDialog(myCardTable,
            resultText, "Round Results", JOptionPane.PLAIN_MESSAGE); // Display dialog with results
      myCardTable.getPnlPlayArea().revalidate();
   }

   private void renderHands() {
      computerLabels = new JLabel[model.getNumCardsPerHand()];
      humanLabels = new JButton[model.getNumCardsPerHand()];
      // CREATE COMP LABELS ----------------------------------------------------
      for (int i = 0; i < computerLabels.length; i++) {
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         // ADD COMP LABELS TO PANELS -----------------------------------------
         myCardTable.getPnlComputerHand().add(computerLabels[i]);
      }
      // CREATE HUMAN LABELS ----------------------------------------------------
      for (int i = 0; i < humanLabels.length; i++) {
         // player hand should be buttons
         JButton playCardButton =
               new JButton(GUICard.getIcon(model.getLowCardGame().getHand(HUMAN_HAND_INDEX).inspectCard(i)));
         playCardButton.setActionCommand(String.valueOf(i));
         playCardButton.addActionListener(new CardButtonListener());
         humanLabels[i] = playCardButton;
         // ADD HUMAN LABELS TO PANELS -----------------------------------------
         myCardTable.getPnlHumanHand().add(humanLabels[i]);
      }
   }

   private Card computerPlayCard() {
      Hand hand = model.getLowCardGame().getHand(COMPUTER_HAND_INDEX);
      Card cardToPlay = null;
      hand.sort();  //want to sort out the hand to get 1 card lower than humanCardToPlay
      if (playedCardLabels[HUMAN_HAND_INDEX] == null) {  //if no display on humanCard, then play at index 0.
         cardToPlay = model.getLowCardGame().playCard(COMPUTER_HAND_INDEX, 0); //which if is sorted then would be
         // lowest card
      } else {
         for (int i = hand.getNumCards() - 1; i > 0; i--) { // find highest card that is sufficient to win round
            if (cardsInPlay[HUMAN_HAND_INDEX].getValue() < hand.inspectCard(i).getValue()) {
               cardToPlay = model.getLowCardGame().playCard(COMPUTER_HAND_INDEX, i);
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

   private Card humanPlayCard(int handIndex) {
      Hand hand = model.getLowCardGame().getHand(HUMAN_HAND_INDEX);
      Card cardToPlay = model.getLowCardGame().playCard(HUMAN_HAND_INDEX, handIndex);
      // update ui
      JLabel playArea = (JLabel) myCardTable.getPnlPlayArea().getComponent(HUMAN_HAND_INDEX);
      playArea.setIcon(GUICard.getIcon(cardToPlay));
      return cardToPlay;
   }

   /**
    * Inner button listener class
    */
   private class CardButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         int slotNumber = Integer.valueOf(e.getActionCommand()); // get slot number played
         JButton button = (JButton) e.getSource();
         cardsInPlay[1] = humanPlayCard(slotNumber);
         button.setIcon(null);
         button.setEnabled(false);
         //human is playing first this round
         if (model.isHumanWin()) {
            cardsInPlay[0] = computerPlayCard();
         }
         handleRoundResults();
         resetForNewRound();
      }
   }
}
