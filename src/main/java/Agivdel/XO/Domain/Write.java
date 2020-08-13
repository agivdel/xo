package Agivdel.XO.Domain;

/**
 * Объект класса Writer создается в конце хода игрока.
 * Обновление данных Data происходит только в данном методе write().
 * Каждый раз при создании нового объекта Writer поля data присваиваются полям объекта Writer
 * (т.е. перед новой записью данных в winLines и oddsTable мы используем для проверки данные с последней записи).
 */
public class Write {
    private final String[] gameTable;
    private final String[] winLines;
    private final Odds[][] oddsTable;

    public Write() {
        Data data = new Data();//берем данные с предыдущего вызова метода write() (т.е. с последнего обновления)
        this.gameTable = data.getGameTable();
        this.winLines = data.getWinLines();
        this.oddsTable = data.getOddsTable();

    }

    void write(int gameCell, String signXO) {
        gameTable[gameCell] = signXO;//1) вписываем в клетку знак игрока - крестик или нолик

        String cellNum = String.valueOf(gameCell);
        for (int i = 0; i < Fin.BASE_LINES.length; i++) {
            if (Fin.BASE_LINES[i].contains(cellNum)) {
                winLines[i] = winLines[i].replace(cellNum, signXO);
            }
        }

        Check check = new Check();
        for (int cell = 0; cell < oddsTable.length; cell++) {
            for (int line = 0; line < oddsTable[cell].length; line++) {
                if (check.isEmpty(cell) && oddsTable[cell][line].getIndexLine().contains(cellNum)) {
                    oddsTable[cell][line].oddsPlus(signXO);
                }
            }
        }
    }
}
