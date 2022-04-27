import { Time } from '@angular/common';

export interface WeatherInfo {
  id?: number;
  latitude: number;
  longitude: number;
  date: Date;
  time: Time;
  temp: number;
  precipitation: number;
}
