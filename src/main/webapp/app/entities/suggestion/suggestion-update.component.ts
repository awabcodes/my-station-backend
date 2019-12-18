import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { ISuggestion, Suggestion } from 'app/shared/model/suggestion.model';
import { SuggestionService } from './suggestion.service';

@Component({
  selector: 'jhi-suggestion-update',
  templateUrl: './suggestion-update.component.html'
})
export class SuggestionUpdateComponent implements OnInit {
  isSaving: boolean;
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    message: [null, [Validators.required]],
    date: [null, [Validators.required]]
  });

  constructor(protected suggestionService: SuggestionService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ suggestion }) => {
      this.updateForm(suggestion);
    });
  }

  updateForm(suggestion: ISuggestion) {
    this.editForm.patchValue({
      id: suggestion.id,
      title: suggestion.title,
      message: suggestion.message,
      date: suggestion.date
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const suggestion = this.createFromForm();
    if (suggestion.id !== undefined) {
      this.subscribeToSaveResponse(this.suggestionService.update(suggestion));
    } else {
      this.subscribeToSaveResponse(this.suggestionService.create(suggestion));
    }
  }

  private createFromForm(): ISuggestion {
    return {
      ...new Suggestion(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      message: this.editForm.get(['message']).value,
      date: this.editForm.get(['date']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuggestion>>) {
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
