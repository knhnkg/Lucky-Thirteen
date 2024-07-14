package lucky.score;

import ch.aplu.jcardgame.Hand;
import lucky.players.Player;

/**
 * Determines the calculation option that provides the maximum score possible.
 * This class extends the CompositeScoreStrategy class.
 * This class constitutes a part of composite design pattern.
 */
public class CompositeMaxScoreStrategy extends CompositeScoreStrategy{

    /**
     * Compares the score calculated using various options and keeps the maximum score possible.
     *
     * @param player The player that will receive the score.
     * @param playingArea The cards to be considered for score calculation.
     * @return An integer of maximum score possible.
     */
    public int calculateScore(Player player, Hand playingArea) {
        int maxScore = 0;
        for (ScoreStrategy strategy : this.scoringStrategies) {
            int score = strategy.calculateScore(player, playingArea);
            if (score > maxScore) {
                maxScore = score;
            }
        }
        return maxScore;
    }
}
