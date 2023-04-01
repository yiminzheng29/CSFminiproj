import { Component, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from './models';
import { UserService } from './user.service';
import { HomeComponent } from './userComponents/home.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  
  constructor(private userSvc: UserService) {}

  userSub$!: Subscription
  firstname!: string


  ngOnInit(): void {
      console.info("In app comp")
      this.userSub$ = this.userSvc.user.subscribe(values => {
        this.firstname = values?.firstname as string
      })
  }
  logout() {
    this.userSvc.logout()
  }

}



