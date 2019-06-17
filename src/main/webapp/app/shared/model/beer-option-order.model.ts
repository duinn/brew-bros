import { IOrder } from 'app/shared/model/order.model';
import { IBeerOption } from 'app/shared/model/beer-option.model';

export interface IBeerOptionOrder {
  id?: number;
  amount?: number;
  order?: IOrder;
  beerOption?: IBeerOption;
}

export const defaultValue: Readonly<IBeerOptionOrder> = {};
