package Agivdel.XO.Domain;

import Agivdel.XO.Domain.Fin;

class Odds {

    private int odds_O = 0;
    private int odds_X = 0;
    private String indexLine = "";

    void setIndexLine(String indexLine) {
        this.indexLine = indexLine;
    }

    public String getIndexLine() {
        return indexLine;
    }

    void oddsPlus(String signXO) {
        if (signXO.equals(Fin.X)) {
            this.odds_X++;
        } else this.odds_O++;
    }

    public int getOdds(String signXO) {
        if (signXO.equals(Fin.X)) {
            return odds_X;
        } else return odds_O;
    }

    public boolean isOnlyOneSign() {
        return this.odds_O == 0 | this.odds_X == 0;
    }
}