# compile vehicle.api module to "target/vehicle-api"
javac -p mods -d target/vehicle-api source/vehicle-api/vehicle/api/*.java source/vehicle-api/module-info.java
jar -cvf mods/vehicle-api.jar -C target/vehicle-api/ .

# compile vehicle.car module to "target/vehicle-car"
javac -p mods -d target/vehicle-car source/vehicle-car/vehicle/car/*.java source/vehicle-car/module-info.java
jar -cvf mods/vehicle-car.jar -C target/vehicle-car/ .

# compile car.ferrari module to "target/car-ferrari"
javac -p mods -d target/car-ferrari source/ferrari/car/ferrari/*.java source/ferrari/module-info.java
jar -cvf mods/car-ferrari.jar -C target/car-ferrari/ .

# compile car.skoda module to "target/car-skoda"
javac -p mods -d target/car-skoda source/skoda/car/skoda/*.java source/skoda/module-info.java
jar -cvf mods/car-skoda.jar -C target/car-skoda/ .

# compile nerdy.car.stats module to "target/nerdy-car-stats"
javac -p mods -d target/nerdy-car-stats source/nerdy-car-stats/car/statistics/*.java source/nerdy-car-stats/module-info.java
jar -cvf mods/nerdy-car-stats.jar -C target/nerdy-car-stats/ .

# run list statistics
java -p mods -m nerdy.car.stats/car.statistics.ListStats
