package Agivdel.XO.Domain;

import java.util.Arrays;

/**
 * класс хранит все методы проверки, существующие в программе.
 * для вызова любого метода создается безымянный объект класса Control
 * и в него передается объект Data data с последним апдейтом данных.
 */
public class Check {
    static int emptyCellNumber;

    private final String[] gameTable;
    private final String[] winLines;

    public Check() {//берем данные с предыдущего вызова write() класса Writer (т.е. с последнего обновления data)
        this.gameTable = new Data().getGameTable();//т.к. поля у data статические,
        this.winLines = new Data().getWinLines();
    }

    boolean isDraw() {
        if (emptyCellCount() == 0) {
            System.out.println(Fin.DRAW);
            return true;
        }
        return false;
    }

    int emptyCellCount() {//подсчет пустых клеток
        int j = 0;
        for (int i = 0; i < gameTable.length; i++) {
            if (isEmpty(i)) {
                emptyCellNumber = i;//пригодится, если пустая клетка только одна
                j++;
            }
        }
        return j;
    }

    boolean isEmpty(int gameCell) {
        return gameTable[gameCell].equals(Fin.SIGN_EMPTY);
    }

    boolean wrongChoice(int gameCell) {//если игрок-человек ввел неверное число, ему дается подсказка
        if (gameCell < 0 || gameCell >= 9) {
            System.out.println(Fin.KEY_CHOICE);
            return true;
        }
        return !this.gameTable[gameCell].equals(Fin.SIGN_EMPTY);//возвращаем ЛОЖЬ, если клетка пустая
    }

    boolean isWin() {
        return Arrays.stream(winLines).anyMatch(str->str.equals("xxx") || str.equals("ooo"));
    }
}
