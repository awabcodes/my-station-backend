import { IStation } from 'app/shared/model/station.model';

export interface IReport {
  id?: number;
  title?: string;
  weeklyConsumption?: string;
  monthlyConsumption?: string;
  station?: IStation;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public title?: string,
    public weeklyConsumption?: string,
    public monthlyConsumption?: string,
    public station?: IStation
  ) {}
}
