package lucky;

import ch.aplu.jcardgame.*;

import java.util.*;

import lucky.gameobjects.GameDeck;
import lucky.gameobjects.GameHand;
import lucky.players.CleverPlayer;
import lucky.players.HumanPlayer;
import lucky.players.Player;
import lucky.players.PlayerFactory;

import static ch.aplu.util.BaseTimer.delay;

/**
 * Represents a class that manages the game play logic and flow.
 * This class constitutes a part of facade design pattern.
 * This class is implemented as a singleton.
 */
public class GameController {
    // Singleton strategy
    private static GameController gameController;
    // Game logic attributes
    private int delayTime; // value given by lucky.LuckyThirdteen
    private int thinkingTime;
    private final int NB_START_CARDS = 2; // Non-def value should be given by lucky.LuckyThirdteen
    private final int NB_FACE_UP_CARDS = 2; // Non-def value should be given by lucky.LuckyThirdteen

    // Object-related attributes
    private int nbPlayers; // value given by lucky.LuckyThirdteen
    private List<List<String>> playerAutoMovements;
    private int[] autoIndexHands;
    private List<Card> cardsPlayed;
    private Hand pack;
    private Card selected;

    private GameController(int nbPlayers, int thinkingTime, int delayTime) {
        this.nbPlayers = nbPlayers;
        this.thinkingTime = thinkingTime;
        this.delayTime = delayTime;
    }
    public static GameController getInstance(int nbPlayers, int thinkingTime, int delayTime) {
        if (gameController == null) {
            gameController = new GameController (nbPlayers, thinkingTime, delayTime);
        }
        return gameController;
    }

    // Init methods
    public void setupPlayerAutoMovements(Properties properties) {
        String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
        String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
        String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
        String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");

        String[] playerMovements = new String[] {"", "", "", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }
        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }
        if (player2AutoMovement != null) {
            playerMovements[2] = player2AutoMovement;
        }
        if (player3AutoMovement != null) {
            playerMovements[3] = player3AutoMovement;
        }
        for (String movementString : playerMovements) {
            if (movementString.isEmpty()) {
                playerAutoMovements.add(new ArrayList<>());
                continue;
            }
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
    }

    /**
     * Deals cards to players and playingArea (public cards).
     *
     * @param players Array of players
     * @param playingArea The public card hand.
     * @param nbCardsPerPlayer Number of cards per player.
     * @param nbSharedCards Number of shared public cards.
     * @param random The random seed from LuckyThirdteen.
     * @param properties Game properties.
     */
    public void dealingOut(Player[] players, Hand playingArea, int nbCardsPerPlayer, int nbSharedCards, Random random, Properties properties) {
        pack = GameDeck.getInstance("cover").toHand(false);

        String initialShareKey = "shared.initialcards";
        String initialShareValue = properties.getProperty(initialShareKey);
        if (initialShareValue != null) {
            String[] initialCards = initialShareValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = ((GameHand)pack).getCardFromList(initialCard);
                if (card != null) {
                    card.removeFromHand(true);
                    playingArea.insert(card, true);
                }
            }
        }
        int cardsToShare = nbSharedCards - playingArea.getNumberOfCards();

