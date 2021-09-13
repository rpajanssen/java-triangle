# compile demo module to "target/demo"
javac -p mods -d target/demo source/demo/my/code/*.java source/demo/module-info.java
#jar -cvf mods/demo.jar -C target/demo/ .
