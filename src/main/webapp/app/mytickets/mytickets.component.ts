import { Component, OnInit } from '@angular/core';
import { ITicket } from 'app/shared/model/ticket.model';
import { Account } from 'app/core/user/account.model';
import { Subscription } from 'rxjs';
import { AccountService } from 'app/core/auth/account.service';
import { TicketService } from 'app/entities/ticket/ticket.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-mytickets',
  templateUrl: './mytickets.component.html',
  styleUrls: ['./mytickets.component.scss']
})
export class MyticketsComponent implements OnInit {
  tickets?: ITicket[];

  account?: Account;
  eventSubscriber?: Subscription;
  predicate: any;
  reverse: any;
  links: any;
  totalItems: any;

  constructor(
    private accountService: AccountService,
    private ticketService: TicketService,
    private eventManager: JhiEventManager,
    private parseLinks: JhiParseLinks
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.account = account ? account : undefined;
    });

    this.registerChangeInTickets();
  }

  loadSelf(): void {
    this.ticketService.queryMyTickets().subscribe((res: HttpResponse<ITicket[]>) => {
      res.body ? this.paginateTickets(res.body, res.headers) : (this.tickets = []);
    });
  }

  sort(): String[] {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTickets(data: ITicket[], headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link')!);
    this.totalItems = parseInt(headers.get('X-Total-Count')!, 10);
    this.tickets = data;
  }

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketListModification', () => this.loadSelf());
  }
}
