# compile developer-hipo module to "target/developer-hipo"
find source/developer-hipo/ -name *.java > sources.txt
javac -p mods -d target/developer-hipo @sources.txt
jar -cvf mods/developer-hipo.jar -C target/developer-hipo/ .

