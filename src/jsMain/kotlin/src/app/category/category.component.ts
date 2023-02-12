import { Component } from '@angular/core';

import { NgForm } from "@angular/forms";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: 'category-form',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryInsertComponent {

  constructor(private http: HttpClient) {
  }

  categoryName = ""

  onFormSubmit(form: NgForm) {
    var data = form.value;
    var payload = data.categoryName
    this.http.post("/category", payload)
      .subscribe(response => {
        console.log(response);
      }, (err: HttpErrorResponse) => {
        console.log(err)
      });
  }
}
