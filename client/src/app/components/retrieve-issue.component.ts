import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { IssuesService } from '../issues.service';
import { Issue } from '../models';

@Component({
  selector: 'app-retrieve-issue',
  templateUrl: './retrieve-issue.component.html',
  styleUrls: ['./retrieve-issue.component.css']
})
export class RetrieveIssueComponent implements OnInit, OnDestroy{

  issueDetail!: Issue
  issueNo!: number
  params$!: Subscription

  constructor(private activatedRoute: ActivatedRoute, private issueSvc: IssuesService ) {}

  ngOnInit(): void {
      console.info("in retrieve-details NgOnInit")
      this.params$ = this.activatedRoute.params.subscribe(
        (params) => {
          this.issueNo = params['issueNo']
          console.info("Retrieving issueNo: ", this.issueNo)
        }
      )
      this.issueDetail = this.issueSvc.getIssue(this.issueNo)
  }

  ngOnDestroy(): void {
      this.params$.unsubscribe()
  }
}
