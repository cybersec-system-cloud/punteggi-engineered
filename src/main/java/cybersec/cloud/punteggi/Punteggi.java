package cybersec.cloud.punteggi;

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

@Path("/punteggi")
@Produces(MediaType.APPLICATION_JSON)
public class Punteggi {
    
    private final List<Punteggio> punteggi;
    private final int punteggioIniziale;
    
    public Punteggi(int punteggioIniziale) {
        punteggi = new ArrayList<Punteggio>();
        this.punteggioIniziale = punteggioIniziale;
    }
    
    @POST
    public Response postPunteggio(@QueryParam("giocatore") Optional<String> giocatore, 
            @QueryParam("punteggio") Optional<Integer> punteggio) {
        
        // Se "giocatore" non viene specificato, 
        // restituisce un messaggio di errore (BAD REQUEST)
        if(!giocatore.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Il nickname del giocatore non è specificato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se il giocatore è già presente nella collezione,
        // restituisce un messaggio di errore (CONFLICT)
        if(indicePunteggio(giocatore.get()) != -1) {
            return Response.status(Status.CONFLICT)
                    .entity("Giocatore già inserito in precedenza")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Estrae il punteggio da assegnare al giocatore, se presente
        // altrimenti usa quello di default
        int punti = punteggioIniziale;
        if(punteggio.isPresent()) punti = punteggio.get();
        
        // Se il punteggio da associare a "giocatore" è negativo
        // restituisce un messaggio di errore (BAD REQUEST)
        if (punti < 0) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Non è possibile inserire punteggi negativi")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se sono arrivato qui, non ci sono errori! 
        
        // Aggiunge il nuovo Punteggio "giocatore,punti"
        Punteggio p = new Punteggio(giocatore.get(),punti);
        punteggi.add(p);
        
        // Dopo aver aggiunto il nuovo giocatore, risponde
        // al cliente con un messaggio opportuno (CREATED)
        URI u = UriBuilder.fromResource(Punteggi.class).path(giocatore.get()).build();
        return Response.created(u)
                .build();
    }
    
    private int indicePunteggio(String giocatore) {
        for(int i=0; i<punteggi.size(); i++) {
            if(punteggi.get(i).getGiocatore().equals(giocatore))
                return i;
        }
        return -1;
    }
    
    @GET
    @Path("/{giocatore}")
    public Response getPunteggio(@PathParam("giocatore") String giocatore) {
        int i = indicePunteggio(giocatore);
            
        // Se il punteggio di "giocatore" non c'è, 
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Giocatore non presente nel sistema")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se sono arrivato qui, non ci sono errori! 
        
        // Altrimenti, restituisce il punteggio associato a 
        // quel giocatore (dicendo anche 200 OK)
        return Response.ok()
                .entity(punteggi.get(i))
                .build();
    }
    
    @PUT
    @Path("/{giocatore}")
    public Response putPunteggio(@PathParam("giocatore") String giocatore,
            @QueryParam("punteggio") Optional<Integer> punteggio) {
        int i = indicePunteggio(giocatore);
        
        // Se il punteggio da aggiornare non c'è,
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Il punteggio da aggiornare non è presente nel sistema")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se il punteggio non è specificato,
        // restituisce un messaggio di errore (BAD REQUEST)
        if(!punteggio.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Non è stato inserito il punteggio")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se il punteggio da inserire è negativo,
        // restituisce un messaggio di errore (BAD REQUEST)
        if (punteggio.get() < 0) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Non è possibile inserire punteggi negativi")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se sono arrivato qui, non ci sono errori!
        
        // Altrimenti, aggiorna il punteggio e 
        // restituisce un messaggio di conferma (OK)
        punteggi.remove(i);
        punteggi.add(new Punteggio(giocatore,punteggio.get()));
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/{giocatore}")
    public Response deletePunteggio(@PathParam("giocatore") String giocatore) {
        int i = indicePunteggio(giocatore);
        
        // Se il punteggio da eliminare non c'è,
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Il punteggio da eliminare non è presente nel sistema")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se sono arrivato qui, non ci sono errori!
        
        // Elimina il punteggio e restituisce un messaggio di conferma (OK)
        punteggi.remove(i);
        return Response.ok().build();
    }
}
