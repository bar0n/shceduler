import {Schedule} from './schedule.model';

export class ScheduleLog  {
    constructor(
        public id?: number,
        public created?: any,
        public schedule?: Schedule,
        public completed?: boolean,
    ) {
    }
}
