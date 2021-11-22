# compile developer-api module to "target/developer-api"
javac -p mods -d target/developer-api source/developer-api/com/abnamro/developer/api/*.java source/developer-api/module-info.java
jar -cvf mods/developer-api.jar -C target/developer-api/ .

# compile developer-pool module to "target/developer-pool"
find source/developer-pool/ -name *.java > sources.txt
javac -p mods -d target/developer-pool @sources.txt
jar -cvf mods/developer-pool.jar -C target/developer-pool/ .

# compile company module to "target/company"
find source/company/ -name *.java > sources.txt
javac -p mods -d target/company @sources.txt
jar -cvf mods/company.jar -C target/company/ .

java -p mods -m com.abnamro.company/com.abnamro.company.WorkingDay
