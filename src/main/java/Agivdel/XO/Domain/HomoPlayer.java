package Agivdel.XO.Domain;


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
        int gameCell;
        do {
            gameCell = scanHomoTurn();
        }
        while (new Check().wrongChoice(gameCell));//проверка по последним данным
        return gameCell;
    }

    private int scanHomoTurn() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println(Fin.TURN_MEAT_BAG);
                return scanner.nextInt() - 1;
            } catch (InputMismatchException e) {
                System.out.println(Fin.KEY_CHOICE);
            }
            scanner.skip(".*");
        }
    }
}