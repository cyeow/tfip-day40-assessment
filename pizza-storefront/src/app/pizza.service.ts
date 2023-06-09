import { HttpClient, HttpHeaders } from "@angular/common/http"
import { Injectable, inject } from "@angular/core"
import { Observable, firstValueFrom } from 'rxjs';
import { OrderPending, OrderReq } from "./models";

@Injectable()
export class PizzaService {

  http = inject(HttpClient)

  urlPrefix: string = '/api/'

  // TODO: Task 3
  // You may add any parameters and return any type from placeOrder() method
  // Do not change the method name
  placeOrder(order: any): Observable<any> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json')

    const orderReq: OrderReq = {
      ...order,
      toppings: Object.keys(order.toppings).filter(key => order.toppings[key])
    } as OrderReq
    return this.http.post<any>(this.urlPrefix + 'order', orderReq)
  }

  // TODO: Task 5
  // You may add any parameters and return any type from getOrders() method
  // Do not change the method name
  getOrders(email: string): Observable<OrderPending[]> {
    const headers = new HttpHeaders().set("Accept", "application/json")
    return this.http.get<OrderPending[]>(this.urlPrefix + 'orders/' + email, { headers })
  }

  // TODO: Task 7
  // You may add any parameters and return any type from delivered() method
  // Do not change the method name
  delivered(orderId: string): Promise<boolean> {
    const headers = new HttpHeaders().set("accept", "application/json")
    return firstValueFrom(this.http.delete<any>(this.urlPrefix + 'order/' + orderId, { headers }))
  }

}
