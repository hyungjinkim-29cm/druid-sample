# JAR 파일을 Docker 이미지에 복사
FROM openjdk:17-jdk-slim

# JAR 파일 경로를 정확히 지정
COPY build/libs/druid-api-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너에서 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
