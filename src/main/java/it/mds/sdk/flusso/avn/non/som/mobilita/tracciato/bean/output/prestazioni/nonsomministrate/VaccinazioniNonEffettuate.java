//
// Questo file � stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andr� persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.06.16 alle 05:06:19 PM CEST 
//


/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.non.som.mobilita.tracciato.bean.output.prestazioni.nonsomministrate;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Assistito" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="MancataVaccinazione" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="TipoTrasmissione" use="required" type="{}TipoTrasmissione" /&gt;
 *                           &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
 *                           &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
 *                           &lt;attribute name="Motivazione" use="required" type="{}Motivazione" /&gt;
 *                           &lt;attribute name="DataNonEffettuazione" use="required" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="IdAssistito" use="required" type="{}IdAssistito" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="CodiceRegione" use="required" type="{}CodiceRegione" /&gt;
 *       &lt;attribute name="Modalita" use="required" type="{}Modalita" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "assistito"
})
@XmlRootElement(name = "vaccinazioniNonEffettuate")
public class VaccinazioniNonEffettuate {

    @XmlElement(name = "Assistito", required = true)
    protected List<Assistito> assistito;
    @XmlAttribute(name = "CodiceRegione", required = true)
    protected String codiceRegione;
    @XmlAttribute(name = "Modalita", required = true)
    protected Modalita modalita;

    /**
     * Gets the value of the assistito property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the assistito property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssistito().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Assistito }
     * 
     * 
     */
    public List<Assistito> getAssistito() {
        if (assistito == null) {
            assistito = new ArrayList<Assistito>();
        }
        return this.assistito;
    }

    /**
     * Recupera il valore della propriet� codiceRegione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceRegione() {
        return codiceRegione;
    }

    /**
     * Imposta il valore della propriet� codiceRegione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceRegione(String value) {
        this.codiceRegione = value;
    }

    /**
     * Recupera il valore della propriet� modalita.
     * 
     * @return
     *     possible object is
     *     {@link Modalita }
     *     
     */
    public Modalita getModalita() {
        return modalita;
    }

    /**
     * Imposta il valore della propriet� modalita.
     * 
     * @param value
     *     allowed object is
     *     {@link Modalita }
     *     
     */
    public void setModalita(Modalita value) {
        this.modalita = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="MancataVaccinazione" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="TipoTrasmissione" use="required" type="{}TipoTrasmissione" /&gt;
     *                 &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
     *                 &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
     *                 &lt;attribute name="Motivazione" use="required" type="{}Motivazione" /&gt;
     *                 &lt;attribute name="DataNonEffettuazione" use="required" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="IdAssistito" use="required" type="{}IdAssistito" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "mancataVaccinazione"
    })
    public static class Assistito {

        @XmlElement(name = "MancataVaccinazione", required = true)
        protected List<MancataVaccinazione> mancataVaccinazione;
        @XmlAttribute(name = "IdAssistito", required = true)
        protected String idAssistito;

        /**
         * Gets the value of the mancataVaccinazione property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the mancataVaccinazione property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMancataVaccinazione().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MancataVaccinazione }
         * 
         * 
         */
        public List<MancataVaccinazione> getMancataVaccinazione() {
            if (mancataVaccinazione == null) {
                mancataVaccinazione = new ArrayList<MancataVaccinazione>();
            }
            return this.mancataVaccinazione;
        }

        /**
         * Recupera il valore della propriet� idAssistito.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdAssistito() {
            return idAssistito;
        }

        /**
         * Imposta il valore della propriet� idAssistito.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdAssistito(String value) {
            this.idAssistito = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;attribute name="TipoTrasmissione" use="required" type="{}TipoTrasmissione" /&gt;
         *       &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
         *       &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
         *       &lt;attribute name="Motivazione" use="required" type="{}Motivazione" /&gt;
         *       &lt;attribute name="DataNonEffettuazione" use="required" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class MancataVaccinazione {

            @XmlAttribute(name = "TipoTrasmissione", required = true)
            protected String tipoTrasmissione;
            @XmlAttribute(name = "CodAntigene", required = true)
            protected String codAntigene;
            @XmlAttribute(name = "Dose", required = true)
            protected BigInteger dose;
            @XmlAttribute(name = "Motivazione", required = true)
            protected String motivazione;
            @XmlAttribute(name = "DataNonEffettuazione", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar dataNonEffettuazione;

            /**
             * Recupera il valore della propriet� tipoTrasmissione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipoTrasmissione() {
                return tipoTrasmissione;
            }

            /**
             * Imposta il valore della propriet� tipoTrasmissione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipoTrasmissione(String value) {
                this.tipoTrasmissione = value;
            }

            /**
             * Recupera il valore della propriet� codAntigene.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodAntigene() {
                return codAntigene;
            }

            /**
             * Imposta il valore della propriet� codAntigene.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodAntigene(String value) {
                this.codAntigene = value;
            }

            /**
             * Recupera il valore della propriet� dose.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getDose() {
                return dose;
            }

            /**
             * Imposta il valore della propriet� dose.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setDose(BigInteger value) {
                this.dose = value;
            }

            /**
             * Recupera il valore della propriet� motivazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMotivazione() {
                return motivazione;
            }

            /**
             * Imposta il valore della propriet� motivazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMotivazione(String value) {
                this.motivazione = value;
            }

            /**
             * Recupera il valore della propriet� dataNonEffettuazione.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getDataNonEffettuazione() {
                return dataNonEffettuazione;
            }

            /**
             * Imposta il valore della propriet� dataNonEffettuazione.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setDataNonEffettuazione(XMLGregorianCalendar value) {
                this.dataNonEffettuazione = value;
            }

        }

    }

}
