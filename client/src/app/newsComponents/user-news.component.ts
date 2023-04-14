import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { News } from '../models';
import { NewsService } from '../news.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-news',
  templateUrl: './user-news.component.html',
  styleUrls: ['./user-news.component.css']
})
export class UserNewsComponent implements OnInit{

  gridColumns = 3;

  arr = [1,2,3,4,5,6,7]
  allNewsByUser: News[] = []
  username!: string
  params$!: Subscription
  paramName!: string
  userSub$!: Subscription

  constructor(private newsSvc: NewsService, private activatedRoute: ActivatedRoute, 
    private router: Router, private userSvc: UserService) {}

  toggleGridColumns() {
   
    this.gridColumns = this.gridColumns === 3 ? 4 : 3;
  }

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

}
