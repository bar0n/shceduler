import {NgModule} from '@angular/core';

import {ScheduleModule} from 'primeng/schedule';
import {SchedulerScheduleModule} from './schedule/schedule.module';
import {NotificationsService} from './notifications/notifications.service';
import {NotificationsModule} from './notifications/notification.module';
import {CommonModule} from '@angular/common';
import {SchedulerScheduleLogModule} from './schedule-log/schedule-log.module';
import {MyPathRouterComponent} from "./my-path-router";
import {ConfirmDialogModule} from "primeng/confirmdialog";

@NgModule({
  imports: [
    SchedulerScheduleModule,
    SchedulerScheduleLogModule,
    ScheduleModule,
    CommonModule,
    ConfirmDialogModule,
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
