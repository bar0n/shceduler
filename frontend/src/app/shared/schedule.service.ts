import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';

import {HttpClient} from '@angular/common/http';
import {Schedule} from './model/schedule.model';


@Injectable()
export class ScheduleService {


  constructor(private httpClient: HttpClient) {
  }


  getAllSchedules(): Observable<Schedule[]> {
    return this.httpClient.get<Schedule[]>('api/schedule/');
  }
}
