package Agivdel.XO.Domain;

public class Run {
    static boolean oddTurn = true;//метка нечётного хода

    void start() {//цикл игры
        Check check = new Check();//1-е создание new Data() в конструкторе new Check()
        GameMode.tune();
        while (true) {
            if (check.isDraw())
                break;
            playerChange();//здесь происходят очередные (2-е и 3-е) обновления data (метод write() внутри метода playerTurn())
            Read.print();//4-е создание new Data()
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
        gameCell = player.turn();//2-е создание new Check(), для ИИ
        new Write().write(gameCell, player.getSign());//обновление данных; 2-е создание new Data() и new Check()
    }
}
