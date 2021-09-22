# compile vehicle.api module to "target/vehicle-api" without module path (should compile)
#javac -d target/vehicle-api source/vehicle-api/vehicle/api/*.java source/vehicle-api/module-info.java
#jar -cvf mods/vehicle-api.jar -C target/vehicle-api/ .

# compile vehicle.car module to "target/vehicle-car" without module path (should fail)
javac -d target/vehicle-car source/vehicle-car/vehicle/car/*.java source/vehicle-car/module-info.java
#jar -cvf mods/vehicle-car.jar -C target/vehicle-car/ .
