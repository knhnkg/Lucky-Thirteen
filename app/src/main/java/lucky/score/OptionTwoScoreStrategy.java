package lucky.score;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameCard;
import lucky.players.Player;

import java.util.ArrayList;

/**
 * Contains the logic for score calculation Option 2, which uses one private card and one public card.
 * Implements the score calculation method of ScoreStrategy.
 * This class constitutes a part of strategy pattern.
 */
public class OptionTwoScoreStrategy implements ScoreStrategy {
    /**
     * Calculates compares to find the maximum score attainable using one private card and one public card.
     *
     * @param player The player that will receive the score.
     * @param playingArea The cards to be considered for score calculation.
     * @return An integer of maximum score determined.
     */
    @Override
    public int calculateScore(Player player, Hand playingArea) {
        ArrayList<Card> privateCards = player.getHand().getCardList();
        ArrayList<Card> publicCards = playingArea.getCardList();

        GameCard privateCard1 = (GameCard) privateCards.get(0);
        GameCard privateCard2 = (GameCard) privateCards.get(1);
        GameCard publicCard1 = (GameCard) publicCards.get(0);
        GameCard publicCard2 = (GameCard) publicCards.get(1);

        // Try all possible combinations
        int maxScore = 0;
        if (ScoreActors.getInstance().isThirteenCards(privateCard1, publicCard1, null, null)) {
            int score = privateCard1.getScorePrivateCard() + publicCard1.getScorePublicCard();
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (ScoreActors.getInstance().isThirteenCards(privateCard1, publicCard2, null, null)) {
            int score = privateCard1.getScorePrivateCard() + publicCard2.getScorePublicCard();
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (ScoreActors.getInstance().isThirteenCards(privateCard2, publicCard1, null, null)) {
            int score = privateCard2.getScorePrivateCard() + publicCard1.getScorePublicCard();
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (ScoreActors.getInstance().isThirteenCards(privateCard2, publicCard2, null, null)) {
            int score = privateCard2.getScorePrivateCard() + publicCard2.getScorePublicCard();
            if (maxScore < score) {
                maxScore = score;
            }
        }

        return maxScore;
    }
}

