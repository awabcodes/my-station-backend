import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISuggestion } from 'app/shared/model/suggestion.model';
import { SuggestionService } from './suggestion.service';

@Component({
  templateUrl: './suggestion-delete-dialog.component.html'
})
export class SuggestionDeleteDialogComponent {
  suggestion: ISuggestion;

  constructor(
    protected suggestionService: SuggestionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.suggestionService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'suggestionListModification',
        content: 'Deleted an suggestion'
      });
      this.activeModal.dismiss(true);
    });
  }
}
