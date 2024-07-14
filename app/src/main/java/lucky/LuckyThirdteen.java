package lucky;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import lucky.gameobjects.GameDeck;
import lucky.gameobjects.GameHand;
import lucky.gameobjects.Rank;
import lucky.gameobjects.Suit;
import lucky.players.HumanPlayer;
import lucky.players.Player;
import lucky.score.ScoreActors;

/**
 * Main class for Lucky Thirteen game.
 * Handles game initialisation, gameplay logic and log results.
 * This class is implemented as a singleton.
 */
public class LuckyThirdteen extends CardGame {
    private static LuckyThirdteen luckyThirdteen; // The singleton instance

    // Game logic variables
    private Properties properties;
    private StringBuilder logResult;
    static public final int SEED = 30008;
    private static Random random = null;
    private int thinkingTime;
    private int delayTime;
    private boolean isAuto;

    // Game visual attributes
    private final Location[] HAND_LOCATIONS = new Location[]{
        new Location(350, 625),
                new Location(75, 350),
                new Location(350, 75),
                new Location(625, 350)
    };
    private final Location[] SCORE_LOCATIONS = new Location[]{
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            // new Location(650, 575)
            new Location(575, 575)
    };
    private final Location TEXT_LOCATION = new Location(350, 450);
    private final Location TRICK_LOCATION = new Location(350, 350);
    private final Font BIG_FONT = new Font("Arial", Font.BOLD, 36);

    // Object related attributes
    private final int NB_PLAYERS = 4;
    private Player[] players;
    private int[] scores;
    private Actor[] scoreActors = {null, null, null, null};
    private Hand playingArea;

