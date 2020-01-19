import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Report } from 'app/shared/model/report.model';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';
import { ReportDetailComponent } from './report-detail.component';
import { ReportUpdateComponent } from './report-update.component';
import { IReport } from 'app/shared/model/report.model';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<IReport> {
  constructor(private service: ReportService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReport> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((report: HttpResponse<Report>) => report.body));
    }
    return of(new Report());
  }
}

export const reportRoute: Routes = [
  {
    path: '',
    component: ReportComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'myStationApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReportDetailComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'myStationApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReportUpdateComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'myStationApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReportUpdateComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'myStationApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
