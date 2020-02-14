import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterview } from 'app/shared/model/interview.model';
import { InterviewService } from './interview.service';
import { InterviewDeleteDialogComponent } from './interview-delete-dialog.component';

@Component({
  selector: 'jhi-interview',
  templateUrl: './interview.component.html'
})
export class InterviewComponent implements OnInit, OnDestroy {
  interviews?: IInterview[];
  eventSubscriber?: Subscription;

  constructor(protected interviewService: InterviewService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.interviewService.query().subscribe((res: HttpResponse<IInterview[]>) => {
      this.interviews = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInterviews();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInterview): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInterviews(): void {
    this.eventSubscriber = this.eventManager.subscribe('interviewListModification', () => this.loadAll());
  }

  delete(interview: IInterview): void {
    const modalRef = this.modalService.open(InterviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interview = interview;
  }
}
