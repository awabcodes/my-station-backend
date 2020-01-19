import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReport } from 'app/shared/model/report.model';
import { ReportService } from './report.service';

@Component({
  templateUrl: './report-delete-dialog.component.html'
})
export class ReportDeleteDialogComponent {
  report: IReport;

  constructor(protected reportService: ReportService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.reportService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'reportListModification',
        content: 'Deleted an report'
      });
      this.activeModal.dismiss(true);
    });
  }
}
