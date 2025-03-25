# Use official Tomcat image as the base
FROM tomcat:10.0.10

# Copy the WAR file from target directory to Tomcat's webapps directory
COPY target/Weather.war WeatherByServlet/target/Weather.war
# Expose port 8080 (Tomcat default port)
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
