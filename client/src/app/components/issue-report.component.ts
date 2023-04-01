import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IssuesService } from '../issues.service';
import { Issue } from '../models';

@Component({
  selector: 'app-issue-report',
  templateUrl: './issue-report.component.html',
  styleUrls: ['./issue-report.component.css']
})
export class IssueReportComponent implements OnInit {

  issueForm: FormGroup | undefined;
  suggestions: Issue[] = []

  @Output()
  formClose = new EventEmitter();

  constructor(private fb: FormBuilder, private issueSvc: IssuesService, private router: Router) {}

  ngOnInit(): void {
      this.issueForm = this.createForm()
      this.issueForm.get('title')?.valueChanges.subscribe(
        (title: string) => {
          console.info("title in ngOnInit: ", title)
          this.suggestions = this.issueSvc.getSuggestions(title)
          console.info("suggestions: ", this.suggestions)
        })
  }

  addIssue() {
    {
      if (this.issueForm && this.issueForm.invalid) {
        this.issueForm.markAllAsTouched()
        return
      }
    }
    this.issueSvc.createIssue(this.issueForm?.value)
    this.formClose.emit()
    this.router.navigate(['/'])
  }

  

  createForm(): FormGroup {
    return this.fb.group({
      title: this.fb.control('', [Validators.required]),
      description: this.fb.control(''),
      priority: this.fb.control('', [Validators.required]),
      type: this.fb.control('', [Validators.required])
    })
    

  }
}
