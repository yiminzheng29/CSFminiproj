import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ClarityModule } from '@clr/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './userComponents/home.component'
import { UserService } from './user.service';
import { NewsComponent } from './newsComponents/news.component';
import { RegisterComponent } from './userComponents/register.component';
import { EditUserComponent } from './userComponents/edit-user.component';
import { NewsService } from './news.service';
import { MaterialModule } from './material.module';
import { UserNewsComponent } from './newsComponents/user-news.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { environment } from '../environments/environment';
import { initializeApp } from "firebase/app";
import { FirebaseService } from './firebase.service';
import { NewsResultsComponent } from './newsComponents/news-results.component';
import { EntertainmentNewsComponent } from './newsComponents/entertainment-news.component';
import { SportsNewsComponent } from './newsComponents/sports-news.component';
import { TechnologyNewsComponent } from './newsComponents/technology-news.component';
import { BusinessNewsComponent } from './newsComponents/business-news.component';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TopHeadlinesComponent } from './newsComponents/top-headlines.component';
import { FriendsComponent } from './userComponents/friends.component';
import { SearchFriendsComponent } from './userComponents/search-friends.component';
initializeApp(environment.firebase);

const routes: Routes = [
  {path:'', component: HomeComponent},
  {path:'register', component: RegisterComponent},
  {path:'news', component: NewsComponent},
  {path:'topNews', component: TopHeadlinesComponent},
  {path:'news/entertainment', component: EntertainmentNewsComponent},
  {path:'news/sports', component: SportsNewsComponent},
  {path:'news/business', component: BusinessNewsComponent},
  {path:'news/technology', component: TechnologyNewsComponent},
  {path:'search/:searchQuery', component: NewsResultsComponent},
  {path:'savedNews/:username', component: UserNewsComponent},
  {path:'users/:username', component: EditUserComponent},
  {path:'friends/:username', component: FriendsComponent},
  {path:'searchFriends/:keyword', component: SearchFriendsComponent},
  {path:'**', redirectTo: "/", pathMatch: "full"},
  
];


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NewsComponent,
    RegisterComponent,
    EditUserComponent,
    UserNewsComponent,
    NewsResultsComponent,
    EntertainmentNewsComponent,
    SportsNewsComponent,
    TechnologyNewsComponent,
    BusinessNewsComponent,
    FriendsComponent,
    SearchFriendsComponent,
    TopHeadlinesComponent
],
  imports: [
    BrowserModule,
    BrowserAnimationsModule, 
    ClarityModule,
    ReactiveFormsModule, 
    HttpClientModule,
    MaterialModule,
    MatFormFieldModule,
    MatInputModule,
    FlexLayoutModule,
    LoadingBarRouterModule,
    NgbModule,
    RouterModule.forRoot( routes, {useHash: true})
  ],
  providers: [UserService, NewsService, FirebaseService],
  bootstrap: [AppComponent]
})
export class AppModule { }
