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
    imageData = ""

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
        const content = new FormData()
        content.set("profileImage", user.profileImage)
        content.set("username", user.username)
        content.set("password", user.password)
        content.set("firstname", user.firstname)
        content.set("lastname", user.lastname)
        content.set("email", user.email)
        

        return firstValueFrom(this.http.post<User>('/api/createUser', content))
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
        return firstValueFrom(this.http.post<User>(`/api/user`, {username, password}))
            .then(result => {
                localStorage.setItem("user", JSON.stringify(result))
                this.username = result.firstname as string
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

    getPassword(username: string) {
        return firstValueFrom(this.http.get(`/api/forgotPassword/${username}`))
    }

    register(user: User) {
        return this.http.post(`/register`, user)
    }

    getUser(username: string): Promise<User> {
        return firstValueFrom(this.http.get<User>(`/api/user/${username}`))
    }

    updateUser(user: User) {
        const content = new FormData()
        content.set("username", user.username)
        content.set("password", user.password)
        content.set("firstname", user.firstname)
        content.set("lastname", user.lastname)
        content.set("email", user.email)
        content.set("profileImage", user.profileImage)
        return firstValueFrom(this.http.put(`/api/user/${user.username}/update`, content))
    }

    // for deleting user
    delete(username: string) {
        return firstValueFrom(this.http.delete(`/api/user/${username}/delete`)
            .pipe(map(result => {
                // auto logout if user deleted
                if (username == this.userValue?.username) {
                    this.logout()
                }
                return result
            })))
    }

    // for getting friends
    getFriends(username: string): Promise<User[]> {
        return firstValueFrom(this.http.get<User[]>(`/api/friends/${username}`))
    }

    searchFriends(username: string, keyword: string): Promise<User[]> {
        return firstValueFrom(this.http.get<User[]>(`/api/searchFriends/${username}?keyword=${keyword}`))
    }

    deleteFriend(username: string, friendName: string) {
        return firstValueFrom(this.http.delete(`/api/deleteFriend/${username}?friendsUsername=${friendName}`))
    }

    addFriend(username: string, friendName: string) {
        if (username!=friendName) {
            return firstValueFrom(this.http.post(`/api/addFriends/${username}?friendsUsername=${friendName}`, username))
        }
        return null
    }

    getNonFriends(username: string): Promise<User[]> {
        return firstValueFrom(this.http.get<User[]>(`/api/nonFriends/${username}`))
    }

}

