package Agivdel.XO;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Крестики-нолики с проверкой ИИ вероятностей победы для каждого хода.
 * Используем две таблицы: одно- и двухмерный массивы.
 * Все игровое поле (все массивы) делится на клетки (9*cell).
 * В 2-м. массиве (таблица подсчета шансов OddsTable) каждая клетка делится на линии (4*line).
 * Каждая линия (объект класса OddsLine) содержит поля:
 * 1) odds: это степень заполнения одной игровой вертикали/горизонтали/диагонали в долях от трех
 * (odds = 1 означает заполненную на одну третью какую-либо вертикаль/горизонталь/диагональ,
 * проходящюю через клетку), отдельно для крестиков и для ноликов.
 * 2) indexLine: индексная строка, содержащая три цифры, соответствующие трем клеткам,
 * входящих в определенную вертикаль/горизонталь/диагональ (индекс "678" означает три верхних
 * горизонтальных клетки игрового поля, "048" - диагональ из нижнего левого угла в верхний правый).
 * <p>
 * В зависимости от числа всех вертикалей/горизонталей/диагоналей), проходящих через клетку,
 * линий в каждой клетке может быть 2 (центральные клетки сторон), 3 (угловые клетки) или 4 (центральная клетка).
 * <p>
 * При прочих равных для выигрыша нужно заполнять клетки, в которых либо больше степень заполнения одной из линий
 * (например, одна линия, заполненная на две третьих), либо больше линий одинаковой степени заполнения
 * (например, две линии, заполненные на одну треть).
 * Если клетки имеют равное число линий с одинаковой степенью заполнения, выбирать можно любую из этих клеток.
 * <p>
 * Если противник имеет в какой-либо клетке линию со степенью заполнения, равной 2, это значит,
 * что для выигрыша ему нужно поставить в данную клетку свой знак.
 * Соответственно, противник может поставить свой знак в эту клетку, чтобы не допустить выигрыш другого игрока.
 */

public class XO2 {

    private final String SIGN_EMPTY = ".";

    private int gameCell;
    private int line;
    private int cell;

    private String player1sign = "x";
    private String player2sign = "o";
    private int player1;
    private int player2;
    private int aiLevel;

    boolean firstTurnOfAI = true;

    private int line1Counter = 0;
    private int cellMark = 0;
    private int cell1Counter = 0;
    private int cell0Counter = 0;

    String[] gameField = new String[9];
    OddsLine[][] oddsTable = new OddsLine[9][4];

    public static void main(String[] args) {
        new XO2().game();
    }


    void game(){
        initGameField();
        initOddsField();
        gameModeChoice();
        instructionForHomo();
        while (true) {
            System.out.println("Ход игрока 1 (" + player1sign + "):");
            player1Turn();
            printGameField();
            if (checkWin(player1sign)) {
                System.out.println("Победа игрока 1!");
                break;
            }
            if (isGameFieldFull()) {
                System.out.println("Ничья");
                break;
            }

            System.out.println("Ход игрока 2 (" + player2sign + "):");
            player2Turn();
            printGameField();
            if (checkWin(player2sign)) {
                System.out.println("Победа игрока 2!");
                break;
            }
            if (isGameFieldFull()) {
                System.out.println("Ничья");
                break;
            }
        }
    }


    void initGameField() {
        Arrays.fill(gameField, SIGN_EMPTY);
    }


