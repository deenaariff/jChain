FROM anapsix/alpine-java

COPY target/blockchain-1.0-SNAPSHOT-jar-with-dependencies.jar /home/target/blockchain-1.0-SNAPSHOT-jar-with-dependencies.jar

EXPOSE 5000
CMD ["java","-jar","/home/target/blockchain-1.0-SNAPSHOT-jar-with-dependencies.jar"]
