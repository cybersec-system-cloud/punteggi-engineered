package cybersec.cloud.utenti;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("/utenti")
@Produces(MediaType.APPLICATION_JSON)
public class Utenti {
    
    private final List<Utente> utenti;
    
    public Utenti() {
        utenti = new ArrayList<Utente>();
    }
    
    @POST
    public Response aggiungiUtente(Utente u) {
        // Se esiste un utente con lo stesso "nickname"
        // restituisce un messaggio di errore (CONFLICT)
        if(indiceUtente(u.getNickname()) != -1)
            return Response.status(Status.CONFLICT)
                    .entity("L'utente " + u.getNickname() + " esiste già")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Aggiungere l'utente "u" alla collezione di "utenti"
        utenti.add(u);
        
        // Crea la URI per il nuovo utente e
        // la restituisce al cliente (CREATED)
        URI uUri = UriBuilder.fromResource(Utenti.class)
                    .path(u.getNickname())
                    .build();
        return Response.created(uUri).build();
    }

    private int indiceUtente(String nickname) {
        for(int i=0; i<utenti.size(); i++) {
            if(utenti.get(i).getNickname().equals(nickname))
                return i;
        }
        return -1;
    }
    
    @GET
    @Path("/{nickname}")
    public Response recuperaUtente(@PathParam("nickname") String nickname) {
        int i = indiceUtente(nickname);
        
        // Se il nickname non corrisponde ad alcun utente,
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Utente non presente nel sistema")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Altrimenti, restituisce le informazioni dell'utente (OK)
        return Response.ok()
                .entity(utenti.get(i))
                .build();
    }
    
    @PUT
    @Path("/{nickname}")
    public Response aggiornaUtente(@PathParam("nickname") String nickname,
            @QueryParam("cognome") Optional<String> cognome,
            @QueryParam("nome") Optional<String> nome,
            @QueryParam("email") Optional<String> email) {
        int i = indiceUtente(nickname);
        
        // Se non esiste un utente associato a "nickname"
        // restituisce un messaggio di errore (NOT FOUND)
        if(i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Utente " + nickname + "non presente")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se nessuno tra "cognome", "nome" e "email" è specificato
        // restituisce un messaggio di errore (BAD REQUEST)
        if(!cognome.isPresent() && !nome.isPresent() && !email.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Almeno uno tra cognome, nome e email deve essere indicato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Aggiorna l'utente nella collezione "utenti"
        Utente u = utenti.get(i);
        String c = u.getCognome();
        if(cognome.isPresent()) c = cognome.get();
        String n = u.getNome();
        if(nome.isPresent()) n = nome.get();
        String e = u.getEmail();
        if(email.isPresent()) e = email.get();
        utenti.remove(i);
        utenti.add(new Utente(nickname,c,n,e));
        
        // Restituisce un messaggio di conferma dell'aggiornamento (OK)
        return Response.ok()
                .entity("utente aggiornato")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
    @DELETE
    @Path("/{nickname}")
    public Response cancellaUtente(@PathParam("nickname") String nickname) {
        int i = indiceUtente(nickname);
        
        // Se l'utente non è presente nel sistema
        // restituisce un messaggio di errore (NOT FOUND)
        if(i==-1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Utente " + nickname + " non presente")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Altrimenti, elimina l'utente
        utenti.remove(i);
        
        // Restituisce un messaggio di conferma eliminazione (OK)
        return Response.ok()
                .entity("utente eliminato")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
