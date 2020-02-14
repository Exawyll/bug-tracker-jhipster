import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'selling-product',
        loadChildren: () => import('./selling-product/selling-product.module').then(m => m.BugtrackerjhipsterSellingProductModule)
      },
      {
        path: 'opportunity',
        loadChildren: () => import('./opportunity/opportunity.module').then(m => m.BugtrackerjhipsterOpportunityModule)
      },
      {
        path: 'interview',
        loadChildren: () => import('./interview/interview.module').then(m => m.BugtrackerjhipsterInterviewModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class BugtrackerjhipsterEntityModule {}
