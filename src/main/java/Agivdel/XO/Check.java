package Agivdel.XO;

/**
 * класс хранит все методы проверки, существующие в программе.
 * для вызова любого метода создается безымянный объект класса Control
 * и в него передается объект Data data с последним апдейтом данных.
 */
public class Check {
    private static int emptyCellNumber;
    private static int cell;
    private static int line;
    private static int oneLineInCellCounter;
    private static int noLineInCellCounter;
    private static int cellMark;

    private final String[] gameTable;
    private final String[] winLines;
    private final Odds[][] oddsTable;

    public Check(Data data) {//берем данные с предыдущего вызова write() класса Writer (т.е. с последнего обновления data)

        this.gameTable = data.getGameTable();//т.к. поля у data статические,
        this.winLines = data.getWinLines();
        this.oddsTable = data.getOddsTable();
    }

    int cellFind(String signXO, String anotherSignXO) {
        int desiredCell;

        if ((desiredCell = preWinCellSearch(signXO)) < 9) //ИСТИНА, если нашлась подходящая cell
            return desiredCell;

        if ((desiredCell = preWinCellSearch(anotherSignXO)) < 9)
            return desiredCell;

        if ((desiredCell = twoLinesInCellSearch(signXO)) < 9)
            return desiredCell;

        //если линия с odds=1 есть лишь в одной клетке, выбираем для хода ее
        if (oneLineInCellCounter == 1) {
            cellMark = 0;
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
        //оказались здесь, если нет подходящих клеток. Значит, осталась пустой единственная клетка.
        //Проверяем это и выбираем для хода ее.
        if ((desiredCell = oneEmptyCell()) >= 9) {
            System.out.println("Какая-то ерунда получается...");
        } else {
            return desiredCell;
        }

        return desiredCell;
    }

    //перебор массива и поиск подходящей клетки "за шаг до победы"
    private int preWinCellSearch(String signXO) {//oddsValue всегда равна 2, да и пусть
        int checkValue = 9;//"9" не используется для обозначения клеток игрового поля, поэтому ее безопасно применять
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (isCellRight(2, cell, line, signXO)) {
                    return cell;
                }
            }
        }
        return checkValue;
    }

    //ищем и считаем клетки "за два шага до победы" - со своими odds=1 и odds=0
    private int twoLinesInCellSearch(String signXO) {
        int checkValue = 10;
        int linesInCellCounter = 0;
        oneLineInCellCounter = 0;
        noLineInCellCounter = 0;
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (isCellRight(1, cell, line, signXO)) {
                    linesInCellCounter++;
                    cellMark = cell; //помечаем клетку, для которой начали считать число линий с odds=1
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
    private int iteratingOverArray(int oddsValue, String signXO, int LineInCellCounter) {
        int checkValue = 11;
        int randomTurn = (int) (Math.random() * LineInCellCounter);
        int aCellCounter = 0;
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
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
    private boolean isCellRight(int oddsValue, int cell, int line, String signXO) {
        return (isEmpty(cell)
                && oddsTable[cell][line].isOnlyOneSign()
                && oddsTable[cell][line].getOdds(signXO) == oddsValue);
    }

    int oneEmptyCell() {
        if (emptyCellCount() == 1) {
            return emptyCellNumber;//если пустая клетка только одна, ее номер уже записан в этой переменной
        }
        return 9;
    }

    boolean isDraw() {
        if (emptyCellCount() == 0) {
            System.out.println(Fin.DRAW);
            return true;
        }
        return false;
    }

    private int emptyCellCount() {//подсчет пустых клеток
        int j = 0;
        for (int i = 0; i < gameTable.length; i++) {
            if (isEmpty(i)) {
                emptyCellNumber = i;//пригодится, если пустая клетка только одна
                j++;
            }
        }
        return j;
    }

    boolean isEmpty(int gameCell) {
        return gameTable[gameCell].equals(Fin.SIGN_EMPTY);
    }

    boolean wrongChoice(int gameCell) {//если игрок-человек ввел неверное число, ему дается подсказка
        if ((gameCell < 0 || gameCell >= 9) &
                (GameMode.player1.getClass() == HomoPlayer.class || GameMode.player2.getClass() == HomoPlayer.class)) {
            System.out.println(Fin.KEY_CHOICE);
            return true;
        }
        return !this.gameTable[gameCell].equals(Fin.SIGN_EMPTY);
    }

    boolean isWin() {
        for (String winLine : this.winLines) {
            if (winLine.contains("xxx") || winLine.contains("ooo")) {
                if (Run.oddTurn) {
                    System.out.println(Fin.PL2_WIN);//oddTurn ИСТИНА в конце хода игрока 2
                } else {
                    System.out.println(Fin.PL1_WIN);
                }
                return true;
            }
        }
        return false;
    }
}
