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
    public Response postPunteggio(@QueryParam("giocatore") String giocatore, 
            @QueryParam("punteggio") Optional<Integer> punteggio) {
        
        // Se il giocatore è già presente nella collezione,
        // restituisce un messaggio di errore (CONFLICT)
        if(indicePunteggio(giocatore) != -1) {
            return Response.status(Status.CONFLICT)
                    .entity("Giocatore già inserito in precedenza")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se il giocatore non è presente, lo aggiunge
        // (utilizzando il punteggio di default, se non specificato)
        Punteggio p;
        if(punteggio.isPresent())
            p = new Punteggio(giocatore,punteggio.get());
        else
            p = new Punteggio(giocatore,punteggioIniziale);
        punteggi.add(p);
        
        // Dopo aver aggiunto il nuovo giocatore, risponde
        // al cliente con un messaggio opportuno (CREATED)
        URI u = UriBuilder.fromResource(Punteggi.class).path(giocatore).build();
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
    public Punteggio getPunteggio(@PathParam("giocatore") String giocatore) {
        int i = indicePunteggio(giocatore);
        if (i != -1)
            return punteggi.get(i);
        return null;
    }
    
    @PUT
    @Path("/{giocatore}")
    public void putPunteggio(@PathParam("giocatore") String giocatore,
            @QueryParam("punteggio") int punteggio) {
        int i = indicePunteggio(giocatore);
        if (i != -1) {
            punteggi.remove(i);
            punteggi.add(new Punteggio(giocatore,punteggio));
        }
    }
    
    @DELETE
    @Path("/{giocatore}")
    public void deletePunteggio(@PathParam("giocatore") String giocatore) {
        int i = indicePunteggio(giocatore);
        if (i != -1)
            punteggi.remove(i);
    }
    
}
