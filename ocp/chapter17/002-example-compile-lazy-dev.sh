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
