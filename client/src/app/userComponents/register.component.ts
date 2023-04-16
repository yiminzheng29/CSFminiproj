import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../models';
import { UserService } from '../user.service';
import { FirebaseService } from '../firebase.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{
  
  @ViewChild('image')
  image!: ElementRef

  registrationForm!: FormGroup
  token!: string
  username!: string
  

  constructor(private fb: FormBuilder, private userSvc: UserService, private router: Router, 
      private firebaseSvc: FirebaseService) {}

  ngOnInit(): void {
      this.registrationForm = this.createForm()
  }

  // register a new user
  createUser() {
    
    const user = this.registrationForm.value as User
    user.profileImage = this.image.nativeElement.files[0]
    
    this.userSvc.createUser(user)
    this.username = this.registrationForm.value['username']
    console.info(this.username)
    this.firebaseSvc.requestPermission(this.username)
    // console.info("token: ", this.token as string)
    // console.info(user.username)
    // this.firebaseSvc.saveToken(this.token, user.username)
    this.router.navigate(['/news'])
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [Validators.required]),
      password: this.fb.control('', [Validators.required, Validators.minLength(4)]),
      firstname: this.fb.control('', [Validators.required]), 
      lastname: this.fb.control('', [Validators.required]), 
      // dob: this.fb.control('', [Validators.required]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      profileImage: this.fb.control('', [Validators.required])
      // phone: this.fb.control('', [Validators.required])
    })
  }


}
