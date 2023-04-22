import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { News, User } from '../models';
import { NewsService } from '../news.service';
import { UserService } from '../user.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FirebaseService } from '../firebase.service';
import { animate, state, style, transition, trigger } from '@angular/animations';


@Component({
  selector: 'app-user-news',
  templateUrl: './user-news.component.html',
  styleUrls: ['./user-news.component.css'],
  animations : [
    // Here we are defining what are the states our panel can be in 
    // and the style each state corresponds to.
      trigger('panelState', [
      state('closed', style({ height: '32px', overflow: 'hidden' })),
      state('open', style({ height: '*' })),
      transition('closed <=> open', animate('300ms ease-in-out')),
    ]),
  ],
})
export class UserNewsComponent implements OnInit{

  gridColumns = 3;

  arr = [1,2,3,4,5,6,7]
  allNewsByUser: News[] = []
  username!: string
  params$!: Subscription
  paramName!: string
  userSub$!: Subscription
  shareNews!: FormGroup
  recipient!: string
  selectedNews!: News
  friends!: User[]
  friendsList: string[] = []
  folded = 'closed'

  constructor(private newsSvc: NewsService, private activatedRoute: ActivatedRoute, 
    private router: Router, private userSvc: UserService, private fb: FormBuilder, private firebaseSvc: FirebaseService) {}

  // toggleGridColumns() {
   
  //   this.gridColumns = this.gridColumns === 3 ? 4 : 3;
  // }

  ngOnInit(): void {
    // to validate login
    if (localStorage.getItem("user") == null) {
      this.router.navigate(['/'])
    }
      this.userSub$ = this.userSvc.user.subscribe(value => {
        this.username = value?.username as string
      })
      this.params$ = this.activatedRoute.params.subscribe(
        (params) => {
          this.paramName = params['username']
        }
      )
    if (this.username != this.paramName) {
      this.router.navigate(['/'])
    }
      this.getAllNewsByUser()
      this.getFriends()
  }

  async getFriends() {
    this.friends = await this.userSvc.getFriends(this.username)
    this.friends.forEach(x => {
      this.friendsList.push(x.username)
    })
  }

  async getAllNewsByUser() {
    this.allNewsByUser = await this.newsSvc.getAllNewsByUsername(this.username)
    console.info(this.allNewsByUser)
  }

  async likeNews(i: number) {
    console.info("username: ", this.username)
    if (this.allNewsByUser[i].newsId?.length == 6) {
      console.info("unliking post", this.allNewsByUser[i].newsId)
      this.newsSvc.unlikeNews(this.allNewsByUser[i].newsId as string, this.username)
      this.allNewsByUser.splice(i,1)
      console.info(this.allNewsByUser) // refreshes the browser
    }
    else {
      this.allNewsByUser[i] = await this.newsSvc.saveNews(this.allNewsByUser[i], this.username)
      console.info("liking post", this.allNewsByUser[i].newsId)
    }
  }

  toggleFold(i: number) {
    this.folded = this.folded === 'open' ? 'closed' : 'open'
    this.allNewsByUser[i].toggle = this.folded
    this.shareNews = this.fb.group({
        recipient: this.fb.control<string>(''),
      })
    
  }

  // share(i: number): void {
  //   this.selectedNews = this.allNewsByUser[i] as News
  //   // this.firebaseSvc.shareNews(this.selectedNews.title, this.username)
  //   console.info(this.shareNews.value['recipient'])
  //   this.recipient = this.shareNews.value['recipient'] as string
  //   this.firebaseSvc.shareNews(this.selectedNews.title, this.selectedNews.url, 
  //     this.recipient, this.selectedNews.urlImage)
  //   this.shareNews.reset()
  //   this.ngOnInit()
  //   this.folded='closed'

  // }
  share(recipient: string, i: number): void {
    this.selectedNews = this.allNewsByUser[i] as News
    this.firebaseSvc.shareNews(this.selectedNews.title, this.username, 
      recipient, this.selectedNews.urlImage)
    this.shareNews.reset()
    this.ngOnInit()
    this.folded='closed'
  }

}
