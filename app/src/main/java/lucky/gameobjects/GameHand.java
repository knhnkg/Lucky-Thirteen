package lucky.gameobjects;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.HandLayout;
import ch.aplu.jcardgame.Hand;


import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a player's hand that holds the cards.
 * This class extends the Hand class.
 */
public class GameHand extends Hand {
    public GameHand(Deck deck) {
        this(deck, null);
    }
    public GameHand(Deck deck, HandLayout handLayout) {
        super(deck, handLayout);
    }

    /**
     * Returns a random card from the hand.
     *
     * @param random The random seed from LuckyThirdteen.
     * @return A random card.
     */
    public Card randomCard(Random random) {
        int x = random.nextInt(this.getCardList().size());
        return this.getCardList().get(x);
    }

    /**
     * Retrieves a card from hand, given the card's name.
     *
     * @param cardName String version of specified card.
     * @return The card with the specified name. Null if not found.
     */
    public Card getCardFromList(String cardName) {
        Rank cardRank = Rank.getRankFromString(cardName);
        Suit cardSuit = Suit.getSuitFromString(cardName);

        for (Card card: this.getCardList()) {
            if (card.getSuit() == cardSuit
                    && card.getRank() == cardRank) {
                return card;
            }
        }
        return null;
    }

    /**
     * Deals a card from the given pack to hand.
     *
     * @param pack The pack to deal the card from.
     * @param random The random seed from LuckyThirdteen.
     */
    public void dealACardToHand(Hand pack, Random random) {
        if (pack.isEmpty()) return;
        Card dealt = ((GameHand)pack).randomCard(random);
        dealt.removeFromHand(false);
        this.insert(dealt, true);
    }
}