import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyStationSharedModule } from 'app/shared/shared.module';
import { StationComponent } from './station.component';
import { StationDetailComponent } from './station-detail.component';
import { StationUpdateComponent } from './station-update.component';
import { StationDeleteDialogComponent } from './station-delete-dialog.component';
import { stationRoute } from './station.route';

@NgModule({
  imports: [MyStationSharedModule, RouterModule.forChild(stationRoute)],
  declarations: [StationComponent, StationDetailComponent, StationUpdateComponent, StationDeleteDialogComponent],
  entryComponents: [StationDeleteDialogComponent]
})
export class MyStationStationModule {}
