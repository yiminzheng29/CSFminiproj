import { Component, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { News, User } from '../models';
import { NewsService } from '../news.service';
import { UserService } from '../user.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { FirebaseService } from '../firebase.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css'],
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
export class NewsComponent implements OnInit, OnDestroy{

  allNews: News[] = []
  userSub$!: Subscription
  username!: string
  selectedNews!: News
  toggle = false
  allResponses: string[] = []
  shareNews!: FormGroup
  recipient!: string
  folded = 'closed'
  newsId!: string
  liked = false
  message: any
  searchQuery!: string
  searchForm!: FormGroup
  friends!: User[]
  friendsList: string[] = []

  
  constructor(private fb: FormBuilder, private newsSvc: NewsService, private userSvc: UserService, private router: Router,
 private firebaseSvc: FirebaseService) {}

  ngOnInit() {
    console.log('in news component')
    // to validate login
    if (localStorage.getItem("user") == null) {
      this.router.navigate(['/'])
    }
      this.userSub$ = this.userSvc.user.subscribe(value => {
        this.username = value?.username as string
      })
      this.newsSvc.getAllNews("us", this.username)
        .then((result) => {
          this.allNews = result
          result.forEach(x => {
            this.allResponses.push(x.newsId as string)
          })
        
        })
        .catch(error => {
        })
    this.message = this.firebaseSvc.listen()
    this.searchForm = this.createForm()
    this.getFriends()
    
  }

  submitQuery() {
    this.searchQuery = this.searchForm.value['searchQuery'] as string
    this.router.navigate(['/search/',this.searchQuery])
  }

  async getFriends() {
    this.friends = await this.userSvc.getFriends(this.username)
    this.friends.forEach(x => {
      this.friendsList.push(x.username)
    })
   
  }

  createForm() {
    return this.fb.group({
      searchQuery: this.fb.control<string>('')
    })
  }


  // like news based on index
  async likeNews(i: number) {
    this.userSub$ = this.userSvc.user.subscribe(value => {
      this.username = value?.username as string
    })
    this.selectedNews = this.allNews[i] as News
    if (this.selectedNews.newsId?.length == 6) {
      console.info("unliking post", this.allNews[i].newsId)
      this.newsSvc.unlikeNews(this.allNews[i].newsId as string, this.username)
      this.ngOnInit() // refreshes the browser
    }
    else {
      this.allNews[i] = await this.newsSvc.likeNews(this.allNews[i], this.username)
    }
  }

  toggleFold(i: number) {
    this.folded = this.folded === 'open' ? 'closed' : 'open'
    this.allNews[i].toggle = this.folded
    this.shareNews = this.fb.group({
        recipient: this.fb.control<string>(''),
      })
    
  }

  share(recipient: string, i: number): void {
    this.selectedNews = this.allNews[i] as News
    this.firebaseSvc.shareNews(this.selectedNews.title, this.username, 
      recipient, this.selectedNews.url)
    this.shareNews.reset()
    this.ngOnInit()
    this.folded='closed'
  }

  ngOnDestroy(): void {
      this.userSub$.unsubscribe()
  }


  
  
}
