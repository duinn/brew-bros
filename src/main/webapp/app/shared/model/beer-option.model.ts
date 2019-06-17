import { IBeerOptionOrder } from 'app/shared/model/beer-option-order.model';

export interface IBeerOption {
  id?: number;
  amount?: number;
  name?: string;
  brand?: string;
  volume?: number;
  abv?: number;
  beerOptionOrders?: IBeerOptionOrder;
}

export const defaultValue: Readonly<IBeerOption> = {};
