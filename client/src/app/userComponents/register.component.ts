import { Component, OnInit } from '@angular/core';
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
  
  loginForm!: FormGroup
  token!: string
  username!: string

  constructor(private fb: FormBuilder, private userSvc: UserService, private router: Router, 
      private firebaseSvc: FirebaseService) {}

  ngOnInit(): void {
      this.loginForm = this.createForm()
  }

  // register a new user
  createUser() {
    this.userSvc.createUser(this.loginForm.value)
    console.info("User created", this.loginForm.value as User)
    this.username = this.loginForm.value['username']
    this.token = this.firebaseSvc.requestPermission(this.username)
    console.info("token: ", this.token)
    this.firebaseSvc.saveToken(this.token, this.username)
    this.router.navigate(['/news'])
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [Validators.required]),
      password: this.fb.control('', [Validators.required, Validators.minLength(4)]),
      firstname: this.fb.control('', [Validators.required]), 
      lastname: this.fb.control('', [Validators.required]), 
      dob: this.fb.control('', [Validators.required]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      phone: this.fb.control('', [Validators.required])
    })
  }
}
