import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuggestion } from 'app/shared/model/suggestion.model';

@Component({
  selector: 'jhi-suggestion-detail',
  templateUrl: './suggestion-detail.component.html'
})
export class SuggestionDetailComponent implements OnInit {
  suggestion: ISuggestion;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ suggestion }) => {
      this.suggestion = suggestion;
    });
  }

  previousState() {
    window.history.back();
  }
}
