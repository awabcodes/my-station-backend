import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyStationSharedModule } from 'app/shared/shared.module';
import { ReportComponent } from './report.component';
import { ReportDetailComponent } from './report-detail.component';
import { ReportUpdateComponent } from './report-update.component';
import { ReportDeleteDialogComponent } from './report-delete-dialog.component';
import { reportRoute } from './report.route';

@NgModule({
  imports: [MyStationSharedModule, RouterModule.forChild(reportRoute)],
  declarations: [ReportComponent, ReportDetailComponent, ReportUpdateComponent, ReportDeleteDialogComponent],
  entryComponents: [ReportDeleteDialogComponent]
})
export class MyStationReportModule {}
