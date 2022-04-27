import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements OnInit {
  @Output() onMapClick: EventEmitter<number[]> = new EventEmitter();
  private map!: L.Map;
  private center: L.LatLngExpression = [59.437, 24.7536];
  latitude!: number;
  longitude!: number;
  marker!: L.Marker;

  constructor() {}

  private initMap(): void {
    this.map = new L.Map('map', {
      center: this.center,
      zoom: 12,
    });

    this.map.on({
      click: (event) => {
        if (this.marker) {
          this.map.removeLayer(this.marker);
        }
        this.onMapClick.emit([event.latlng.lat, event.latlng.lng]),
          (this.latitude = event.latlng.lat),
          (this.longitude = event.latlng.lng);
        this.marker = new L.Marker(event.latlng).addTo(this.map);
      },
    });

    L.tileLayer('https://{s}.tile.osm.org/{z}/{x}/{y}.png').addTo(this.map);
  }

  ngOnInit(): void {
    this.initMap();
  }
}
