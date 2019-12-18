import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'station',
        loadChildren: () => import('./station/station.module').then(m => m.MyStationStationModule)
      },
      {
        path: 'suggestion',
        loadChildren: () => import('./suggestion/suggestion.module').then(m => m.MyStationSuggestionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MyStationEntityModule {}
