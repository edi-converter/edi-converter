# EDI2Excel – EDI/EDIFACT/X12 ➔ Excel/CSV Konverter

**Einfaches Java-Tool zur lokalen Konvertierung von EDI/EDIFACT/X12-Dateien in CSV/Excel-Tabellen.**

---

## Was macht dieses Tool?

Dieses Programm ermöglicht es, EDI/EDIFACT/X12-Dateien **ohne Internetverbindung** in ein CSV-Format umzuwandeln, das von Excel und anderen Tabellenkalkulationsprogrammen geöffnet werden kann.

**Hinweis:**  
Das Tool ist als Hilfsprogramm gedacht und verarbeitet alle Daten ausschließlich lokal auf dem ausführenden Rechner.  
Es werden **keine Daten an Dritte oder ins Internet übertragen**.

---

## Funktionen

- Konvertierung von EDI/EDIFACT/X12-Dateien zu CSV
- Lokale Verarbeitung, keine Server- oder Cloud-Anbindung
- Benutzerfreundliche Oberfläche (GUI)
- Unterstützt Windows, Mac und Linux (Java 17+ notwendig)

---

## Nutzung

1. Stelle sicher, dass Java 17 oder neuer installiert ist.
2. Kompiliere das Programm (oder starte die .jar-Datei, falls vorhanden):


    javac -d out src/*.java

    java -cp out EdiConverterUI


3. Wähle eine EDI-Datei und einen Speicherort für die Ausgabe (CSV/Excel).
4. Klicke auf **Exportieren**.

---

## Rechtliches & Haftungsausschluss

- Das Tool speichert und verarbeitet alle Daten ausschließlich lokal auf dem jeweiligen Rechner.
- Es findet **keine Übertragung, Speicherung oder Analyse von Daten außerhalb Ihres Geräts** statt.
- **Keine Gewährleistung:** Nutzung auf eigene Gefahr. Für die Korrektheit der Konvertierung, die Sicherheit oder rechtliche Konformität kann keine Haftung übernommen werden.
- Die Nutzung des Tools stellt **keine Rechtsberatung** dar.  
  Prüfen Sie bitte individuell, ob die Verwendung in Ihrem Unternehmen rechtlich zulässig ist.

---

## Lizenz

**MIT License** – Nutzung, Modifikation und Weitergabe sind erlaubt (siehe LICENSE).  
Siehe Lizenztext für Details.

---

## Kontakt

Feedback oder Fragen?  
Gerne als GitHub-Issue.

---

**Dies ist ein Community-Projekt. Keine offizielle EDI- oder Excel-Lösung.**

