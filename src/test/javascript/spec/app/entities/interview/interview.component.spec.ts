import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BugtrackerjhipsterTestModule } from '../../../test.module';
import { InterviewComponent } from 'app/entities/interview/interview.component';
import { InterviewService } from 'app/entities/interview/interview.service';
import { Interview } from 'app/shared/model/interview.model';

describe('Component Tests', () => {
  describe('Interview Management Component', () => {
    let comp: InterviewComponent;
    let fixture: ComponentFixture<InterviewComponent>;
    let service: InterviewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BugtrackerjhipsterTestModule],
        declarations: [InterviewComponent],
        providers: []
      })
        .overrideTemplate(InterviewComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Interview(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.interviews && comp.interviews[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
