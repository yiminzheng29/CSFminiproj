import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../models';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FirebaseService } from '../firebase.service';


@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit, OnDestroy{

  gridColumns = 3;
  friends!: User[]
  username!: string
  userSub$!: Subscription
  searchFriends!: User[]
  nonFriends!: User[]
  searchForm!: FormGroup

  constructor(private userSvc: UserService, private router: Router, private fb: FormBuilder, private fbSvc: FirebaseService) {}

  ngOnInit(): void {
    if (localStorage.getItem("user") == null) {
      this.router.navigate(['/'])
    }
      this.userSub$ = this.userSvc.user.subscribe(value => {
        this.username = value?.username as string
      })
    this.getAllFriends()
    this.getNonFriends()
    this.searchForm = this.createForm()
  }
  
  toggleGridColumns() {
    this.gridColumns = this.gridColumns === 3 ? 4 : 3;
  }

  async getAllFriends() {
    this.friends = await this.userSvc.getFriends(this.username)
    console.info(this.friends)
  }

  deleteFriend(friendUsername: string) {
    this.userSvc.deleteFriend(this.username, friendUsername)
    const message = "You have been removed as a friend by " + this.username
    this.fbSvc.addFriendNotification(message, this.username, friendUsername)
    this.ngOnInit()
  }

  async getNonFriends () {
    this.nonFriends = await this.userSvc.getNonFriends(this.username)
    console.info(this.nonFriends)
  }

  addFriend(friendUsername: string) {
    this.userSvc.addFriend(this.username, friendUsername)
    const message = "You have been added as a friend by " + this.username
    this.fbSvc.addFriendNotification(message, this.username, friendUsername)
    this.ngOnInit()
  }

  searchForFriends() {
    this.router.navigate(['/searchFriends', this.searchForm.value['searchQuery']])
  }

  createForm() {
    return this.fb.group({
      searchQuery: this.fb.control<string>('')
    })
  }

  ngOnDestroy(): void {
      this.userSub$.unsubscribe()
  }

}
