/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita;

import it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole.ParserTracciatoImpl;
import it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole.RecordDtoAvnNonSomMobilita;
import it.mds.sdk.flusso.avn.non.som.mobilita.tracciato.TracciatoSplitterImpl;
import it.mds.sdk.flusso.avn.non.som.mobilita.tracciato.bean.output.prestazioni.nonsomministrate.VaccinazioniNonEffettuate;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConvIntegrationTest {

    private int dimensioneBlocco = 300;
    String pathFileCsv;
    String pathFileCsv1000;
    File fileCsv;

    File fileCsv1000;

    @BeforeEach
    void init() {
        Properties prop = loadPropertiesFromFile("config-flusso-vnm-test.properties");
        this.pathFileCsv = prop.getProperty("test.filecsv");
        this.pathFileCsv1000 = prop.getProperty("test.filecsv1000");

        ClassLoader classLoader = getClass().getClassLoader();
        fileCsv = new File(Objects.requireNonNull(classLoader.getResource(pathFileCsv)).getFile());
        fileCsv1000 = new File(Objects.requireNonNull(classLoader.getResource(pathFileCsv1000)).getFile());
        System.out.println("print");
    }

    @Test
    void integBLocco() {
        ParserTracciatoImpl p = new ParserTracciatoImpl();
        List<RecordDtoGenerico> list = p.parseTracciatoBlocco(fileCsv, 1, dimensioneBlocco);
        CampiInputBean c = new CampiInputBean("Q1", "2022", "120");

        for (RecordDtoGenerico r : list) {
            r.setCampiInput(c);
        }
        List<RecordDtoAvnNonSomMobilita> r2 = list.stream().map(k -> (RecordDtoAvnNonSomMobilita) k).collect(Collectors.toList());
        TracciatoSplitterImpl splitter = new TracciatoSplitterImpl();
        splitter.dividiTracciato(r2, "34");
        System.out.println(list.size());

    }

    @Test
    void scritturaNBlocchiSuXML() {
        ParserTracciatoImpl p = new ParserTracciatoImpl();

        //Prova blocchi
        List<RecordDtoGenerico> blocco = p.parseTracciatoBlocco(fileCsv1000, 1, dimensioneBlocco);
        List<RecordDtoAvnNonSomMobilita> bloccoConv = blocco.stream().map(k -> (RecordDtoAvnNonSomMobilita) k).collect(Collectors.toList());

        TracciatoSplitterImpl t = new TracciatoSplitterImpl();
        VaccinazioniNonEffettuate vaccinazioniNonEffettuate = t.creaVaccinazioniNonEffettuate(bloccoConv, null);
        blocco = null;
        bloccoConv = null;
        //System.gc();
        System.out.println("blocco 1");
        blocco = p.parseTracciatoBlocco(fileCsv1000, dimensioneBlocco + 1, dimensioneBlocco + 300);
        bloccoConv = blocco.stream().map(k -> (RecordDtoAvnNonSomMobilita) k).collect(Collectors.toList());
        vaccinazioniNonEffettuate = t.creaVaccinazioniNonEffettuate(bloccoConv, vaccinazioniNonEffettuate);
        blocco = null;
        bloccoConv = null;

        System.out.println("blocco 2");
        blocco = p.parseTracciatoBlocco(fileCsv1000, dimensioneBlocco + 301, dimensioneBlocco + 600);
        bloccoConv = blocco.stream().map(k -> (RecordDtoAvnNonSomMobilita) k).collect(Collectors.toList());
        vaccinazioniNonEffettuate = t.creaVaccinazioniNonEffettuate(bloccoConv, vaccinazioniNonEffettuate);
        blocco = null;
        bloccoConv = null;
        //System.gc();
        System.out.println("blocco 3");
        blocco = p.parseTracciatoBlocco(fileCsv1000, dimensioneBlocco + 601, 1000);
        bloccoConv = blocco.stream().map(k -> (RecordDtoAvnNonSomMobilita) k).collect(Collectors.toList());
        vaccinazioniNonEffettuate = t.creaVaccinazioniNonEffettuate(bloccoConv, vaccinazioniNonEffettuate);
        blocco = null;
        bloccoConv = null;
        //System.gc();
        System.out.println("blocco 4");
    }

    private Properties loadPropertiesFromFile(String fileName) {
        Properties prop = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(fileName);
            prop.load(stream);
            stream.close();
        } catch (Exception e) {
            String msg = String.format("Failed to load file '%s' - %s - %s", fileName, e.getClass().getName(),
                    e.getMessage());
            System.out.println(msg);
        }
        return prop;
    }

}
