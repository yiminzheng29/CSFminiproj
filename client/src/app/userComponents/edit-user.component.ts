import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { Subscription } from 'rxjs';
import { User } from '../models';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit, OnDestroy {
  
  @ViewChild('image')
  image!: ElementRef

  editForm!: FormGroup
  username?: string
  userSub$!: Subscription
  paramName?: string
  params$!: Subscription
  user!: User


  constructor(private fb: FormBuilder, private userSvc: UserService, private router: Router, 
    private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    if (localStorage.getItem("user") == null) {
      this.router.navigate(['/'])
    }
      this.userSub$ = this.userSvc.user.subscribe(value => {
        this.username = value?.username as string
      })
      this.editForm = this.edit()
      this.params$ = this.activatedRoute.params.subscribe(
        (params) => {
          this.paramName = params['username']
        }
      )
    if (this.username != this.paramName) {
      this.router.navigate(['/'])
    }
    this.getUser()
  }

  async getUser() {
    this.user = await this.userSvc.getUser(this.username as string)
  }

  editUser() {
    const user = this.editForm.value as User
    user.profileImage = this.image.nativeElement.files[0]
    this.userSvc.updateUser(user)
    this.router.navigate(['/news'])
  }

  deleteUser() {
    this.userSvc.delete(this.username as string)
  }

  private edit(): FormGroup {
    return this.fb.group({
      username: this.username,
      password: this.fb.control('', [Validators.required, Validators.minLength(4)]),
      firstname: this.fb.control('', [Validators.required]), 
      lastname: this.fb.control('', [Validators.required]), 
      email: this.fb.control('', [Validators.required, Validators.email]),
      profileImage: this.fb.control('', [Validators.required])
    })
  }

  ngOnDestroy(): void {
    this.userSub$.unsubscribe()
    this.params$.unsubscribe()
}

}
