import { Component } from '@angular/core';
import { NewsService } from '../news.service';

@Component({
  selector: 'app-authors',
  templateUrl: './authors.component.html',
  styleUrls: ['./authors.component.css']
})
export class AuthorsComponent {

  authors: string[] = []
  
  constructor(private newsSvc: NewsService) {}
  ngOnInit(): void {
    this.newsSvc.getAuthors("us", "kwr")
      .then((result) => {
        console.info("authors: ", result)
        this.authors = result
      })
      .catch(error => {
        console.error("error: ", error)
      }
      )
}
}
