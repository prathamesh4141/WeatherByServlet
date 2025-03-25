package com.weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet("/getWeather")
public class WeatherServlet extends HttpServlet {
	private static String API_KEY;
    private static final String API_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        // Load environment variables from .env
        Dotenv dotenv = Dotenv.configure()
        	    .directory(getServletContext().getRealPath("/WEB-INF/classes"))
        	    .filename(".env")
        	    .load();


        API_KEY = dotenv.get("API_KEY");

        // Print confirmation in console
        System.out.println("Weather App initialized! API Key loaded successfully.");
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String location = request.getParameter("location");

        if (location == null || location.isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Please provide a valid location!\"}");
            return;
        }

        String apiUrl = API_URL + location + "?unitGroup=metric&key=" + API_KEY + "&contentType=json";

        try {
            HttpRequest apiRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> apiResponse = HttpClient.newHttpClient()
                    .send(apiRequest, HttpResponse.BodyHandlers.ofString());

            int responseCode = apiResponse.statusCode();

            if (responseCode != 200) {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Failed to fetch weather data!\"}");
                return;
            }

            String output = apiResponse.body();

            // ✅ Send JSON response to JSP
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(output);  // Correctly write the JSON response
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Error fetching data: " + e.getMessage() + "\"}");
        }
    }
}