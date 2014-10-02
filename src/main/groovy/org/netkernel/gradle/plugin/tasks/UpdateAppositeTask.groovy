package org.netkernel.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.netkernel.gradle.plugin.model.NetKernelInstance
import org.netkernel.gradle.plugin.model.PropertyHelper

/*
 * A task to synchronize and update NetKernel with latest packages from Apposite
 */

class UpdateAppositeTask extends DefaultTask {
    // Static Defaults

    //Variable parameters

    //Helpers
    def propertyHelper = new PropertyHelper()

    @Input
    NetKernelInstance netKernelInstance

    @TaskAction
    void updateApposite() {
        println("UPDATING NETKERNEL WITH APPOSITE");


        def url=nkinstance.getUrl()+
                ":"+
                nkinstance.getBackendPort()

        def synchronize=url+"/tools/apposite/unattended/v1/synchronize";
        def update=url+"/tools/apposite/unattended/v1/update";

        callAppositeAPI(synchronize);
        sleep(5000);
        callAppositeAPI(update)
        sleep(30000);
    }

    def callAppositeAPI(url)
    {   println ("APPOSITE API CALL: $url")
        ant.get(src: url,
                dest: "temp-apposite.html",
                verbose: true,
                usetimestamp : true)
    }
    
}
