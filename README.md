# **1.Introduzione**

## ***1.1 Obiettivi del documento***

Il Ministero della Salute (MdS) metterà a disposizione degli Enti, da cui riceve dati, applicazioni SDK specifiche per flusso logico e tecnologie applicative (Java, PHP e C#) per verifica preventiva (in casa Ente) della qualità del dato prodotto.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.002.png)

Nel presente documento sono fornite la struttura e la sintassi dei tracciati previsti dalla soluzione SDK per avviare il proprio processo elaborativo e i controlli di merito sulla qualità, completezza e coerenza dei dati.

Gli obiettivi del documento sono:

- fornire una descrizione funzionale chiara e consistente dei tracciati di input a SDK;
- fornire le regole funzionali per la verifica di qualità, completezza e coerenza dei dati;

In generale, la soluzione SDK è costituita da 2 diversi moduli applicativi (Access Layer e Validation Engine) per abilitare

- l’interoperabilità con il contesto tecnologico dell’Ente in cui la soluzione sarà installata;
- la validazione del dato ed il suo successivo invio verso il MdS.

La figura che segue descrive la soluzione funzionale ed i relativi benefici attesi.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.003.png)

## ***1.2 Acronimi***

Nella tabella riportata di seguito sono elencati tutti gli acronimi e le definizioni adottati nel presente documento.


|**#**|**Acronimo / Riferimento**|**Definizione**|
| - | - | - |
|1|NSIS|Nuovo Sistema Informativo Sanitario|
|2|SDK|Software Development Kit|
|3|AVN|Anagrafe Nazionale Vaccini|
|4|SSN|Sistema Sanitario Nazionale|
|5|CI|Codice Identificativo|
|6|AIC|Autorizzazione alla Immissione in Commercio del vaccino in Italia rilasciata dall’Agenzia Italiana del Farmaco|


# **2. Architettura SDK**

## ***2.1 Architettura funzionale***

Di seguito una rappresentazione architetturale del processo di gestione e trasferimento dei flussi dall’ente verso l’area MdS attraverso l’utilizzo dell’applicativo SDK e il corrispondente diagramma di sequenza.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.004.jpeg)


1. L’utente dell’ente caricherà in una apposita directory (es. /sdk/input/) il flusso sorgente.  L’utente avvierà l’SDK passando in input una serie di parametri descritti in dettaglio al paragrafo 3.1
1. La compenente Access Layer estrae dalla chiamata dell’ente i parametri utilizzati per lanciare l’SDK,  genera un identificativo ID\_RUN, e un file chiamato “{ID\_RUN}.json” in cui memorizza le informazioni dell’esecuzione.
1. I record del flusso verranno sottoposti alle logiche di validazione e controllo definite nel Validation Engine. Nel processare il dato, il Validation Engine acquisirà da MdS eventuali anagrafiche di validazione del dato stesso.
1. Generazione del file degli scarti contenente tutti i record in scarto con evidenza degli errori riscontrati. I file di scarto saranno memorizzati in cartelle ad hoc (es. /sdk/esiti).
1. Tutti i record che passeranno i controlli verranno inseriti in un file xml copiato in apposita cartella (es /sdk/xml\_output), il quale verrà eventualmente trasferito a MdS utilizzando la procedura “invioFlussi” esposta da GAF WS (tramite PDI). A fronte di un’acquisizione, il MdS fornirà a SDK un identificativo (ID\_UPLOAD) che sarà usato da SDK sia per fini di logging che di recupero del File Unico degli Scarti.
1. A conclusione del processo di verifica dei flussi, il Validation Engine eseguirà le seguenti azioni:

 a. Aggiornamento file contenente il riepilogo dell’esito dell’elaborazione del Validation Engine e del ritorno dell’esito da parte di MdS. I file contenenti l’esito dell’elaborazione saranno memorizzati in cartelle ad hoc (es. /sdk/run).

 b. Consolidamento del file di log applicativo dell’elaborazione dell’SDK in cui sono disponibili una serie di informazioni tecniche (Es. StackTrace di eventuali errori).

 c. Copia del file generato al punto 5, se correttamente inviato al MdS, in apposita cartella (es. /sdk/sent).

Ad ogni step del precedente elenco e a partire dal punto 2, l’SDK aggiornerà di volta in volta il file contenente l’esito dell’elaborazione.

**Nota: l’SDK elaborerà un solo file di input per esecuzione.**

