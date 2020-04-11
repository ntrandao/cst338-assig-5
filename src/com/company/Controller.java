package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
   /**
    * Constants to keep track of Human and Computer hand indexes
    */
   static int COMPUTER_HAND_INDEX = 0;
   static int HUMAN_HAND_INDEX = 1;

   private Model model;
   private View view;

   Controller(Model m, View v) {
      model = m;
      view = v;

      model.getLowCardGame().deal(); // deal to players
      initView();
   }

   public void initController() {
      playCards(); // start playing game
   }

   private void initView() {
      view.setCardTable(new CardTable("CardTable", model.getNumCardsPerHand(), model.getNumPlayers()));

      view.setComputerLabels(new JLabel[model.getNumCardsPerHand()]);
      view.setHumanLabels(new JButton[model.getNumCardsPerHand()]);
      view.setPlayedCardLabels(new JLabel[model.getNumPlayers()]);

      view.getCardTable().setSize(800, 600);
      view.getCardTable().setLocationRelativeTo(null);
      view.getCardTable().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      view.getCardTable().getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER); // put placeholders in play area
      view.getCardTable().getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER);
      view.getCardTable().getPnlPlayArea().add(new JLabel("Computer", JLabel.CENTER));
      view.getCardTable().getPnlPlayArea().add(new JLabel("You", JLabel.CENTER));
      renderHands();
      // show everything to the user
      view.getCardTable().setVisible(true);
   }

   /**
    * Show a dialog with Game Results.
    */
   private void handleEndGame() {
      int numHumanWinnings = model.getNumWinningsPerPlayer(HUMAN_HAND_INDEX);
      int numComputerWinnings = model.getNumWinningsPerPlayer(COMPUTER_HAND_INDEX);

      String resultText = "";
      if  (numHumanWinnings == numComputerWinnings) {
         resultText = "You tied!";
      } else if (numHumanWinnings > numComputerWinnings) {
         resultText = "You win!";
      } else {
         resultText = "Computer wins!";
      }
      String displayText =
            "Game is Over. Final Scores: \n" + "Computer: " + numComputerWinnings + " Cards\n"
                  + "You: " + numHumanWinnings + " Cards\n" + resultText;
      JOptionPane.showMessageDialog(view.getCardTable(), displayText, "Round Results", JOptionPane.PLAIN_MESSAGE);
   }

   /**
    * Play cards from each hand to playing area
    */
   private void playCards() {
      if (model.isComputerWin()) {
         model.getCardsInPlay()[0] = computerPlayCard();
      }
   }

   /**
    * Reset for next round
    */
   private void resetForNewRound() {
      Component[] playAreaLabels = view.getCardTable().getPnlPlayArea().getComponents();
      for (int i = 0; i < playAreaLabels.length; i++) {
         ((JLabel) playAreaLabels[i]).setIcon(null); // set the play area icons to null
      }
      // Deal out new cards
      for (int i = 0; i < model.getNumPlayers(); i++) {
         model.getLowCardGame().takeCard(i); // replenish hand
      }
      view.getCardTable().getPnlHumanHand().removeAll();
      view.getCardTable().getPnlComputerHand().removeAll();

      if (model.getLowCardGame().getNumCardsRemainingInDeck() == 0)
         model.setNumCardsPerHand(model.getNumCardsPerHand() - 1);
      renderHands();
      view.getCardTable().revalidate();
      view.getCardTable().repaint();
      // End Game if either player is out of cards
      if (model.getLowCardGame().getHand(COMPUTER_HAND_INDEX).getNumCards() == 1 || model.getLowCardGame().getHand(HUMAN_HAND_INDEX).getNumCards() == 1) {
         handleEndGame();
      }
      // Otherwise check if computer starts new round
      else if (model.isComputerWin()) {
         model.getCardsInPlay()[0] = computerPlayCard();
      }
   }

   /**
    * Calculate and Display Results
    */
   private void handleRoundResults() {
      String resultText = "";
      int winnerIndex = 0; // start with index: 0 as winner
      for (int i = 1; i < model.getCardsInPlay().length; i++) {
         int cardValue = GUICard.valueAsInt(model.getCardsInPlay()[i]);
         int currentLowest = GUICard.valueAsInt(model.getCardsInPlay()[winnerIndex]);
         if (cardValue < currentLowest) {
            winnerIndex = i;
         } else if (cardValue == currentLowest) {
            if (GUICard.suitAsInt(model.getCardsInPlay()[i]) < GUICard.suitAsInt(model.getCardsInPlay()[winnerIndex])) { // break tie on suit
               winnerIndex = i;
            }
         }
      }
      // save winnings
      int numCardsWon = model.getNumWinningsPerPlayer(winnerIndex);
      model.setCardWinningsPerPlayer(winnerIndex, numCardsWon, model.getCardsInPlay()[0]); // actually place cards in winnings
      model.setCardWinningsPerPlayer(winnerIndex, numCardsWon + 1, model.getCardsInPlay()[1]);
      model.setNumWinningsPerPlayer(winnerIndex, numCardsWon + 2);

      if (winnerIndex == HUMAN_HAND_INDEX) {
         model.setHumanWin(true);
         model.setComputerWin(false);
         resultText = "You Won";
      } else {
         model.setHumanWin(false);
         model.setComputerWin(true);
         resultText = "You Lost";
      }
      JOptionPane.showMessageDialog(view.getCardTable(),
            resultText, "Round Results", JOptionPane.PLAIN_MESSAGE); // Display dialog with results
      view.getCardTable().getPnlPlayArea().revalidate();
   }

   private void renderHands() {
      view.setComputerLabels(new JLabel[model.getNumCardsPerHand()]);
      view.setHumanLabels(new JButton[model.getNumCardsPerHand()]);

      JLabel[] computerLabels = view.getComputerLabels();
      JButton[] humanLabels = view.getHumanLabels();
      // CREATE COMP LABELS ----------------------------------------------------
      for (int i = 0; i < computerLabels.length; i++) {
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         // ADD COMP LABELS TO PANELS -----------------------------------------
         view.getCardTable().getPnlComputerHand().add(computerLabels[i]);
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
         view.getCardTable().getPnlHumanHand().add(humanLabels[i]);
      }
   }

   private Card computerPlayCard() {
      Hand hand = model.getLowCardGame().getHand(COMPUTER_HAND_INDEX);
      Card cardToPlay = null;
      hand.sort();  //want to sort out the hand to get 1 card lower than humanCardToPlay
      if (view.getPlayedCardLabels()[HUMAN_HAND_INDEX] == null) {  //if no display on humanCard, then play at index 0.
         cardToPlay = model.getLowCardGame().playCard(COMPUTER_HAND_INDEX, 0); //which if is sorted then would be
         // lowest card
      } else {
         for (int i = hand.getNumCards() - 1; i > 0; i--) { // find highest card that is sufficient to win round
            if (model.getCardsInPlay()[HUMAN_HAND_INDEX].getValue() < hand.inspectCard(i).getValue()) {
               cardToPlay = model.getLowCardGame().playCard(COMPUTER_HAND_INDEX, i);
               break;
            }
         }
      }
      if (cardToPlay == null) { // if still null then computer can't win round, discard highest
         cardToPlay = hand.playCard(hand.getNumCards() - 1);
      }
      // update ui
      JLabel playArea = (JLabel) view.getCardTable().getPnlPlayArea().getComponent(COMPUTER_HAND_INDEX);
      playArea.setIcon(GUICard.getIcon(cardToPlay));
      return cardToPlay;
   }

   private Card humanPlayCard(int handIndex) {
      Hand hand = model.getLowCardGame().getHand(HUMAN_HAND_INDEX);
      Card cardToPlay = model.getLowCardGame().playCard(HUMAN_HAND_INDEX, handIndex);
      // update ui
      JLabel playArea = (JLabel) view.getCardTable().getPnlPlayArea().getComponent(HUMAN_HAND_INDEX);
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
         model.getCardsInPlay()[1] = humanPlayCard(slotNumber);
         button.setIcon(null);
         button.setEnabled(false);
         //human is playing first this round
         if (model.isHumanWin()) {
            model.getCardsInPlay()[0] = computerPlayCard();
         }
         handleRoundResults();
         resetForNewRound();
      }
   }
}
