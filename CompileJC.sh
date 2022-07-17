#! /bin/bash
# Compile the Java Calculator to .jar file

javac -d out/ src/*
jar cmvf META-INF/MANIFEST.MF JavaCalculator.jar out/*
java -jar --enable-preview JavaCalculator.jar 