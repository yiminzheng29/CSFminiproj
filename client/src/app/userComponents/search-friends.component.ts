import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from '../models';
import { FirebaseService } from '../firebase.service';

@Component({
  selector: 'app-search-friends',
  templateUrl: './search-friends.component.html',
  styleUrls: ['./search-friends.component.css']
})
export class SearchFriendsComponent implements OnInit, OnDestroy{

  username!: string
  keyword!: string
  userSub$!: Subscription
  searchResults!: User[]
  friends!: User[]
  params$!: Subscription
  gridColumns = 3;
  yourFriends: string[] = []
  allFriends: string[] = []
  index!: number

  constructor(private userSvc: UserService, private router: Router, private activatedRoute: ActivatedRoute, private fbSvc: FirebaseService) {}

  ngOnInit(): void {
    this.yourFriends = []
    this.params$ = this.activatedRoute.params.subscribe((params) => {
      this.keyword = params['keyword']
      console.info(this.keyword)
    })
      //to validate login
    if (localStorage.getItem("user") == null) {
      this.router.navigate(['/'])
    }
      this.userSub$ = this.userSvc.user.subscribe(value => {
        this.username = value?.username as string
    })
    this.searchFriend()
    this.friendsList()
  }
  toggleGridColumns() {
    this.gridColumns = this.gridColumns === 3 ? 4 : 3;
  }

  async searchFriend() {
    this.searchResults = await this.userSvc.searchFriends(this.username, this.keyword)
    this.searchResults.forEach(x => {
      this.allFriends.push(x.username as string)
    })
    this.index = this.allFriends.indexOf(this.username)
    this.searchResults.splice(this.index, 1) // remove yourself from list of friends
    console.info(this.searchResults)
  }

  async friendsList() {
    this.friends = await this.userSvc.getFriends(this.username)
    this.friends.forEach(x => {
      this.yourFriends.push(x.username as string)
    })
    console.info(this.yourFriends)
  }

  deleteFriend(friendUsername: string) {
    this.userSvc.deleteFriend(this.username, friendUsername)
    this.router.navigate(['/friends', this.username])
  }

  addFriend(friendUsername: string) {
    this.userSvc.addFriend(this.username, friendUsername)
    const message = "You have been added as a friend by " + this.username
    this.fbSvc.addFriendNotification(message, this.username, friendUsername)
    this.router.navigate(['/friends', this.username])
  }

  ngOnDestroy(): void {
    this.userSub$.unsubscribe()
    this.params$.unsubscribe()
}
}
