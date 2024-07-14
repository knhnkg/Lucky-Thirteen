package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameHand;

import java.util.Random;

import static ch.aplu.util.BaseTimer.delay;

/**
 * Represents a human player in the game.
 * This class extends the Player class.
 */
public class HumanPlayer extends Player {
    public HumanPlayer(int thinkingTime) {
        super(thinkingTime);
    }

    /**
     * Method that selects a card to discard.
     * Not implemented by the human player, as this behaviour is handled by the UI.
     */
    @Override
    public Card getSelectedCard(Hand pack, Random random) {
        return null;
    }
}
