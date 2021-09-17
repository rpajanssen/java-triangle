# compile developer-api module to "target/developer-api" without module path (should compile)
javac -d target/developer-api source/developer-api/com/abnamro/developer/api/*.java source/developer-api/module-info.java
jar -cvf mods/developer-api.jar -C target/developer-api/ .

# compile developer-feeding module to "target/developer-feeding" without module path (should compile)
find source/developer-feeding/ -name *.java > sources.txt
javac -d target/developer-feeding @sources.txt
jar -cvf mods/developer-feeding.jar -C target/developer-feeding/ .

# compile developer-meetups module to "target/developer-meetups" without module path (should fail)
find source/developer-meetups/ -name *.java > sources.txt
javac -d target/developer-meetups @sources.txt
jar -cvf mods/developer-meetups.jar -C target/developer-meetups/ .
