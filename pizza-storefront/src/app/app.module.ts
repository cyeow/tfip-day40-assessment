import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main.component';
import { OrdersComponent } from './components/orders.component';
import { PizzaService } from './pizza.service';

const appRoutes: Routes = [
  {path:'', component: MainComponent, title: 'Pizza Factory'},
  {path:'orders/:email', component: OrdersComponent},
  {path:'**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    OrdersComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes, { useHash: true }),
    HttpClientModule
  ],

  providers: [PizzaService],
  bootstrap: [AppComponent]
})
export class AppModule { }
