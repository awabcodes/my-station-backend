import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IStation, Station } from 'app/shared/model/station.model';
import { StationService } from './station.service';

@Component({
  selector: 'jhi-station-update',
  templateUrl: './station-update.component.html'
})
export class StationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    gasLevel: [null, [Validators.required, Validators.min(0), Validators.max(100)]],
    benzeneLevel: [null, [Validators.required, Validators.min(0), Validators.max(100)]],
    lastTankFill: [null, [Validators.required]],
    city: [null, [Validators.required]],
    location: [null, [Validators.required]],
    mapUrl: [null, [Validators.required]]
  });

  constructor(protected stationService: StationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ station }) => {
      this.updateForm(station);
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
      mapUrl: station.mapUrl
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const station = this.createFromForm();
    if (station.id !== undefined) {
      this.subscribeToSaveResponse(this.stationService.update(station));
    } else {
      this.subscribeToSaveResponse(this.stationService.create(station));
    }
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
      mapUrl: this.editForm.get(['mapUrl']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
