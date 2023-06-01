import { Component, OnInit, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { PizzaService } from '../pizza.service';
import { Order } from '../models';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PIZZA_TOPPINGS: string[] = [
  'chicken', 'seafood', 'beef', 'vegetables',
  'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  pizzaSize = SIZES[0]

  form!: FormGroup

  fb = inject(FormBuilder)
  router = inject(Router)
  pizzaSvc = inject(PizzaService)

  constructor() { }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)]
  }

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    let toppingGrp = this.fb.group({})
    PIZZA_TOPPINGS.forEach(topping => {
      toppingGrp.addControl(topping, this.fb.control<boolean>(false, [ Validators.required ]))
    })

    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      size: this.fb.control<number>(0, [Validators.required]),
      base: this.fb.control<string>('', [Validators.required]),
      sauce: this.fb.control<string>('', [Validators.required]),
      toppings: toppingGrp,
      comments: this.fb.control<string>(''),
    })
  }

  placeOrder(): void {
    firstValueFrom(this.pizzaSvc.placeOrder({... this.form.value} as Order))
    .then(
      res => {
        this.router.navigate(['/orders', res['email']])
      }
    )
    .catch(
      err => {
        alert(err.error.error)
      }
    )
  }
}


// export function requireCheckboxesToBeCheckedValidator(minRequired = 1): ValidatorFn {
//   return function validate (formGroup: FormGroup) {
//     let checked = 0;

//     Object.keys(formGroup.controls).forEach(key => {
//       const control = formGroup.controls[key];

//       if (control.value === true) {
//         checked ++;
//       }
//     });

//     if (checked < minRequired) {
//       return {
//         requireOneCheckboxToBeChecked: true,
//       };
//     }

//     return null;
//   };
// }