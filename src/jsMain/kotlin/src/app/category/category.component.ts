import { Component } from '@angular/core';

import { NgForm } from "@angular/forms";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'category-form',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryInsertComponent {

  constructor(private http: HttpClient) {
  }

  categoryName = ""
  errorMessage = ""

  onFormSubmit(form: NgForm) {
    var data = form.value;
    var payload = data.categoryName
    this.http.post("/category", payload)
      .subscribe(response => {
        console.log(response);
        this.errorMessage = ""
//         TODO: This still shows even for 200
      }, (err: HttpErrorResponse) => {
        console.log(err)
        this.errorMessage = err.error
      });
  }
}
