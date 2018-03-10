import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/operator/map'

import {HttpClient, HttpResponse} from '@angular/common/http';
import {Schedule} from '../../shared/model/schedule.model';
import DateUtils from '../../shared/util/date-utils';
import {SERVER_API_URL} from '../../app.constants';

export type EntityResponseType = HttpResponse<Schedule>;

@Injectable()
export class EventService {

  private resourceUrl = SERVER_API_URL + 'api/events';
  private dateUtils: DateUtils = new DateUtils();

  constructor(private http: HttpClient) {
  }

  getEvents(start, end, cron): Observable<any[]> {
    let req = {
      start: start,
      end: end,
      cron: cron

    };
    return this.http.post<any[]>(this.resourceUrl, req).map(x => x.map(y => this.dateUtils.convertDateTimeFromServer(y)));
  }
}
