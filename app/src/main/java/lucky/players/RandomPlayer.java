package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameHand;

import java.util.Random;

import static ch.aplu.util.BaseTimer.delay;

/**
 * Represents a random player in the game.
 * This class extends the Player class.
 */
public class RandomPlayer extends Player {
    public RandomPlayer(int thinkingTime) {
        super(thinkingTime);
    }

    /**
     * Method that selects a card to discard.
     * A random player randomly selects a card to discard.
     *
     * @param pack The deck of cards to deal from.
     * @param random The random seed from LuckyThirdteen.
     * @return A random card to discard.
     */
    @Override
    public Card getSelectedCard(Hand pack, Random random) {
        ((GameHand)this.hand).dealACardToHand(pack, random); // Get a card from the deck

        delay(thinkingTime);

        // Get a random index from list of cards to discard
        int x = random.nextInt(this.hand.getCardList().size());
        return this.hand.getCardList().get(x);
    }
}
