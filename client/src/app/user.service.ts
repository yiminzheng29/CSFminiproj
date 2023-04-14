import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, firstValueFrom, Observable } from "rxjs";
import { ActivatedRouteSnapshot, CanActivate, CanDeactivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { CanLeave, User } from "./models";
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class UserService implements CanActivate, CanDeactivate<CanLeave>{

    canLogin = false
    username!: string

    private userSubject: BehaviorSubject<User | null>
    public user: Observable<User | null>

    constructor(private http: HttpClient, private router: Router) {
        this.userSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('user')!))
        this.user = this.userSubject.asObservable()
    }

    public get userValue() {
        return this.userSubject.value
    }

    // creates user in this area
    createUser(user: User): Promise<User> {
        return firstValueFrom(this.http.post<User>('http://localhost:8080/api/createUser', user))
    }

    canDeactivate(component: CanLeave, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (!component.canLeave())
            return true
        return prompt("Type YES to exit") == "YES"
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (this.canLogin)
            return true
        return this.router.navigate(['/'])
    }

    // to check if username and password exists:
    login(username: string, password: string): Promise<User> {
        return firstValueFrom(this.http.post<User>(`http://localhost:8080/api/user`, {username, password}))
            .then(result => {
                localStorage.setItem("user", JSON.stringify(result))
                this.username = result.firstname as string
                console.info("Username in service", this.username)
                console.info(">>>> username: ", result)
                this.userSubject.next(result)
                return result;
        })
    }

    logout() {
        console.info("Logging out")
        localStorage.removeItem('user')
        this.userSubject.next(null)
        this.router.navigate(['/'])

    }

    register(user: User) {
        return this.http.post(`/register`, user)
    }

    getUser(username: string): Promise<User> {
        return firstValueFrom(this.http.get<User>(`http://localhost:8080/api/user/${username}`))
    }

    // for updating user details
    // update(username: string, params: any) {
    //     return this.http.put(`/users/${username}`, params)
    //         .pipe(map(result => {
    //             if (username == this.userValue?.username) {
    //                 const user = { ...this.userValue, ...params}
    //                 localStorage.setItem("user", JSON.stringify(result))
    //                 this.userSubject.next(user)
    //             }
    //             return result
    //         }))
    // }

    updateUser(user: User) {
        console.info(user)
        return firstValueFrom(this.http.put(`http://localhost:8080/api/user/${user.username}/update`, user))
    }

    // for deleting user
    delete(username: string) {
        return firstValueFrom(this.http.delete(`http://localhost:8080/api/user/${username}/delete`)
            .pipe(map(result => {
                // auto logout if user deleted
                if (username == this.userValue?.username) {
                    this.logout()
                }
                return result
            })))
    }

}

