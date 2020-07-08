package Agivdel.XO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HomoPlayer {

    private static int gameCell;

    static public void turn(String signXO) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                System.out.println("Ваш ход: ");
                gameCell = scanner.nextInt() - 1;
            } catch (InputMismatchException e) {
                e.getStackTrace();
            }
            scanner.skip(".*");
        }
        while (Field.wrongChoice(gameCell));
        Field.writeTable(gameCell, signXO);
        Odds.writeTable(gameCell, signXO);
    }
}
