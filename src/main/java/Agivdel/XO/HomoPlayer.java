package Agivdel.XO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HomoPlayer implements Player {
    private final String sign;


    public HomoPlayer(String sign) {
        this.sign = sign;
    }

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public int turn() {
        Scanner scanner = new Scanner(System.in);
        int gameCell = 0;
        try {
            System.out.println("Ваш ход: ");
            gameCell = scanner.nextInt() - 1;
        } catch (InputMismatchException e) {
            e.getStackTrace();
        }
        scanner.skip(".*");
        return gameCell;
    }
}