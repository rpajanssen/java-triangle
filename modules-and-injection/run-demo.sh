
# run with class path
#java -cp demo-app/target/*:demo-api/target/*:demo-implementation/target/* demo.application.MyApp

# run with module path
#java -p demo-app/target/classes:demo-api/target/classes:demo-implementation/target/classes -m demo.application/demo.application.MyApp

# run with module path with guice
java -p lib:demo-app/target/classes:demo-api/target/classes:demo-implementation/target/classes -m demo.application/demo.application.MyApp
