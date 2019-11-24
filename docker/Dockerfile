# build contaier
FROM openjdk:8-jdk-alpine as build
WORKDIR /app
VOLUME /tmp

# copy source code and pom file for maven
COPY pom.xml .
COPY src src

# install maven
RUN cd /tmp && \
    wget http://ftp.halifax.rwth-aachen.de/apache/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz && \
    tar xzvf apache-maven-3.6.2-bin.tar.gz && \
    mv apache-maven-3.6.2 /opt/

# do not -DskipTests
RUN /opt/apache-maven-3.6.2/bin/mvn install 
# move jar and extract for it deployment container
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# deployment contaier
FROM openjdk:8-jdk-alpine
WORKDIR /app
ARG DEPENDENCY=/app/target/dependency

# copy java app
# do not copy fat jar to take advantage of dockers cache during build
# COPY jpa-0.0.1-SNAPSHOT.jar /app/
ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib lib
COPY --from=build ${DEPENDENCY}/META-INF META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes classes

# run java app
#ENTRYPOINT ["java","-cp","/app/lib/*:/app/classes", "de.rrob.spring.App"]
CMD ["java","-cp","./lib/*:./classes", "de.rrob.spring.App"]

# expose port
EXPOSE 9091

# sample docker run
# docker run -ti -p 9091:9091 -v \
#  /Users/robertrichter/development/java/spring_jpa/jpa/config:/app/config \
#  robertrichter/spring-demo