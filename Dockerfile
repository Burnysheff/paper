# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Задаем рабочую директорию
WORKDIR /app

# Копируем файл .jar приложения в контейнер
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Задаем порт, который будет прослушивать приложение
EXPOSE 8080

# Команда, которая будет выполнять запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]