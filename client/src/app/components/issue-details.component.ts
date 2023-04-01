import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IssuesService } from '../issues.service';
import { Issue } from '../models';


@Component({
  selector: 'app-issue-details',
  templateUrl: './issue-details.component.html',
  styleUrls: ['./issue-details.component.css']
})
export class IssueDetailsComponent implements OnInit{

  issueForm: FormGroup | undefined

  @Input()
  issue: Issue | undefined

  @Output()
  formClose = new EventEmitter()

  constructor(private fb: FormBuilder, private issueSvc: IssuesService) {}

  ngOnInit(): void {
      this.issueForm = this.createForm()
  }

  private createForm(): FormGroup {
    return this.fb.group({
      title: [this.issue?.title, Validators.required],
      description: [this.issue?.description],
      priority: [this.issue?.priority, Validators.required]
    })
  }

  saveNewDetails() {
    if (this.issue) {
      this.issueSvc.amendIssue(this.issue.issueNo, this.issueForm?.value)
      this.formClose.emit()
    }
  }
  
}
