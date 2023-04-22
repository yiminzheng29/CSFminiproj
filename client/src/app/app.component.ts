import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from './user.service';
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { environment } from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  
  constructor(private userSvc: UserService) {}

  message:any = null;
  userSub$!: Subscription
  firstname!: string
  username!: string
  profilePic!: string


  ngOnInit(): void {
      this.userSub$ = this.userSvc.user.subscribe(values => {
        this.firstname = values?.firstname as string
        this.username = values?.username as string
      })
      

      this.requestPermission();
      this.listen();
      this.profilePic = "https://bucketym1.sgp1.digitaloceanspaces.com/"+this.username


  }


  requestPermission() {
    const messaging = getMessaging();
    getToken(messaging, 
     { vapidKey: environment.firebase.vapidKey}).then(
       (currentToken) => {
         if (currentToken) {
           console.log("Hurraaa!!! we got the token.....");
         } else {
           console.log('No registration token available. Request permission to generate one.');
         }
     }).catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
    });
  }

  listen() {
    const messaging = getMessaging();
    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      this.message=payload;
    });
  }
  
  logout() {
    this.userSvc.logout()
  }

}



