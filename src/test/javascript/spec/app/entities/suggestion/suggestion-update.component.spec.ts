import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MyStationTestModule } from '../../../test.module';
import { SuggestionUpdateComponent } from 'app/entities/suggestion/suggestion-update.component';
import { SuggestionService } from 'app/entities/suggestion/suggestion.service';
import { Suggestion } from 'app/shared/model/suggestion.model';

describe('Component Tests', () => {
  describe('Suggestion Management Update Component', () => {
    let comp: SuggestionUpdateComponent;
    let fixture: ComponentFixture<SuggestionUpdateComponent>;
    let service: SuggestionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MyStationTestModule],
        declarations: [SuggestionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SuggestionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SuggestionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SuggestionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Suggestion(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Suggestion();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
