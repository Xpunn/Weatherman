package com.example.weatherman.repository;

import com.example.weatherman.model.WeatherInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherInfoRepo extends MongoRepository<WeatherInfo, String> {
    List<WeatherInfo> findWeatherInfoByDate(LocalDate date);
}
