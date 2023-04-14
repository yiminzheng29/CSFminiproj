import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { News } from "./models";

@Injectable()
export class NewsService {
    
    constructor(private http: HttpClient) {}

    public getAuthors(country: string, username: string): Promise<string[]> {

        const params = new HttpParams()
            .set("country", country)
            .set("username", username)
        return firstValueFrom(this.http.get<string[]>('http://localhost:8080/api/news/authors', {params}))
    }

    public getAllNews(country: string, username: string): Promise<News[]> {

        const params = new HttpParams()
            .set("country", country)
            .set("username", username)
        return firstValueFrom(this.http.get<News[]>('http://localhost:8080/api/news/allNews', {params}))
    }

    public likeNews(news: News, username: string): Promise<News> {
        const content = new FormData()
        content.set("title", news.title)
        content.set("sourceName", news.sourceName)
        content.set("description", news.description)
        content.set("author", news.author)
        content.set("url", news.url)
        content.set("urlImage", news.urlImage)
        content.set("publishedAt", news.publishedAt.toString())
        content.set("content", news.content)
        content.set("username", username)
        return firstValueFrom(this.http.post<News>('http://localhost:8080/api/like', content))
    }

    public saveNews(news: News, username: string): Promise<News> {
        const content = new FormData()
        content.set("title", news.title)
        content.set("sourceName", news.sourceName)
        content.set("description", news.description)
        content.set("author", news.author)
        content.set("url", news.url)
        content.set("urlImage", news.urlImage)
        content.set("publishedAt", news.publishedAt.toString())
        content.set("content", news.content)
        content.set("username", username)
        return firstValueFrom(this.http.post<News>('http://localhost:8080/api/save', content))
    }

    public unlikeNews(newsId: string, username: string) {
        this.http.delete(`http://localhost:8080/api/dislike/${newsId}?username=${username}`, {responseType: 'text'})
        .subscribe(value => {
            console.info(value)
        })
    }

    public getAllNewsByUsername(username: string): Promise<News[]> {
        return firstValueFrom(
            this.http.get<News[]>(`http://localhost:8080/api/news/${username}`)
        )
    }

    public searchNews(searchQuery: string, username: string): Promise<News[]> {
        const params = new HttpParams()
            .set("username", username)
        console.info(params)
        return firstValueFrom(
            this.http.get<News[]>(`http://localhost:8080/api/news/${searchQuery}/results`, {params})
        )
    }
}