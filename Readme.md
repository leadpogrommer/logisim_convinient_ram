# Logisim AutoRAM
[![Release](https://github.com/leadpogrommer/logisim_convinient_ram/actions/workflows/release.yml/badge.svg?branch=master)](https://github.com/leadpogrommer/logisim_convinient_ram/releases)  
Simple logisim library that adds RAM chip which loads its initial state from specified file on every reset
## Building
Use `./gradlew shadowJar`

## Loading
Select `Project > Load Librry > JAR library`, then select library file. When it asks for class name, type `ru.leadpogommer.autoram.Components`
