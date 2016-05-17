# identifier-extractor

[![Build Status](https://travis-ci.org/anqooqie/identifier-extractor.svg)](https://travis-ci.org/anqooqie/identifier-extractor)

This program parses java files and extracts identifiers from them as JSON.

## Usage
    git clone https://github.com/anqooqie/identifier-extractor.git
    cd identifier-extractor
    mvn compile package dependency:copy-dependencies
    java -jar target/identifier-extractor-1.0.0-SNAPSHOT.jar <input >output

## Example of Input
    /path/to/java/file
    /path/to/another/java/file

## Example of Output
    {"filePath":"/path/to/java/file","importDeclarations":[],"packageName":"jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor","staticImportDeclarations":[],"topLevelClasses":[{"accessLevel":"PUBLIC","annotations":[],"constructors":[],"fields":[],"isAbstract":false,"isFinal":false,"isStrictfp":false,"methods":[],"name":"Class","superInterfaces":[],"superclass":null,"typeParameters":[]}],"topLevelInterfaces":[]}
    {"filePath":"/path/to/another/java/file","importDeclarations":[],"packageName":"jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor","staticImportDeclarations":[],"topLevelClasses":[{"accessLevel":"PUBLIC","annotations":[],"constructors":[],"fields":[],"isAbstract":false,"isFinal":false,"isStrictfp":false,"methods":[],"name":"AnotherClass","superInterfaces":[],"superclass":null,"typeParameters":[]}],"topLevelInterfaces":[]}
