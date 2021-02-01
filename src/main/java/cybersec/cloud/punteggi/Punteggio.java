package cybersec.cloud.punteggi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Punteggio {
    
    private String giocatore;
    private int punteggio;
    
    public Punteggio() {
        // Ci pensa Jackson
    }
    
    public Punteggio(String giocatore, int punteggio) {
        this.giocatore = giocatore;
        this.punteggio = punteggio;
    }
    
    @JsonProperty
    public String getGiocatore() {
        return this.giocatore;
    }
    
    @JsonProperty
    public int getPunteggio() {
        return this.punteggio;
    }
    
}