    /**
     * Singleton implementation but set to public due to direct instantiation by Test file
     * @param properties from the driver file
     */
    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "1"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "1"));
    }
    public static LuckyThirdteen getInstance(Properties properties) {
        if (luckyThirdteen == null) {
            luckyThirdteen = new LuckyThirdteen (properties);
        }
        return luckyThirdteen;
    }

    // Initializer methods
    public void initScore(int[] scores, int nbPlayers) {
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] = 0;
            String text = "[" + scores[i] + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, BIG_FONT);
            addActor(scoreActors[i], SCORE_LOCATIONS[i]);
        }
    }
    public void initAttributes() {
        this.random = new Random(SEED);
        this.logResult = new StringBuilder();
        this.players = new Player[NB_PLAYERS];
        this.playingArea = new GameHand(GameDeck.getInstance("cover"));
        this.scores = new int[NB_PLAYERS];
    }
    public void initGame() {
        initAttributes();

        // Calls the init of score actors and game
        initScore(scores, NB_PLAYERS);
        for (int i = 0; i < NB_PLAYERS; i++) updateScore(scores, i);

        GameController.getInstance(NB_PLAYERS, thinkingTime, delayTime).initGame(NB_PLAYERS, players, playingArea,
                                                                                   random, properties, delayTime);

        // Draw Game
        int trickWidth = 40;
        playingArea.setView(this, new RowLayout(TRICK_LOCATION, (playingArea.getNumberOfCards() + 2)
                                                                                                        * trickWidth));
        playingArea.draw();
        RowLayout[] layouts = new RowLayout[NB_PLAYERS];
        int handWidth = 400;
        for (int i = 0; i < NB_PLAYERS; i++) {
            layouts[i] = new RowLayout(HAND_LOCATIONS[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            players[i].getHand().setView(this, layouts[i]);
            players[i].getHand().setTargetArea(new TargetArea(TRICK_LOCATION));
            players[i].getHand().draw();
        }
    }

    // Visual methods
    public void setStatus(String string) {
        setStatusText(string);
    }

    // Score-related methods
    public void updateScore(int[] scores, int player) {
        removeActor(scoreActors[player]);
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + displayScore + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, BIG_FONT);
        addActor(scoreActors[player], SCORE_LOCATIONS[player]);
    }

    // Logging methods for testing purposes
    public void addCardPlayedToLog(Player player) {
        Hand hand = player.getHand();

        if (hand.getNumberOfCards() < 2) {
            return;
        }
        logResult.append("P").append(player).append("-");

        for (int i = 0; i < hand.getNumberOfCards(); i++) {
            Rank cardRank = (Rank) hand.get(i).getRank();
            Suit cardSuit = (Suit) hand.get(i).getSuit();
            logResult.append(cardRank.getRankCardLog()).append(cardSuit.getSuitShortHand());
            if (i < hand.getNumberOfCards() - 1) {
                logResult.append("-");
            }
        }
        logResult.append(",");
    }
    public void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round").append(roundNumber).append(":");
    }
    public void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int score : scores) {
            logResult.append(score).append(",");
        }
        logResult.append("\n");
    }
    public void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int score : scores) {
            logResult.append(score).append(",");
        }
        logResult.append("\n");
        logResult.append("Winners:").append(winners.stream().map(String::valueOf).collect(Collectors.
                                                                             joining(", ")));
    }

    /**
     * Plays a round of the game.
     *
     * @param roundNumber The round number.
     */
    public void playRound(int roundNumber) {
        int nextPlayer = 0;
        boolean finishedAuto;

        addRoundInfoToLog(roundNumber);

        for (int i = 0; i < NB_PLAYERS; i++) {
            // Calls playRound in lucky.GameController
            finishedAuto = GameController.getInstance(NB_PLAYERS, thinkingTime, delayTime).playRound1(players,
                                                                                  nextPlayer, random, isAuto);
            if (!isAuto || finishedAuto) {
                if (players[nextPlayer] instanceof HumanPlayer) {
                    setStatus("players.Player 0 is playing. Please double click on a card to discard");
                    GameController.getInstance(NB_PLAYERS, thinkingTime, delayTime).playRound2(players, nextPlayer,
                                                                                                           random);
                } else {
                    setStatus("players.Player " + nextPlayer + " thinking...");
                    GameController.getInstance(NB_PLAYERS, thinkingTime, delayTime).playRound2(players, nextPlayer,
                                                                                                           random);
                }
            }

            addCardPlayedToLog(players[nextPlayer]);
            nextPlayer = (nextPlayer + 1) % NB_PLAYERS;
        }

        // Runs the post-round processing things
        addEndOfRoundToLog();
    }

    /**
     * Plays the game for a specified number of rounds.
     *
     * @param nbRounds The number of rounds.
     */
    public void playGame(int nbRounds) {
        // Initialise game
        initGame();
        // Calls playRound method
        for (int i = 1; i < nbRounds + 1; i ++) {
            playRound(i);
        }

        for (int i = 0; i < NB_PLAYERS; i++) {
            System.out.println("Player " + i + ": " + players[i].getHand().getCardList());
        }
        // Calculate score at end of game
        ScoreActors.getInstance().calculateScoreEndOfGame(playingArea, players, scores);

        // Post-Game processing
        for (int i = 0; i < NB_PLAYERS; i++) updateScore(scores, i);
        int maxScore = 0;
        for (int i = 0; i < NB_PLAYERS; i++) if (scores[i] > maxScore) maxScore = scores[i];
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < NB_PLAYERS; i++) if (scores[i] == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    winners.stream().map(String::valueOf).collect(Collectors.joining(", "));
        }
        addActor(new Actor("sprites/gameover.gif"), TEXT_LOCATION);
        setStatusText(winText);
        refresh();
        addEndOfGameToLog(winners);
    }

    /**
     * Runs the game by setting up game window and starting the game.
     *
     * @return The game log.
     */
    public String runApp() {
        String version = "1.0";
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        // Facade GoF applied to make things simple
        playGame(4); // nbRounds always 4 for now

        return logResult.toString();
    }
}