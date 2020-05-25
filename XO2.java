package Agivdel;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Крестики-нолики с проверкой ИИ вероятностей победы для каждого хода.
 * Используем две таблицы: одно- и двухмерный массивы.
 * Все игровое поле (все массивы) делится на клетки (9*cell).
 * В 2- массиве (таблица подсчета шансов OddsTable) каждая клетка делится на линии (4*line).
 * Каждая линия (объект класса OddsLine) содержит поля:
 * 1) odds: это степень заполнения одной игровой вертикали/горизонтали/диагонали в долях от трех
 * (odds = 1 означает заполненную на одну третью какую-либо вертикаль/горизонталь/диагональ,
 * проходящюю через клетку), отдельно для кретсиков и для ноликов.
 * 2) stringLine: индексная строка, содержащая три цифры, соответствующие трем клеткам,
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

    private final String SIGN_EMPTY = "." ;

    String[] gameField = new String[9];
    OddsLine[][] oddsTable = new OddsLine[9][4];

    private int gameCell;
    private int line;
    private int cell;

    private int aiLevel;
    boolean firstTurnOfAI = true;
    boolean firstTurn = true;
    private int player1;
    private int player2;
    private String player1sign;
    private String player2sign;

    public static void main(String[] args) /*throws Exception*/ {
        new XO2().game();
    }


    void game() /*throws Exception*/ {
        initGameField();
        initOddsField();
        gameModeChoice();
        instruction();
        while (true) {
            System.out.println("Ход игрока 1 (" + player1sign + "):");
            playerTurn();
            printNumericKeypad();
            if (checkWin(player1sign)) {
                System.out.println("Победа игрока 1!");
                break;
            }
            if (isGameFieldFull()) {
                System.out.println("Ничья");
                break;
            }

            System.out.println("Ход игрока 2 (" + player2sign + "):");
            playerTurn();
            printNumericKeypad();
            if (checkWin(player2sign)) {
                System.out.println("Победа игрока 2!");
                break;
            }
            if (isGameFieldFull()) {
                System.out.println("Ничья");
                break;
            }
        }
        //printNumericKeypad();
        //System.out.println("Конец игры");
    }


    void initGameField() {
        Arrays.fill(gameField, SIGN_EMPTY);
    }


    void initOddsField() {
        String[] baseLines = {"678", "345", "012", "630", "741", "852", "048", "642"};

        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                oddsTable[cell][line] = new OddsLine();
            }
        }

        for (cell = 0; cell < oddsTable.length; cell++) {
            line = 0;
            for (String baseStringLine : baseLines) {
                if (baseStringLine.contains(String.valueOf(cell))) {
                    oddsTable[cell][line].setStringLine(baseStringLine);
                    line++;
                }
            }
        }
    }


    //добавить защиту от неверного ввода
    void instruction() {
        if (player1 == 0 || player2 == 0)
            System.out.println("Для ввода номера клетки воспользуйтесь клавиатурой NumKeypad (от 1 до 9)");
    }


    void printNumericKeypad() {
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


    void gameModeChoice() /*throws Exception*/ {
        int playerSign = 0;
        int pl = 0;
        Scanner ch = new Scanner(System.in);
        do {
            try {
                System.out.print("Первым ходит игрок 1. Выберите его (0 - человек, 1 - компьютер): ");
                pl = ch.nextInt();
                player1 = pl;
//                if (pl < 0 || pl > 1) {
//                    throw new Exception();
//                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка! Нажмите 0 или 1");
            }
        }
        while (player1 < 0 || player1 > 1);


        do {
            try {
                System.out.print("Выберите игрока 2 (0 - человек, 1 - компьютер): ");
                pl = ch.nextInt();
                player2 = pl;
//                if (pl < 0 || pl > 1) {
//                    throw new Exception();
//                }
            } catch (Exception e) {
                System.out.println("Ошибка! Нажмите 0 или 1");
            }
        }
        while (pl < 0 || pl > 1);

        do {
            try {
                System.out.print("Выберите знак игрока 1 (0 - нолики, 1 - крестики): ");
                pl = ch.nextInt();
                playerSign = pl;
//                if (pl < 0 || pl > 1) {
//                    throw new Exception();
//                }
            } catch (Exception e) {
                System.out.println("Ошибка! Нажмите 0 или 1");
            }
        }
        while (pl < 0 || pl > 1);


        if (playerSign == 0) {
            player1sign = "o" ;
            player2sign = "x" ;
        }
        if (playerSign == 1) {
            player1sign = "x" ;
            player2sign = "o" ;
        }

        if (player1 == 1 || player2 == 1) {
            do {
                try {

                    System.out.print("Выберите уровень компьютерного игрока: 0 (простой), 1 (средний) или 2 (сложный): ");
                    aiLevel = ch.nextInt();
                } catch (final InputMismatchException e) {
                    System.out.println("Ошибка! Нажмите 0, 1 или 2");
                }
            }
            while (aiLevel < 0 || aiLevel > 2);
        }
    }


    void playerTurn() {

        if (firstTurn) {
            if (player1 == 0) {
                homoTurn(player1sign);
            }
            if (player1 == 1) {
                aiTurn(player1sign, player2sign);
            }
            firstTurn = false;
        } else {
            if (player2 == 0) {
                homoTurn(player2sign);
            }
            if (player2 == 1) {
                aiTurn(player2sign, player1sign);
            }
            firstTurn = true;
        }
    }


    //добавить защиту от ошибки ввода
    void homoTurn(String signXO) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Ваш ход: ");
            gameCell = sc.nextInt() - 1;
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
                        gameCell = choice(signXO, anotherSignXO);
                    }
                    break;
                case (2):
                    if (firstTurnOfAI && gameField[4].equals(SIGN_EMPTY)) {
                        gameCell = 4;
                        firstTurnOfAI = false;
                    } else {
                        gameCell = choice(signXO, anotherSignXO);
                    }
                    break;
            }
        }
        while (wrongChoice(gameCell));
        gameField[gameCell] = signXO;
        writeField(signXO);
    }


    boolean wrongChoice(int gameCell) {
        if (gameCell < 0 || gameCell >= 9) {
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

    /**
     * Попробуй функцию оценки вынести в отдельный класс. Чтобы она принимала поле и игрока.
     * И возвращала массив возможных ходов, отсортированный в порядке оптимальности
     */
    int choice(String signXO, String anotherSignXO) {
        int line1Counter = 0;
        int cellMark = 0;
        int cell1Counter = 0;
        int cell0Counter = 0;

        //проверяем массив на наличие клеток сначала со своим odds=2, а затем с чужим.
        if (iteratingOverArray(2, signXO) != 9 ) //ИСТИНА, если нашлась подходящая cell
            return iteratingOverArray(2, signXO);

        if (iteratingOverArray(2, anotherSignXO) != 9 )
            return iteratingOverArray(2, anotherSignXO);

        //проверяем массив на наличие клеток со своими odds=1 и odds=0
        for (cell = 0; cell < oddsTable.length; cell++) {//перебор массива, возврат cell
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (isCellRight(1, cell, line, signXO)) {
                    line1Counter++; //начали считать число линий с odds=1
                    cellMark = cell; //помечаем клетку, для которой начали считать число линий с odds=1
                    // Если таких линий 2 в данной клетке, выбираем клетку этой линии для хода
                    if (line1Counter == 2) {
                        return cell;
                    }
                    // Если такая линия 1 в данной клетке, начинаем считать такие же клетки (с одной линией)
                    if (line1Counter == 1) {
                        cell1Counter++;
                    }
                }
                // Если таких линий нет в данной клетке, начинаем считать такие же клетки (без линий)
                else if (isCellRight(0, cell, line, signXO)) {
                    cell0Counter++;
                }
            }
            line1Counter = 0;
        }

        //если линия с odds=1 есть лишь в одной клетке, выбираем для хода ее
        if (cell1Counter == 1) {
            return cellMark;
        }

        //выбираем случайно одну из клеток, где линии не имеют odds=1
        else if (cell1Counter > 1) {//если уж дошло до вызова этого метода, он всегда будет возвращать НЕ "9"
            if (iteratingOverArray(1, signXO, cell1Counter) != 9) {//!!!всегда будет ИСТИНА
                return iteratingOverArray(1, signXO, cell1Counter);
            }
        }

        //выбираем случайно одну из клеток, где линии не имеют odds>0
        else if (cell0Counter > 1) {
            if (iteratingOverArray(0, signXO, cell0Counter) != 9) {
                return iteratingOverArray(0, signXO, cell0Counter);
            }
        }
        return cell;
    }

    //перебор массива и поиск подходящей клетки
    int iteratingOverArray(int oddsValue, String signXO) {
        int checkValue = 9;//"9" не используется для обозначения клеток игрового поля, поэтому ее безопасно применять
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (isCellRight(oddsValue, cell, line, signXO)) {
                    return cell;
                }
            }
        }
        return checkValue;
    }

    //перебор массива и случайный выбор одной из подходящих клеток
    int iteratingOverArray(int oddsValue, String signXO, int Cell0Counter) {
        int checkValue = 9;//"9" не используется для обозначения клеток игрового поля, поэтому ее безопасно применять
        int randomTurn = (int) (Math.random() * Cell0Counter);
        int aCell0Counter = 0;
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (isCellRight(oddsValue, cell, line, signXO)) {
                    if (aCell0Counter == randomTurn) {
                        return cell;
                    }
                    aCell0Counter++;
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
    private String indexLine = "" ;

    public void setStringLine(String stringLine) {
        this.indexLine = stringLine;
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