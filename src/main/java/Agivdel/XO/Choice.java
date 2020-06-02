package Agivdel.XO;

public class Choice {

    private static int cell;
    private static int line;
    private static int oneLineInCellCounter;
    private static int noLineInCellCounter;

    static int cell(String signXO, String anotherSignXO) {
        int desiredCell;

        if ((desiredCell = preWinCellSearch(signXO)) < 9) //ИСТИНА, если нашлась подходящая cell
            return desiredCell;

        if ((desiredCell = preWinCellSearch(anotherSignXO)) < 9)
            return desiredCell;

        if ((desiredCell = twoLinesInCellSearch(signXO)) < 9)
            return desiredCell;

        //если линия с odds=1 есть лишь в одной клетке, выбираем для хода ее
        if (oneLineInCellCounter == 1) {
            int cellMark = 0;
            return cellMark;//cellMark инициализирована в методе usualCellSearch()
        } //выбираем случайно одну из клеток, где линии имеют одну odds=1
        else if (oneLineInCellCounter > 1) {
            if ((desiredCell
                    = iteratingOverArray(1, signXO, oneLineInCellCounter)) < 9) {
                return desiredCell;
            }
        } //выбираем случайно одну из клеток, где линии вообще не имеют odds>0
        else if (noLineInCellCounter > 1) {
            if ((desiredCell = iteratingOverArray(0, signXO, noLineInCellCounter)) < 9) {//!!!всегда будет ИСТИНА
                return desiredCell;
            }
        }

        if ((desiredCell = Field.isOnlyOneCell()) < 9) {
            return desiredCell;
        }
        else System.out.println("Какая-то ерунда получается...");

        return desiredCell;
    }

    //перебор массива и поиск подходящей клетки "за шаг до победы"
    private static int preWinCellSearch(String signXO) {//oddsValue всегда равна 2, да и пусть
        int checkValue = 9;//"9" не используется для обозначения клеток игрового поля, поэтому ее безопасно применять
        for (cell = 0; cell < Odds.Table.length; cell++) {
            for (line = 0; line < Odds.Table[cell].length; line++) {
                if (isCellRight(2, cell, line, signXO)) {
                    return cell;
                }
            }
        }
        return checkValue;
    }

    //ищем и считаем клетки "за два шага до победы" - со своими odds=1 и odds=0
    private static int twoLinesInCellSearch(String signXO) {
        int checkValue = 10;
        int linesInCellCounter = 0;
        oneLineInCellCounter = 0;
        noLineInCellCounter = 0;
        for (cell = 0; cell < Odds.Table.length; cell++) {
            for (line = 0; line < Odds.Table[cell].length; line++) {
                if (isCellRight(1, cell, line, signXO)) {
                    linesInCellCounter++;
                    int cellMark = cell; //помечаем клетку, для которой начали считать число линий с odds=1
                    if (linesInCellCounter == 2) {
                        return cell;
                    }
                    if (linesInCellCounter == 1) {
                        oneLineInCellCounter++;
                    }
                } else if (isCellRight(0, cell, line, signXO)) {
                    noLineInCellCounter++;
                }
            }
            linesInCellCounter = 0;
        }
        return checkValue;
    }

    //перебор массива и случайный выбор одной из подходящих клеток "за два/три шага до победы"
    private static int iteratingOverArray(int oddsValue, String signXO, int noLineInCellCounter) {
        int checkValue = 11;
        int randomTurn = (int) (Math.random() * noLineInCellCounter);
        int aCellCounter = 0;
        for (cell = 0; cell < Odds.Table.length; cell++) {
            for (line = 0; line < Odds.Table[cell].length; line++) {
                if (isCellRight(oddsValue, cell, line, signXO)) {
                    if (aCellCounter == randomTurn) {
                        return cell;
                    }
                    aCellCounter++;
                }
            }
        }
        return checkValue;
    }

    //возвращает истину, если клетка пустая, индекс линии не пустой, в линии записан лишь один знак и odds=oddsValue
    private static boolean isCellRight(int oddsValue, int cell, int line, String signXO) {
        return (Field.game[cell].equals(Field.SIGN_EMPTY)
                && !Odds.Table[cell][line].getIndexLine().equals("")
                && Odds.Table[cell][line].isOnlyOneSign()
                && Odds.Table[cell][line].getOdds(signXO) == oddsValue);
    }
}
