package com.example.weatherman.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WeatherInfo {
    private Double longitude;
    private Double latitude;
    private LocalDateTime time;
    private Double temp;
    private Double precipitation;
}
