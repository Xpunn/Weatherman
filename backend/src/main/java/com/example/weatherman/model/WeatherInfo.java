package com.example.weatherman.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Document
public class WeatherInfo {
    @Id
    private String id;
    private Double latitude;
    private Double longitude;
    @Indexed
    private LocalDate date;
    private LocalTime time;
    private Double temp;
    private Double precipitation;

    public WeatherInfo(Double latitude, Double longitude, LocalDate date, LocalTime time, Double temp, Double precipitation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.precipitation = precipitation;
    }
}
