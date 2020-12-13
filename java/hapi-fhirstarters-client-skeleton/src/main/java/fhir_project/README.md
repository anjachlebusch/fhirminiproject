# Impfausweis mit HL7 FHIR
## Allgemeine Anmerkungen
Da die Operation $document auf dem Uni-Server (funke) nicht funktioniert hat, haben wir uns entschlossen, die Ressourcen auf dem offiziellen HAPI FHIR Server zu erstellen. Sollten diese inzwischen gelöscht worden sein, können sie durch Ausführung der Java-Datei `FHIRUebung7.java` neu erstellt werden. Dabei werden die IDs aller Ressourcen ausgegeben.

## Erstellung der Ressourcen
Ein von uns modellierter Impfausweis kann durch Ausführung der Java-Datei `FHIRUebung7.java` neu erstellt werden. Dabei werden die IDs aller Ressourcen ausgegeben. Zur Darstellung wird die ID der Composition benötigt.

## Darstellung des Impfausweises
Zur Darstellung wurde Javascript mit der Bibliothek JSON2HTML verwendet. Der von uns modellierte Impfausweis kann durch Ausführung der Datei darstellung.html im Browser angezeigt werden. Um eine neu erstellte Composition zur Darstellung zu verwenden, kann die ID in der URL in Zeile 5 des HTML-Codes entsprechend angepasst werden. Diese URL wird für den Request auf den Server verwendet, die daraus resultierende JSON-Response wird mit JSON2HTML gerendert.