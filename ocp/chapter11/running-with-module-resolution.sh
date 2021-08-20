# compile nerdy.car.stats module to "target/nerdy-car-stats"
javac -p mods -d target/nerdy-car-stats source/nerdy-car-stats/car/statistics/*.java source/nerdy-car-stats/module-info.java
jar -cvf mods/nerdy-car-stats.jar -C target/nerdy-car-stats/ .

java --show-module-resolution -p mods -m nerdy.car.stats/car.statistics.ListStats