In generale, le classi di controllo previste su Validation Engine sono:

- Controlli FORMALI (es. correttezza dei formati e struttura record)
- Controlli SINTATTICI (es. check correttezza del Codice Fiscale)
- Controlli di OBBLIGATORIETÀ DEL DATO (es. Codice Prestazione su flusso…)
- Controlli STRUTTURE FILE (es. header/footer ove applicabile)
- Controlli di COERENZA CROSS RECORD
- Controlli di corrispondenza dei dati trasmessi con le anagrafiche di riferimento
- Controlli di esistenza di chiavi duplicate nel file trasmesso rispetto alle chiavi logiche individuate per ogni tracciato.

Si sottolinea che la soluzione SDK non implementa controlli che prevedono la congruità del dato input con la base date storica (es: non viene verificato se per un nuovo inserimento (Tipo = I) la chiave del record non sia già presente sulla struttura dati MdS).

## ***2.2 Architettura di integrazione***

La figura sottostante mostra l’architettura di integrazione della soluzione SDK con il MdS. Si evidenzia in particolare che:

- Tutti i dati scambiati fra SDK e MdS saranno veicolati tramite Porta di Interoperabilità (PDI);
- Il MdS esporrà servizi (API) per il download di dati anagrafici;
- SDK provvederà ad inviare vs MdS l’output (record validati) delle proprie elaborazioni. A fronte di tale invio, il MdS provvederà a generare un identificativo di avvenuta acquisizione del dato (ID\_UPLOAD) che SDK memorizzerà a fini di logging.


![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.006.png)

# **3 Funzionamento della soluzione SDK**

In questa sezione è descritta le specifica di funzionamento del flusso **VNM**  per l’alimentazione dello stesso.


## ***3.1 Input SDK***

In fase di caricamento del file verrano impostati i seguenti parametri che andranno in input al SDK in fase di processamento del file:


|**NOME PARAMETRO**|**DESCRIZIONE**|**LUNGHEZZA**|**DOMINIO VALORI**|
| :- | :- | :- | :- |
|ID CLIENT|Identificativo univoco della trasazione che fa richiesta all'SDK|100|Non definito|
|NOME FILE INPUT|Nome del file per il quale si richiede il processamento lato SDK|256|Non definito|
|ANNO RIFERIMENTO|Stringa numerica rappresentante l’anno di riferimento per cui si intende inviare la fornitura|4|Anno (Es. 2022)|
|TIPO TRASMISSIONE |Indica se la trasmissione dei dati verso MDS avverrà in modalità full (F) o record per record (R). Per questo flusso la valorizzazione del parametro sarà impostata di default a F|1|F/R|
|FINALITA' ELABORAZIONE|Indica se i flussi in output prodotti dal SDK verranno inviati verso MDS (Produzione) oppure se rimarranno all’interno del SDK e il processamento vale solo come test del flusso (Test)|1|Produzione/Test|
|CODICE REGIONE|<p>Individua la Regione a cui afferisce la struttura. Il codice da utilizzare è quello a tre caratteri definito con DM 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n.240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.</p><p></p>|3|Es. 010|

Inoltre è previsto anche il parametro Periodo Riferimento, i cui valori differiscono in base al flusso specifico.
Di seguito la tabella specifica per il flusso VNM:


|**NOME PARAMETRO**|**DESCRIZIONE**|**LUNGHEZZA**|**DOMINIO VALORI**|
| :- | :- | :- | :- |
|PERIODO RIFERIMENTO|Stringa alfanumerica rappresentante il periodo per il quale si intende inviare la fornitura|2|**13** (rappresenta un alias per il quale MDS usa come data di competenza l’anno e il mese di riferimento del record piuttosto che il parametro periodo di riferimento passato in input alla procedura InvioFlussi)|

## ***3.2 Tracciato input a SDK***

Il flusso di input avrà formato **csv** posizionale e una naming convention libera a discrezione dell’utente che carica il flusso senza alcun vincolo di nomenclatura specifica (es: nome\_file.csv). Il separatore per il file csv sarà la combinazione di caratteri tra doppi apici: “~“

All’interno della specifica del tracciato sono indicati i dettagli dei campi di business del tracciato di input atteso da SDK, il quale differisce per i sei  flussi dell’area AVN. All’interno di tale file è presente la colonna **Posizione nel file** la quale rappresenta l’ordinamento delle colonne del tracciato di input da caricare all’SDK.


