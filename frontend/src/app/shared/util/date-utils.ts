import * as moment from 'moment';
import {APP_LOCAL_DATETIME_FORMAT} from "../../app.constants";
import {DatePipe} from '@angular/common';


export const convertDateTimeFromServer = date =>
  date ? moment(date).format(APP_LOCAL_DATETIME_FORMAT) : null;

export default class DateUtils {

  private  pattern = 'yyyy-MM-dd';

  private  datePipe = new DatePipe('en');


  convertDateTimeFromServer(date) /**
   * Method to convert the date time from server into JS date object
   */ {
    if (date) {
      return new Date(date);
    }
    else {
      return null;
    }
  };


  convertLocalDateFromServer(date) /**
   * Method to convert the date from server into JS date object
   */ {
    if (date) {
      var dateString = date.split('-');
      return new Date(dateString[0], dateString[1] - 1, dateString[2]);
    }
    return null;
  };

  convertLocalDateToServer(date, pattern) /**
   * Method to convert the JS date object into specified date pattern
   */ {
    if (pattern === void 0) {
      pattern = this.pattern;
    }
    if (date) {
      var newDate = new Date(date.year, date.month - 1, date.day);
      return this.datePipe.transform(newDate, pattern);
    }
    else {
      return null;
    }
  };

  /**
   * Method to get the default date pattern
   */
  /**
   * Method to get the default date pattern
   */
  dateformat = /**
   * Method to get the default date pattern
   */
  function () {
    return this.pattern;
  };

  toDate(date) {
    if (date === undefined || date === null) {
      return null;
    }
    var dateParts = date.split(/\D+/);
    if (dateParts.length === 7) {
      return new Date(dateParts[0], dateParts[1] - 1, dateParts[2], dateParts[3], dateParts[4], dateParts[5], dateParts[6]);
    }
    if (dateParts.length === 6) {
      return new Date(dateParts[0], dateParts[1] - 1, dateParts[2], dateParts[3], dateParts[4], dateParts[5]);
    }
    return new Date(dateParts[0], dateParts[1] - 1, dateParts[2], dateParts[3], dateParts[4]);
  };

}
