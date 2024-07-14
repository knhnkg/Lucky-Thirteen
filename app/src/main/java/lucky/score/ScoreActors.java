package lucky.score;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameCard;
import lucky.gameobjects.Rank;
import lucky.players.Player;

/**
 * Handles the score calculation at the end of the game by checking the number of players
 * that achieved thirteen and determining the score to allocate to each player.
 * This class is implemented as a singleton.
 */
public class ScoreActors {
    private static final int THIRTEEN_GOAL = 13;
    private static ScoreActors scoreActors;
    private ScoreActors() {}
    /**
     * Provides the singleton instance of the ScoreActors by checking its existence.
     * @Return A singleton instance of ScoreActors.
     */
    public static ScoreActors getInstance() {
        if (scoreActors == null) {
            scoreActors = new ScoreActors();
        }
        return scoreActors;
    }

    /**
     * Checks whether a player has achieved thirteen at the end of the game.
     * Allocate a score for each player based on the number of players that achieved thirteen.
     *
     * @param players The list of player participating in the game.
     * @param playingArea The cards to be considered for score calculation.
     * @param scores The list to keep track of players' scores.
     */
    public void calculateScoreEndOfGame(Hand playingArea, Player[] players, int[] scores) {
        List<Boolean> isThirteenChecks = Arrays.asList(false, false, false, false);

        // Check if any player can sum to thirteen
        for (int i = 0; i < players.length; i++) {
            isThirteenChecks.set(i, isThirteen(playingArea, players[i].getHand()));
        }

        // Keep track of players who can sum to thirteen
        List<Integer> indexesWithThirteen = new ArrayList<>();
        for (int i = 0; i < isThirteenChecks.size(); i++) {
            if (isThirteenChecks.get(i)) {
                indexesWithThirteen.add(i);
            }
        }

        // Count how many players can sum to thirteen
        long countTrue = indexesWithThirteen.size();
        Arrays.fill(scores, 0);

        // CASE 1: If there is only one player who achieves thirteen, they win the game
        if (countTrue == 1) {
            int winnerIndex = indexesWithThirteen.get(0);
            scores[winnerIndex] = 100;
        }
        // CASE 3: If more than one player has the sum of thirteen, players using scoring rule to win
        else if (countTrue > 1) {
            for (Integer thirteenIndex : indexesWithThirteen) {
                scores[thirteenIndex] = ScoreStrategyFactory.getInstance().getCompositeMaxScoreStrategy().
                                                      calculateScore(players[thirteenIndex], playingArea);
            }
        // CASE 2: If no player has sum of thirteen, players calculate score with 2 private cards
        } else {
            for (int i = 0; i < scores.length; i++) {
                GameCard privateCard1 = (GameCard) players[i].getHand().getCardList().get(0);
                GameCard privateCard2 = (GameCard) players[i].getHand().getCardList().get(1);

                scores[i] = privateCard1.getScorePrivateCard() +
                        privateCard2.getScorePrivateCard();
            }
        }
    }

    /**
     * Determines whether a player can achieve thirteen by checking all possible card combinations.
     *
     * @param playingArea The public cards used in the game.
     * @param hand The private cards held by the player.
     * @return A boolean value of true if thirteen is achievable or false if not.
     */
    public boolean isThirteen(Hand playingArea, Hand hand) {
        List<Card> privateCards = hand.getCardList();
        List<Card> publicCards = playingArea.getCardList();

        boolean isThirteenPrivate = isThirteenCards(privateCards.get(0), privateCards.get(1), null, null);
        boolean isThirteenAll = isThirteenCards(privateCards.get(0), privateCards.get(1), publicCards.get(0),
                                                                                         publicCards.get(1));
        boolean isThirteenMixed = isThirteenMixedCards(privateCards, publicCards);

        return (isThirteenMixed || isThirteenPrivate) || isThirteenAll;
    }

    /**
     * Determines whether a player can achieve thirteen by checking all possible card combinations.
     *
     * @param privateCards The public cards to be used to check for combination(s) that create thirteen.
     * @param publicCards The private cards to be used to check for combination(s) that create thirteen.
     * @return A boolean value of true if thirteen is achievable or false if not.
     */
    public boolean isThirteenMixedCards(List<Card> privateCards, List<Card> publicCards) {
        for (Card privateCard : privateCards) {
            for (Card publicCard : publicCards) {
                if (isThirteenCards(privateCard, publicCard, null, null)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retrieve rank values for each card and use them to check if any sum values create thirteen.
     *
     * @param card1 The card to consider for combination(s) that create thirteen.
     * @param card2 The card to consider for combination(s) that create thirteen.
     * @param card3 The card to consider for combination(s) that create thirteen.
     * @param card4 The card to consider for combination(s) that create thirteen.
     * @return A boolean value of true if thirteen is achievable or false if not.
     */
    public boolean isThirteenCards(Card card1, Card card2, Card card3, Card card4) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();

        Rank rank3;
        Rank rank4;
        if (card3 != null && card4 != null) {
            rank3 = (Rank) card3.getRank();
            rank4 = (Rank) card4.getRank();
            return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues(),
                                               rank3.getPossibleSumValues(), rank4.getPossibleSumValues());
        }

        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues(), null, null);
    }

    /**
     * Check if any combination of possible sum values of the input cards can create thirteen.
     *
     * @param possibleValues1 A list of possible sum values for a card used for score calculation.
     * @param possibleValues2 A list of possible sum values for a card used for score calculation.
     * @param possibleValues3 A list of possible sum values for a card used for score calculation.
     * @param possibleValues4 A list of possible sum values for a card used for score calculation.
     * @return A boolean value of true if thirteen is achievable or false if not.
     */
    public boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2, int[] possibleValues3,
                                                                                           int[] possibleValues4) {
        // Initialise a hashset to store sums from possibleValues1 and possibleValues2
        Set<Integer> sums = new HashSet<>();

        // Calculate all possible sums from possibleValues 1 and possibleValues2
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                sums.add(value1 + value2);
            }
        }

        // If sums from two cards needs to be considered, check if any combination
        // can create thirteen and return boolean accordingly
        if (possibleValues3 == null && possibleValues4 == null) {
            return sums.contains(THIRTEEN_GOAL);
        }

        // If sums from four cards are considered, continue calculation using
        // possibleValues3, possibleValues4 and the values stored in the set
        else if (possibleValues3 != null && possibleValues4 != null) {
            for (int value3 : possibleValues3) {
                for (int value4 : possibleValues4) {
                    // Check if there are any sum from values in possibleValues3 and possibleValues4
                    // that will make up a complementary value that will sum up to thirteen when
                    // added to any of the sums stored in the set
                    if (sums.contains(THIRTEEN_GOAL - (value3 + value4))) {
                        // Stop loop iteration and return true if combination has been found
                        return true;
                    }
                }
            }
        }

        // Return false if no combination add up to thirteen
        return false;
    }
}

