import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStation } from 'app/shared/model/station.model';
import { StationService } from './station.service';

@Component({
  templateUrl: './station-delete-dialog.component.html'
})
export class StationDeleteDialogComponent {
  station: IStation;

  constructor(protected stationService: StationService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.stationService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'stationListModification',
        content: 'Deleted an station'
      });
      this.activeModal.dismiss(true);
    });
  }
}
