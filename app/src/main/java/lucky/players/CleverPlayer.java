package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameHand;
import lucky.score.ScoreActors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ch.aplu.util.BaseTimer.delay;

/**
 * Represents a clever player in the game.
 * This class extends the Player class.
 */
public class CleverPlayer extends Player {
    private List<Card> discardedCards; // List of cards that has been discarded by every player.
    public CleverPlayer(int thinkingTime) {
        super(thinkingTime);
        this.discardedCards = new ArrayList<>();
    }

    /**
     * Adds a discarded card to the list.
     *
     * @param card The discarded card to add.
     */
    public void addDiscardedCard(Card card) {
        discardedCards.add(card);
    }

    /**
     * Method that selects a card to discard.
     * A clever player discards a card that has the least likely chance of summing to thirteen.
     *
     * @param pack The deck of cards to deal from.
     * @param random The random seed from LuckyThirdteen.
     * @return The selected card to discard.
     */
    @Override
    public Card getSelectedCard(Hand pack, Random random) {
        ((GameHand)this.hand).dealACardToHand(pack, random); // Get a card from the deck

        delay(thinkingTime);

        // Strategic logic to discard card from hand
        int numCards = this.hand.getCardList().size();

        // First, check if player can make thirteen with any 2 private cards
        for (int i=0; i<numCards-1; i++) {
            for (int j=i+1; j<numCards; j++) {
                if (ScoreActors.getInstance().isThirteenCards(this.hand.get(i), this.hand.get(j), null, null)) {
                    // If 13 can be made, then discard the extra card
                    for (Card card : this.hand.getCardList()) {
                        if (card != this.hand.get(i) && card != this.hand.get(j)) {
                            return card;
                        }
                    }
                }
            }
        }
        // If 13 cannot be made with the private cards
        int[] thirteenCount = {0, 0, 0}; // Keeps track of how many times thirteen can be made with discarded cards

        for (Card card : discardedCards) {
            // Sum each discarded card with a private card
            for (int i = 0; i < numCards; i++) {
                if (ScoreActors.getInstance().isThirteenCards(this.hand.get(i), card, null, null)) {
                    thirteenCount[i]++;
                }
            }
        }
        // Discard the card that sums to thirteen the most
        int discardCardIndex = getMax(thirteenCount);

        return this.hand.get(discardCardIndex);
    }

    /**
     * Finds the index of the maximum count in the array.
     * @param array The array to search.
     * @return The index of max value.
     */
    public int getMax(int[] array) {
        int maxCard = 0;
        int maxCount = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxCount) {
                maxCount = array[i];
                maxCard = i;
            }
        }
        return maxCard;
    }
}