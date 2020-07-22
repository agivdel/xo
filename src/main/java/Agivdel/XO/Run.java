package Agivdel.XO;

import java.util.Arrays;

public class Run {
    static boolean oddTurn = true;//метка нечётного хода
    Data data = new Data();//первое обращение к классу Data, статическая инициализация всех статических полей Data
    Check check = new Check(data);

    void start() {//цикл игры
        GameMode.tune();
        while (true) {
            if (check.isDraw())
                break;
            playerChange();//здесь происходит очередное обновление data (метод write() внутри метода playerTurn())
            Read.print();
            if (check.isWin()) {
                break;
            }
        }
    }

    void playerChange() {//смена ходов игроков
        if (oddTurn) {//нечётный ход
            playerTurn(1, GameMode.player1);
        } else {//чётный ход
            playerTurn(2, GameMode.player2);
        }
        oddTurn = !oddTurn;
    }

    void playerTurn(int n, Player player) {//ход одного игрока
        int gameCell;
        System.out.println("Ход игрока " + n + " (" + player.getSign() + "):");
        do {
            gameCell = player.turn();
        }
        while (check.wrongChoice(gameCell));//проверка по последним данным
        new Write().write(gameCell, player.getSign());//обновление данных
    }
}
