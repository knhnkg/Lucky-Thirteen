package lucky.gameobjects;

/**
 * Enumeration representing the suits in the card.
 */
public enum Suit {
    SPADES ("S", 4), HEARTS ("H", 3),
    DIAMONDS ("D", 2), CLUBS ("C", 1);
    private String suitShortHand = "";
    private int multiplicationFactor = 1;
    public static final int PUBLIC_CARD_MULTIPLICATION_FACTOR = 2;
    Suit(String shortHand, int multiplicationFactor) {
        this.suitShortHand = shortHand;
        this.multiplicationFactor = multiplicationFactor;
    }

    public String getSuitShortHand() {
        return suitShortHand;
    }

    public int getMultiplicationFactor() {
        return multiplicationFactor;
    }

    public static Suit[] getSuitValues() {
        return Suit.values();
    }

    public static Suit getSuitFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        String suitString = cardName.substring(cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }
}