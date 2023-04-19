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
import { CommentService } from './comments.service';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireMessagingModule } from '@angular/fire/compat/messaging';
import { AngularFireStorageModule } from '@angular/fire/compat/storage';
import { AngularFireDatabaseModule } from '@angular/fire/compat/database';
import { environment } from '../environments/environment';
import { initializeApp } from "firebase/app";
import { FirebaseService } from './firebase.service';
import { NewsResultsComponent } from './newsComponents/news-results.component';
import { EntertainmentNewsComponent } from './newsComponents/entertainment-news.component';
import { SportsNewsComponent } from './newsComponents/sports-news.component';
import { TechnologyNewsComponent } from './newsComponents/technology-news.component';
import { BusinessNewsComponent } from './newsComponents/business-news.component';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
initializeApp(environment.firebase);

const routes: Routes = [
  {path:'', component: HomeComponent},
  // {path:'add-issue', component: IssueReportComponent},
  // {path:'issues/:issueNo', component: RetrieveIssueComponent},
  // {path:'update/:issueNo', component: IssueDetailsComponent},
  {path:'register', component: RegisterComponent},
  {path:'news', component: NewsComponent},
  {path:'news/entertainment', component: EntertainmentNewsComponent},
  {path:'news/sports', component: SportsNewsComponent},
  {path:'news/business', component: BusinessNewsComponent},
  {path:'news/technology', component: TechnologyNewsComponent},
  {path:'search/:searchQuery', component: NewsResultsComponent},
  {path:'savedNews/:username', component: UserNewsComponent},
  {path:'users/:username', component: EditUserComponent},
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
    RouterModule.forRoot( routes, {useHash: true}),
    // AngularFireDatabaseModule,
    // AngularFireStorageModule,
    // AngularFireModule.initializeApp(environment.firebase),
    // AngularFireMessagingModule
    

  ],
  providers: [UserService, NewsService, CommentService, FirebaseService],
  bootstrap: [AppComponent]
})
export class AppModule { }
