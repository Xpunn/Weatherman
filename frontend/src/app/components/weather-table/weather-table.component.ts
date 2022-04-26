import { Component, Input, OnInit } from '@angular/core';
import { WeatherInfo } from 'src/app/weatherInfo';

@Component({
  selector: 'app-weather-table',
  templateUrl: './weather-table.component.html',
  styleUrls: ['./weather-table.component.css'],
})
export class WeatherTableComponent implements OnInit {
  @Input() weatherInfos: WeatherInfo[][] = [];

  constructor() {}

  ngOnInit(): void {}
}
