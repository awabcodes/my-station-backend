import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { StationService } from 'app/entities/station/station.service';
import { UserService } from 'app/core/user/user.service';
import { FormBuilder, Validators } from '@angular/forms';
import { IStation, Station } from 'app/shared/model/station.model';
import { HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IUser } from 'app/core/user/user.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account;
  authSubscription: Subscription;
  modalRef: NgbModalRef;

  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    gasLevel: [null, [Validators.required, Validators.min(0), Validators.max(100)]],
    benzeneLevel: [null, [Validators.required, Validators.min(0), Validators.max(100)]],
    lastTankFill: [null, [Validators.required]],
    city: [null, [Validators.required]],
    location: [null, [Validators.required]],
    mapUrl: [null, [Validators.required]],
    user: [null, Validators.required]
  });

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    protected jhiAlertService: JhiAlertService,
    protected stationService: StationService,
    protected userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;

      this.isSaving = false;
      this.stationService.query({ 'userId.equals': this.account.id }).subscribe((station: HttpResponse<Station[]>) => {
        this.updateForm(station.body[0]);
      });
    });
    this.registerAuthenticationSuccess();
  }

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe(account => {
        this.account = account;

        this.isSaving = false;
        this.stationService.query({ 'userId.equals': this.account.id }).subscribe((station: HttpResponse<Station[]>) => {
          this.updateForm(station.body[0]);
        });
      });
    });
  }

  updateForm(station: IStation) {
    this.editForm.patchValue({
      id: station.id,
      name: station.name,
      gasLevel: station.gasLevel,
      benzeneLevel: station.benzeneLevel,
      lastTankFill: station.lastTankFill != null ? station.lastTankFill.format(DATE_TIME_FORMAT) : null,
      city: station.city,
      location: station.location,
      mapUrl: station.mapUrl,
      user: station.user
    });
  }

  save() {
    this.isSaving = true;
    const station = this.createFromForm();
    this.subscribeToSaveResponse(this.stationService.update(station));
  }

  private createFromForm(): IStation {
    return {
      ...new Station(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      gasLevel: this.editForm.get(['gasLevel']).value,
      benzeneLevel: this.editForm.get(['benzeneLevel']).value,
      lastTankFill:
        this.editForm.get(['lastTankFill']).value != null ? moment(this.editForm.get(['lastTankFill']).value, DATE_TIME_FORMAT) : undefined,
      city: this.editForm.get(['city']).value,
      location: this.editForm.get(['location']).value,
      mapUrl: this.editForm.get(['mapUrl']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }
}
