export class Schedule {
  constructor(id?: number,
              name?: string,
              cron?: string,
              ) {
    this.id = id ? id : null;
    this.name = name ? name : null;
    this.cron = cron ? cron : null;

  }

  public id?: number;
  public name?: string;
  public cron?: string;

}
