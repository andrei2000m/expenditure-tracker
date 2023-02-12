import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { MatFormFieldModule } from "@angular/material/form-field";
// import { MatTabsModule } from "@angular/material/tabs";

import { AppComponent } from './app.component';
import { CategoryInsertComponent } from "./category/category.component";

@NgModule({
  declarations: [
    AppComponent,
    CategoryInsertComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    MatFormFieldModule,
//     MatTabsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
