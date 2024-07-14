package lucky.score;

import ch.aplu.jcardgame.Hand;
import lucky.players.Player;

import java.util.ArrayList;

/**
 * Declares the methods required for score calculation.
 * Implements the score calculation method of ScoreStrategy.
 * This class constitutes a part of  composite design pattern.
 */
public abstract class CompositeScoreStrategy implements ScoreStrategy {
    protected ArrayList<ScoreStrategy> scoringStrategies = new ArrayList<>();

    /**
     * Appends score calculation strategies into the arraylist.
     *
     * @param strategy The strategy to be added to the arraylist.
     */
    public void add(ScoreStrategy strategy){
        scoringStrategies.add(strategy);
    }

    /**
     * Declaration for method to calculate score.
     *
     * @param player The player that will receive the score.
     * @param playingArea The cards to be considered for score calculation.
     * @Return An integer of the most desirable score based on the calculation logic.
     */
    public abstract int calculateScore(Player player, Hand playingArea);
}
