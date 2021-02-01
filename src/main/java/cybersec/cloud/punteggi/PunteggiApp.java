package cybersec.cloud.punteggi;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class PunteggiApp extends Application<PunteggiConfig> {
    
    public static void main(String[] args) throws Exception {
        new PunteggiApp().run(args);
    }

    @Override
    public void run(PunteggiConfig c, Environment e) throws Exception {
        final Punteggi risorsaPunteggi = new Punteggi(c.getPunteggioIniziale());
        e.jersey().register(risorsaPunteggi);
    }
    
}
