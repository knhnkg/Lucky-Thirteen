package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameHand;

import java.util.Random;

/**
 * Abstract class representing player in the game.
 * Each player maintains their own set of private cards.
 */
public abstract class Player {
    protected Hand hand; // The set of private cards held by each player.
    protected int thinkingTime;
    public Player(int thinkingTime) {
        this.thinkingTime = thinkingTime;
    }

    /**
     * Gets the hand of cards held by player.
     *
     * @return The player's hand.
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Sets the hand of cards held by player.
     *
     * @param hand The player's hand.
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * Abstract method that selects a card to discard for each player turn.
     *
     * @param pack The deck of cards to deal from.
     * @param random The random seed from LuckyThirdteen.
     * @return The selected card to be discarded.
     */
    public abstract Card getSelectedCard(Hand pack, Random random);

    /**
     * Applies automatic movement based on the pre-defined sequence of cards in game properties.
     *
     * @param pack The deck of cards to deal from.
     * @param nextMovement The string of pre-defined movements.
     * @return The selected card to be discarded, or null if no valid card is found.
     */
    public Card applyAutoMovement(Hand pack, String nextMovement) {
        if (pack.isEmpty()) return null;

        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        Card dealt = ((GameHand)pack).getCardFromList(cardDealtString);

        if (dealt != null) {
            dealt.removeFromHand(false);
            this.hand.insert(dealt, true);
        } else {
            System.out.println("cannot draw card: " + cardDealtString + " - hand: " + this.hand);
        }

        if (cardStrings.length > 1) {
            String cardDiscardString = cardStrings[1];
            return ((GameHand)this.hand).getCardFromList(cardDiscardString);
        } else {
            return null;
        }
    }
}