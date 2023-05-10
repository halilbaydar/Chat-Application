import {Module} from '@nestjs/common';
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {ModuleWorkflow} from "./modules/workflow/module.workflow";

@Module({
    imports: [ModuleWorkflow],
    controllers: [AppController],
    providers: [AppService],
})
export class AppModule {
}
