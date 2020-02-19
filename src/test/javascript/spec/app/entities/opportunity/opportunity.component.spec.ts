import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BugtrackerjhipsterTestModule } from '../../../test.module';
import { OpportunityComponent } from 'app/entities/opportunity/opportunity.component';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';
import { Opportunity } from 'app/shared/model/opportunity.model';

describe('Component Tests', () => {
  describe('Opportunity Management Component', () => {
    let comp: OpportunityComponent;
    let fixture: ComponentFixture<OpportunityComponent>;
    let service: OpportunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BugtrackerjhipsterTestModule],
        declarations: [OpportunityComponent],
        providers: []
      })
        .overrideTemplate(OpportunityComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OpportunityComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OpportunityService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Opportunity(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.opportunities && comp.opportunities[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
