/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita.service;

import it.mds.sdk.connettoremds.ConnettoreMds;
import it.mds.sdk.connettoremds.crittografia.Crittografia;
import it.mds.sdk.connettoremds.exception.ConnettoreMdsException;
import it.mds.sdk.connettoremds.exception.CrittografiaException;
import it.mds.sdk.connettoremds.gaf.webservices.bean.ArrayOfUploadEsito;
import it.mds.sdk.connettoremds.gaf.webservices.bean.ResponseUploadFile;
import it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole.ParserTracciatoImpl;
import it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole.RecordDtoAvnNonSomMobilita;
import it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole.conf.ConfigurazioneFlussoAvnNonSomMobilita;
import it.mds.sdk.flusso.avn.non.som.mobilita.tracciato.TracciatoSplitterImpl;
import it.mds.sdk.flusso.avn.non.som.mobilita.tracciato.bean.output.prestazioni.nonsomministrate.VaccinazioniNonEffettuate;
import it.mds.sdk.gestoreesiti.GestoreRunLog;
import it.mds.sdk.gestoreesiti.conf.Configurazione;
import it.mds.sdk.gestoreesiti.modelli.InfoRun;
import it.mds.sdk.gestoreesiti.modelli.ModalitaOperativa;
import it.mds.sdk.gestoreesiti.validazioneXSD.MainTester;
import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestorefile.exception.XSDNonSupportedException;
import it.mds.sdk.gestorefile.factory.GestoreFileFactory;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.gestorevalidazione.BloccoValidazione;
import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;
import it.mds.sdk.libreriaregole.validator.ValidationEngine;
import it.mds.sdk.rest.exception.ParseCSVException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
class FlussoAvnNonSomMobilitaServiceTest {

    private final String VNM_CSV = "VNM_TEST.csv";
    private final int dimensioneBlocco = 250000;

    private final String idClient = "";
    private final String periodoRiferimento = "Q1";
    private final String annoRiferimento = "2022";
    private final String codiceRegione = "120";
    private final ParserTracciatoImpl parser = Mockito.mock(ParserTracciatoImpl.class);
    private final TracciatoSplitterImpl tracciatoSplitter = Mockito.mock(TracciatoSplitterImpl.class);
    private final ConnettoreMds connettoreMds = Mockito.mock(ConnettoreMds.class);
    private final GestoreRunLog grl = Mockito.mock(GestoreRunLog.class);
    private final Crittografia crittografia = Mockito.mock(Crittografia.class);
    private final ValidationEngine validationEngine = Mockito.mock(ValidationEngine.class);
    private final VaccinazioniNonEffettuate vaccinazioniNonEffettuate = Mockito.mock(VaccinazioniNonEffettuate.class);
    private final String idRun = "928";
    private final Configurazione config = new Configurazione();
    private final String percorso = String.format("%s/ESITO_%s.json", config.getEsito().getPercorso(), idRun);
    private final String percorsoTemp = String.format("%s/ESITO_%s_TEMP.json", config.getEsito().getPercorso(), idRun);
    @Autowired
    private ConfigurazioneFlussoAvnNonSomMobilita configurazioneFlusso;
    @InjectMocks
    @Spy
    private FlussoAvnNonSomMobilitaService flusso;
    @Mock
    private RegoleFlusso regoleFlusso;
    @Spy
    private ConfigurazioneFlussoAvnNonSomMobilita conf;
    private final GestoreFile gestoreFile = Mockito.mock(GestoreFile.class);
    private BloccoValidazione bloccoValidazione;
    private RecordDtoAvnNonSomMobilita recordDto;
    //    private ConfigurazioneFlussoAvnMobilitaAnag configurazione;
    private final ParserTracciatoImpl parserTracciato = Mockito.mock(ParserTracciatoImpl.class);
    private InfoRun infoRun;
    private MockedStatic<GestoreFileFactory> mockedStatic;
    private MockedStatic<GestoreFileFactory> utilities;

