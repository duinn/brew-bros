import { IBeerOption } from 'app/shared/model/beer-option.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IBeerOptionOrder {
  id?: number;
  amount?: number;
  beerOptions?: IBeerOption[];
  order?: IOrder;
}

export const defaultValue: Readonly<IBeerOptionOrder> = {};
