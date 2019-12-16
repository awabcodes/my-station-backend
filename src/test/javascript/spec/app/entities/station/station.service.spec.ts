import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { StationService } from 'app/entities/station/station.service';
import { IStation, Station } from 'app/shared/model/station.model';

describe('Service Tests', () => {
  describe('Station Service', () => {
    let injector: TestBed;
    let service: StationService;
    let httpMock: HttpTestingController;
    let elemDefault: IStation;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(StationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Station(0, 'AAAAAAA', 0, 0, currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lastTankFill: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Station', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lastTankFill: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            lastTankFill: currentDate
          },
          returnedFromService
        );
        service
          .create(new Station(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Station', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            gasLevel: 1,
            benzeneLevel: 1,
            lastTankFill: currentDate.format(DATE_TIME_FORMAT),
            city: 'BBBBBB',
            location: 'BBBBBB',
            mapUrl: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastTankFill: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Station', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            gasLevel: 1,
            benzeneLevel: 1,
            lastTankFill: currentDate.format(DATE_TIME_FORMAT),
            city: 'BBBBBB',
            location: 'BBBBBB',
            mapUrl: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            lastTankFill: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Station', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
