/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole;

import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.parser.ParserTracciato;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ParserTracciatoImplTest {

    private static final String FILE_TRACCIATO_VNX = "VNM_test.csv";

    @Test
    void parseTracciatoOK() {
        ParserTracciato parserTracciato = new ParserTracciatoImpl();
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        File tracciato = new File(absolutePath + FileSystems.getDefault().getSeparator() + FILE_TRACCIATO_VNX);

        List<RecordDtoGenerico> listaRecord = parserTracciato.parseTracciato(tracciato);
        listaRecord.forEach(System.out::println);
        assertFalse(ArrayUtils.isEmpty(listaRecord.toArray()));
    }

    @Test
    void parseTracciatoBloccOK() {
        ParserTracciato parserTracciato = new ParserTracciatoImpl();
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        File tracciato = new File(absolutePath + FileSystems.getDefault().getSeparator() + FILE_TRACCIATO_VNX);

        List<RecordDtoGenerico> listaRecord = parserTracciato.parseTracciatoBlocco(tracciato, 1, 250000);
        listaRecord.forEach(System.out::println);
        assertFalse(ArrayUtils.isEmpty(listaRecord.toArray()));
    }
}