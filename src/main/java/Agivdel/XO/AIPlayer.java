package Agivdel.XO;

public class AIPlayer implements Player {
    private final String sign;
    private final String anotherSign;
    private final int level;

    static boolean firstTurnOfAI = true;//первым ходом ИИ должен занять центральную клетку

    public AIPlayer(String sign, String anotherSign, int level) {
        this.sign = sign;
        this.anotherSign = anotherSign;
        this.level = level;
    }

    @Override//нужно обязательно переопределять, т.к. в классе PlayerTurn используется тип Player
    public String getSign() {
        return sign;
    }

    @Override
    public int turn() {
        int gameCell = 0;
        switch (level) {
            case (1):
                gameCell = randomAITurn();
                break;
            case (2):
                if (firstTurnOfAI) {
                    gameCell = randomAITurn();
                    firstTurnOfAI = false;
                } else {
                    gameCell = notRandomAITurn();
                }
                break;
            case (3):
                if (firstTurnOfAI & Field.game[4].equals(Fin.SIGN_EMPTY)) {
                    gameCell = 4;
                    firstTurnOfAI = false;
                } else {
                    gameCell = notRandomAITurn();
                }
                break;
        }
        return gameCell;
    }

    private int randomAITurn() {
        return (int) (Math.random() * 9);
    }

    private int notRandomAITurn() {
        return new CellChoice().run(sign, anotherSign);
    }
}
