# === 빌드 스테이지 ===
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
# Gradle 프로젝트 구조가 동일하다면 아래 내용 유사하게 사용
COPY gradlew ./
COPY gradle ./gradle
RUN chmod +x ./gradlew
COPY build.gradle settings.gradle ./
# build.gradle 에 맞는 의존성 다운로드 (필요시 dependencies 작업 수정)
RUN ./gradlew dependencies --no-daemon
COPY src ./src
RUN ./gradlew build -x test --no-daemon

# === 최종 런타임 스테이지 ===
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
# --- WebSocket 앱이 사용하는 포트로 수정! (예: 8082) ---
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]