        for (int j = 0; j < cardsToShare; j++) {
            if (pack.isEmpty()) return;
            Card dealt = ((GameHand)pack).randomCard(random);
            dealt.removeFromHand(true);
            playingArea.insert(dealt, true);
        }

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard: initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = ((GameHand)pack).getCardFromList(initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    players[i].getHand().insert(card, false);
                }
            }
        }

        for (int i = 0; i < nbPlayers; i++) {
            int cardsToDealt = nbCardsPerPlayer - players[i].getHand().getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (pack.isEmpty()) return;
                Card dealt = ((GameHand)pack).randomCard(random);
                dealt.removeFromHand(false);
                players[i].getHand().insert(dealt, false);
            }
        }
    }

    /**
     * Initialises and sets up the game.
     *
     * @param nbPlayers Number of players.
     * @param players Array of players
     * @param playingArea The public card hand.
     * @param random The random seed from LuckyThirdteen.
     * @param properties Game properties.
     * @param delayTime Delay time in game per player.
     */
    public void initGame(int nbPlayers, Player[] players, Hand playingArea, Random random, Properties properties, int delayTime) {
        playerAutoMovements = new ArrayList<>();
        cardsPlayed = new ArrayList<>();
        setupPlayerAutoMovements(properties);

        this.nbPlayers = nbPlayers;
        this.autoIndexHands = new int [nbPlayers];
        this.delayTime = delayTime;
        this.pack = new GameHand(GameDeck.getInstance("cover"));

        // Use factory method to create each player type
        for (int i = 0; i < nbPlayers; i++) {
            String playerTypeKey = "players." + i;
            String playerType = properties.getProperty(playerTypeKey); // Gets the player type from properties file
            if (playerType != null) {
                players[i] = PlayerFactory.getInstance().createPlayer(playerType, thinkingTime);
                players[i].setHand(new GameHand(GameDeck.getInstance("cover")));
            }
        }
        dealingOut(players, playingArea, NB_START_CARDS, NB_FACE_UP_CARDS, random, properties);

        for (int i = 0; i < nbPlayers; i++) {
            players[i].getHand().sort(GameHand.SortType.SUITPRIORITY, false);
        }
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human players.Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                players[0].getHand().setTouchEnabled(false);
            }
        };
        players[0].getHand().addCardListener(cardListener);
    }

    /**
     * Plays a round for auto-mode of the game.
     *
     * @param players Array of players.
     * @param nextPlayer Index of the next player.
     * @param random The random seed from LuckyThirdteen.
     * @param isAuto Flag for auto-mode.
     * @return true if pre-defined moves are finished, false otherwise.
     */
    public boolean playRound1(Player[] players, int nextPlayer, Random random, boolean isAuto) {
        selected = null;
        boolean finishedAuto = false;

        if (isAuto) {
            int nextPlayerAutoIndex = autoIndexHands[nextPlayer];
            List<String> nextPlayerMovement = playerAutoMovements.get(nextPlayer);
            String nextMovement;

            if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
                nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
                nextPlayerAutoIndex++;

                autoIndexHands[nextPlayer] = nextPlayerAutoIndex;

                // Apply movement for player (removed the previous hand selection)
                selected = players[nextPlayer].applyAutoMovement(pack, nextMovement);
                delay(delayTime); // temporary static, but maybe can change?
                if (selected != null) {
                    selected.removeFromHand(true);
                } else {
                    // Players play game with their own logic if predefined moves run out
                    if (!(players[nextPlayer] instanceof HumanPlayer)) {
                        // Player selects card to discard internally
                        selected = players[nextPlayer].getSelectedCard(pack, random);
                        selected.removeFromHand(true);
                    }
                }
            } else {
                finishedAuto = true;
            }
        }
        delay(delayTime);
        return finishedAuto;
    }

    /**
     * Plays a round of the game, handling human and computer players.
     *
     * @param players Array of players.
     * @param nextPlayer Index of the next player.
     * @param random The random seed from LuckyThirdteen.
     */
    public void playRound2(Player[] players, int nextPlayer, Random random) {
        if (players[nextPlayer] instanceof HumanPlayer) {
            players[0].getHand().setTouchEnabled(true);
            selected = null;
            ((GameHand)players[0].getHand()).dealACardToHand(pack, random);
            while (null == selected) delay(delayTime);
            selected.removeFromHand(true);
        } else {
            // Player selects card to discard internally
            selected = players[nextPlayer].getSelectedCard(pack, random);
            selected.removeFromHand(true);
        }

        if (selected != null) {
            cardsPlayed.add(selected);
            selected.setVerso(false);  // In case it is upside down
            delay(delayTime);
        }
        // Update discarded cards list for CleverPlayer
        for (Player player : players) {
            if (player instanceof CleverPlayer) {
                ((CleverPlayer) player).addDiscardedCard(selected);
            }
        }
    }
}