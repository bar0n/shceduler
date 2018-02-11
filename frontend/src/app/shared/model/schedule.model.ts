export class Schedule {
  constructor(id?: number,
              name?: string,
              cron?: string,
              start?: Date,
              stop?: Date,
              next?: Date,
              active?: boolean,
              description?: string,
              person?: string,
              author?: string,
              email?: string,
              periodTxt?: string,
  ) {
    this.id = id ? id : null;
    this.name = name ? name : null;
    this.cron = cron ? cron : null;
    this.start = start ? start : new Date();
    this.stop = stop ? stop : null;
    this.next = next ? next : null;
    this.active = active ? active : true;
    this.description = description ? description : null;
    this.person = person ? person : null;
    this.author = author ? author : null;
    this.email = email ? email : null;
    this.periodTxt = periodTxt ? periodTxt : null;
  }

  public id?: number;
  public name?: string;
  public cron?: string;
  public start?: Date;
  public stop?: Date;
  public next?: Date;
  public active?: boolean = true;
  public description?: string;
  public person?: string;
  public author?: string;
  public email?: string;
  public periodTxt?: string;

}
