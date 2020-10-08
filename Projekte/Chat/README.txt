README zu Chat
==============

Wenn man das Chat-Programme von der Kommandozeile starten möchte, erzeugt
man unter Windows am besten das ausführbare Programm und ein Start-Skript mit
dem Befehl

   ..\gradlew   

Programmstart des Programms von der Kommandozeile:

   build\install\chat\bin\chat
 
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
