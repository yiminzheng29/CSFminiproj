import { Component, OnInit } from '@angular/core';
import { IssuesService } from '../issues.service';
import { Issue } from '../models';

@Component({
  selector: 'app-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.css']
})
export class IssueListComponent implements OnInit{
  
  issues: Issue[] = []
  showReportIssue = false
  selectedIssue: Issue | null = null
  amendIssue: Issue | null = null

  constructor(private issueSvc: IssuesService) {}

  ngOnInit(): void {
      this.getIssues()
  }

  private getIssues() {
    this.issues = this.issueSvc.getPendingIssues()
  }

  onCloseReport() {
    this.showReportIssue = false
    this.getIssues()
  }

  onConfirm(confirmed: boolean) {
    if (confirmed && this.selectedIssue) {
      this.issueSvc.completeIssue(this.selectedIssue)
      this.getIssues()
    }
    this.selectedIssue = null
  }

  onCloseEdit() {
    this.amendIssue = null
    this.getIssues()
  }
}
