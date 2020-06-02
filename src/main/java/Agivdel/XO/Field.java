package Agivdel.XO;

import java.util.ArrayList;
import java.util.Arrays;

public class Field {

    static final String SIGN_EMPTY = ".";
    static String[] game = new String[9];
    static String[] checkLines = Arrays.copyOf(Odds.BASELINES, 8);
    static ArrayList<String> signLines = new ArrayList<String>(8);

    static void init() {
        Arrays.fill(game, SIGN_EMPTY);
        signLines.addAll(Arrays.asList(Odds.BASELINES));
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

    static int isOnlyOneCell() {
        if (emptyCell() == 1) {
            for (int i = 0; i < game.length; i++) {
                if (game[i].equals(SIGN_EMPTY)) {
                    return i;
                }
            }
        }
        return 9;
    }

    static boolean isDraw() {
        if (emptyCell() == 0) {
            System.out.println("Ничья");
            return true;
        }
        return false;
    }

    static int emptyCell() {
        int j = 0;
        for (String c : game) {
            if (c.equals(SIGN_EMPTY)) {
                j++;
            }
        }
        return j;
    }

    static boolean wrongChoice(int gameCell) {
        if ((gameCell < 0 || gameCell >= 9) & (GameMode.player1 == 0 || GameMode.player2 == 0)) {
            System.out.println("Нажмите цифровую клавишу от 1 до 9");
            return true;
        }
        return !game[gameCell].equals(SIGN_EMPTY);
    }

    static void writeTable(int gameCell, String signXO) {
        game[gameCell] = signXO;
        for (int i = 0; i < checkLines.length; i++) {
            if (checkLines[i].contains(String.valueOf(gameCell))) {
                String s = signLines.get(i).replace(String.valueOf(gameCell), signXO);
                signLines.set(i, s);
            }
        }
    }

    static boolean isWin() {
        for (String signLine : signLines) {
            if (signLine.contains("xxx") || signLine.contains("ooo")) {
                return true;
            }
        }
        return false;
    }
}
