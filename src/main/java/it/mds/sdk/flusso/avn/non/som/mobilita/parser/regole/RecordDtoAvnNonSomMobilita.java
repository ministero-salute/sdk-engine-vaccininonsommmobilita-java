/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita.parser.regole;


import com.opencsv.bean.CsvBindByPosition;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDtoAvnNonSomMobilita extends RecordDtoGenerico {

    //ID_ASS~MOD_INV~TIP_TRS~COD_REG~COD_ANT~DOSE~COD_MTV~DAT_NON_EFF

    @CsvBindByPosition(position = 0)
    private String codIdAssistito;

    @CsvBindByPosition(position = 1)
    private String modalita;

    @CsvBindByPosition(position = 2)
    private String tipoTrasmissione;

    @CsvBindByPosition(position = 3)
    private String codRegione;

    @CsvBindByPosition(position = 4)
    private String codiceAntigene;

    @CsvBindByPosition(position = 5)
    private Integer dose;

    @CsvBindByPosition(position = 6)
    private String motivazione;

    @CsvBindByPosition(position = 7)
    private String dataNonEffettuazione;

}
