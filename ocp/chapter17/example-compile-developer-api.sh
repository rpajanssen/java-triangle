# compile developer-api module to "target/developer-api"
javac -p mods -d target/developer-api source/developer-api/com/abnamro/developer/api/*.java source/developer-api/module-info.java
jar -cvf mods/developer-api.jar -C target/developer-api/ .

# compile developer-feeding module to "target/developer-feeding"
#
# since we have a lot of source java files it is (almost) impossible to create a command line command
# to compile the module (you really need a build tool) but with a trick providing a file as argument
# to the compiler we are able to still compile without a build tool... we generate a file listing
# all java sources first and pass that to the compiler
find source/developer-feeding/ -name *.java > sources.txt
javac -p mods -d target/developer-feeding @sources.txt
jar -cvf mods/developer-feeding.jar -C target/developer-feeding/ .

# compile developer-meetups module to "target/developer-meetups"
find source/developer-meetups/ -name *.java > sources.txt
javac -p mods -d target/developer-meetups @sources.txt
jar -cvf mods/developer-meetups.jar -C target/developer-meetups/ .

# compile developer-lazy module to "target/developer-lazy"
find source/developer-lazy/ -name *.java > sources.txt
javac -p mods -d target/developer-lazy @sources.txt
jar -cvf mods/developer-lazy.jar -C target/developer-lazy/ .

# compile developer-hipo module to "target/developer-hipo"
find source/developer-hipo/ -name *.java > sources.txt
javac -p mods -d target/developer-hipo @sources.txt
jar -cvf mods/developer-hipo.jar -C target/developer-hipo/ .

# compile developer-pool module to "target/developer-pool"
find source/developer-pool/ -name *.java > sources.txt
javac -p mods -d target/developer-pool @sources.txt
jar -cvf mods/developer-pool.jar -C target/developer-pool/ .

# compile company module to "target/company"
find source/company/ -name *.java > sources.txt
javac -p mods -d target/company @sources.txt
jar -cvf mods/company.jar -C target/company/ .

java -p mods -m com.abnamro.company/com.abnamro.company.WorkingDay
