# Stage: 1
FROM openjdk:8 AS builder

WORKDIR /app

# 将源代码放入后编译、打包
ADD . /app
RUN mkdir /root/.gradle
RUN echo 'ossrhUsername=user \
          ossrhPassword=passwd > /root/.gradle/gradle.properties'
# use BuildKit
RUN ./gradlew bootJar

# Stage: 2
FROM openjdk:8-jre

COPY --from=builder /app/demo/build/libs/8x-demo-server.jar /app.jar


ENTRYPOINT ["java", "-jar", "/app.jar"]
