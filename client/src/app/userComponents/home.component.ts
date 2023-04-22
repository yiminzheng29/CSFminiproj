import { Component, OnInit, Output, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { User } from '../models';
import { UserService } from '../user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit{

  form!: FormGroup

  submitted = false
  loading = false

  @Input()
  user: User | null = null

  constructor(private fb: FormBuilder, private router: Router, private userSvc: UserService) {}

  ngOnInit() {
      console.info("in home component")
      this.form = this.fb.group({
        username: this.fb.control('', [Validators.required]),
        password: this.fb.control('', [Validators.required])
      })
  }

  canLeave(): boolean {
    return this.form.valid;
  }

    async onSubmit() {
      this.submitted = true

      // if invalid form, return
      if (this.form.invalid) {
        return
      }

      this.loading = true
      this.user = await this.userSvc.login(this.form.value.username, this.form.value.password)
      this.router.navigate(['/topNews'])
    }
}
