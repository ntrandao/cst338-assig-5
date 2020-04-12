package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class to drive game.
 */
public class Controller {
   /**
    * Constants to keep track of Human and Computer hand indexes.
    */
   static final int COMPUTER_HAND_INDEX = 0;
   static final int HUMAN_HAND_INDEX = 1;

   static boolean cannotPlay; // a flag to check if we need to re-deal on stack

   /**
    * Integers to track how many times Computer and Human could not play any cards.
    */
   static int  COMPUTER_CANNOT_PLAY;
   static int HUMAN_CANNOT_PLAY;

   /**
    * Temp space to store a selected Card before played
    */
   Card hSelectedCard;
   Card cSelectedCard;

   /**
    *
    */
   int hSlotNum;
   int cSlotNum;

   private Model model;
   private View view;

   Controller(Model m, View v) {
      model = m;
      view = v;

      hSelectedCard = null;
      cSelectedCard = null;

      hSlotNum = 3; // invalid slot on stack(0-2) to start
      cSlotNum = 3;

      cannotPlay = false;

      COMPUTER_CANNOT_PLAY = 0;
      HUMAN_CANNOT_PLAY = 0;

      model.getLowCardGame().deal(); // deal to players
      initView();
   }

   /**
    * Attach event listeners
    */
   public void initController() {
      for (int i = 0; i < model.getNumCardsPerHand(); i++) { // attach button listener to human cards
         // player hand should be buttons
         JButton playCardButton =
               new JButton(GUICard.getIcon(model.getLowCardGame().getHand(HUMAN_HAND_INDEX).inspectCard(i)));
         playCardButton.setActionCommand(String.valueOf(i));
         playCardButton.addActionListener(new CardButtonListener());
         view.setHumanLabelAtIndex(i, playCardButton);

         // SET COMPUTER BACK CARD LABELS --------------------------------------
         view.setComputerLabelAtIndex(i, new JLabel(GUICard.getBackCardIcon()));

         // ADD LABELS TO PANELS -----------------------------------------
         view.getCardTable().getPnlComputerHand().add(view.getComputerLabelAtIndex(i));
         view.getCardTable().getPnlHumanHand().add(view.getHumanLabelAtIndex(i));
      }

      renderHands();
      // show everything to the user
      view.getCardTable().setVisible(true);

      playCards(); // start playing game
   }

