FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

# install curl
RUN apk --no-cache add curl

# install maven
ENV MAVEN_VERSION 3.3.9

RUN curl -fsSL http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

# generate jar from derdiedas
COPY pom.xml .
COPY src src
RUN mvn install -DskipTests -Dit.skip=true

FROM openjdk:8-jdk-alpine
VOLUME /tmp

# copy jar to temp directory
ARG DEPENDENCY=/workspace/app/target
COPY --from=build ${DEPENDENCY}/*.jar derdiedas.jar

# run derdiedas app
ENTRYPOINT ["java","-jar","/derdiedas.jar"]