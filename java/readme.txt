--- INFORMAZIONI RAPIDE ---

I 4 moduli sono stati sviluppati con IntelliJ Idea, e per ognuno di essi viene fornito il relativo file .iml
La versione di Java utilizzata è la jdk-8u191

--- Librerie utilizzate per testing JUnit su ogni progetto --- 
junit-4.12.jar
hamcrest-core-1.3.jar

--- Database ---
Nel progetto del sistema centrale è stato utilizzato un database con SQLite

- Libreria utilizzata
sqlite-jdbc-3.23.1.jar
- Salvata in 
sistema-centrale\lib\sqlite-jdbc-3.23.1.jar

- Il file contenente la base di dati si trova in 
sistema-centrale\db\traffic_monitor.db

Contiene già le tabelle ed i dati necessari al funzionamento del sistema

--- RMI ---
In ogni progetto è presente una classe RMIConfig all'interno del package dati
In questa classe è possibile visualizzare e modificare i parametri di collegamento per l'RMI
Di default sono tutti settati su localhost:

--- Altro ---
Il sistema necessita di una connesione ad internet dal momento che vengono utilizzate
le API di Google Maps per visualizzare la mappa nel progetto dell'interfaccia-utente