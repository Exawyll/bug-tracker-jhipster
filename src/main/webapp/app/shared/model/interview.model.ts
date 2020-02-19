import { Moment } from 'moment';
import { IOpportunity } from 'app/shared/model/opportunity.model';
import { InterviewTypeEnum } from 'app/shared/model/enumerations/interview-type-enum.model';

export interface IInterview {
  id?: number;
  occuredDate?: Moment;
  jobTitle?: string;
  description?: string;
  salary?: number;
  contact?: string;
  type?: InterviewTypeEnum;
  opportunity?: IOpportunity;
}

export class Interview implements IInterview {
  constructor(
    public id?: number,
    public occuredDate?: Moment,
    public jobTitle?: string,
    public description?: string,
    public salary?: number,
    public contact?: string,
    public type?: InterviewTypeEnum,
    public opportunity?: IOpportunity
  ) {}
}
