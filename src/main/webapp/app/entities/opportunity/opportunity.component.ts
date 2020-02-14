import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';
import { OpportunityDeleteDialogComponent } from './opportunity-delete-dialog.component';

@Component({
  selector: 'jhi-opportunity',
  templateUrl: './opportunity.component.html'
})
export class OpportunityComponent implements OnInit, OnDestroy {
  opportunities?: IOpportunity[];
  eventSubscriber?: Subscription;

  constructor(
    protected opportunityService: OpportunityService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.opportunityService.query().subscribe((res: HttpResponse<IOpportunity[]>) => {
      this.opportunities = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOpportunities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOpportunity): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOpportunities(): void {
    this.eventSubscriber = this.eventManager.subscribe('opportunityListModification', () => this.loadAll());
  }

  delete(opportunity: IOpportunity): void {
    const modalRef = this.modalService.open(OpportunityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.opportunity = opportunity;
  }
}
