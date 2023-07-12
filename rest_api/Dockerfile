# Docker tutorial on spring boot docker image:
# https://www.docker.com/blog/9-tips-for-containerizing-your-spring-boot-code/

# Spring boot tutorial on using the docker image:
# https://spring.io/guides/topicals/spring-boot-docker/

# eclipse-temurin docker hub page:
# https://hub.docker.com/_/eclipse-temurin

# build stage
FROM eclipse-temurin:17-jdk-alpine AS build

# setup workdir
WORKDIR /app

# copy over maven wrapper
# https://www.baeldung.com/maven-wrapper
COPY .mvn/ .mvn

# copy over dependencies
COPY mvnw pom.xml ./

# setup maven in offline mode
# https://maven.apache.org/plugins/maven-dependency-plugin/go-offline-mojo.html
RUN ./mvnw dependency:go-offline

# copy over soure code
COPY ./src ./src

# install dependencies 
RUN ./mvnw install -DskipTests

# 
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../rest_api-1.0-SNAPSHOT.jar)

# prod stage 
FROM eclipse-temurin:17-jre-jammy as prod

# setup workdir
# WORKDIR /app

# expose port
EXPOSE 8080

# copy over jar from builder stage
# COPY --from=builder /opt/app/target/*.jar /opt/app/api.jar
ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# run jar
# ENTRYPOINT ["java", "-jar", "/opt/app/api.jar" ]
# ENTRYPOINT [ "./mvnw", "spring-boot:run" ]
ENTRYPOINT ["java","-cp","app:app/lib/*","net.palettehub.api.App"]