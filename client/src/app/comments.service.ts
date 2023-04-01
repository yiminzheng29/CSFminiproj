import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { comments, News } from './models';

@Injectable({
    providedIn: 'root'
})

export class CommentService {

    constructor(private http: HttpClient) {}

    postComment(comment: comments, newsId: string): Promise<comments> {
        const content = new FormData()
        content.set("username", comment.username)
        content.set("firstname", comment.firstname)
        content.set("lastname", comment.lastname)
        content.set("newsId", newsId)
        content.set("comment", comment.comment)
        return firstValueFrom(this.http.post<comments>('http://localhost:8080/api/postComment',content))
    }

    getAllComments(): Promise<comments[]> {
        console.info('test')
        return firstValueFrom(this.http.get<comments[]>('http://localhost:8080/api/getComments'))
    }
}