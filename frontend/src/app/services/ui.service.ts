import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UiService {
  private showSearch: boolean = false;
  private subject = new Subject<any>();

  constructor() {}

  toggleSearch(): void {
    this.showSearch = !this.showSearch;
    this.subject.next(this.showSearch);
  }

  onToggle(): Observable<any> {
    return this.subject.asObservable();
  }
}
