import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { FirebaseService } from '../firebase.service';
import { News, User } from '../models';
import { NewsService } from '../news.service';
import { UserService } from '../user.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { animate, state, style, transition, trigger } from '@angular/animations';


@Component({
  selector: 'app-entertainment-news',
  templateUrl: './entertainment-news.component.html',
  styleUrls: ['./entertainment-news.component.css'],
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
export class EntertainmentNewsComponent {
  searchQuery!: string
  username!: string
  params$!: Subscription
  userSub$!: Subscription
  results: News[] = []
  selectedNews!: News
  toggle = false
  allResponses: string[] = []
  folded = 'closed'
  searchForm!: FormGroup
  shareNews!: FormGroup
  friends!: User[]
  friendsList: string[] = []
  
  constructor(private newsSvc:NewsService, private activatedRoute: ActivatedRoute, private router: Router,
    private userSvc: UserService, private firebaseSvc: FirebaseService, private fb: FormBuilder) {}

  ngOnInit(): void {
      this.params$ = this.activatedRoute.params.subscribe((params) => {
        this.searchQuery = params['searchQuery']
        console.info(this.searchQuery)
      })
        //to validate login
      if (localStorage.getItem("user") == null) {
        this.router.navigate(['/'])
      }
        this.userSub$ = this.userSvc.user.subscribe(value => {
          this.username = value?.username as string
      })
    this.newsSvc.searchNews("entertainment", this.username)
      .then(
        (ans) => {
          this.results = ans
          console.info(this.results)
        }
        
      )
      this.searchForm = this.createForm()
      this.getFriends()
    }

    async getFriends() {
      this.friends = await this.userSvc.getFriends(this.username)
      this.friends.forEach(x => {
        this.friendsList.push(x.username)
      })
    
  }

  submitQuery() {
    this.searchQuery = this.searchForm.value['searchQuery'] as string
    console.info(this.searchQuery)
    this.router.navigate(['/search/',this.searchQuery])
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
      this.selectedNews = this.results[i] as News
      console.info("username: ", this.username)
      console.info("news: ", this.selectedNews)
      if (this.selectedNews.newsId?.length == 6) {
        console.info("unliking post", this.results[i].newsId)
        this.newsSvc.unlikeNews(this.results[i].newsId as string, this.username)
        // this.allNews[i].liked=false
        this.ngOnInit() // refreshes the browser
      }
      else {
        this.results[i] = await this.newsSvc.likeNews(this.results[i], this.username)
        // this.allNews[i].liked=true
        console.info("liking post", this.results[i].newsId)
      }
    }

    toggleFold(i: number) {
      this.folded = this.folded === 'open' ? 'closed' : 'open'
      this.results[i].toggle = this.folded
      this.shareNews = this.fb.group({
          recipient: this.fb.control<string>(''),
        })
      
    }

    // share(i: number): void {
    //   this.selectedNews = this.results[i] as News
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
      this.selectedNews = this.results[i] as News
      this.firebaseSvc.shareNews(this.selectedNews.title, this.selectedNews.url, 
        recipient, this.selectedNews.urlImage)
      this.shareNews.reset()
      this.ngOnInit()
      this.folded='closed'
    }
    
  ngOnDestroy(): void {
      this.params$.unsubscribe()
      this.userSub$.unsubscribe()
  }
}