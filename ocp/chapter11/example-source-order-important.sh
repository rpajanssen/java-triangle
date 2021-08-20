# compile vehicle-api module : but we listed the module-info.java first
# note : OCP book suggests this would fail but is incorrect
javac -p mods -d target/vehicle-api source/vehicle-api/module-info.java source/vehicle-api/vehicle/api/*.java

# leaving the classes out does make it fail
#javac -p mods -d target/vehicle-api source/vehicle-api/module-info.java
