## EDF viewer backend

Backend part of the EDF viewer application.

EDF files (European Data Format) is a binary file format for storing biomedical time-series data used in healthcare.

### Requirements
* Java 21+
* Maven

### Setup
* Install the EDF reader package by running:

`mvn install:install-file
-Dfile=edf4j.jar
-DgroupId=ru.mipt.edf
-DartifactId=edf4j
-Dversion=1.0
-Dpackaging=jar
`

* Set `EDF_SOURCE_DIRECTORY` environment variable which is the absolute path of the directory containing edf files.
* Start the application. 
* The API is available on localhost:8080
