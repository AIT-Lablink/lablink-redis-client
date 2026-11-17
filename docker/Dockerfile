# ---------- Stage 1: resolve deps + build (Maven + JDK 11) ----------
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /build

# Copy ONLY the POM first — this layer caches all Maven Central downloads
COPY pom.xml .
RUN mvn -B -e dependency:go-offline

# The published fat jar is the only input. The client targets Java 8.
RUN mvn -B package -DskipTests \
    && mvn -B dependency:copy-dependencies -DoutputDirectory=target/lib

# ---------- Stage 2: runtime (JDK 11) ----------
FROM eclipse-temurin:11-jdk-jammy

WORKDIR /app
COPY --from=build /build/target/lib /app/lib

# Connection string for configuration server. Override at runtime with -e LLCONFIG=...
# LOGGER_CONFIG and CONFIG_FILE_URI are derived from this in entrypoint.sh.
ENV LLCONFIG=http://lablink-config:10101/get?id=

# Redis client configuration id (appended to LLCONFIG).
ENV REDIS_CONFIG=lablink-redisclient.config

# Path to class implementing the main routine.
ENV REDIS_CLASS=at.ac.ait.lablink.clients.redisclient.RedisOpalClient

# Path to Java JAR file of the Redis client.
ENV REDIS_JAR_FILE=/app/lib/redisclient-0.0.2-jar-with-dependencies.jar

# Specify the maximum size (in bytes) of the memory allocation pool.
ENV MEMORY_FLAG=-Xmx512m

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
