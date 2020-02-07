export interface ISellingProduct {
  id?: number;
  v2ProductName?: string;
  v2ProductCategory?: string;
  unitsSold?: number;
  revenue?: number;
}

export class SellingProduct implements ISellingProduct {
  constructor(
    public id?: number,
    public v2ProductName?: string,
    public v2ProductCategory?: string,
    public unitsSold?: number,
    public revenue?: number
  ) {}
}
