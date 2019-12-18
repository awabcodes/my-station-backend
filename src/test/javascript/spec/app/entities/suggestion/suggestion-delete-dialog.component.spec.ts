import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MyStationTestModule } from '../../../test.module';
import { SuggestionDeleteDialogComponent } from 'app/entities/suggestion/suggestion-delete-dialog.component';
import { SuggestionService } from 'app/entities/suggestion/suggestion.service';

describe('Component Tests', () => {
  describe('Suggestion Management Delete Component', () => {
    let comp: SuggestionDeleteDialogComponent;
    let fixture: ComponentFixture<SuggestionDeleteDialogComponent>;
    let service: SuggestionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MyStationTestModule],
        declarations: [SuggestionDeleteDialogComponent]
      })
        .overrideTemplate(SuggestionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SuggestionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SuggestionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
