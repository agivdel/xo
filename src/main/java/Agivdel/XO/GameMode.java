package Agivdel.XO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GameMode {

    static int player1;
    static int player2;
    static int result;
    static String player1sign = "x";
    static String player2sign = "o";
    static int aiLevel;
    static String player1Choice = "Первым ходит игрок 1. Выберите его (0 - человек, 1 - компьютер): ";
    static String player2Choice = "Выберите игрока 2 (0 - человек, 1 - компьютер): ";
    static String playerSignChoice = "Выберите знак игрока 1 (0 - нолики, 1 - крестики): ";
    static String aiLevelChoice = "Выберите уровень компьютерного игрока: 0 (простой), 1 (средний) или 2 (сложный): ";

    static void choice() {
        player1 = input(player1Choice, 0, 1);
        player2 = input(player2Choice, 0, 1);
        if (input(playerSignChoice, 0, 1) == 0) {
            player1sign = "o";
            player2sign = "x";
        }
        if (player1 == 1 || player2 == 1) {
            aiLevel = input2(aiLevelChoice, 0, 1, 2);
        }
        instructionForHomo();
    }

    private static int input(String str, int...pl) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(str);
                result = scanner.nextInt();
                if (result == pl[0] || result == pl[1]/* || result == pl[2]*/) {
                    return result;
                }
            } catch (InputMismatchException e) {
                //ignore
            }
            System.err.print("Ошибка! Введите одну из показанных выше цифр ");
            scanner.skip(".*");
        }
    }

    private static int input2(String str, int...pl) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(str);
                result = scanner.nextInt();
                if (result == pl[0] || result == pl[1] || result == pl[2]) {
                    return result;
                }
            } catch (InputMismatchException e) {
                //ignore
            }
            System.err.print("Ошибка! Введите одну из показанных выше цифр ");
            scanner.skip(".*");
        }
    }

    private static void instructionForHomo() {
        if (player1 == 0 || player2 == 0) {
            System.out.println("Игровое поле соответствует клавишам от 1 до 9 клавиатуры NumKeypad:");
            Field.print();
        }
    }
}