Di seguito la tabella in cui è riportata la specifica del tracciato di input per il flusso in oggetto:


|**Nome campo**|**Posizione nel File**|**Key**|**Descrizione**|**Tipo** |**Obbligatorietà**|**Informazioni di Dominio**|**Lunghezza campo**|**XPATH Tracciato Output**|
| :- | :- | :- | :-: | :-: | :-: | :-: | :-: | :-: |
|Codice Identificatico dell’Assistito|0|KEY|Codice identificativo dell’assistito|AN|*OBB*|Il campo deve avere lunghezza massima di 20 caratteri in input alla procedura di cifratura che produrrà un output di massimo 172 caratteri.<br>Le modalità di alimentazione del presente campo sono descritte nel paragarfo 3.6 Codice Identificativo dell’ Assistito – procedura di cifratura|172|**/vaccinazioniNonEffettuate/Assistito/@IdAssistito**|
|Modalita|1| |Campo tecnico utilizzato per distinguere le modalità di invio di: schede vaccinali di soggetti residenti in regione |A|OBB|Devono essere utilizzati i codici 'RE  per la trasmissione delle informazioni anagarfiche relative alla scheda vaccinale dei **soggetti residenti** nella regione che sta trasmettendo.|2|**/vaccinazioniNonEffettuate/@Modalita**|
|Tipo Trasmissione|2| |Campo tecnico utilizzato per distinguere trasmissioni di informazioni nuove, modificate o eventualmente annullate|A|OBB|Valori ammessi: <br>I: Inserimento (per la trasmissione di informazioni nuove o per la ritrasmissione di informazioni precedentemente scartate dal sistema di acquisizione);<br>V: Variazione (per la trasmissione di informazioni per le quali si intende far effettuare una sovrascrittura dal sistema di acquisizione);<br>C: Cancellazione (per la trasmissione di informazioni per le quali si intende far effettuare una cancellazione dal sistema di acquisizione).|1|[**/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@TipoTrasmissione**](mailto:/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@TipoTrasmissione)|
|Codice Regione|3|KEY|Individua la Regione che trasmette il dato. |AN|OBB|Il codice da utilizzare è quello a tre caratteri definito con DM 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n.240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.I valori ammessi sono quelli riportati all’**Allegato 1 – Regioni**|3|[**/vaccinazioniNonEffettuate/@CodiceRegione** ](mailto:/informazioniAnagrafiche/@CodiceRegione)|
|Antigene |4|KEY|Indica il singolo antigene/principio vaccinale che costituisce il vaccino somministrato|AN|OBB|Valorizzare sulla base degli elenchi dei vaccini autorizzati resi disponibili da parte dell’AIFA. <br>In caso di vaccini combinati indicare i singoli antigeni che compongono il vaccino.<br>Ad esempio in caso di somministrazione di vaccino trivalente MPR (Morbillo/Parotite/Rosolia) dovranno essere inseriti tre record.<br>I valori ammessi sono quelli riportati all’Allegato 5 – Antigeni/Principi Vaccinali|2|[**/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@CodAntigene**](mailto:/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@CodAntigene)|
|Dose|5|KEY|Indica il numero di dose somministrata rispetto al calendario vaccinale per il singolo antigene/principio vaccinale|N|OBB|I valori ammessi sono da 1 a 99|Max 2|[**/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@Dose**](mailto:/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@Dose)|
|Motivazione|6| |Indica la motivazione per cui la vaccinazione non è stata effettuata|AN|OBB|I valori ammessi sono quelli riportati all’Allegato 6 – Motivi esclusione|2|[**/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@Motivazione**](mailto:/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@Motivazione)|
|Data non effettuazione|7| |Data in cui la vaccinazione prevista non è stata effettuata|D|OBB|Formato: AAAA-MM-GG|10|**/vaccinazioniNonEffettuate/Assistito/MancataVaccinazione/@DataNonEffettuazione**|


## ***3.3 Controlli di validazione del dato (business rules)***

Di seguito sono indicati i controlli da configurare sulla componente di Validation Engine e rispettivi error code associati riscontrabili sui dati di input per il flusso VNM.

Gli errori sono solo di tipo scarti (mancato invio del record).

Al verificarsi anche di un solo errore di scarto, tra quelli descritti, il record oggetto di controllo sarà inserito tra i record scartati.

