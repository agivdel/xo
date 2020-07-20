package Agivdel.XO;

import java.util.Arrays;

public class Field {

    static String[] game = new String[9];
    private static String[] checkedLines = Fin.BASE_LINES;
    private static int emptyCellNumber;

    static void init() {
        Arrays.fill(game, Fin.SIGN_EMPTY);
    }

    static void print() {
        System.out.print(game[6] + " ");
        System.out.print(game[7] + " ");
        System.out.print(game[8] + " ");
        System.out.println();
        System.out.print(game[3] + " ");
        System.out.print(game[4] + " ");
        System.out.print(game[5] + " ");
        System.out.println();
        System.out.print(game[0] + " ");
        System.out.print(game[1] + " ");
        System.out.print(game[2] + " ");
        System.out.println("\n_________________");
    }

    static void writeTable(int gameCell, String signXO) {//разделить метод на два
        game[gameCell] = signXO;//1) вписываем в клетку знак игрока - крестик или нолик
        for (int i = 0; i < Fin.BASE_LINES.length; i++) {//2)
            if (Fin.BASE_LINES[i].contains(String.valueOf(gameCell))) {
                checkedLines[i] = checkedLines[i].replace(String.valueOf(gameCell),signXO);
            }
        }
    }

    static int isOnlyOneCell() {
        if (emptyCellCount() == 1) {
            return emptyCellNumber;//если пустая клетка только одна, ее номер уже записан в этой переменной
        }
        return 9;
    }

    static boolean isDraw() {
        if (emptyCellCount() == 0) {
            System.out.println(Fin.DRAW);
            return true;
        }
        return false;
    }

    private static int emptyCellCount() {//подсчет пустых клеток
        int j = 0;
        for (int i = 0; i < game.length; i++) {
            if (game[i].equals(Fin.SIGN_EMPTY)) {
                emptyCellNumber = i;//пригодится, если пустая клетка только одна
                j++;
            }
        }
        return j;
    }

    static boolean wrongChoice(int gameCell) {
        if ((gameCell < 0 || gameCell >= 9) & playerIsHomo()) {
            System.out.println(Fin.KEY_CHOICE);
            return true;
        }
        return !game[gameCell].equals(Fin.SIGN_EMPTY);
    }

    static boolean playerIsHomo() {
        HomoPlayer homo = new HomoPlayer("");//безымянный объект для сравнения классов
        return (GameMode.player1.getClass().equals(homo.getClass()) || GameMode.player2.getClass().equals(homo.getClass()));
    }

    static boolean isWin() {
        for (String checkedLine : checkedLines) {
            if (checkedLine.contains("xxx") || checkedLine.contains("ooo")) {
                if (PlayerTurn.oddTurn) {
                    System.out.println(Fin.PL2_WIN);//oddTurn ИСТИНА в конце хода игрока 2
                } else {
                    System.out.println(Fin.PL1_WIN);
                }
                return true;
            }
        }
        return false;
    }
}
