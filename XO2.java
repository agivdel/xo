package Agivdel;

import java.util.Arrays;
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
 *
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

    private final String SIGN_X = "x";
    private final String SIGN_O = "o";
    private final String SIGN_EMPTY = ".";

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


    public static void main(String[] args) {
        new XO2().game();
    }


    void game() {
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
                System.out.println("Ничья:");
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
                System.out.println("Ничья:");
                break;
            }
        }
        printNumericKeypad();
        System.out.println("Конец игры");
    }


    void initGameField() {
        Arrays.fill(gameField, SIGN_EMPTY);
    }


    void initOddsField() {
        String[] baseStringLines =  {
                "678",
                "345",
                "012",
                "630",
                "741",
                "852",
                "048",
                "642"
        };

        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                oddsTable[cell][line] = new OddsLine();
            }
        }

        for (cell = 0; cell < oddsTable.length; cell++) {
            line = 0;
            for (String baseStringLine : baseStringLines) {
                if (baseStringLine.contains(String.valueOf(cell))) {
                    oddsTable[cell][line].setStringLine(baseStringLine);
                    line++;
                }
            }
        }
    }


    void instruction() {
        if (player1 == 0 | player2 == 0)
            System.out.println("Для ввода номера клетки воспользуйтесь клавиатурой NumKeypad (от 0 до 9)");
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
        System.out.println();
    }


    void gameModeChoice() {
        Scanner ch = new Scanner(System.in);
        int pl;
        System.out.print("Первым ходит игрок 1. Выберите его (0 - человек, 1 - компьютер): ");
        do {
            //while (!ch.hasNextInt()) {
            //System.out.println("Неверный ввод. Повторите");
            //ch.nextInt();
            //}
            player1 = ch.nextInt();
            //ch.nextLine();
        }
        while (player1 < 0 || player1 > 1);

        System.out.print("Выберите игрока 2 (0 - человек, 1 - компьютер): ");
        do {
            //while (!ch.hasNextInt()) {
            //System.out.println("Неверный ввод. Повторите");
            //ch.nextInt();
            //}
            player2 = ch.nextInt();
            //ch.nextLine();
        }
        while (player2 < 0 || player2 > 1);

        System.out.print("Выберите знак игрока 1 (0 - нолики, 1 - крестики): ");
        int playerSign;
        do {

            //while (!ch.hasNextInt()) {
            //System.out.println("Неверный ввод. Повторите");
            //ch.nextInt();
            //}
            playerSign = ch.nextInt();
        }
        while (playerSign < 0 || playerSign > 1);
        if (playerSign == 0) {
            player1sign = SIGN_O;
            player2sign = SIGN_X;
        }
        if (playerSign == 1) {
            player1sign = SIGN_X;
            player2sign = SIGN_O;
        }

        if (player1 == 1 || player2 == 1) {
            do {

                System.out.print("Выберите уровень компьютерного игрока: 0 (простой), 1 (средний) или 2 (сложный): ");
                aiLevel = ch.nextInt();
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
                aiTurn(player1sign);
            }
            firstTurn = false;
        } else {
            if (player2 == 0) {
                homoTurn(player2sign);
            }
            if (player2 == 1) {
                aiTurn(player2sign);
            }
            firstTurn = true;
        }
    }


    void homoTurn(String signXO) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Ваш ход:");
            gameCell = sc.nextInt() - 1;
        }
        while (wrongChoice(gameCell));
        gameField[gameCell] = signXO;
        writeField(signXO);
    }


    void aiTurn(String signXO) {
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
                        gameCell = choice(SIGN_O);
                    }
                    break;
                case (2):
                    if (firstTurnOfAI && gameField[4].equals(SIGN_EMPTY)) {
                        gameCell = 4;
                        firstTurnOfAI = false;
                    } else {
                        gameCell = choice(signXO);
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
        //проверка на заполнение вертикалей и горизонталей
        if ((gameField[0].equals(dot) && gameField[3].equals(dot) && gameField[6].equals(dot)) ||
                (gameField[1].equals(dot) && gameField[4].equals(dot) && gameField[7].equals(dot)) ||
                (gameField[2].equals(dot) && gameField[5].equals(dot) && gameField[8].equals(dot)) ||
                (gameField[0].equals(dot) && gameField[1].equals(dot) && gameField[2].equals(dot)) ||
                (gameField[3].equals(dot) && gameField[4].equals(dot) && gameField[5].equals(dot)) ||
                (gameField[6].equals(dot) && gameField[7].equals(dot) && gameField[8].equals(dot)))
            return true;
        //проверка на заполнение диагоналей
        return (gameField[0].equals(dot) && gameField[4].equals(dot) && gameField[8].equals(dot)) ||
                (gameField[2].equals(dot) && gameField[4].equals(dot) && gameField[6].equals(dot));
    }


    void writeField(String signXO) {
        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (gameField[cell].equals(SIGN_EMPTY)
                        && oddsTable[cell][line].getStringLine().contains(String.valueOf(gameCell))) {
                    oddsTable[cell][line].OddsPlus(signXO);
                }
            }
        }
    }

    /**Попробуй функцию оценки вынести в отдельный класс. Чтобы она принимала поле и игрока.
     * И возвращала массив возможных ходов, отсортированный в порядке оптимальности
     */
    int choice(String signXO) {
        int line1Counter = 0;
        int cellMark = 0;
        int cell1Counter = 0;
        int cell0Counter = 0;
        int randomTurn;

        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (gameField[cell].equals(SIGN_EMPTY)
                        && !oddsTable[cell][line].getStringLine().equals("")
                        && oddsTable[cell][line].getOdds(signXO) == 2) {
                    return cell;
                }
            }
        }

        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (gameField[cell].equals(SIGN_EMPTY)
                        && !oddsTable[cell][line].getStringLine().equals("")
                        && oddsTable[cell][line].getAnotherOdds(signXO) == 2) {
                    return cell;
                }
            }
        }

        for (cell = 0; cell < oddsTable.length; cell++) {
            for (line = 0; line < oddsTable[cell].length; line++) {
                if (gameField[cell].equals(SIGN_EMPTY)
                        && !oddsTable[cell][line].getStringLine().equals("")
                        && oddsTable[cell][line].isOnlyOneSign()
                        && oddsTable[cell][line].getOdds(signXO) == 1) {
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
                else if (gameField[cell].equals(SIGN_EMPTY)
                        && !oddsTable[cell][line].getStringLine().equals("")
                        && oddsTable[cell][line].getOdds(signXO) == 0) {
                    cell0Counter++;
                }
            }
            line1Counter = 0;
        }

        //если пришли сюда, значит, в таблице шансов нет клеток с odds=2 (своих или чужих),
        //нет клеток, в которых есть две линии с odds=1 (line1Counter=2),
        //а остались лишь клетки, в каждой из которых есть:
        //либо одна линия с odds=1 (число таких клеток равно cell1Counter),
        //либо все линии имеют odds=0 (число  таких клекто равно cell0Counter).

        //если линия с odds=1 есть лишь в одной клетке, выбираем для хода ее
        if (cell1Counter == 1) {
            return cellMark;//если лишь одна клетка имеет линию с odds=1, для нее уже сработала метка cellMark
        }
        //если есть несколько клеток с одной линией с odds=1, выбираем случайно порядковый номер одной из этих клеток
        else if (cell1Counter > 1) {
            randomTurn = (int) (Math.random() * cell1Counter);
            cell1Counter = 0;
            for (cell = 0; cell < oddsTable.length; cell++) {
                for (line = 0; line < oddsTable[cell].length; line++) {
                    if (gameField[cell].equals(SIGN_EMPTY)
                            && !oddsTable[cell][line].getStringLine().equals("")
                            && oddsTable[cell][line].isOnlyOneSign()
                            && oddsTable[cell][line].getOdds(signXO) == 1) {
                        //когда порядковый номер клетки будет равен случайному randomTurn
                        if (cell1Counter == randomTurn) {
                            return cell;
                        }
                        cell1Counter++;
                    }
                }
            }
        }
        //если есть несколько клеток, где линии не имеют odds>0, выбираем случайно одну из этих клеток
        else if (cell0Counter > 1) {
            randomTurn = (int) (Math.random() * cell0Counter);
            cell0Counter = 0;
            for (cell = 0; cell < oddsTable.length; cell++) {
                for (line = 0; line < oddsTable[cell].length; line++) {
                    if (gameField[cell].equals(SIGN_EMPTY)
                            && !oddsTable[cell][line].getStringLine().equals("")
                            && oddsTable[cell][line].getOdds(signXO) == 0) {
                        //когда порядковый номер клетки будет равен случайному randomTurn
                        if (cell0Counter == randomTurn) {
                            return cell;
                        }
                        cell0Counter++;
                    }
                }
            }
        }
        return cell;
    }

}

class OddsLine {
    private int odds_O = 0;
    private int odds_X = 0;
    private String stringLine = "";

    public void setStringLine(String stringLine) {
        this.stringLine = stringLine;
    }

    public String getStringLine() {
        return stringLine;
    }

    public void OddsPlus(String signXO) {
        if (signXO.equals("x")) {
            this.odds_X++;
        }
        else {
            this.odds_O++;
        }
    }

    public int getOdds(String signXO) {
        if (signXO.equals("x")) {
            return odds_X;
        }
        return odds_O;
    }

    public int getAnotherOdds(String signXO) {
        if (signXO.equals("o")) {
            return odds_X;
        }
        return odds_O;
    }

    public boolean isOnlyOneSign() {
        return this.odds_O == 0 | this.odds_X == 0;
    }
}