Business Rule non implementabili lato SDK:

- Storiche (Business Rule che effettuano controlli su dati già acquisiti/consolidati che non facciano parte del dato anagrafico)
- Transazionali (Business Rule che effettuano controlli su record, i quali rappresentano transazioni, su cui andrebbe garantito l’ACID (Atomicità-Consistenza-Isolamento-Durabilità))
- Controllo d’integrità (cross flusso) (Business Rule che effettuano controlli sui record utilizzando informazioni estratte da record di altri flussi)

Di seguito le BR per il flusso in oggetto:


|**CAMPO**|**FLUSSO**|**CODICE ERRORE**|**FLAG ATTIVAZIONE**|**DESCRIZIONE ERRORE**|**DESCRIZIONE ALGORITMO** |**TABELLA ANAGRAFICA**|**CAMPI DI COERENZA**|**SCARTI/ANOMALIE**|**TIPOLOGIA BR**|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|Tipo Prestazione|VNM|1901.2|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Tipo Prestazione|VNM|1902.2|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi : I,i,V,v,C,c| | |scarti|Basic|
|Tipologia Erogatore|VNM|5001|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML o attributo non presente o tag XML o attributo  presente ma non valorizzato.| | |scarti|Basic|
|Tipologia Erogatore|VNM|5002|ATTIVA|Non appartenenza al dominio di riferimento|Valore diverso da quelli  ammessi:0,1,2,3,4,5,6,7,8,9,10,11,12,99| | |scarti|Basic|
|Codice Struttura|VNM|3005|ATTIVA|Mancata valorizzazione del codice struttura|Il valore è nullo e il campo Tipologia erogatore è diverso da “6” e da “99”’| |scarti|scarti|Basic|
|Codice Struttura|VNM|3015|ATTIVA|Codice struttura non presente nell’anagrafica STS11|Se il tipo erogatore è valorizzato con **1** il codice non è presente nei modelli STS11** (di cui al decreto ministeriale 5 dicembre 2006) a 6 cifre|Anagrafiche: STS11|scarti|scarti|Anagrafica|
|Codice Struttura|VNM|3020|ATTIVA|Codice struttura non presente nell’anagrafica ASL – MRA Fase 1|Se il tipo erogatore è valorizzato con **2 o 3 o 4 o 5 o 7 o 10 o 11 o 12** il codice non è presente nell’anagrafe di riferimento (D.M. 05/12/2006 e successive modifiche - Anagrafica MRA fase 1) – 6 cifre|Anagrafiche: ASL |scarti|scarti|Anagrafica|
|Codice Regione |VNM|1921|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Codice Regione |VNM|1922|ATTIVA|Non appartenenza al dominio di riferimento|Il valore inserito e controllato non è presente in anagrafica regioni|Regioni| |scarti|Anagrafica|
|Codice Regione |VNM|1905|ATTIVA|Il codice regione non coincide con la regione inviante.|Il campo Codice Regione non coincide con la regione che sta trasmettendo il file. | | |scarti|Basic|
|Codice Identificatico dell’Assistito|VNM|1701|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Codice Identificatico dell’Assistito|VNM|1703|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 172 caratteri| | |scarti|Basic|
|Modalità|VNM|1801|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Modalità|VNM|1802|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi : TR, MV| | |scarti|Basic|
|Modalità|VNM|1803|ATTIVA|Modalità non coerente con il flusso inviato|in caso di flusso Trasferimenti/Mobilità va indicata la modalità TR, MV| | |scarti|Basic|
|Antigene|VNM|6141|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Antigene|VNM|6140|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri| | |scarti|Basic|
|Antigene|VNM|4100|ATTIVA|Antigene non valorizzato in modo corretto |Il valore è composto da caratteri diversi da numeri| | |scarti|Basic|
|Dose|VNM|6051|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Dose|VNM|6050|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza al massimo 2 caratteri| | |scarti|Basic|
|Motivazione|VNM|2703|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri| | |scarti|basic|
|Motivazione|VNM|6141|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Motivazione|VNM|5000 (Ex. 6142)|ATTIVA|Motivazione non presente nelle anagrafiche di riferimento |Se il codice con è presente nell’anagrafe di riferimento – Allegato 6 delle presenti specifiche|Motivi esclusione| |scarti|Anagrafica|
|Data non effettuazione|VNM|8012|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Data non effettuazione|VNM|8013|ATTIVA|Tipo dato errato o formato data errato|Il campo deve essere valorizzato con il formato data AAAA-MM-GG| | |scarti|Basic|



