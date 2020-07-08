package Agivdel.XO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GameMode {

    static int player1;
    static int player2;
    static String player1sign = "x";
    static String player2sign = "o";
    static int aiLevel;

    static final String player1Choice = "Первым ходит игрок 1. Выберите его (0 - человек, 1 - компьютер): ";
    static final String player2Choice = "Выберите игрока 2 (0 - человек, 1 - компьютер): ";
    static final String playerSignChoice = "Выберите знак игрока 1 (0 - нолики, 1 - крестики): ";
    static final String aiLevelChoice = "Выберите уровень компьютерного игрока: 0 (простой), 1 (средний) или 2 (сложный): ";

    static void choice() {
        player1 = input(player1Choice, 0, 1, 1);//при выборе знака игрока третий аргумент не задействован
        player2 = input(player2Choice, 0, 1, 1);
        if (input(playerSignChoice, 0, 1) == 0) {
            player1sign = "o";
            player2sign = "x";
        }
        if (player1 == 1 || player2 == 1) {
            aiLevel = input(aiLevelChoice, 0, 1, 2);//а здесь нужны все три аргумента
        }
        HomoInstruct();
    }

    private static int input(String str, int...pl) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(str);
                int result = scanner.nextInt();
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

    private static void HomoInstruct() {
        if (player1 == 0 || player2 == 0) {
            System.out.println("Игровое поле соответствует клавишам от 1 до 9 клавиатуры NumKeypad:");
            System.out.println("7 8 9\n4 5 6\n1 2 3\n_________________");
        }
    }
}
