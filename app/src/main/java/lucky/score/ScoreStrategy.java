package lucky.score;

import ch.aplu.jcardgame.Hand;
import lucky.players.Player;

/**
 * Declares the method for score calculation.
 * This class constitutes a part of strategy pattern.
 */
public interface ScoreStrategy {
    /**
     * Declaration for method to calculate score.
     *
     * @param player The player that will receive the score.
     * @param playingArea The cards to be considered for score calculation.
     * @return An integer of the most desirable score based on the calculation logic.
     */
    int calculateScore(Player player, Hand playingArea);
}
