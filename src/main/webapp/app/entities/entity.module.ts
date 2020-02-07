import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'selling-product',
        loadChildren: () => import('./selling-product/selling-product.module').then(m => m.BugtrackerjhipsterSellingProductModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class BugtrackerjhipsterEntityModule {}
