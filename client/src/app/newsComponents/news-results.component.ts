import { Component, OnDestroy, OnInit } from '@angular/core';
import { NewsService } from '../news.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { UserService } from '../user.service';
import { News } from '../models';
import { FirebaseService } from '../firebase.service';

@Component({
  selector: 'app-news-results',
  templateUrl: './news-results.component.html',
  styleUrls: ['./news-results.component.css'],
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
export class NewsResultsComponent implements OnInit, OnDestroy{

  searchQuery!: string
  username!: string
  params$!: Subscription
  userSub$!: Subscription
  results: News[] = []
  selectedNews!: News
  toggle = false
  allResponses: string[] = []
  folded = 'closed'

  constructor(private newsSvc:NewsService, private activatedRoute:ActivatedRoute, private router: Router,
    private userSvc: UserService, private firebaseSvc: FirebaseService) {}

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
    this.newsSvc.searchNews(this.searchQuery, this.username)
      .then(
        (ans) => {
          this.results = ans
          console.info(this.results)
        }
        
      )
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

    // toggleFold(i: number) {
    //   this.folded = this.folded === 'open' ? 'closed' : 'open'
      
    //   console.info(this.results[i])
    //   // this.commentField = this.fb.group({
    //   //     username: this.username,
    //   //     firstname: this.userSvc.userValue?.firstname,
    //   //     lastname: this.userSvc.userValue?.lastname,
    //   //     comment: this.fb.control<string>('',[Validators.minLength(1), Validators.required]),
    //   //     // published: Date.now().toString(),
    //   //     // newsId: this.newsId
    //   //   })
      
    // }

    share(i: number): void {
      this.selectedNews = this.results[i] as News
      // this.firebaseSvc.shareNews(this.selectedNews.title, this.username)
      this.firebaseSvc.shareNews(this.selectedNews.title, this.username, 
        "test")
  
    }

  // async getNewsResults() {
  //   this.results = await this.newsSvc.searchNews(this.searchQuery, this.username)
  // }

  ngOnDestroy(): void {
      this.params$.unsubscribe()
      this.userSub$.unsubscribe()
  }


}
