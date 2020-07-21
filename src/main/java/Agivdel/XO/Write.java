package Agivdel.XO;

/**
 * Объект класса Writer создается в конце хода игрока.
 * Обновление данных Data происходит только в данном методе write().
 * Каждый раз при создании нового объекта Writer поля data присваиваются полям объекта Writer
 * (т.е. перед новой записью данных в winLines и oddsTable мы используем для проверки данные с последней записи).
 *
 */
public class Write {
    private final Data data;
    private final String[] gameTable;
    private final String[] winLines;
    private final Odds[][] oddsTable;

    public Write() {
        this.data = new Data();//берем данные с предыдущего вызова метода write() (т.е. с последнего обновления)
        this.gameTable = data.getGameTable();
        this.winLines = data.getWinLines();
        this.oddsTable = data.getOddsTable();
    }

    void write(int gameCell, String signXO) {
        gameTable[gameCell] = signXO;//1) вписываем в клетку знак игрока - крестик или нолик
        for (int i = 0; i < Fin.BASE_LINES.length; i++) {//2)
            if (Fin.BASE_LINES[i].contains(String.valueOf(gameCell))) {
                winLines[i] = winLines[i].replace(String.valueOf(gameCell),signXO);
            }
        }
        for (int cell = 0; cell < oddsTable.length; cell++) {//3)
            for (int line = 0; line < oddsTable[cell].length; line++) {
                if (new Check(data).isEmpty(cell) && oddsTable[cell][line].getIndexLine().contains(String.valueOf(gameCell))) {
                    oddsTable[cell][line].oddsPlus(signXO);
                }
            }
        }
    }
}
