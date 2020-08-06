package Agivdel.XO.Domain;

import java.util.Arrays;

/**
 * Статические поля класса хранят данные об игровом поле, поле вероятностей, выигрышных линиях -
 * для любого объекта класса Data и в любой момент времени (до следующего апдейта) эти данные совпадают.
 * Не являются константами!
 */
public class Data {

    private static String[] gameTable;//игровое поле
    private static String[] winLines;//в этом массиве записываем знаки игроков для проверки победы
    private static Odds[][] oddsTable;//для записи вероятностей выигрыша

    //статический блок инициализации, при первой загрузке класса
    static {
        gameTable = new String[9];
        Arrays.fill(gameTable, Fin.SIGN_EMPTY);
        winLines = Fin.BASE_LINES;
        oddsTable = new Odds[][]{new Odds[3], new Odds[2], new Odds[3],
                new Odds[2], new Odds[4], new Odds[2],
                new Odds[3], new Odds[2], new Odds[3]};
        for (int cell = 0; cell < oddsTable.length; cell++) {
            int line = 0;
            for (String baseLine : Fin.BASE_LINES) {
                if (baseLine.contains(String.valueOf(cell))) {
                    oddsTable[cell][line] = new Odds();
                    oddsTable[cell][line].setIndexLine(baseLine);
                    line++;
                }
            }
        }
    }

    public String[] getGameTable() {
        return gameTable;
    }

    public String[] getWinLines() {
        return winLines;
    }

    public Odds[][] getOddsTable() {
        return oddsTable;
    }
}
