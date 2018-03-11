import { Component, OnInit } from '@angular/core';
import {BlockService} from './block.service';

@Component({
  selector: 'mob-block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {
  blockedPanel = false;
  constructor( private blockService: BlockService) { }

  ngOnInit() {
    this.blockService.observable.subscribe((b: boolean) => this.blockedPanel = b);
  }

}
