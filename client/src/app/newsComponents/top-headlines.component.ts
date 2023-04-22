import { Component, OnInit } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { News, User } from '../models';
import { UserService } from '../user.service';
import { NewsService } from '../news.service';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FirebaseService } from '../firebase.service';

@Component({

  selector: 'app-top-headlines',
  templateUrl: './top-headlines.component.html',
  styleUrls: ['./top-headlines.component.css'],
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
export class TopHeadlinesComponent implements OnInit{
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
  recipient!: string
  friends!: User[]
  friendsList: string[] = []

  constructor(private newsSvc:NewsService, private activatedRoute: ActivatedRoute, private router: Router,
    private userSvc: UserService, private firebaseSvc: FirebaseService, private fb: FormBuilder) {}

  ngOnInit(): void {
        //to validate login
      if (localStorage.getItem("user") == null) {
        this.router.navigate(['/'])
      }
        this.userSub$ = this.userSvc.user.subscribe(value => {
          this.username = value?.username as string
      })
    this.newsSvc.getTopHeadlines(5, this.username)
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

    share(recipient: string, i: number): void {
      this.selectedNews = this.results[i] as News
      this.firebaseSvc.shareNews(this.selectedNews.title, this.username, 
        recipient, this.selectedNews.urlImage)
      this.shareNews.reset()
      this.ngOnInit()
      this.folded='closed'
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
      if (this.selectedNews.newsId?.length == 6 && this.selectedNews.liked == true) {
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
      this.ngOnInit()
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

  // ngOnDestroy(): void {
  //     this.params$.unsubscribe()
  //     this.userSub$.unsubscribe()
  // }
}
