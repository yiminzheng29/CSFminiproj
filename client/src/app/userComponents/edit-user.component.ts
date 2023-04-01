import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {

  form!: FormGroup
  username?: string

  constructor(private fb: FormBuilder, private route: Router, private activatedRoute: ActivatedRoute, private userSvc: UserService) {}

  ngOnInit(): void {
      
  }

}
