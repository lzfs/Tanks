README zu EmptyProject
======================

Dieses Projekt ist der Rahmen für das im Programmierprojekt zu
erstellende eigene Spiel. Sinnvollerweise sollte man diesem Verzeichnis
dann einen geeigneten Namen geben. Entsprechend sind die Dateien
settings.gradle im übergeordneten Verzeichnis und build.gradle in
diesem Verzeichnis anzupassen.



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
