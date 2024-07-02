/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole.conf;

import it.mds.sdk.connettore.anagrafiche.conf.Configurazione;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@Getter
@Component("configurazioneFlussoAvnNonSommMobilita")
public class ConfigurazioneFlussoAvnNonSomMobilita {

    private static final String FILE_CONF = "config-flusso-avn-non-som-mob.properties";

    Rules rules;
    XmlOutput xmlOutput;
    Flusso flusso;
    SogliaInvio sogliaInvio;

    private Certificato certificato;
    private Sent sent;
    private NomeFlusso nomeFLusso;

    private DimensioneBlocco dimensioneBlocco;

    private Separatore separatore;

    public ConfigurazioneFlussoAvnNonSomMobilita() {
        this(leggiConfigurazioneEsterna());
    }

    public ConfigurazioneFlussoAvnNonSomMobilita(final Properties conf) {

        this.rules = ConfigurazioneFlussoAvnNonSomMobilita.Rules.builder()
                .withPercorso(conf.getProperty("regole.percorso", ""))
                .build();
        this.xmlOutput = ConfigurazioneFlussoAvnNonSomMobilita.XmlOutput.builder()
                .withPercorso(conf.getProperty("xmloutput.percorso", ""))
                .build();
        this.flusso = ConfigurazioneFlussoAvnNonSomMobilita.Flusso.builder()
                .withPercorso(conf.getProperty("flusso.percorso", ""))
                .build();
        this.sogliaInvio = ConfigurazioneFlussoAvnNonSomMobilita.SogliaInvio.builder()
                .withSoglia(conf.getProperty("soglia.invio.mds", ""))
                .build();
        this.certificato = ConfigurazioneFlussoAvnNonSomMobilita.Certificato.builder()
                .withPercorsoCertificato(conf.getProperty("certificato.percorso", ""))
                .build();
        this.sent = ConfigurazioneFlussoAvnNonSomMobilita.Sent.builder()
                .withPercorsoSent(conf.getProperty("sent.percorso", ""))
                .build();
        this.nomeFLusso = ConfigurazioneFlussoAvnNonSomMobilita.NomeFlusso.builder()
                .withNomeFlusso(conf.getProperty("nome.flusso", ""))
                .build();
        this.dimensioneBlocco = ConfigurazioneFlussoAvnNonSomMobilita.DimensioneBlocco.builder()
                .withDimensioneBlocco(conf.getProperty("dimensione.blocco", "1000"))
                .build();
        this.separatore = ConfigurazioneFlussoAvnNonSomMobilita.Separatore.builder()
                .withSeparatore(conf.getProperty("separatore", "~"))
                .build();

    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Rules {
        String percorso;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class XmlOutput {
        String percorso;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Flusso {
        String percorso;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class SogliaInvio {
        String soglia;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Certificato {
        String percorsoCertificato;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Sent {
        String percorsoSent;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class NomeFlusso {
        String nomeFlusso;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class DimensioneBlocco {
        String dimensioneBlocco;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Separatore {
        String separatore;
    }

    private static Properties leggiConfigurazione(final String nomeFile) {
        final Properties prop = new Properties();
        try (final InputStream is = ConfigurazioneFlussoAvnNonSomMobilita.class.getClassLoader().getResourceAsStream(nomeFile)) {
            prop.load(is);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        }
        return prop;
    }

    private static Properties leggiConfigurazioneEsterna() {
        log.debug("{}.leggiConfigurazioneEsterna - BEGIN", it.mds.sdk.connettore.anagrafiche.conf.Configurazione.class.getName());
        Properties properties = new Properties();
        try (InputStreamReader in = new InputStreamReader(new FileInputStream("/sdk/properties/" + FILE_CONF),
                StandardCharsets.UTF_8)) {
            properties.load(in);
        } catch (IOException e) {
            log.error("{}.leggiConfigurazioneEsterna", Configurazione.class.getName(), e);
            return leggiConfigurazione(FILE_CONF);
        }
        return properties;
    }
}
