import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { IssueDetailsComponent } from './components/issue-details.component';
import { IssueListComponent } from './components/issue-list.component';
import { ClarityModule } from '@clr/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { IssueReportComponent } from './components/issue-report.component';
import { ConfirmDialogComponent } from './components/confirm-dialog.component';
import { RouterModule, Routes } from '@angular/router';
import { RetrieveIssueComponent } from './components/retrieve-issue.component';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './userComponents/home.component'
import { UserService } from './user.service';
import { NewsComponent } from './newsComponents/news.component';
import { RegisterComponent } from './userComponents/register.component';
import { EditUserComponent } from './userComponents/edit-user.component';
import { NewsService } from './news.service';
import { AuthorsComponent } from './newsComponents/authors.component';
import { MaterialModule } from './material.module';
import { UserNewsComponent } from './newsComponents/user-news.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CommentsComponent } from './newsComponents/comments.component';
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
import { SearchNewsComponent } from './newsComponents/search-news.component';
import { NewsResultsComponent } from './newsComponents/news-results.component';
initializeApp(environment.firebase);

const routes: Routes = [
  {path:'', component: HomeComponent},
  {path:'add-issue', component: IssueReportComponent},
  {path:'issues/:issueNo', component: RetrieveIssueComponent},
  {path:'update/:issueNo', component: IssueDetailsComponent},
  {path:'register', component: RegisterComponent},
  {path:'news', component: NewsComponent},
  {path:'search', component: SearchNewsComponent},
  {path:'search/:searchQuery', component: NewsResultsComponent},
  {path:'authors', component: AuthorsComponent},
  {path:'news/:username', component: UserNewsComponent},
  {path:'users/:username', component: EditUserComponent},
  {path:'**', redirectTo: "/", pathMatch: "full"},
  
];


@NgModule({
  declarations: [
    AppComponent,
    IssueDetailsComponent,
    IssueListComponent,
    IssueReportComponent,
    ConfirmDialogComponent,
    RetrieveIssueComponent,
    HomeComponent,
    NewsComponent,
    RegisterComponent,
    EditUserComponent,
    AuthorsComponent,
    UserNewsComponent,
    CommentsComponent,
    SearchNewsComponent,
    NewsResultsComponent,

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
