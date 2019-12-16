import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStation } from 'app/shared/model/station.model';

type EntityResponseType = HttpResponse<IStation>;
type EntityArrayResponseType = HttpResponse<IStation[]>;

@Injectable({ providedIn: 'root' })
export class StationService {
  public resourceUrl = SERVER_API_URL + 'api/stations';

  constructor(protected http: HttpClient) {}

  create(station: IStation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(station);
    return this.http
      .post<IStation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(station: IStation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(station);
    return this.http
      .put<IStation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(station: IStation): IStation {
    const copy: IStation = Object.assign({}, station, {
      lastTankFill: station.lastTankFill != null && station.lastTankFill.isValid() ? station.lastTankFill.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastTankFill = res.body.lastTankFill != null ? moment(res.body.lastTankFill) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((station: IStation) => {
        station.lastTankFill = station.lastTankFill != null ? moment(station.lastTankFill) : null;
      });
    }
    return res;
  }
}
