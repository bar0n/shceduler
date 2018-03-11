import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class BlockService {

  private subject = new Subject();
  observable = this.subject.asObservable();

  emit(b: boolean) {
    this.subject.next(b);
  }

}
