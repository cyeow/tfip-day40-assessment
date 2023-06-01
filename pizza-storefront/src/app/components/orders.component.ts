import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { OrderPending } from '../models';
import { PizzaService } from '../pizza.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  email!: string
  
  activatedRoute = inject(ActivatedRoute)
  pizzaSvc = inject(PizzaService)

  ordersPending$!: Observable<OrderPending[]>

  ngOnInit(): void {
    this.email = this.activatedRoute.snapshot.params['email']
    this.ordersPending$ = this.pizzaSvc.getOrders(this.email);
  }
}
