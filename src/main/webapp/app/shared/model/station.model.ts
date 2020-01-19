import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IReport } from 'app/shared/model/report.model';

export interface IStation {
  id?: number;
  name?: string;
  gasLevel?: number;
  benzeneLevel?: number;
  lastTankFill?: Moment;
  city?: string;
  location?: string;
  mapUrl?: string;
  user?: IUser;
  report?: IReport;
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
    public mapUrl?: string,
    public user?: IUser,
    public report?: IReport
  ) {}
}