## ***3.4 Accesso alle anagrafiche***

I controlli applicativi saranno implementati a partire dall’acquisizione dei seguenti dati anagrafici disponibili in ambito MdS e recuperati con servizi ad hoc (Service Layer mediante PDI):

- REGIONE
- ASL
- tabella di raccordo regioni-province-comuni-asl
- Codifica Alpha2
- HPS11
- STS11
- MRA
- RIA11
- Condizioni sanitarie a rischio
- Categorie a rischio
- AIFA
- ELENCO MdS
- Tipologie Formulazione
- Antigeni
- Principi Vaccinali
- Comuni Istat

Il dato anagrafico sarà presente sottoforma di tabella composta da tre colonne:

- Valore (in cui è riportato il dato, nel caso di più valori, sarà usato il carattere # come separatore)


- Data inizio validità (rappresenta la data di inizio validità del campo Valore)
 - Formato: AAAA-MM-DD
 - Notazione inizio validità permanente: **1900-01-01**


- Data Fine Validità (rappresenta la data di fine validità del campo Valore)
  - Formato: AAAA-MM-DD
  - Notazione fine validità permanente: **9999-12-31**

Affinchè le Business Rule che usano il dato anagrafico per effettuare controlli siano correttamente funzionanti, occorre controllare che la data di competenza del record su cui si effettua il controllo (la quale varia in base al flusso), sia compresa tra le data di validità.  Tutte le tabelle anagrafiche hanno dei dati con validità permanente ad eccezione delle seguenti per le quali sono previste date di validità specifiche:

- Tabella di raccordo regioni-province-comuni-asl
- Comuni Istat
- Codifica Alpha2
- Anagrafiche: ASL
- Anagrafiche: REGIONE

Di seguito viene mostrato un caso limite di anagrafica in cui sono presenti delle sovrapposizioni temporali e contraddizioni di validità permanente/specifico range:


|ID|VALUE|VALID\_FROM|VALID\_TO|
| - | - | - | - |
|1|VALORE 1|1900-01-01|9999-12-31|
|2|VALORE 1|2015-01-01|2015-12-31|
|3|VALORE 1|2018-01-01|2023-12-31|
|4|VALORE 1|2022-01-01|2024-12-31|


Diremo che il dato presente sul tracciato di input è valido se e solo se:

∃ VALUE\_R = VALUE\_A “tale che” VALID\_FROM<= **DATA\_COMPETENZA** <= VALID\_TO

(Esiste almeno un valore compreso tra le date di validità)

Dove:

- VALUE\_R rappresenta i campi del tracciato di input coinvolti nei controlli della specifica BR

- VALUE\_A rappresenta i campi dell’anagrafica coinvolti nei controlli della specifica BR

- VALID\_FROM/VALID\_TO rappresentano le colonne dell’anagrafica

- DATA\_COMPETENZA data da utilizzare per il filtraggio del dato anagrafico specifica per flusso


La DATA\_COMPETENZA da utilizzare per filtrare il dato anagrafico per il flusso VNM sulle tabelle con date di validità specifiche riportate in precedenza è l’anno della data non effettuazione del record.

## ***3.5 Alimentazione MdS***

### **3.5.1 Invio Flussi**

A valle delle verifiche effettuate dal Validation Engine, qualora il caricamento sia stato effettuato con il parametro Tipo Elaborazione impostato a P, verranno inviati verso MdS tutti i record corretti secondo le regole di validazione impostate.

Verrà richiamata la procedura invioFlussi di GAF WS (tramite PDI) alla quale verranno passati in input i parametri così come riportati nella seguente tabella:


|**NOME PARAMETRO**|**VALORIZZAZIONE**|
| :- | :- |
|ANNO RIFERIMENTO|Parametro ANNO RIFERIMENTO in input a SDK|
|PERIODO RIFERIMENTO|Parametro PERIODO RIFERIMENTO in input a SDK |
|CATEGORIA FLUSSI|AVT|
|NOME FLUSSO|VNM (Prestazioni Non Effettuate)|
|NOME FILE|Parametro popolato dall’SDK in fase di invio flusso con il nome file generato dal Validation Engine in fase di produzione file.|


### **3.5.2 Flussi di Output per alimentazione MdS**


I flussi generati dall’SDK saranno presenti sotto la cartella /sdk/xml\_output e dovranno essere salvati e inviati verso MdS rispettando la seguente nomenclatura:


**SDK\_AVT\_VNM\_\_13\_{ID\_RUN}.xml**


Dove:

- ID\_RUN rappresenta l’identificativo univoco



### **3.5.3 Gestione Risposta Mds**

A valle della presa in carico del dato da parte di MdS, SDK riceverà una response contenente le seguenti informazioni:

1. **codiceFornitura**: stringa numerica indicante l’identificativo univoco della fornitura inviata al GAF
1. **errorCode**: stringa alfanumerica di 256 caratteri rappresentante il codice identificativo dell’errore eventualmente riscontrato
1. **errorText**: stringa alfanumerica di 256 caratteri rappresentante il descrittivo dell’errore eventualmente riscontrato
1. Insieme delle seguenti triple, una per ogni file inviato:

 a. **idUpload**: stringa numerica indicante l’identificativo univoco del singolo file ricevuto ed accettato dal MdS, e corrispondente al file inviato con la denominazione riportata nell’elemento “nomeFile” che segue.

 b. **esito**: stringa alfanumerica di 4 caratteri rappresentante l’esito dell’operazione (Vedi tabella sotto).

 c. **nomeFile**: stringa alfanumerica di 256 caratteri rappresentante il nome dei file inviati.

Di seguito la tabella di riepilogo dei codici degli esiti possibili dell’invio del file:


|**ESITO**|**DESCRIZIONE**|
| :- | :- |
|AE00|Errore di autenticazione al servizio|
|IF00|Operazione completata con successo|
|IF01|Incongruenza tra CF utente firmatario e cf utente inviante|
|IF02|Firma digitale non valida|
|IF03|Firma digitale scaduta|
|IF04|Estensione non ammessa|
|IF05|Utente non abilitato all’invio per la Categoria Flusso indicata|
|IF06|Utente non abilitato all’invio per il Flusso indicata|
|IF07|Periodo non congurente con la Categoria Flusso indicata|
|IF08|Il file inviato è vuoto|
|IF09|Errore interno al servizio nella ricezione del file|
|IF10|Il numero di allegati segnalati nel body non corrisponde al numero di allegati riscontrati nella request|
|IF11|Il nome dell’allegato riportato nel body non è presente tra gli allegati della request (content-id)|
|IF12|Presenza di nomi file duplicati|
|IF13|Errore interno al servizio nella ricezione del file|
|IF14|Errore interno al servizio nella ricezione del file|
|IF15|Errore interno al servizio nella ricezione del file|
|IF99|Errore generico dell’operation|


Copia dei file inviati verso MdS il cui esito è positivo (ovvero risposta della procedura Invio Flussi con IF00) saranno trasferiti e memorizzati in una cartella ad hoc di SDK (es. /sdk/sent) rispettando la seguente naming:


**SDK\_AVT\_VNM\_\_13\_{ID\_RUN}.xml**


Dove :

- ID\_RUN rappresenta l’identificativo univoco.

## ***3.6 Scarti di processamento***

In una cartella dedicata (es. /sdk/esiti) verrà creato un file json contenente il dettaglio degli scarti riscontrati ad ogni esecuzione del processo SDK.

Il naming del file sarà:  ESITO\_{ID\_RUN}.json

Dove:

- ID\_RUN rappresenta l’identificativo univoco dell’elaborazione

Di seguito il tracciato del record da produrre.


|**CAMPO**|**DESCRIZIONE**|
| :- | :- |
|NUMERO RECORD|Numero del record del flusso input|
|RECORD PROCESSATO|Campi esterni rispetto al tracciato, che sono necessari per la validazione dello stesso.</p><p>Record su cui si è verificato uno scarto, riportato in maniera strutturata (nome\_campo-valore).|
|LISTA ESITI|<p>Lista di oggetti contenente l’esito di validazione per ciascun campo:</p><p>- Campo: nome campo su cui si è verificato uno scarto</p><p>- Valore Scarto: valore del campo su cui si è verificato uno scarto</p><p>- Valore Esito: esito di validazione del particolare campo</p><p>- Errori Validazione: contiene i campi Codice (della Business Rule) e Descrizione (della Business Rule)</p>|

## ***3.7 Informazioni dell’esecuzione***

In una cartella dedicata (es. /sdk/run) verrà creato un file contenente il dettaglio degli esiti riscontrati ad ogni esecuzione del processo SDK. Verrà prodotto un file di log per ogni giorno di elaborazione.

Il naming del file sarà:  

{ID\_RUN}.json

Dove:

- ID\_RUN rappresenta l’identificativo univoco dell’elaborazione

Di seguito il tracciato del record da produrre.


|**CAMPO**|**DESCRIZIONE**|
| :- | :- |
|ID RUN (chiave)|Identificativo univoco di ogni esecuzione del SDK|
|ID\_CLIENT|Identificativo Univoco della trasazione sorgente che richiede processamento lato SDK|
|ID UPLOAD (chiave)|Identificativo di caricamento fornito da MdS|
|TIPO ELABORAZIONE|F (full)/R (per singolo record) - Impostato di default a F|
|MODALITA’ OPERATIVA|P (=produzione) /T (=test)|
|DATA INIZIO ESECUZIONE|Timestamp dell’ inizio del processamento|
|DATA FINE ESECUZIONE|Timestamp di completamento del processamento|
|STATO ESECUZIONE |<p>Esito dell’esecuzione dell’ SDK. </p><p>Possibili valori: </p><p>- IN ELABORAZIONE: Sdk in esecuzione;</p><p>- ELABORATA: Esecuzione completata con successo;</p><p>- KO: Esecuzione fallita: </p><p>- KO SPECIFICO: Esecuzione fallita per una fase/componente più rilevante della soluzione (es. ko\_gestione\_file, ko\_gestione\_validazione, ko\_invio\_ministero, etc.); </p><p>- KO GENERICO: un errore generico non controllato.</p>|
|FILE ASSOCIATI RUN|nome del file di input elaborato dall’SDK|
|NOME FLUSSO|valore fisso che identifica lo specifico SDK in termini di categoria e nome flusso|
|NUMERO RECORD |Numero di record del flusso input|
|NUMERO RECORD ACCETTATI|Numero validi|
|NUMERO RECORD SCARTATI|Numero scarti|
|VERSION|Versione del SDK (Access Layer e Validation Engine)|
|TIMESTAMP CREAZIONE|Timestamp creazione della info run|
|API (\*DPM)|Rappresenta L’API utilizzata per il flusso DPM (non valorizzata per gli altri flussi)|
|IDENTIFICATIVO SOGGETTO ALIMENTANTE (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|TIPO ATTO (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|NUMERO ATTO (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|TIPO ESITO MDS (\*DPM)|Esito della response dell’API 2 (non valorizzata per gli altri flussi) |
|DATA RICEVUTA MDS (\*DPM)|Data della response dell’API 3 (non valorizzata per gli altri flussi)|
|CODICE REGIONE|Codice Regione del Mittente|
|ANNO RIFERIMENTO|Anno cui si riferiscono i dati del flusso|
|PERIODO DI RIFERIMENTO|Rappresenta il periodo di riferimento passato in input all’SDK|
|DESCRIZIONE STATO ESECUZIONE |Specifica il messaggio breve dell’errore, maggiori informazioni saranno presenti all’interno del log applicativo|
|NOME FILE OUTPUT MDS|Nome dei file di output inviati verso MdS|
|ESITO ACQUISIZIONE FLUSSO|Codice dell’esito del processo di acquisizione del flusso su MdS. Tale campo riflette la proprietà invioFlussiReturn/listaEsitiUpload/item/esito della response della procedura **invioFlussi**. (Es IF00)|
|CODICE ERRORE INVIO FLUSSI|Codice d’errore della procedura di invio. Tale campo riflette la proprietà InvioFlussiReturn/errorCode della response della procedura **invioFlussi**|
|TESTO ERRORE INVIO FLUSSI|Descrizione codice d’errore della procedura.Tale campo riflette la proprietà InvioFlussiReturn/ errorText della response della procedura **invioFlussi**|


Inoltre, a supporto dell’entità che rappresenta lo stato dell’esecuzione, sotto la cartella /sdk/log, saranno presenti anche i file di log applicativi (aggregati giornalmente) non strutturati, nei quali saranno presenti informazioni aggiuntive, ad esempio lo StackTrace (in caso di errori).

Il naming del file, se non modificata la politica di rolling (impostazioni), è il seguente:

**SDK \_AVT-VNM.log**

## mantainer:
 Accenture SpA until January 2026