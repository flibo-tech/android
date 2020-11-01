package com.pivot.flibo.di.component;


import com.pivot.flibo.di.PerService;
import com.pivot.flibo.di.module.ServiceModule;
import com.pivot.flibo.service.SyncService;

import dagger.Component;

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(SyncService service);

}
