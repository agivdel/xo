package Agivdel.XO;

public class AIPlayer {

    private static int gameCell;
    private static boolean firstTurnOfAI = true;

    static void turn(String signXO, String anotherSignXO) {
        do {
            switch (GameMode.aiLevel) {
                case (0):
                    gameCell = (int) (Math.random() * 9);
                    break;
                case (1):
                    if (firstTurnOfAI) {
                        gameCell = (int) (Math.random() * 9);
                        firstTurnOfAI = false;
                    } else {
                        gameCell = Choice.cell(signXO, anotherSignXO);
                    }
                    break;
                case (2):
                    if (firstTurnOfAI && Field.game[4].equals(Field.SIGN_EMPTY)) {
                        gameCell = 4;
                        firstTurnOfAI = false;
                    } else {
                        gameCell = Choice.cell(signXO, anotherSignXO);
                    }
                    break;
            }
        }
        while (Field.wrongChoice(gameCell));
        Field.writeTable(gameCell, signXO);
        Odds.writeTable(gameCell, signXO);
    }
}