    void initOddsField() {

        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                oddsTable[cell][line] = new OddsLine();
            }
        }

        for (cell = 0; cell < oddsTable.length; cell++) {
            line = 0;
            for (String baseLine : OddsLine.BASELINES) {
                if (baseLine.contains(String.valueOf(cell))) {
                    oddsTable[cell][line].setIndexLine(baseLine);
                    line++;
                }
            }
        }
    }


    void instructionForHomo() {
        if (player1 == 0 || player2 == 0)
            System.out.println("Для ввода номера клетки воспользуйтесь клавиатурой NumKeypad (от 1 до 9)");
    }


    void printGameField() {
        System.out.print(gameField[6] + " ");
        System.out.print(gameField[7] + " ");
        System.out.print(gameField[8] + " ");
        System.out.println();
        System.out.print(gameField[3] + " ");
        System.out.print(gameField[4] + " ");
        System.out.print(gameField[5] + " ");
        System.out.println();
        System.out.print(gameField[0] + " ");
        System.out.print(gameField[1] + " ");
        System.out.print(gameField[2] + " ");
        System.out.println("\n_________________");
    }


    void gameModeChoice() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Первым ходит игрок 1. Выберите его (0 - человек, 1 - компьютер): ");
                player1 = scanner.nextInt();
                if (player1 == 0 || player1 == 1) {
                    break;
                }
            } catch (InputMismatchException e) {
                //ignore
            }
            System.err.print("Ошибка! Нажмите 0 или 1: ");
            scanner.skip(".*");
        }

        while (true) {
            try {
                System.out.print("Выберите игрока 2 (0 - человек, 1 - компьютер): ");
                player2 = scanner.nextInt();
                if (player2 == 0 || player2 == 1) {
                    break;
                }
            } catch (InputMismatchException e) {
                //ignore
            }
            System.err.print("Ошибка! Нажмите 0 или 1: ");
            scanner.skip(".*");
        }

        while (true) {
            try {
                System.out.print("Выберите знак игрока 1 (0 - нолики, 1 - крестики): ");
                int playerSign = scanner.nextInt();
                if (playerSign == 0 || playerSign == 1) {
                    if (playerSign == 0) {
                        player1sign = "o";
                        player2sign = "x";
                    }
                    break;
                }
            } catch (InputMismatchException e) {
                //ignore
            }
            System.err.print("Ошибка! Нажмите 0 или 1: ");
            scanner.skip(".*");
        }

        if (player1 == 1 || player2 == 1) {
            while (true) {
                try {
                    System.out.print("Выберите уровень компьютерного игрока: 0 (простой), 1 (средний) или 2 (сложный): ");
                    aiLevel = scanner.nextInt();
                    if (aiLevel == 0 || aiLevel == 1 || aiLevel == 2) {
                        break;
                    }
                } catch (InputMismatchException e) {
                    //ignore
                }
                System.err.print("Ошибка! Нажмите 0, 1 или 2: ");
                scanner.skip(".*");
            }
        }
    }


    void player1Turn(){
        if (player1 == 0) {
            homoTurn(player1sign);
        }
        if (player1 == 1) {
            aiTurn(player1sign, player2sign);
        }
    }


    void player2Turn(){
        if (player2 == 0) {
            homoTurn(player2sign);
        }
        if (player2 == 1) {
            aiTurn(player2sign, player1sign);
        }
    }





    void homoTurn(String signXO) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                System.out.println("Ваш ход: ");
                gameCell = scanner.nextInt() - 1;
            } catch (InputMismatchException e) {
                //ignore
            }
            scanner.skip(".*");
        }
        while (wrongChoice(gameCell));
        gameField[gameCell] = signXO;
        writeField(signXO);
    }


    void aiTurn(String signXO, String anotherSignXO) {
        do {
            switch (aiLevel) {
                case (0):
                    gameCell = (int) (Math.random() * 9);
                    break;
                case (1):
                    if (firstTurnOfAI) {
                        gameCell = (int) (Math.random() * 9);
                        firstTurnOfAI = false;
                    } else {
                        gameCell = cellChoice(signXO, anotherSignXO);
                    }
                    break;
                case (2):
                    if (firstTurnOfAI && gameField[4].equals(SIGN_EMPTY)) {
                        gameCell = 4;
                        firstTurnOfAI = false;
                    } else {
                        gameCell = cellChoice(signXO, anotherSignXO);
                    }
                    break;
            }
        }
        while (wrongChoice(gameCell));
        gameField[gameCell] = signXO;
        writeField(signXO);
    }


    boolean wrongChoice(int gameCell) {
        if ((gameCell < 0 || gameCell >= 9) & (player1 == 0 || player2 == 0)) {
            System.out.println("Нажмите цифровую клавишу от 1 до 9");
            return true;
        }
        return !gameField[gameCell].equals(SIGN_EMPTY);
    }


    boolean isGameFieldFull() {
        int i = 0;
        for (gameCell = 0; gameCell < 9; gameCell++) {
            if (gameField[gameCell].equals(SIGN_EMPTY)) {
                i++;
            }
        }
        return i <= 1;
    }


    boolean checkWin(String dot) {
        if ((gameField[0].equals(dot) && gameField[3].equals(dot) && gameField[6].equals(dot)) ||
                (gameField[1].equals(dot) && gameField[4].equals(dot) && gameField[7].equals(dot)) ||
                (gameField[2].equals(dot) && gameField[5].equals(dot) && gameField[8].equals(dot)) ||
                (gameField[0].equals(dot) && gameField[1].equals(dot) && gameField[2].equals(dot)) ||
                (gameField[3].equals(dot) && gameField[4].equals(dot) && gameField[5].equals(dot)) ||
                (gameField[6].equals(dot) && gameField[7].equals(dot) && gameField[8].equals(dot)))
            return true;
        return (gameField[0].equals(dot) && gameField[4].equals(dot) && gameField[8].equals(dot)) ||
                (gameField[2].equals(dot) && gameField[4].equals(dot) && gameField[6].equals(dot));
    }


    void writeField(String signXO) {
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (gameField[cell].equals(SIGN_EMPTY)
                        && oddsTable[cell][line].getIndexLine().contains(String.valueOf(gameCell))) {
                    oddsTable[cell][line].OddsPlus(signXO);
                }
            }
        }
    }


    int cellChoice(String signXO, String anotherSignXO) {
        int desiredCell;

        if ((desiredCell = preWinCellSearch(signXO)) != 9) //ИСТИНА, если нашлась подходящая cell
            return desiredCell;

        if ((desiredCell = preWinCellSearch(anotherSignXO)) != 9)
            return desiredCell;

        if ((desiredCell = usualCellSearch(signXO)) != 9)
            return desiredCell;

        //если линия с odds=1 есть лишь в одной клетке, выбираем для хода ее
        if (cell1Counter == 1) {
            return cellMark;//cellMark инициализирована в методе usualCellSearch()
        } //выбираем случайно одну из клеток, где линии имеют одну odds=1
        else if (cell1Counter > 1) {
            if ((desiredCell
                    = iteratingOverArray(1, signXO, cell1Counter)) != 9) {
                return desiredCell;
            }
        } //выбираем случайно одну из клеток, где линии вообще не имеют odds>0
        else if (cell0Counter > 1) {
            if ((desiredCell = iteratingOverArray(0, signXO, cell0Counter)) != 9) {//!!!всегда будет ИСТИНА
                return desiredCell;
            }
        }
        return cell;
    }


    //перебор массива и поиск подходящей клетки "за шаг до победы"
    int preWinCellSearch(String signXO) {//oddsValue всегда равна 2, да и пусть
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
    int usualCellSearch(String signXO) {
        int checkValue = 9;
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (isCellRight(1, cell, line, signXO)) {
                    line1Counter++;
                    cellMark = cell; //помечаем клетку, для которой начали считать число линий с odds=1
                    if (line1Counter == 2) {
                        return cell;
                    }
                    if (line1Counter == 1) {
                        cell1Counter++;
                    }
                } else if (isCellRight(0, cell, line, signXO)) {
                    cell0Counter++;
                }
            }
            line1Counter = 0;
        }
        return checkValue;
    }


    //перебор массива и случайный выбор одной из подходящих клеток "за два шага до победы"
    int iteratingOverArray(int oddsValue, String signXO, int Cell10Counter) {
        int checkValue = 9;
        int randomTurn = (int) (Math.random() * Cell10Counter);
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
    boolean isCellRight(int oddsValue, int cell, int line, String signXO) {
        return (gameField[cell].equals(SIGN_EMPTY)
                && !oddsTable[cell][line].getIndexLine().equals("")
                && oddsTable[cell][line].isOnlyOneSign()
                && oddsTable[cell][line].getOdds(signXO) == oddsValue);
    }
}

class OddsLine {
    private int odds_O = 0;
    private int odds_X = 0;
    private String indexLine = "";

    static final String[] BASELINES = {"678", "345", "012", "630", "741", "852", "048", "642"};

    public void setIndexLine(String indexLine) {
        this.indexLine = indexLine;
    }

    public String getIndexLine() {
        return indexLine;
    }

    public void OddsPlus(String signXO) {
        if (signXO.equals("x")) {
            this.odds_X++;
        } else this.odds_O++;
    }

    public int getOdds(String signXO) {
        if (signXO.equals("x")) {
            return odds_X;
        } else return odds_O;
    }

    public boolean isOnlyOneSign() {
        return this.odds_O == 0 | this.odds_X == 0;
    }
}