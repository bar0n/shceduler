import {NgModule} from '@angular/core';

import {ScheduleModule} from 'primeng/schedule';
import {SchedulerScheduleModule} from './schedule/schedule.module';
import {NotificationsService} from './notifications/notifications.service';
import {NotificationsModule} from './notifications/notification.module';
import {CommonModule} from '@angular/common';
import {SchedulerScheduleLogModule} from './schedule-log/schedule-log.module';
import {MyPathRouterComponent} from "./my-path-router";
import {ScheduleEditComponent} from "./schedule/schedule-edit.component";
import {TableModule} from "primeng/table";
import {RouterModule} from "@angular/router";
import {CronJobsModule} from "ngx-cron-jobs";
import {InputTextareaModule} from "primeng/inputtextarea";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {CalendarModule, CheckboxModule, DropdownModule, InputTextModule} from "primeng/primeng";
import {InputSwitchModule} from "primeng/inputswitch";
import {BrowserModule} from "@angular/platform-browser";
import {DialogModule} from "primeng/dialog";
import {ButtonModule} from "primeng/button";


@NgModule({
  imports: [
    SchedulerScheduleModule,
    SchedulerScheduleLogModule,
    ScheduleModule,
    CommonModule,
    NotificationsModule
  ],
  declarations: [MyPathRouterComponent, ],
  entryComponents: [],
  providers: [NotificationsService],
  exports: [
    SchedulerScheduleModule,
    SchedulerScheduleLogModule
  ]
})
export class SchedulerEntityModule {
  static forRoot() {
    return {
      ngModule: SchedulerEntityModule,
      providers: [],
    };
  }
}
