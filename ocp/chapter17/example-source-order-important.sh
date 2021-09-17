# compile and package developer-api module : but we listed the module-info.java first
# note : OCP book suggests this would fail but is incorrect
javac -p mods -d target/developer-api source/developer-api/module-info.java source/developer-api/com/abnamro/developer/api/*.java

# leaving the classes out does make it fail
#javac -p target -d target/developer-api source/developer-api/module-info.java
