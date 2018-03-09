import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {SERVER_API_URL} from '../../app.constants';
import {ScheduleLog} from '../../shared/model/schedule-log.model';
import DateUtils from '../../shared/util/date-utils';
import {createRequestOption} from '../../shared/model/request-util';


export type EntityResponseType = HttpResponse<ScheduleLog>;

@Injectable()
export class ScheduleLogService {

  private resourceUrl = SERVER_API_URL + 'api/scheduleLogs';
  private dateUtils: DateUtils = new DateUtils();

  constructor(private http: HttpClient) {
  }

  create(scheduleLog: ScheduleLog): Observable<EntityResponseType> {
    const copy = this.convert(scheduleLog);
    return this.http.post<ScheduleLog>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  update(scheduleLog: ScheduleLog): Observable<EntityResponseType> {
    const copy = this.convert(scheduleLog);
    return this.http.put<ScheduleLog>(this.resourceUrl, copy, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ScheduleLog>(`${this.resourceUrl}/${id}`, {observe: 'response'})
      .map((res: EntityResponseType) => this.convertResponse(res));
  }

  query(req?: any): Observable<HttpResponse<ScheduleLog[]>> {
    const options = createRequestOption(req);
    return this.http.get<ScheduleLog[]>(this.resourceUrl, {params: options, observe: 'response'})
      .map((res: HttpResponse<ScheduleLog[]>) => this.convertArrayResponse(res));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
    const body: ScheduleLog = this.convertItemFromServer(res.body);
    return res.clone({body});
  }

  private convertArrayResponse(res: HttpResponse<ScheduleLog[]>): HttpResponse<ScheduleLog[]> {
    const jsonResponse: ScheduleLog[] = res.body;
    const body: ScheduleLog[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({body});
  }

  /**
   * Convert a returned JSON object to ScheduleLog.
   */
  private convertItemFromServer(scheduleLog: ScheduleLog): ScheduleLog {
    const copy: ScheduleLog = Object.assign({}, scheduleLog);
    copy.created = this.dateUtils
      .convertDateTimeFromServer(scheduleLog.created);
    copy.next = this.dateUtils
      .convertDateTimeFromServer(scheduleLog.next);
    return copy;
  }

  /**
   * Convert a ScheduleLog to a JSON which can be sent to the server.
   */
  private convert(scheduleLog: ScheduleLog): ScheduleLog {
    const copy: ScheduleLog = Object.assign({}, scheduleLog);

    copy.created = this.dateUtils.toDate(scheduleLog.created);
    copy.next = this.dateUtils.toDate(scheduleLog.next);
    return copy;
  }
}
