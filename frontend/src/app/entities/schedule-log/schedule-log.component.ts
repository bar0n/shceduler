import {Component, OnInit, OnDestroy} from '@angular/core';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';

import {ScheduleLog} from '../../shared/model/schedule-log.model';
import {ScheduleLogService} from './schedule-log.service';
import {NotificationsService} from '../notifications';

@Component({
  selector: 'jhi-schedule-log',
  templateUrl: './schedule-log.component.html'
})
export class ScheduleLogComponent implements OnInit, OnDestroy {

  scheduleLogs: ScheduleLog[];
  error: any;
  success: any;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    private scheduleLogService: ScheduleLogService,
    private notificationsService: NotificationsService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.itemsPerPage = 20;
    /*  this.routeData = this.activatedRoute.data.subscribe((data) => {
          this.page = data.pagingParams.page;
          this.previousPage = data.pagingParams.page;
          this.reverse = data.pagingParams.ascending;
          this.predicate = data.pagingParams.predicate;
      });*/
  }

  loadAll() {
    this.scheduleLogService.query({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    }).subscribe(
      (res: HttpResponse<ScheduleLog[]>) => this.onSuccess(res.body, res.headers),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/schedule-log'], {
      queryParams:
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate(['/schedule-log', {
      page: this.page,
      sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    }]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();

  }

  ngOnDestroy() {
  }

  trackId(index: number, item: ScheduleLog) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    // this.page = pagingParams.page;
    this.scheduleLogs = data;
  }

  private onError(error) {
    this.notificationsService.notify('error', error.message, null);
  }
  y:any;
  onChangeCompleted(log) {
    this.scheduleLogService.update(log).subscribe(x => this.notificationsService.notify('success', null, null),
      y => {
      this.y = y;
        this.notificationsService.notify('error', JSON.stringify(y), null)
      });
  }
}
