package lucky.gameobjects;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a card deck in the game.
 * This class extends the Deck class.
 * This class is implemented as a singleton.
 */
public class GameDeck extends Deck {
    // Singleton strategy
    private static GameDeck gameDeck;
    private int nbCards;

    private GameDeck(String cover) {
        super(Suit.getSuitValues(), Rank.getRankValues(), "cover");
        this.nbCards = this.getNumberOfCards();
    }

    /**
     * Gets the instance of GameDeck, ensuring it is a singleton.
     *
     * @param cover custom card cover asset
     * @return The instance of GameDeck.
     */
    public static GameDeck getInstance(String cover) {
        if (gameDeck == null) {
            gameDeck = new GameDeck(cover);
        }
        return gameDeck;
    }

    /**
     * Converts a Deck object into a Hand object
     * Used in getting the dealt out cards to player's hand.
     *
     * @return Hand type object
     */
    @Override
    public Hand toHand() {
        return this.dealingOut(0, 0, false)[0];
    }

    /**
     * Deals out cards from the deck to player.
     *
     * @param nbPlayers number of players in the game
     * @param nbCardsPerPlayer number of cards per player
     * @param shuffle shuffle deck before distribution
     * @return An array of hands with the dealt cards.
     */
    @Override
    public Hand[] dealingOut(int nbPlayers, int nbCardsPerPlayer, boolean shuffle) {
        if (nbPlayers * nbCardsPerPlayer > this.nbCards) {
            fail("Error in Deck.dealing out.\n" + this.nbCards + " cards in deck. Not enough for" + "\n" +
                      nbPlayers + (nbPlayers > 1 ? " players with " : "player with ") + nbCardsPerPlayer +
                                      (nbCardsPerPlayer > 1 ? " cards per player." : "card per player.") +
                                                                         "\nApplication will terminate.");
        }

        ArrayList<Card> cards = new ArrayList<>();
        Suit[] suits = (Suit[]) this.getSuits();
        Rank[] ranks = (Rank[]) this.getRanks();

        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                Card card = new GameCard(this, suit, rank);
                cards.add(card);
            }
        }

        // Shuffle and dealing out to blocks not actually used
        if (shuffle) {
            Collections.shuffle(cards);
        }

        Hand[] hands = new Hand[nbPlayers + 1];

        for(int p = 0; p < nbPlayers; ++p) {
            hands[p] = new GameHand(this, null);

            for(int k = 0; k < nbCardsPerPlayer; ++k) {
                hands[p].insert(cards.get(p * nbCardsPerPlayer + k), false);
            }
        }

        hands[nbPlayers] = new GameHand(this, null);

        for(int p = nbPlayers * nbCardsPerPlayer; p < this.nbCards; ++p) {
            hands[nbPlayers].insert(cards.get(p), false);
        }

        return hands;
    }
}
