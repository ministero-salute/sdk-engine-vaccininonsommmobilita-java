//
// Questo file � stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andr� persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.06.16 alle 05:06:19 PM CEST 
//


/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita.tracciato.bean.output.prestazioni.nonsomministrate;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.myschema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.myschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VaccinazioniNonEffettuate }
     * 
     */
    public VaccinazioniNonEffettuate createVaccinazioniNonEffettuate() {
        return new VaccinazioniNonEffettuate();
    }

    /**
     * Create an instance of {@link VaccinazioniNonEffettuate.Assistito }
     * 
     */
    public VaccinazioniNonEffettuate.Assistito createVaccinazioniNonEffettuateAssistito() {
        return new VaccinazioniNonEffettuate.Assistito();
    }

    /**
     * Create an instance of {@link VaccinazioniNonEffettuate.Assistito.MancataVaccinazione }
     * 
     */
    public VaccinazioniNonEffettuate.Assistito.MancataVaccinazione createVaccinazioniNonEffettuateAssistitoMancataVaccinazione() {
        return new VaccinazioniNonEffettuate.Assistito.MancataVaccinazione();
    }

}
