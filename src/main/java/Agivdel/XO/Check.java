package Agivdel.XO;

import java.util.ArrayList;

public class Check {

    void game() {

//        бесконечный цикл из двух шагов:
        while (true) {
//        1. ход игрока
//            playerTurn();
//        2. проверка на победу или ничью
//            if (checkWin());
//              если да, выход из метода
            return;
        }





    }






    public static ArrayList emptyCellsCheck(ArrayList field) {
        for (Object cell : field) //почему нужно указывать класс Object, а не ArrayList?
            if (cell.equals("")) {
                field.remove(cell);
            }
        field.forEach(cell ->{ if (cell.equals("")) { field.remove(cell); } });
            return field;
    }


    public static boolean checkWin(ArrayList aNewBoard, String player) {
        ArrayList<String> board = new ArrayList<>(aNewBoard);
        if ((board.get(0).equals(player) && board.get(1).equals(player) && board.get(2).equals(player)) ||
                (board.get(3).equals(player) && board.get(4).equals(player) && board.get(5).equals(player)) ||
                (board.get(6).equals(player) && board.get(7).equals(player) && board.get(8).equals(player)) ||
                (board.get(0).equals(player) && board.get(3).equals(player) && board.get(6).equals(player)) ||
                (board.get(1).equals(player) && board.get(4).equals(player) && board.get(7).equals(player)) ||
                (board.get(2).equals(player) && board.get(5).equals(player) && board.get(8).equals(player)) ||
                (board.get(0).equals(player) && board.get(4).equals(player) && board.get(8).equals(player)) ||
                (board.get(2).equals(player) && board.get(4).equals(player) && board.get(6).equals(player)))
            return true;
        return false;
    }

}
