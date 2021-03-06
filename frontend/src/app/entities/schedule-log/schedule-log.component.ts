import {Component, OnInit, OnDestroy} from '@angular/core';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';

import {ScheduleLog} from '../../shared/model/schedule-log.model';
import {ScheduleLogService} from './schedule-log.service';
import {NotificationsService} from '../notifications';
import {ConfirmationService} from "primeng/api";
import {Schedule} from "../../shared/model/schedule.model";

@Component({
  selector: 'jhi-schedule-log',
  styles: [`
    /* Column Priorities */
    @media only all {
      th.ui-p-6,
      td.ui-p-6,
      th.ui-p-5,
      td.ui-p-5,
      th.ui-p-4,
      td.ui-p-4,
      th.ui-p-3,
      td.ui-p-3,
      th.ui-p-2,
      td.ui-p-2,
      th.ui-p-1,
      td.ui-p-1 {
        display: none;
      }
    }

    /* Show priority 1 at 320px (20em x 16px) */
    @media screen and (min-width: 20em) {
      th.ui-p-1,
      td.ui-p-1 {
        display: table-cell;
      }
    }

    /* Show priority 2 at 480px (30em x 16px) */
    @media screen and (min-width: 30em) {
      th.ui-p-2,
      td.ui-p-2 {
        display: table-cell;
      }
    }

    /* Show priority 3 at 640px (40em x 16px) */
    @media screen and (min-width: 40em) {
      th.ui-p-3,
      td.ui-p-3 {
        display: table-cell;
      }
    }

    /* Show priority 4 at 800px (50em x 16px) */
    @media screen and (min-width: 50em) {
      th.ui-p-4,
      td.ui-p-4 {
        display: table-cell;
      }
    }

    /* Show priority 5 at 960px (60em x 16px) */
    @media screen and (min-width: 60em) {
      th.ui-p-5,
      td.ui-p-5 {
        display: table-cell;
      }
    }

    /* Show priority 6 at 1,120px (70em x 16px) */
    @media screen and (min-width: 70em) {
      th.ui-p-6,
      td.ui-p-6 {
        display: table-cell;
      }
    }
  `],
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
    private router: Router,
    public confirmationService: ConfirmationService,
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
      page: this.page,
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
    let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate === undefined) {
      result = ['id' + ',' + (this.reverse ? 'asc' : 'desc')];
    }
    return result;
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;

    this.scheduleLogs = data;
  }

  private onError(error) {
    this.notificationsService.notify('error', error.message, null);
  }


  onChangeCompleted(log: ScheduleLog) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to change completion?',
      accept: () => {
        log.completed = !log.completed;
        this.scheduleLogService.update(log).subscribe(x =>
            this.notificationsService.notify('success', null, "Completed"),
          y => {

            this.notificationsService.notify('error', JSON.stringify(y), null)
          });
      }
    });

  }

  onDelete(log: ScheduleLog) {
    this.scheduleLogService.delete(log.id).subscribe(x => {
        this.loadAll();
        this.notificationsService.notify('success', null, "Deleted");
      }
      ,
      y => {

        this.notificationsService.notify('error', JSON.stringify(y), null)
      });
  }

  paginate(event) {
    this.page = event.page;
    this.itemsPerPage = event.rows;
    this.loadAll();
  }
  handleSort(event) {
    if (event.field) {
      let reverse1 = event.order === 1;
      if (this.reverse !== reverse1 || this.predicate !== event.field){
        this.reverse = reverse1;
        this.predicate = event.field;
        this.loadAll();
      }

    }
  }
}
