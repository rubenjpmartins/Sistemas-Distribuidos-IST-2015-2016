mvn clean
mvn generate-sources
mvn compile
mvn -Dws.i=2 exec:java