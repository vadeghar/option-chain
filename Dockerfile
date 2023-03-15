FROM ibm-semeru-runtimes:open-11-jre-focal
MAINTAINER https://renanfranca.github.io/about.html
COPY target/option-chain-1.0-SNAPSHOT.jar option-chain-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app.jar"]
