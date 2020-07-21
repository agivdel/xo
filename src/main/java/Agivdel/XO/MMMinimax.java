//package Agivdel.XO;
//
//import java.util.ArrayList;
//
//public class MMMinimax {
//
//    public ArrayList<String> newField = new ArrayList<String>();
//
//
//    ArrayList<String> origBoard = new ArrayList<String>();//{".",".",".",".",".",".",".",".","."};
//
//    ArrayList<String> emptyCells = new ArrayList<String>();//каждый элемент - индекс свободной клетки в игровом поле
//
//    String homoPlayer = "o";
//    String aiPlayer = "x";
//    String player;
//    String anotherPlayer;
//
//    int methodCalls = 0;
//    int aScore;
//    MMMove bestSpot = minimax(origBoard, aiPlayer);
//
//
//
//
//    MMMove minimax(ArrayList newField, String player) {
//        methodCalls++;
//        MMMove finalScore = null;
//        emptyCells = MMCheck.emptyCellsCheck(newField);
//        if (MMCheck.checkWin(newField, homoPlayer)) {
//            finalScore.setScore(-10);
//            return finalScore;
//        } else if (MMCheck.checkWin(newField, aiPlayer)) {
//            finalScore.setScore(10);
//            return finalScore;
//        } else if (emptyCells.size() == 0) {
//            finalScore.setScore(0);
//            return finalScore;
//        }
//
//        ArrayList<MMMove> moves = new ArrayList<MMMove>();
//        MMMove move;
//
//        for (String emptyCell : emptyCells) {
//            move = new MMMove(Integer.parseInt(emptyCell));//сохраняем номер клетиу в move.index
//            newField.set(Integer.parseInt(emptyCell), player);//ходим на очередную пустую клетку текущим игроком
//            if (player.equals(aiPlayer)) {//вызываем минимакс за противника
//                MMMove result = minimax(newField, homoPlayer);
//                move.setScore(result.getScore());
//            } else {
//                MMMove result = minimax(newField, aiPlayer);
//                move.setScore(result.getScore());
//            }
//
//            newField.set(move.getIndex(), "");//очищаем клетку
//            moves.add(move);//добавляем объект move  в массив moves
//        }
//
//        //к этому моменту на всех уровнях рефлексии были созданы объекты move и добавлены в массив moves.
//        int bestMove = 9;
//        int bestScore = 10000;
//        if (player.equals(aiPlayer)) {//выбираем максимальное число
//            for (int i = 0; i < moves.size(); i++) {
//                if (moves.get(i).getScore() > -bestScore) {
//                    bestScore = moves.get(i).getScore();
//                    bestMove = i;
//                }
//            }
//        } else {//выбираем минимальное число
//            for (int i = 0; i < moves.size(); i++) {
//                if (moves.get(i).getScore() < bestScore) {
//                    bestScore = moves.get(i).getScore();
//                    bestMove = i;
//                }
//            }
//        }
//        return moves.get(bestMove);
//    }}
//
//
