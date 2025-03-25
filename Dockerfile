# Use Tomcat 10.0.10 as the base image
FROM tomcat:10.0.10

# Set the working directory inside the container
WORKDIR /usr/local/tomcat/webapps/

# Copy the WAR file from src/main/webapp to Tomcat's webapps directory
COPY src/main/webapp/Weather.war /usr/local/tomcat/webapps/Weather.war

# Expose port 8080 for the application
EXPOSE 8080

# Start Tomcat when the container launches
CMD ["catalina.sh", "run"]