    @BeforeEach
    void init() {
        recordDto = new RecordDtoAvnNonSomMobilita();

        bloccoValidazione = new BloccoValidazione();
        bloccoValidazione.setNumeroRecord(2);
        bloccoValidazione.setScartati(1);
        bloccoValidazione.setRecordList(List.of(recordDto));

        infoRun = new InfoRun(
                null, null, null, null, null,
                null, null, null, null, null, null,
                1, 1, 1, null, null, null, null,
                null, null, null, null, null, null, null, "nomeFile",
                null, null, null
        );
    }

    @Test
    void validazioneBlocchiTestModalitaT_KO() {

        mockedStatic = mockStatic(GestoreFileFactory.class);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);

        given(tracciatoSplitter.creaVaccinazioniNonEffettuate(any(), any())).willReturn(vaccinazioniNonEffettuate);
        when(flusso.getParserTracciatoImpl()).thenReturn(parserTracciato);
        given(parserTracciato.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        mockedStatic.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);

        willThrow(new XSDNonSupportedException()).given(gestoreFile).scriviDtoFragment(any(), any(), any());

        given(validationEngine.startValidaFlussoBlocco(list, regoleFlusso, null, grl, 0)).willReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(percorso, percorsoTemp)).willReturn(true);
        given(validationEngine.formatXmlOutput(any(), any(), any())).willReturn(false);

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        Assertions.assertThrows(RuntimeException.class,
                () -> this.flusso.validazioneBlocchi(
                        dimensioneBlocco,
                        VNM_CSV,
                        regoleFlusso,
                        idRun,
                        idClient,
                        ModalitaOperativa.T,
                        periodoRiferimento,
                        annoRiferimento,
                        codiceRegione,
                        grl
                ));
        mockedStatic.close();
    }

    @Test
    void validazioneBlocchiTestModalitaT_KO2() {

        mockedStatic = mockStatic(GestoreFileFactory.class);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);

        given(tracciatoSplitter.creaVaccinazioniNonEffettuate(any(), any())).willReturn(vaccinazioniNonEffettuate);
        when(flusso.getParserTracciatoImpl()).thenReturn(parserTracciato);
        given(parserTracciato.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        mockedStatic.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);
        doNothing().when(gestoreFile).scriviDtoFragment(any(), any(), any());

        when(validationEngine.startValidaFlussoBlocco(anyList(), any(), anyString(), any(), anyInt())).thenReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(anyString(), anyString())).willReturn(true);
        given(validationEngine.formatXmlOutput(any(), any(), any())).willReturn(false);

        willThrow(new XSDNonSupportedException()).given(validationEngine).puliziaFileAvn(any(), any(), any());

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.T,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        mockedStatic.close();
    }

    @Test
    void validazioneBlocchiTestModalitaT_OK() {

        mockedStatic = mockStatic(GestoreFileFactory.class);
        MainTester mainTester = Mockito.mock(MainTester.class);

        ConfigurazioneFlussoAvnNonSomMobilita configurazioneFlussoSism = new ConfigurazioneFlussoAvnNonSomMobilita();
        when(spy(conf.getFlusso())).thenReturn(configurazioneFlussoSism.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);
        given(parser.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        bloccoValidazione.setRecordList(Collections.emptyList());

        given(parserTracciato.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        when(validationEngine.startValidaFlussoBlocco(anyList(), any(), anyString(), any(), anyInt())).thenReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(anyString(), anyString())).willReturn(true);
        given(validationEngine.formatXmlOutput(any(), any(), any())).willReturn(false);

        mockedStatic.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);
        doNothing().when(gestoreFile).scriviDtoFragment(any(), any(), any());

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        bloccoValidazione.setRecordList(List.of(recordDto));

        when(flusso.getParserTracciatoImpl()).thenReturn(parserTracciato);
        when(flusso.getMainTester()).thenReturn(mainTester);
        when(mainTester.xmlValidationAgainstXSD(any(), any())).thenReturn(true);

        willReturn("nomeFile").given(validationEngine).puliziaFileAvn(any(), any(), any());

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.T,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        mockedStatic.close();
    }

    @Test
    void validazioneBlocchiTestModalitaKO_ParseTracciatoBlocco() throws CrittografiaException {

        Path path = Mockito.mock(Path.class);
        List<RecordDtoGenerico> list = List.of(recordDto);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        when(flusso.getParserTracciatoImpl()).thenReturn(parser);
        when(crittografia.criptaFile(any(), any())).thenReturn(path);
        given(parser.parseTracciatoBlocco(any(), anyInt(), anyInt())).willThrow(new ParseCSVException());
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);

        utilities = mockStatic(GestoreFileFactory.class);
        utilities.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.P,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        utilities.close();
    }

    @Test
    void validazioneBlocchiTestModalitaKO_ScriviDtoFragment() throws CrittografiaException {

        Path path = Mockito.mock(Path.class);
        utilities = mockStatic(GestoreFileFactory.class);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);
        when(flusso.getParserTracciatoImpl()).thenReturn(parser);
        given(parser.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        bloccoValidazione.setRecordList(List.of(recordDto));

        given(validationEngine.startValidaFlussoBlocco(anyList(), any(), anyString(), any(), anyInt())).willReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(any(), any())).willReturn(true);

        willDoNothing().given(flusso).inviaTracciatoMds(any(), any(), any(), any(), any());

        utilities.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);
        doThrow(new XSDNonSupportedException()).when(gestoreFile).scriviDtoFragment(eq(null), any(), any());

        given(tracciatoSplitter.creaVaccinazioniNonEffettuate(any(), any())).willReturn(null);

        willReturn("nomeFile").given(validationEngine).puliziaFileAvn(any(), any(), any());

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        when(crittografia.criptaFile(any(), any())).thenReturn(path);

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.P,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        utilities.close();
    }

    @Test
    void validazioneBlocchiTestModalitaKO_puliziaFile() throws CrittografiaException {

        Path path = Mockito.mock(Path.class);
        utilities = mockStatic(GestoreFileFactory.class);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);
        when(flusso.getParserTracciatoImpl()).thenReturn(parser);
        given(parser.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        bloccoValidazione.setRecordList(List.of(recordDto));

        given(validationEngine.startValidaFlussoBlocco(anyList(), any(), anyString(), any(), anyInt())).willReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(any(), any())).willReturn(true);

        willDoNothing().given(flusso).inviaTracciatoMds(any(), any(), any(), any(), any());

        utilities.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);
        doNothing().when(gestoreFile).scriviDtoFragment(eq(null), any(), any());

        given(tracciatoSplitter.creaVaccinazioniNonEffettuate(any(), any())).willReturn(null);

        willReturn("nomeFile").given(validationEngine).puliziaFileAvn(any(), any(), any());

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        when(crittografia.criptaFile(any(), any())).thenReturn(path);

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.P,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        utilities.close();
    }

    @Test
    void validazioneBlocchiTestModalitaKO_criptaFile() throws CrittografiaException {

        utilities = mockStatic(GestoreFileFactory.class);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);
        when(flusso.getParserTracciatoImpl()).thenReturn(parser);
        given(parser.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        bloccoValidazione.setRecordList(List.of(recordDto));

        given(validationEngine.startValidaFlussoBlocco(anyList(), any(), anyString(), any(), anyInt())).willReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(any(), any())).willReturn(true);

        willDoNothing().given(flusso).inviaTracciatoMds(any(), any(), any(), any(), any());

        utilities.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);
        doNothing().when(gestoreFile).scriviDtoFragment(eq(null), any(), any());

        given(tracciatoSplitter.creaVaccinazioniNonEffettuate(any(), any())).willReturn(null);

        willReturn("nomeFile").given(validationEngine).puliziaFileAvn(any(), any(), any());

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        when(crittografia.criptaFile(any(), any())).thenThrow(CrittografiaException.class);

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.P,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        utilities.close();

    }

    @Test
    void validazioneBlocchiTestModalitaP_OK() throws CrittografiaException {

        Path path = Mockito.mock(Path.class);

        when(spy(conf.getFlusso())).thenReturn(configurazioneFlusso.getFlusso());
        when(conf.getXmlOutput()).thenReturn(configurazioneFlusso.getXmlOutput());
        List<RecordDtoGenerico> list = List.of(recordDto);
        when(flusso.getParserTracciatoImpl()).thenReturn(parser);
        given(parser.parseTracciatoBlocco(any(), anyInt(), anyInt())).willReturn(list);

        bloccoValidazione.setRecordList(List.of(recordDto));

        given(validationEngine.startValidaFlussoBlocco(anyList(), any(), anyString(), any(), anyInt())).willReturn(bloccoValidazione);
        given(validationEngine.formatJsonEsiti(any(), any())).willReturn(true);

        willDoNothing().given(flusso).inviaTracciatoMds(any(), any(), any(), any(), any());

        utilities = mockStatic(GestoreFileFactory.class);

        given(tracciatoSplitter.creaVaccinazioniNonEffettuate(any(), any())).willReturn(vaccinazioniNonEffettuate);
        utilities.when(() -> GestoreFileFactory.getGestoreFile("XML")).thenReturn(gestoreFile);

        willReturn("nomeFile").given(validationEngine).puliziaFileAvn(any(), any(), any());
        willReturn(true).given(validationEngine).validateXML(any(), any());

        given(grl.getRun(any())).willReturn(infoRun);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        given(grl.updateRun(any())).willReturn(infoRun);

        when(crittografia.criptaFile(any(), any())).thenReturn(path);

        this.flusso.validazioneBlocchi(
                dimensioneBlocco,
                VNM_CSV,
                regoleFlusso,
                idRun,
                idClient,
                ModalitaOperativa.P,
                periodoRiferimento,
                annoRiferimento,
                codiceRegione,
                grl
        );
        utilities.close();
    }

    @Test
    void inviaTracciatoMdsTest_KOMinistero() throws ConnettoreMdsException {
        String nomeFileXml = conf.getXmlOutput().getPercorso() + "SDK_VNM_Q1_" + periodoRiferimento + "_" + idRun +
                ".xml";
        given(grl.getRun(any())).willReturn(infoRun);
        ResponseUploadFile responseUploadFile = new ResponseUploadFile();
        responseUploadFile.setErrorCode("x");
        given(connettoreMds.invioTracciati(any(), any(), any(), any(), any())).willReturn(responseUploadFile);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        flusso.inviaTracciatoMds(
                idRun,
                nomeFileXml,
                grl,
                periodoRiferimento,
                annoRiferimento
        );
    }

    @Test
    void inviaTracciatoMdsTest_ErrorCodeNull() throws ConnettoreMdsException {
        String nomeFileXml = conf.getXmlOutput().getPercorso() + "SDK_VNM_Q1_" + periodoRiferimento + "_" + idRun +
                ".xml";
        given(grl.getRun(any())).willReturn(infoRun);
        ResponseUploadFile responseUploadFile = new ResponseUploadFile();
        responseUploadFile.setErrorCode(null);
        given(connettoreMds.invioTracciati(any(), any(), any(), any(), any())).willReturn(responseUploadFile);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        flusso.inviaTracciatoMds(
                idRun,
                nomeFileXml,
                grl,
                periodoRiferimento,
                annoRiferimento
        );
    }

    @Test
    void inviaTracciatoMdsTest_ListaEsitiNotNull() throws ConnettoreMdsException {
        String nomeFileXml = conf.getXmlOutput().getPercorso() + "SDK_VNM_Q1_" + periodoRiferimento + "_" + idRun +
                ".xml";
        given(grl.getRun(any())).willReturn(infoRun);
        ResponseUploadFile responseUploadFile = new ResponseUploadFile();
        ArrayOfUploadEsito arr = Mockito.mock(ArrayOfUploadEsito.class);

        responseUploadFile.setListaEsitiUpload(arr);
        responseUploadFile.setErrorCode(null);
        given(connettoreMds.invioTracciati(any(), any(), any(), any(), any())).willReturn(responseUploadFile);
        given(grl.cambiaStatoRun(any(), any())).willReturn(infoRun);
        flusso.inviaTracciatoMds(
                idRun,
                nomeFileXml,
                grl,
                periodoRiferimento,
                annoRiferimento
        );
    }

}