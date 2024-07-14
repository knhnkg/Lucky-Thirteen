package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameHand;

import java.util.Random;

import static ch.aplu.util.BaseTimer.delay;

/**
 * Represents a basic player in the game.
 * This class extends the Player class.
 */
public class BasicPlayer extends Player {
    public BasicPlayer(int thinkingTime) {
        super(thinkingTime);
    }

    /**
     * Method that selects a card to discard.
     * A basic player discards a card with the lowest value.
     *
     * @param pack The deck of cards to deal from.
     * @param random The random seed from LuckyThirdteen.
     * @return The card with lowest value to discard.
     */
    @Override
    public Card getSelectedCard(Hand pack, Random random) {
        ((GameHand)this.hand).dealACardToHand(pack, random); // Get a card from the deck

        delay(thinkingTime);

        // Logic to discard card with lowest value
        Card lowestCard = null;
        int lowestVal = Integer.MAX_VALUE;

        // Iterate through each card and find the card that calculates to the lowest value
        for (Card card : hand.getCardList()) {
            int cardVal = card.getValue();
            if (cardVal < lowestVal) {
                lowestVal = cardVal;
                lowestCard = card;
            }
        }
        return lowestCard;
    }
}
