package Agivdel.XO;

public class AIPlayer implements Player {
    private final String sign;
    private final String anotherSign;
    private final int level;
    private boolean firstTurnOfAI = true;//первым ходом ИИ должен занять центральную клетку

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
        Check check = new Check(new Data());//обновляем данные
        int gameCell = 0;
        switch (level) {
            case (1):
                gameCell = (int) (Math.random() * 9);
                break;
            case (2):
                if (firstTurnOfAI) {
                    gameCell = (int) (Math.random() * 9);
                    firstTurnOfAI = false;
                } else {
                    gameCell = check.cellFind(sign, anotherSign);
                }
                break;
            case (3):
                if (firstTurnOfAI & check.isEmpty(4)) {
                    gameCell = 4;
                    firstTurnOfAI = false;
                } else {
                    gameCell = check.cellFind(sign, anotherSign);
                }
                break;
        }
        return gameCell;
    }
}
