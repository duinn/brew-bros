export interface IBeerOption {
  id?: number;
  amount?: number;
  name?: string;
  brand?: string;
  volume?: number;
  abv?: number;
}

export const defaultValue: Readonly<IBeerOption> = {};
