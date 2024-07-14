package lucky.players;

/**
 * Factory class for creating different types of players.
 * This class is implemented as a singleton.
 */
public class PlayerFactory {
    private static PlayerFactory playerFactory; // The singleton instance
    private PlayerFactory() {}

    /**
     * Method to get the singleton instance.
     *
     * @return Instance of PlayerFactory.
     */
    public static PlayerFactory getInstance() {
        if (playerFactory == null) {
            playerFactory = new PlayerFactory();
        }
        return playerFactory;
    }

    /**
     * Creates a player of the specified type.
     *
     * @param playerType Type of player to create (human, random, basic, clever).
     * @param thinkingTime Delay for each robot's turn.
     * @return The created player instance.
     */
    public Player createPlayer(String playerType, int thinkingTime) {
        switch (playerType) {
            case "human":
                return new HumanPlayer(thinkingTime);
            case "random":
                return new RandomPlayer(thinkingTime);
            case "basic":
                return new BasicPlayer(thinkingTime);
            case "clever":
                return new CleverPlayer(thinkingTime);
            default:
                throw new IllegalArgumentException("Unknown player type");
        }
    }
}
