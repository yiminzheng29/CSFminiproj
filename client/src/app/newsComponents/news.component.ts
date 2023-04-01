import { Component, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { comments, News } from '../models';
import { NewsService } from '../news.service';
import { UserService } from '../user.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { CommentService } from '../comments.service';

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
  allComments: comments[] = []
  userSub$!: Subscription
  username!: string
  selectedNews!: News
  toggle = false
  allResponses: string[] = []
  commentField!: FormGroup
  folded = 'closed'
  postComment!: comments
  newsId!: string
  liked = false

  
  constructor(private fb: FormBuilder, private newsSvc: NewsService, private userSvc: UserService, private router: Router,
    private commentSvc: CommentService) {}

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
          console.info("news: ", result)
          this.allNews = result
          result.forEach(x => {
            this.allResponses.push(x.newsId as string)
          })
          // console.info(this.allResponses)
        
        })
        .catch(error => {
          console.error("error: ", error)
        })
        // console.info(this.allResponses)
    this.showComments()
    console.info(this.allComments)
  }

  // like news based on index
  async likeNews(i: number) {
    this.userSub$ = this.userSvc.user.subscribe(value => {
      this.username = value?.username as string
    })
    this.selectedNews = this.allNews[i] as News
    console.info("username: ", this.username)
    console.info("news: ", this.selectedNews)
    if (this.selectedNews.newsId?.length == 6) {
      console.info("unliking post", this.allNews[i].newsId)
      this.newsSvc.unlikeNews(this.allNews[i].newsId as string, this.username)
      // this.allNews[i].liked=false
      this.ngOnInit() // refreshes the browser
    }
    else {
      this.allNews[i] = await this.newsSvc.likeNews(this.allNews[i], this.username)
      // this.allNews[i].liked=true
      console.info(this.allNews)
      console.info("liking post", this.allNews[i].newsId)
    }
  }

  toggleFold(i: number) {
    this.folded = this.folded === 'open' ? 'closed' : 'open'
    
    console.info(this.allNews[i])
    this.commentField = this.fb.group({
        username: this.username,
        firstname: this.userSvc.userValue?.firstname,
        lastname: this.userSvc.userValue?.lastname,
        comment: this.fb.control<string>('',[Validators.minLength(1), Validators.required]),
        // published: Date.now().toString(),
        // newsId: this.newsId
      })
    
  }

  async submitComment(i: number) {
    this.allNews[i] = await this.newsSvc.saveNews(this.allNews[i], this.username)
    this.newsId = this.allNews[i].newsId as string
    this.postComment = this.commentField.value as comments
    
    this.postComment = await this.commentSvc.postComment(this.postComment, this.newsId)
    this.allComments.push(this.postComment)
    console.info(this.postComment)
    this.commentField.reset() // reset the inputs
    this.ngOnInit() // refreshes the browser
  }

  async showComments() {
    this.allComments = await this.commentSvc.getAllComments()
    for (let i = 0; i < this.allNews.length; i ++) {
      this.allNews[i].comments = this.allComments
      this.allNews[i].comments?.filter(c => {
        c.newsId = this.allNews[i].newsId
      })
    }
  }

  ngOnDestroy(): void {
      this.userSub$.unsubscribe()
  }
}

// .then(
//   (result) => {result.forEach(x => {
//     if (x.newsId == this.allNews[i].newsId) {
//       this.allComments.push(x as comments)
//     }
//   })
    
//   }
// )