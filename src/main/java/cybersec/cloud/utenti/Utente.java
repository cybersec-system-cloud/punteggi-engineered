package cybersec.cloud.utenti;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Utente {
    
    private String nickname;
    private String nome;
    private String cognome;
    private String email;
    
    public Utente() {
        // Ci pensa Jackson
    }
    
    public Utente(String nickname, String nome, String cognome, String email) {
        this.nickname = nickname;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    
    @JsonProperty
    public String getNickname() {
        return this.nickname;
    }
    
    @JsonProperty
    public String getNome() {
        return this.nome;
    }
    
    @JsonProperty
    public String getCognome() {
        return this.cognome;
    }
    
    @JsonProperty
    public String getEmail() {
        return this.email;
    }
    
}
