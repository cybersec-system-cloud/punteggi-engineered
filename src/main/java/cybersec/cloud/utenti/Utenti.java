package cybersec.cloud.utenti;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/utenti")
@Produces(MediaType.APPLICATION_JSON)
public class Utenti {
    
    private final List<Utente> utenti;
    
    public Utenti() {
        utenti = new ArrayList<Utente>();
    }
    
    // Da implementare
    
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
        
        // Da implementare
        return null;
    }
}