   /**
    * Initialize the view with starting values
    */
   private void initView() {
      view.setCardTable(new CardTable("CardTable", model.getNumCardsPerHand(), model.getNumPlayers()));

      view.setComputerLabels(new JLabel[model.getNumCardsPerHand()]);
      view.setHumanLabels(new JButton[model.getNumCardsPerHand()]);
      view.setPlayedCardLabels(new JButton[model.getNumStacks()]);

      view.getCardTable().setSize(800, 600);
      view.getCardTable().setLocationRelativeTo(null);
      view.getCardTable().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // play area
      view.getCardTable().getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER); // put placeholders in
      view.getCardTable().getPnlPlayArea().add(new JLabel(new ImageIcon()), JLabel.CENTER);
      view.getCardTable().getPnlPlayArea().add(new JLabel("Computer", JLabel.CENTER));
      view.getCardTable().getPnlPlayArea().add(new JLabel("You", JLabel.CENTER));
   }

   /**
    * Seal a card to each stack in the middle of the table.
    */
   private void dealStacks() {

      for (int i = 0; i < model.getNumCardsInPlay(); i++) {
         Card c = model.getLowCardGame().getCardFromDeck();
         if(!c.getErrorFlag())
         {
            model.setCardInPlay(i, c);
         }
         else { // invalid card, deck is empty, end game
            handleEndGame();
            return;
         }
      }
   }

   /**
    * Show a dialog with Game Results.
    */
   private void handleEndGame() {
      String resultText = "";
      if (COMPUTER_CANNOT_PLAY == HUMAN_CANNOT_PLAY) {

         resultText = "You tied!";
      } else if (COMPUTER_CANNOT_PLAY > HUMAN_CANNOT_PLAY) {
         resultText = "You win!";
      } else {
         resultText = "Computer wins!";
      }
      String displayText =
            "Game is Over. Final Scores: \n" + "Computer: " + COMPUTER_CANNOT_PLAY + " forfeits\n"
                  + "You: " + HUMAN_CANNOT_PLAY + " forfeits\n" + resultText;
      JOptionPane.showMessageDialog(view.getCardTable(), displayText, "Round Results", JOptionPane.PLAIN_MESSAGE);
   }

   /**
    * Play cards from each hand to playing area
    */
   private void playCards() {
      if (model.isComputerWin()) {
         model.setCardInPlay(0, computerPlayCard());
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

      renderHands();
      view.getCardTable().revalidate();
      view.getCardTable().repaint();
      // End Game if either player is out of cards
      if (model.getLowCardGame().getHand(COMPUTER_HAND_INDEX).getNumCards() == 0 && model.getLowCardGame().getHand(HUMAN_HAND_INDEX).getNumCards() == 0) {
         handleEndGame();
      }
      // Otherwise check if computer starts new round
      else if (model.isComputerWin()) {
         model.setCardInPlay(0, computerPlayCard());
      }
   }

   /**
    * Calculate and Display Results
    */
   private void handleRoundResults() {
      String resultText = "";
      int winnerIndex = 0; // start with index: 0 as winner
      for (int i = 1; i < model.getNumCardsInPlay(); i++) {
         int cardValue = GUICard.valueAsInt(model.getCardInPlay(i));
         int currentLowest = GUICard.valueAsInt(model.getCardInPlay(winnerIndex));
         if (cardValue < currentLowest) {
            winnerIndex = i;
         } else if (cardValue == currentLowest) {
            if (GUICard.suitAsInt(model.getCardInPlay(i)) < GUICard.suitAsInt(model.getCardInPlay(winnerIndex))) { //
               // break tie on suit
               winnerIndex = i;
            }
         }
      }
      // save winnings

      // revisit

      if (winnerIndex == HUMAN_HAND_INDEX) {
         model.setHumanWin(true);
         model.setComputerWin(false);
         resultText = "You Won";
      } else {
         model.setHumanWin(false);
         model.setComputerWin(true);
         resultText = "You Lost";
      }

      View.displayMessage(resultText, "Round Results"); // Display dialog with results
      view.getCardTable().getPnlPlayArea().revalidate();
   }

   /**
    * Update player hands in the UI
    */
   private void renderHands() {
      // GENERATE COMP LABELS ----------------------------------------------------
      for (int i = 0; i < model.getNumCardsPerHand(); i++) {
         Card card = model.getLowCardGame().getHand(COMPUTER_HAND_INDEX).inspectCard(i);

         if (card == null || card.getErrorFlag()) {
            view.getComputerLabelAtIndex(i).setIcon(null);
            view.getComputerLabelAtIndex(i).setEnabled(false);
         }

         // ADD COMP LABELS TO PANELS -----------------------------------------
         view.getCardTable().getPnlComputerHand().add(view.getComputerLabelAtIndex(i));
      }

      // GENERATE HUMAN LABELS ----------------------------------------------------
      for (int i = 0; i < model.getNumCardsPerHand(); i++) {
         // player hand should be buttons
         Card card = model.getLowCardGame().getHand(HUMAN_HAND_INDEX).inspectCard(i);

         if (card == null || card.getErrorFlag()) {
            view.getHumanLabelAtIndex(i).setIcon(null);
            view.getHumanLabelAtIndex(i).setEnabled(false);
         } else {
            view.getHumanLabelAtIndex(i).setIcon(GUICard.getIcon(card));
            view.getHumanLabelAtIndex(i).setEnabled(true);
         }

         // ADD HUMAN LABELS TO PANELS -----------------------------------------
         view.getCardTable().getPnlHumanHand().add(view.getHumanLabelAtIndex(i));
      }
   }

   /**
    * Computer play card logic.
    *
    * @return The card the Computer will play.
    */
   private Card computerPlayCard() {
      Hand hand = model.getLowCardGame().getHand(COMPUTER_HAND_INDEX);
      Card cardToPlay = null;
      hand.sort();  //want to sort out the hand to get 1 card lower than humanCardToPlay
      if (view.getPlayedCardLabels()[HUMAN_HAND_INDEX] == null) {  //if no display on humanCard, then play at index 0.
         cardToPlay = model.getLowCardGame().playCard(COMPUTER_HAND_INDEX, 0); //which if is sorted then would be
         // lowest card
      } else {
         for (int i = hand.getNumCards() - 1; i > 0; i--) { // find highest card that is sufficient to win round
            if (model.getCardInPlay(HUMAN_HAND_INDEX).getValue() < hand.inspectCard(i).getValue()) {
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

   /**
    * After Human plays card, handles updating the UI and returns the Card played.
    *
    * @param handIndex The index in the Human hand of the card to be played.
    * @return The Card to be played.
    */
   private Card humanPlayCard(int handIndex) {
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
         int slotNumber = Integer.valueOf(e.getActionCommand()); // get slot number of the card played
         JButton button = (JButton) e.getSource();
         // determine if card is valid

         model.setCardInPlay(1, humanPlayCard(slotNumber));

         button.setIcon(null);
         button.setEnabled(false);
         //human is playing first this round
         if (model.isHumanWin()) {
            model.setCardInPlay(0, computerPlayCard());
         }
         handleRoundResults();
         resetForNewRound();
      }
   }

   /**
    * Inner select stack button listener class Allows the user to specify which stack they are trying to play their
    * selected card on
    */
   private class SelectStackButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         int slotNumber = Integer.valueOf(e.getActionCommand()); // get slot number played
      }

   }

   /**
    * Inner "cannot play" button listener class
    */
   private class CannotPlayButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         HUMAN_CANNOT_PLAY += 1;
         if(cannotPlay) // second cannot play in sequence, re-deal to stacks
         {
            dealStacks();
            cannotPlay = false;
         }
         else
         {
            cannotPlay = true;
         }
         computerPlayCard();
      }
   }
}