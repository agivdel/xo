package Agivdel.XO;

public class Player {

    private static boolean firstTurnMark = true;

    static void turn() {
        if (firstTurnMark) {
            System.out.println("Ход игрока 1 (" + GameMode.player1sign + "):");
            player1Turn();
            firstTurnMark = false;
        }
        else {
            System.out.println("Ход игрока 2 (" + GameMode.player2sign + "):");
            player2Turn();
            firstTurnMark = true;
        }
    }

    static void player1Turn() {
        if (GameMode.player1 == 0) {
            HomoPlayer.turn(GameMode.player1sign);
        }
        if (GameMode.player1 == 1) {
            AIPlayer.turn(GameMode.player1sign, GameMode.player2sign);
        }
    }

    static void player2Turn(){
        if (GameMode.player2 == 0) {
            HomoPlayer.turn(GameMode.player2sign);
        }
        if (GameMode.player2 == 1) {
            AIPlayer.turn(GameMode.player2sign, GameMode.player1sign);
        }
    }
}