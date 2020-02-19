import { IInterview } from 'app/shared/model/interview.model';
import { NetworkEnum } from 'app/shared/model/enumerations/network-enum.model';

export interface IOpportunity {
  id?: number;
  companyName?: string;
  place?: string;
  contactFrom?: NetworkEnum;
  interviews?: IInterview[];
}

export class Opportunity implements IOpportunity {
  constructor(
    public id?: number,
    public companyName?: string,
    public place?: string,
    public contactFrom?: NetworkEnum,
    public interviews?: IInterview[]
  ) {}
}
