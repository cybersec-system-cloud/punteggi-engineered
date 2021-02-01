package cybersec.cloud.punteggi;

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
    public void postPunteggio(@QueryParam("giocatore") String giocatore, 
            @QueryParam("punteggio") Optional<Integer> punteggio) {
        
        Punteggio p;
        if(punteggio.isPresent())
            p = new Punteggio(giocatore,punteggio.get());
        else
            p = new Punteggio(giocatore,punteggioIniziale);
        
        punteggi.add(p);
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
