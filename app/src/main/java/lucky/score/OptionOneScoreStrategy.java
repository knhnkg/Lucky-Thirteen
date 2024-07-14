package lucky.score;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.gameobjects.GameCard;
import lucky.players.Player;

import java.util.ArrayList;

/**
 * Contains the logic for score calculation Option 1, which uses two private cards.
 * Implements the score calculation method of ScoreStrategy.
 * This class constitutes a part of strategy pattern.
 */
public class OptionOneScoreStrategy implements ScoreStrategy {
    /**
     * Calculates the score attainable using two private cards.
     *
     * @param player The player that will receive the score.
     * @param playingArea The cards to be considered for score calculation.
     * @return An integer of score calculated.
     */
    @Override
    public int calculateScore(Player player, Hand playingArea) {
        ArrayList<Card> privateCards = player.getHand().getCardList();
        GameCard privateCard1 = (GameCard) privateCards.get(0);
        GameCard privateCard2 = (GameCard) privateCards.get(1);

        int score = 0;
        if (ScoreActors.getInstance().isThirteenCards(privateCard1, privateCard2, null, null)) {
            score = privateCard1.getScorePrivateCard() + privateCard2.getScorePrivateCard();
        }
        return score;
    }
}
