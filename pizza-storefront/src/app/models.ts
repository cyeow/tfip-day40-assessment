export interface Order {
    name: string
    email: string
    size: number
    base: string
    sauce: string
    toppings: Map<string, boolean>
    comments: string
}

export interface OrderReq {
    name: string
    email: string
    size: number
    base: string
    sauce: string
    toppings: string[]
    comments: string
}

export interface OrderPending {
    orderId: string
    date: Date
    total: number
}