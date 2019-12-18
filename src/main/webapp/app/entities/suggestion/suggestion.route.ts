import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Suggestion } from 'app/shared/model/suggestion.model';
import { SuggestionService } from './suggestion.service';
import { SuggestionComponent } from './suggestion.component';
import { SuggestionDetailComponent } from './suggestion-detail.component';
import { SuggestionUpdateComponent } from './suggestion-update.component';
import { ISuggestion } from 'app/shared/model/suggestion.model';

@Injectable({ providedIn: 'root' })
export class SuggestionResolve implements Resolve<ISuggestion> {
  constructor(private service: SuggestionService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuggestion> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((suggestion: HttpResponse<Suggestion>) => suggestion.body));
    }
    return of(new Suggestion());
  }
}

export const suggestionRoute: Routes = [
  {
    path: '',
    component: SuggestionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'myStationApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SuggestionDetailComponent,
    resolve: {
      suggestion: SuggestionResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'myStationApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SuggestionUpdateComponent,
    resolve: {
      suggestion: SuggestionResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'myStationApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SuggestionUpdateComponent,
    resolve: {
      suggestion: SuggestionResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'myStationApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
