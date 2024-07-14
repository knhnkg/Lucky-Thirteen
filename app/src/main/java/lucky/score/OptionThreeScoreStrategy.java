package lucky.score;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameCard;
import lucky.players.Player;

import java.util.ArrayList;

/**
 * Contains the logic for score calculation Option 3, which uses two privates card and two public cards.
 * Implements the score calculation method of ScoreStrategy.
 * This class constitutes a part of strategy pattern.
 */
public class OptionThreeScoreStrategy implements ScoreStrategy {
    /**
     * Calculates the score attainable using two privates card and two public cards.
     *
     * @param player The player that will receive the score.
     * @param playingArea The cards to be considered for score calculation.
     * @return An integer of score calculated.
     */
    @Override
    public int calculateScore(Player player, Hand playingArea) {
        ArrayList<Card> privateCards = player.getHand().getCardList();
        ArrayList<Card> publicCards = playingArea.getCardList();

        GameCard privateCard1 = (GameCard) privateCards.get(0);
        GameCard privateCard2 = (GameCard) privateCards.get(1);
        GameCard publicCard1 = (GameCard) publicCards.get(0);
        GameCard publicCard2 = (GameCard) publicCards.get(1);

        int score = 0;
        if (ScoreActors.getInstance().isThirteenCards(privateCard1, privateCard2, publicCard1, publicCard2)) {
            score = privateCard1.getScorePrivateCard() + privateCard2.getScorePrivateCard() +
                    publicCard1.getScorePublicCard() + publicCard2.getScorePublicCard();
        }
        return score;
    }
}
