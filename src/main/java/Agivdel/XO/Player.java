package Agivdel.XO;

public class Player {

    private static boolean player1TurnMark = true;

    static void turn() {
        if (player1TurnMark) {
            System.out.println("Ход игрока 1 (" + GameMode.player1sign + "):");
            playerTurn(GameMode.player1, GameMode.player1sign, GameMode.player2sign);
            player1TurnMark = false;
        }
        else {
            System.out.println("Ход игрока 2 (" + GameMode.player2sign + "):");
            playerTurn(GameMode.player2, GameMode.player2sign, GameMode.player1sign);
            player1TurnMark = true;
        }
    }

    static void playerTurn(int playerNumber, String playerSign, String anotherPlayerSign){
        if (playerNumber == 0) {
            HomoPlayer.turn(playerSign);
        }
        if (playerNumber == 1) {
            AIPlayer.turn(playerSign, anotherPlayerSign);
        }
    }
}