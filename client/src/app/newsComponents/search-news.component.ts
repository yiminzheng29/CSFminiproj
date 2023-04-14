import { Component, OnInit } from '@angular/core';
import { NewsService } from '../news.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-search-news',
  templateUrl: './search-news.component.html',
  styleUrls: ['./search-news.component.css']
})
export class SearchNewsComponent implements OnInit{

  searchForm!: FormGroup
  searchQuery!: string
  // username!: string
  paramName!: string
  userSub$!: Subscription

  constructor(private activatedRoute: ActivatedRoute, 
    private router: Router, private userSvc: UserService, private fb: FormBuilder) {}

  ngOnInit(): void {
    if (localStorage.getItem("user") == null) {
      this.router.navigate(['/'])
    }
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
    }
      
    )
  }
}
