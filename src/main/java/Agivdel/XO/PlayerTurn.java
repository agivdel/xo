package Agivdel.XO;

public class PlayerTurn {
    static PlayerTurn pt = new PlayerTurn();
    static boolean oddTurn = true;//метка нечетного хода

    static void start() {
        while (true) {
            if (Field.isDraw())
                break;
            pt.round();
            Field.print();
            if (Field.isWin()) {
                break;
            }
        }
    }

    void round() {
        if (oddTurn) {
            run(1, GameMode.player1);
        } else {
            run(2, GameMode.player2);
        }
        oddTurn = !oddTurn;
    }

    void run(int n, Player player) {
        int gameCell;
        System.out.printf("Ход игрока %s (%s)\n", n, player.getSign());
        do {
            gameCell = player.turn();
        }
        while (Field.wrongChoice(gameCell));
        Field.writeTable(gameCell, player.getSign());
        Odds.writeTable(gameCell, player.getSign());
    }
}
