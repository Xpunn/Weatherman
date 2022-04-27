package com.example.weatherman.controller;

import com.example.weatherman.model.WeatherInfo;
import com.example.weatherman.service.WeatherInfoService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/weather")
@AllArgsConstructor
public class Controller {
    private final WeatherInfoService service;

    // API key from env variables
    @Value("${API_KEY}")
    private String apiKey;

    @GetMapping(value = "/all")
    public ResponseEntity<List<WeatherInfo>> getAllWeatherFromDb() {
        return new ResponseEntity<>(service.getAllWeatherInfos(), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<List<WeatherInfo>> addWeatherInfo(@RequestBody List<List<WeatherInfo>> weatherInfos) {
        List<WeatherInfo> addedWeatherInfos = new ArrayList<>();

        for (List<WeatherInfo> weatherInfoList : weatherInfos) {
            for (WeatherInfo weatherInfo : weatherInfoList) {
                addedWeatherInfos.add(service.addWeatherInfo(weatherInfo));
            }
        }

        return new ResponseEntity<>(addedWeatherInfos, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{startDate}/{endDate}")
    public ResponseEntity<List<WeatherInfo>> getWeatherFromDbByDate(@PathVariable Map<String, String> pathvariables) {
        LocalDateTime startingDate = LocalDateTime.parse(pathvariables.get("starDate"));
        LocalDateTime endDate = LocalDateTime.parse(pathvariables.get("endDate"));

        // Create a list of dates between end and start dates
        Stream<LocalDate> dates = startingDate.toLocalDate().datesUntil(endDate.toLocalDate().plusDays(1));
        List<LocalDate> list = dates.toList();

        // Find all the WeatherInfos with the right dates
        List<WeatherInfo> weatherInfoList = new ArrayList<>();
        for (LocalDate localDate : list) {
            weatherInfoList.addAll(service.findWeatherInfoByDate(localDate));
        }

        return new ResponseEntity<>(weatherInfoList, HttpStatus.OK);
    }

    @GetMapping(value = "/{lat}/{long}/{startDate}/{endDate}")
    public ResponseEntity<List<List<WeatherInfo>>> getWeatherInfo(@PathVariable Map<String, String> pathVariables) {
        Double latitude = Double.parseDouble(pathVariables.get("lat"));
        Double longitude = Double.parseDouble(pathVariables.get("long"));
        LocalDateTime startingDate = LocalDateTime.parse(pathVariables.get("startDate"));
        LocalDateTime endDate = LocalDateTime.parse(pathVariables.get("endDate"));

        // Gets the info from the 2 sources
        List<WeatherInfo> weatherInfoFromApiMetNo = getWeatherFromApiMetNo(latitude, longitude, startingDate, endDate);
        List<WeatherInfo> weatherInfoFromWeatherApi = getWeatherFromWeatherApi(latitude, longitude, startingDate, endDate);

        List<List<WeatherInfo>> weatherInfo = new ArrayList<>();
        weatherInfo.add(weatherInfoFromApiMetNo);
        weatherInfo.add(weatherInfoFromWeatherApi);

        return new ResponseEntity<>(weatherInfo, HttpStatus.OK);
    }

    // Get weather info from api.met.no
    private List<WeatherInfo> getWeatherFromApiMetNo(Double latitude, Double longitude, LocalDateTime startingDate,
                                                     LocalDateTime endDate) {

        // Sends a get request with the latitude and longitude parameters
        String url = String.format("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=%f&lon=%f",
                latitude, longitude);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "WeathermanTestApp/0.1");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        List<WeatherInfo> weatherInfoList = new ArrayList<>();

        Iterator<JsonNode> timeSeriesI = response.getBody().get("properties").get("timeseries").elements();

        while (timeSeriesI.hasNext()) {
            JsonNode timeSeries = timeSeriesI.next();

            LocalDateTime dateTime = LocalDateTime.parse(timeSeries.get("time").asText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            LocalDate date = dateTime.toLocalDate();
            LocalTime time = dateTime.toLocalTime();

            // If the time is in the date range gets the other stats and creates the WeatherInfo
            if (dateTime.isAfter(startingDate) && dateTime.isBefore(endDate)) {
                Double temp = timeSeries.get("data").get("instant").get("details").get("air_temperature").asDouble();

                if (timeSeries.get("data").get("next_1_hours") != null) {
                    Double precipitation = timeSeries.get("data").get("next_1_hours").get("details")
                            .get("precipitation_amount").asDouble();

                    weatherInfoList.add(new WeatherInfo(latitude, longitude, date, time, temp, precipitation));
                } else if (timeSeries.get("data").get("next_6_hours") != null) {
                    Double precipitation = timeSeries.get("data").get("next_6_hours").get("details")
                            .get("precipitation_amount").asDouble();

                    // Creates and adds the WeatherInfo to the list
                    weatherInfoList.add(new WeatherInfo(latitude, longitude, date, time, temp, precipitation));
                }
            }
        }

        return weatherInfoList;
    }

    // Get weather info from weather api
    private List<WeatherInfo> getWeatherFromWeatherApi(Double latitude, Double longitude, LocalDateTime startingDate,
                                                       LocalDateTime endDate) {

        // Sends a get request
        String url = String.format("http://api.weatherapi.com/v1/forecast.json?key=%s&q=%f,%f&days=10",
                apiKey, latitude, longitude);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        List<WeatherInfo> weatherInfoList = new ArrayList<>();

        Iterator<JsonNode> forecastDayList = response.getBody().get("forecast").get("forecastday").elements();

        // Gets all the info from the request body and creates the WeatherInfos
        while (forecastDayList.hasNext()) {
            JsonNode forecastDay = forecastDayList.next();
            Iterator<JsonNode> hours = forecastDay.get("hour").elements();

            while (hours.hasNext()) {
                JsonNode hour = hours.next();

                LocalDateTime dateTime = LocalDateTime.parse(hour.get("time").asText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDate date = dateTime.toLocalDate();
                LocalTime time = dateTime.toLocalTime();
                if (dateTime.isAfter(startingDate) && dateTime.isBefore(endDate)) {
                    Double temp = hour.get("temp_c").asDouble();
                    Double precipitation = hour.get("precip_mm").asDouble();

                    // Creates and adds the WeatherInfo to the list
                    weatherInfoList.add(new WeatherInfo(latitude, longitude, date, time, temp, precipitation));
                }
            }
        }

        return weatherInfoList;
    }
}
