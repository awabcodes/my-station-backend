import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MyStationTestModule } from '../../../test.module';
import { StationUpdateComponent } from 'app/entities/station/station-update.component';
import { StationService } from 'app/entities/station/station.service';
import { Station } from 'app/shared/model/station.model';

describe('Component Tests', () => {
  describe('Station Management Update Component', () => {
    let comp: StationUpdateComponent;
    let fixture: ComponentFixture<StationUpdateComponent>;
    let service: StationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MyStationTestModule],
        declarations: [StationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Station(123);
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
        const entity = new Station();
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
