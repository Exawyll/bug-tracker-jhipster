import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISellingProduct } from 'app/shared/model/selling-product.model';

type EntityResponseType = HttpResponse<ISellingProduct>;
type EntityArrayResponseType = HttpResponse<ISellingProduct[]>;

@Injectable({ providedIn: 'root' })
export class SellingProductService {
  public resourceUrl = SERVER_API_URL + 'api/selling-products';

  constructor(protected http: HttpClient) {}

  create(sellingProduct: ISellingProduct): Observable<EntityResponseType> {
    return this.http.post<ISellingProduct>(this.resourceUrl, sellingProduct, { observe: 'response' });
  }

  update(sellingProduct: ISellingProduct): Observable<EntityResponseType> {
    return this.http.put<ISellingProduct>(this.resourceUrl, sellingProduct, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISellingProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISellingProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
