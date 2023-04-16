import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FirebaseService } from '../firebase.service';
import { News } from '../models';
import { NewsService } from '../news.service';
import { UserService } from '../user.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-business-news',
  templateUrl: './business-news.component.html',
  styleUrls: ['./business-news.component.css']
})
export class BusinessNewsComponent {
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
    this.newsSvc.searchNews("business", this.username)
      .then(
        (ans) => {
          this.results = ans
          console.info(this.results)
        }
        
      )
      this.searchForm = this.createForm()
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

    share(i: number): void {
      this.selectedNews = this.results[i] as News
      // this.firebaseSvc.shareNews(this.selectedNews.title, this.username)
      this.firebaseSvc.shareNews(this.selectedNews.title, this.username, 
        "test")
  
    }

  ngOnDestroy(): void {
      this.params$.unsubscribe()
      this.userSub$.unsubscribe()
  }
}
