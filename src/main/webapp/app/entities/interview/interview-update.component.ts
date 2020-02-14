import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IInterview, Interview } from 'app/shared/model/interview.model';
import { InterviewService } from './interview.service';
import { IOpportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';

@Component({
  selector: 'jhi-interview-update',
  templateUrl: './interview-update.component.html'
})
export class InterviewUpdateComponent implements OnInit {
  isSaving = false;

  opportunities: IOpportunity[] = [];
  occuredDateDp: any;

  editForm = this.fb.group({
    id: [],
    occuredDate: [],
    jobTitle: [],
    description: [],
    salary: [],
    contact: [],
    type: [],
    opportunity: []
  });

  constructor(
    protected interviewService: InterviewService,
    protected opportunityService: OpportunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interview }) => {
      this.updateForm(interview);

      this.opportunityService
        .query()
        .pipe(
          map((res: HttpResponse<IOpportunity[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IOpportunity[]) => (this.opportunities = resBody));
    });
  }

  updateForm(interview: IInterview): void {
    this.editForm.patchValue({
      id: interview.id,
      occuredDate: interview.occuredDate,
      jobTitle: interview.jobTitle,
      description: interview.description,
      salary: interview.salary,
      contact: interview.contact,
      type: interview.type,
      opportunity: interview.opportunity
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interview = this.createFromForm();
    if (interview.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewService.update(interview));
    } else {
      this.subscribeToSaveResponse(this.interviewService.create(interview));
    }
  }

  private createFromForm(): IInterview {
    return {
      ...new Interview(),
      id: this.editForm.get(['id'])!.value,
      occuredDate: this.editForm.get(['occuredDate'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      salary: this.editForm.get(['salary'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      type: this.editForm.get(['type'])!.value,
      opportunity: this.editForm.get(['opportunity'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterview>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IOpportunity): any {
    return item.id;
  }
}
