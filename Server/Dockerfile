# Sử dụng một image của Tomcat (hoặc bạn có thể dùng một image khác)
FROM tomcat:10-jdk17

# Copy file WAR vào thư mục webapps của Tomcat
COPY build/libs/Server-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose cổng mà Tomcat chạy
EXPOSE 8080

# Chạy Tomcat
CMD ["catalina.sh", "run"]
