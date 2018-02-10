import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ScheduleService} from './schedule.service';
import {Schedule} from "../../shared/model/schedule.model";
import {NotificationsService} from "../notifications/notifications.service";


@Component({
    selector: 'schedule',
    templateUrl: './schedule.component.html'
})
export class ScheduleComponent implements OnInit, OnDestroy {

    schedules: Schedule[];
    error: any;
    success: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    displayDialog: boolean;

    schedule: Schedule = {};

    selectedSchedule: Schedule;

    newSchedule: boolean;

    constructor(
        private scheduleService: ScheduleService,
       // private parseLinks: JhiParseLinks,
        private notificationsService: NotificationsService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
    //    private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = 20;

    }

    loadAll() {
        this.scheduleService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<Schedule[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/schedule'], {queryParams:
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
        this.router.navigate(['/schedule', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        /*this.principal.identity().then((account) => {
            this.currentAccount = account;
        });*/
        this.registerChangeInSchedules();
    }

    ngOnDestroy() {
      //  this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Schedule) {
        return item.id;
    }
    registerChangeInSchedules() {
       // this.eventSubscriber = this.eventManager.subscribe('scheduleListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
      //  this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.schedules = data;
    }
    private onError(error) {
      this.notificationsService.notify('error', error, error.message);
    }


  save() {
    let schedules = [...this.schedules];
    if (this.newSchedule)
      schedules.push(this.schedule);
    else
      schedules[this.schedules.indexOf(this.selectedSchedule)] = this.schedule;

    this.schedules = schedules;
    this.schedule = null;
    this.displayDialog = false;
  }

  delete() {
    let index = this.schedules.indexOf(this.selectedSchedule);
    this.schedules = this.schedules.filter((val, i) => i != index);
    this.schedule = null;
    this.displayDialog = false;
  }

  onRowSelect(event) {
    this.newSchedule = false;
    this.schedule = this.cloneSchedule(event.data);
    this.displayDialog = true;
  }

  cloneSchedule(c: Schedule): Schedule {
    let schedule = {};
    for (let prop in c) {
      schedule[prop] = c[prop];
    }
    return schedule;
  }

  showDialogToAdd() {
    this.newSchedule = true;
    this.schedule = {};
    this.displayDialog = true;
  }



}
