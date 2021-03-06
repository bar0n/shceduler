import {Component, OnInit} from '@angular/core';

import {Router} from '@angular/router';


@Component({
  selector: 'mob-my-path-router',
  template: ``
})
export class MyPathRouterComponent implements OnInit {


  constructor(private router: Router) {

  }

  ngOnInit() {
    const url = this.router.url;
    if (url  === "/") {
      this.router.navigate(["schedule"]);
    }
    if (url.indexOf('#') !== -1) {
      const newUrl = url.replace('/#', '');
      this.router.navigate([newUrl]);
    }
  }
}
