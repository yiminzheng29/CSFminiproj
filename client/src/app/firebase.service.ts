import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { environment } from "../environments/environment";
import { News } from "./models";
import { first, firstValueFrom } from "rxjs";

@Injectable()
export class FirebaseService {

    constructor(private http: HttpClient) {}

    message: any
    token!: string

    requestPermission(username: string) {
        const messaging = getMessaging();
        getToken(messaging, 
        { vapidKey: environment.firebase.vapidKey}).then(
        (currentToken) => {
            if (currentToken) {
            console.log(username, currentToken);
            this.saveToken(username, currentToken)
            } else {
            console.log('No registration token available. Request permission to generate one.');
            }
        }).catch((err) => {
            console.log('An error occurred while retrieving token. ', err);
        }
        
        );
          
    }

    listen(): string {
        const messaging = getMessaging();
        onMessage(messaging, (payload) => {
        console.log('Message received. ', payload);
        this.message=payload;
        
        });
        return this.message
    }

    public saveToken(username: string, token: string) {
        const params = new HttpParams()
            .set("username", username)
            .set("token", token)
        return firstValueFrom(this.http.post('http://localhost:8080/api/saveToken', params))
        
    }

    public shareNews(message: string, sender: string, recipient: string) {

        const content = new FormData()
        content.set("message", message)
        content.set("sender", sender)
        content.set("recipient", recipient)
        console.info(content)
        // const params = new HttpParams()
        // params.set("recipient", recipient)
        // params.set("message", message)
        // params.set("sender", sender)
        return firstValueFrom(this.http.post('http://localhost:8080/api/news/sendNews', content))
    }

    // public test(message: string) {
    //     this.http.post('http://localhost:8080/api/test', message)
    // }
}