package Agivdel.XO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GameMode {
    static Player player1;//статические, т.к. нужно как-то сохранять данные об игроках
    static Player player2;

    static void tune() {
        new GameMode().choice();
    }

    void choice() {
        String player1Sign = Fin.X;;//по умолчанию игрок 1 играет крестиками
        String player2Sign = Fin.O;
        //playerLevel: "уровень" игрока (0 - человек, 1 - слабый ИИ, 2 - средний ИИ, 3 - сильный ИИ)
        int player1Level = input(Fin.PL1_CHOICE, 0, 1, 2, 3);//считываем с консоли данные для игрока 1...
        if (input(Fin.PL_SIGN_CHOICE, 0, 1, 1, 1) == 0) {//...уточняем знак игрока 1.
            player1Sign = Fin.O;
            player2Sign = Fin.X;
        }
        int player2Level = input(Fin.PL2_CHOICE, 0, 1, 2, 3);//считываем данные для игрока 2
        player1 = playerCreate(player1Sign, player2Sign, player1Level);
        player2 = playerCreate(player2Sign, player1Sign, player2Level);
        if (player1Level == 0 || player2Level == 0) {//если хотя бы один игрок человек, даем ему подсказку по клавишам
            System.out.println(Fin.GAME_FIELD_EXAMPLE);
        }
    }

    private int input(String str, int...pl) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(str);
                int result = scanner.nextInt();
                if (result == pl[0] || result == pl[1] || result == pl[2] || result == pl[3]) {
                    return result;
                }
            } catch (InputMismatchException e) {
                //ignore
            }
            System.err.print(Fin.INPUT_MISTAKE);
            scanner.skip(".*");
        }
    }

    private Player playerCreate(String playerSign, String playerAnotherSign, int playerLevel) {
        if (playerLevel == 0) {
            return new HomoPlayer(playerSign);
        } else {
            return new AIPlayer(playerSign, playerAnotherSign, playerLevel);
        }
    }
}
