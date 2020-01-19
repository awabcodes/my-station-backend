import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IReport, Report } from 'app/shared/model/report.model';
import { ReportService } from './report.service';
import { IStation } from 'app/shared/model/station.model';
import { StationService } from 'app/entities/station/station.service';

@Component({
  selector: 'jhi-report-update',
  templateUrl: './report-update.component.html'
})
export class ReportUpdateComponent implements OnInit {
  isSaving: boolean;

  stations: IStation[];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    weeklyConsumption: [null, [Validators.required]],
    monthlyConsumption: [null, [Validators.required]],
    station: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected reportService: ReportService,
    protected stationService: StationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ report }) => {
      this.updateForm(report);
    });
    this.stationService.query({ 'reportId.specified': 'false' }).subscribe(
      (res: HttpResponse<IStation[]>) => {
        if (!this.editForm.get('station').value || !this.editForm.get('station').value.id) {
          this.stations = res.body;
        } else {
          this.stationService
            .find(this.editForm.get('station').value.id)
            .subscribe(
              (subRes: HttpResponse<IStation>) => (this.stations = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(report: IReport) {
    this.editForm.patchValue({
      id: report.id,
      title: report.title,
      weeklyConsumption: report.weeklyConsumption,
      monthlyConsumption: report.monthlyConsumption,
      station: report.station
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const report = this.createFromForm();
    if (report.id !== undefined) {
      this.subscribeToSaveResponse(this.reportService.update(report));
    } else {
      this.subscribeToSaveResponse(this.reportService.create(report));
    }
  }

  private createFromForm(): IReport {
    return {
      ...new Report(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      weeklyConsumption: this.editForm.get(['weeklyConsumption']).value,
      monthlyConsumption: this.editForm.get(['monthlyConsumption']).value,
      station: this.editForm.get(['station']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackStationById(index: number, item: IStation) {
    return item.id;
  }
}
