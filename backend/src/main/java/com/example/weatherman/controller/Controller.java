package com.example.weatherman.controller;

import com.example.weatherman.model.WeatherInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/weather")
public class Controller {

    @GetMapping(value = "/{lat}/{long}/{startDate}/{endDate}")
    public ResponseEntity<List<WeatherInfo>> getWeatherInfo(@PathVariable Map<String, String> pathVariables) {
        Double latitude = Double.parseDouble(pathVariables.get("lat"));
        Double longitude = Double.parseDouble(pathVariables.get("long"));
        LocalDateTime startingDate = LocalDateTime.parse(pathVariables.get("startDate"));
        LocalDateTime endDate = LocalDateTime.parse(pathVariables.get("endDate"));

        List<WeatherInfo> weatherInfoList = getWeatherFromApiMetNo(latitude, longitude, startingDate, endDate);

        return new ResponseEntity<>(weatherInfoList, HttpStatus.OK);
    }

    // Get weather info from api.met.no
    private List<WeatherInfo> getWeatherFromApiMetNo(Double latitude, Double longitude, LocalDateTime startingDate, LocalDateTime endDate) {

        // Send a get request with the latitude and longitude parameters
        String url = String.format("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=%f&lon=%f", latitude, longitude);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "WeathermanTestApp/0.1");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        // Create a list of WeatherInfos with the data from the request
        List<WeatherInfo> weatherInfoList = new ArrayList<>();

        Iterator<JsonNode> list = response.getBody().get("properties").get("timeseries").elements();

        while (list.hasNext()) {
            JsonNode timeSeries = list.next();
            String timeString = timeSeries.get("time").asText();
            LocalDateTime time = LocalDateTime.parse(timeString.substring(0, timeString.length() - 1));

            // If the time is in the date range get the other stats and create the WeatherInfo
            if (time.isAfter(startingDate) && time.isBefore(endDate)) {
                Double temp = timeSeries.get("data").get("instant").get("details").get("air_temperature").asDouble();

                if (timeSeries.get("data").get("next_6_hours") != null) {
                    Double percipitation = timeSeries.get("data").get("next_6_hours").get("details").get("precipitation_amount")
                            .asDouble();

                    weatherInfoList.add(new WeatherInfo(latitude, longitude, time, temp, percipitation));
                }
            }
        }

        return weatherInfoList;
    }
}
