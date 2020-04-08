package com.company;

/**
 * Hand class represents a hand of cards held by a user.
 */
public class Hand {
   // Limit size of array
   public static final int MAX_CARDS = 50;

   private Card myCards[];
   private int numCards;

   /**
    * No argument constructor
    */
   Hand() {
      resetHand();
   }

   /**
    * Remove all cards from the hand.
    */
   public void resetHand() {
      this.myCards = new Card[MAX_CARDS];
      this.numCards = 0;
   }

   /**
    * Add a copy of the card to the next available position in the myCards array.
    *
    * @param card Card instance to add to the hand.
    * @return true on success array insert, false otherwise
    */
   public boolean takeCard(Card card) {
      if (numCards < MAX_CARDS) {
         Card newCard = new Card(card);
         myCards[numCards] = newCard;
         this.numCards++;
         return true;
      }
      return false;
   }

   /**
    * Removes and returns the card in the top occupied position of the array.
    *
    * @return the card that was removed from the top of the array, or Card with error flag if nocards to play.
    */
   public Card playCard() {
      if (numCards > 0) {
         numCards--;
         return new Card(myCards[numCards]);
      }
      // No cards to play, return a card with an error flag
      return new Card('1', Card.Suit.CLUBS);
   }

   /**
    * Play a card from a certain position in hand.
    *
    * @param cardIndex Position of Card to play.
    * @return Card to Play.
    */
   public Card playCard(int cardIndex) {
      if ( numCards == 0 ) {// error
         //Creates a card that does not work
         return new Card('1', Card.Suit.SPADES);
      }

      //Decreases numCards.
      Card card = myCards[cardIndex];

      numCards--;
      for(int i = cardIndex; i < numCards; i++) {
         myCards[i] = myCards[i+1];
      }

      myCards[numCards] = null;

      return card;
   }

   /**
    * Diplay the entire hand.
    *
    * @return Hand data as a String.
    */
   public String toString() {
      String result = "Hand = ( ";
      for (int i = 0; i < numCards; i++) {
         result = result + myCards[i].toString() + ", ";
      }
      // Remove last trailing comma and add closing parenthesis
      if (numCards > 0) result = result.substring(0, result.length() - 2);
      return result + " )";
   }

   /**
    * Accessor for the current number of Cards held in the Hand.
    *
    * @return the current number of Cards held in the Hand.
    */
   public int getNumCards() {
      return numCards;
   }

   /**
    * Access individual card.
    *
    * @param k the position in the array to pull a Card from.
    * @return Card at k position in hand or Card with error flag if k is bad.
    */
   public Card inspectCard(int k) {
      if (0 <= k && k < numCards) {
         return new Card(myCards[k]);
      }
      // Return a card with an error flag
      return new Card('1', Card.Suit.CLUBS);
   }

   /**
    * Sort Cards in hand.
    */
   public void sort() {
      if (numCards > 1) { // if 1 or less we don't need to sort
         Card.arraySort(myCards, numCards);
      }
   }
}
