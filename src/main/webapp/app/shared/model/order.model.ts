import { Moment } from 'moment';
import { IBeerOptionOrder } from 'app/shared/model/beer-option-order.model';

export const enum OrderStatus {
  DRAFT = 'DRAFT',
  PLACED = 'PLACED',
  COMPLETE = 'COMPLETE'
}

export interface IOrder {
  id?: number;
  placedDateTime?: Moment;
  deliveryDateTime?: Moment;
  orderStatus?: OrderStatus;
  beerOptionOrders?: IBeerOptionOrder[];
}

export const defaultValue: Readonly<IOrder> = {};
