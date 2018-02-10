import {NgModule} from '@angular/core';

import {NotificationsComponent} from "../notifications/notifications.component";
import {NotificationsService} from "./notifications.service";
import {GrowlModule} from "primeng/growl";


@NgModule({
  imports: [
    GrowlModule,
  ],
  declarations: [
    NotificationsComponent
  ],
  entryComponents: [
  ],
  providers: [
    NotificationsService,
  ],
  exports: [NotificationsComponent],
})
export class NotificationsModule {
}
