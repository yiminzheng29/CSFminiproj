import { Injectable } from '@angular/core';
import { Issue } from './models';

@Injectable({
  providedIn: 'root'
})
export class IssuesService {

  private issues: Issue[] = [];
  idx!: Issue

  constructor() { }

  getPendingIssues(): Issue[] {
    return this.issues.filter(issue => !issue.completed)
  }

  getIssue(issueNo: number): Issue {
    const issue = this.issues.find(i => i.issueNo === issueNo)
    if (issue) {
      const index = this.issues.indexOf(issue)
      this.idx = this.issues[index]
    }
    return this.idx
  }


  createIssue(issue: Issue) {
    issue.issueNo = this.issues.length + 1
    this.issues.push(issue)
  }

  completeIssue(issue: Issue) {
    const selectedIssue: Issue = {
      ...issue,
      completed: new Date()
    }
    const index = this.issues.findIndex(i => i === issue)
    this.issues[index] = selectedIssue;
  }

  getSuggestions(title: string): Issue[] {
    if (title.length > 3) {
      console.info("title: ", title)
      return this.issues.filter((issue) => 
        issue.title.indexOf(title)!==-1)
    }
    console.info("issues: ", this.issues)
    return []
  }

  amendIssue(issueNo: number, issue: Issue) {
    const existingIssue = this.issues.find(i => i.issueNo === issueNo)
    if (existingIssue) {
      const idx = this.issues.indexOf(existingIssue)
      this.issues[idx] = {
        ...existingIssue,
        ...issue
      }
    }
  }

}
