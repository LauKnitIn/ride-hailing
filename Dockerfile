# Dockerfile genérico para todos los microservicios.
# Uso: docker build --build-arg SERVICE_DIR=conductores -t conductores .

# ---------- Etapa de build ----------
FROM maven:3.9-eclipse-temurin-25 AS build
ARG SERVICE_DIR
WORKDIR /build

# Primero solo el pom para cachear dependencias
COPY ${SERVICE_DIR}/pom.xml pom.xml
RUN mvn -B -q dependency:go-offline -DskipTests || true

COPY ${SERVICE_DIR}/src src
RUN mvn -B -q package -DskipTests \
 && cp target/*.jar /build/app.jar

# ---------- Etapa de runtime ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/app.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
