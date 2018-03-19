import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ScheduleComponent} from './entities/schedule/schedule.component';
import {ScheduleLogComponent} from './entities/schedule-log';
import {MyPathRouterComponent} from "./entities/my-path-router";
import {ScheduleEditComponent} from "./entities/schedule/schedule-edit.component";
import {ScheduleLogEditComponent} from "./entities/schedule-log/schedule-log-edit.component";

const routes: Routes = [
 // {path : '', pathMatch: 'full' , redirectTo: '/schedule'},
  {path: '', component: MyPathRouterComponent},
  {path: 'schedule', component: ScheduleComponent},
  {path: 'scheduleEdit/:id', component: ScheduleEditComponent},
  {path: 'scheduleLogEdit/:id', component: ScheduleLogEditComponent},
  {path: 'scheduleAdd', component: ScheduleEditComponent},
  {path: 'scheduleLog', component: ScheduleLogComponent},
  {path: ':path', component: MyPathRouterComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes

   /* ,{ enableTracing: true }*/

    )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
