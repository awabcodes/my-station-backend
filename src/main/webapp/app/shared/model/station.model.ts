import { Moment } from 'moment';

export interface IStation {
  id?: number;
  name?: string;
  gasLevel?: number;
  benzeneLevel?: number;
  lastTankFill?: Moment;
  city?: string;
  location?: string;
  mapUrl?: string;
}

export class Station implements IStation {
  constructor(
    public id?: number,
    public name?: string,
    public gasLevel?: number,
    public benzeneLevel?: number,
    public lastTankFill?: Moment,
    public city?: string,
    public location?: string,
    public mapUrl?: string
  ) {}
}
