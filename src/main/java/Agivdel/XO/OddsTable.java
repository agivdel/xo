//package Agivdel.XO;
//
//public class OddsTable {
//
//    private static Odds[][] Table;//массив принадлежит всему классу
//
//    static {
//        Table = new Odds[][]{new Odds[3], new Odds[2], new Odds[3],
//                new Odds[2], new Odds[4], new Odds[2],
//                new Odds[3], new Odds[2], new Odds[3]};
//
//        for (int cell = 0; cell < Table.length; cell++) {
//            int line = 0;
//            for (String baseLine : Fin.BASE_LINES) {
//                if (baseLine.contains(String.valueOf(cell))) {
//                    Table[cell][line] = new Odds();
//                    Table[cell][line].setIndexLine(baseLine);
//                    line++;
//                }
//            }
//        }
//    }
//
//    void writeTable(int gameCell, String signXO) {
//        for (int cell = 0; cell < Table.length; cell++) {
//            for (int line = 0; line < Table[cell].length; line++) {
//                if (Field.game[cell].equals(Fin.SIGN_EMPTY)
//                        && Table[cell][line].getIndexLine().contains(String.valueOf(gameCell))) {
//                    Table[cell][line].OddsPlus(signXO);
//                }
//            }
//        }
//    }
//}
