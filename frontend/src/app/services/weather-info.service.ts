import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WeatherInfo } from '../weatherInfo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WeatherInfoService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public getWeatherInfo(params: string): Observable<WeatherInfo[][]> {
    return this.http.get<WeatherInfo[][]>(`${this.apiUrl}/${params}`);
  }

  public saveToDb(weatherInfos: WeatherInfo[][]): Observable<WeatherInfo[]> {
    return this.http.post<WeatherInfo[]>(`${this.apiUrl}/add`, weatherInfos);
  }

  public getFromDbByDate(params: string): Observable<WeatherInfo[]> {
    return this.http.get<WeatherInfo[]>(`${this.apiUrl}/${params}`);
  }
}
