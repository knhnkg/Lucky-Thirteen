package lucky.gameobjects;

import ch.aplu.jcardgame.*;

/**
 * Represents a card used in the game.
 * This class extends the Card class.
 */
public class GameCard extends Card {
    public GameCard(Deck deck, Suit suit, Rank rank) {
        super(deck, suit, rank);  // Calls the constructor of ch.aplu.jcardgame.Card
    }

    /**
     * Calculates the score of the sum of the two private cards according to the game rules.
     *
     * @return The calculated score.
     */
    public int getScorePrivateCard() {
        Rank rank = (Rank) this.getRank();
        Suit suit = (Suit) this.getSuit();

        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }

    /**
     * Calculates the score of the sum of the two public cards according to the game rules.
     *
     * @return The calculated score.
     */
    public int getScorePublicCard() {
        Rank rank = (Rank) this.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }

    /**
     * Calculates the total value of a card based on its Rank and Suit.
     *
     * @return The calculated value.
     */
    @Override
    public int getValue() {
        Rank rank = (Rank) this.getRank();
        Suit suit = (Suit) this.getSuit();
        return rank.getRankCardValue() * suit.getMultiplicationFactor();
    }
}