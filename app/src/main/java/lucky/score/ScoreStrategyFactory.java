package lucky.score;

/**
 * Handles the creation of score calculation option.
 * This class is implemented with Factory design pattern.
 * This class is implemented as a singleton.
 */
public class ScoreStrategyFactory {
    private static ScoreStrategyFactory scoreStrategyFactory;
    private CompositeMaxScoreStrategy compositeMaxScoreStrategy;

    private ScoreStrategyFactory() {
        compositeMaxScoreStrategy = new CompositeMaxScoreStrategy();
        compositeMaxScoreStrategy.add(new OptionOneScoreStrategy());
        compositeMaxScoreStrategy.add(new OptionTwoScoreStrategy());
        compositeMaxScoreStrategy.add(new OptionThreeScoreStrategy());
    }

    /**
     * Provides the singleton instance of the ScoreStrategyFactory by checking its existence.
     *
     * @return A singleton instance of ScoreStrategyFactory.
     */
    public static ScoreStrategyFactory getInstance() {
        if (scoreStrategyFactory == null) {
            scoreStrategyFactory = new ScoreStrategyFactory();
        }
        return scoreStrategyFactory;
    }

    /**
     * Retrieves the instance of CompositeMaxScoreStrategy.
     * @return The instance of CompositeMaxScoreStrategy.
     */
    public CompositeMaxScoreStrategy getCompositeMaxScoreStrategy() {
        return compositeMaxScoreStrategy;
    }
}

