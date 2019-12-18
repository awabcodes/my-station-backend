import { Moment } from 'moment';

export interface ISuggestion {
  id?: number;
  title?: string;
  message?: string;
  date?: Moment;
}

export class Suggestion implements ISuggestion {
  constructor(public id?: number, public title?: string, public message?: string, public date?: Moment) {}
}
