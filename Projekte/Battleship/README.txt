README zu Battleship
====================

Beachte: Battleship besteht aus einem Server- und einem
Client-Programm. Zum Spielen müssen ein Server und zwei Clients auf
beliebigen Rechnern gestartet werden.

Wenn man die Programme von der Kommandozeile starten möchte, erzeugt
man unter Windows am besten ausführbare Programme und Start-Skripte mit
dem Befehl

   ..\gradlew   

Programmstart des Clients bzw. Servers von der Kommandozeile:

   build\install\battleship\bin\battleship
   build\install\battleship\bin\server
 
Unter Unix (MacOS oder Linux) ist \ jeweils durch / zu ersetzen.


Gradle-Tasks:
------------

..\gradlew clean

   Entfernt das Verzeichnis build und alle erzeugten Dateien

..\gradlew classes

   Übersetzt den Quellcode und legt unter build den Bytecode sowie
   Ressourcen ab

..\gradlew javadoc

  Erzeugt die Dokumentation aus den JavaDoc-Kommentaren in
  build\docs\javadoc. Sinnvollerweise öffnet man
  build\docs\javadoc\allpackages-index.html

..\gradlew test

  Führt die JUnit-Tests durch

..\gradlew build

  Führt die JUnit-Tests durch und erstellt in build\distributions
  gepackte Distributionsdateien

..\gradlew installDist

  Erstellt unter build/install ein Verzeichnis, das eine ausführbare
  Distribution samt Start-Skripten enthält (siehe oben).
  
--
September 2019
