package org.netkernel.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.netkernel.gradle.plugin.nk.Download
import org.netkernel.gradle.plugin.nk.ExecutionConfig
import org.netkernel.gradle.plugin.util.FileSystemHelper

/**
 *  Manage configuration for the NetKernel plugin.
 */
class NetKernelExtension {

    FileSystemHelper fileSystemHelper = new FileSystemHelper()

    final Download download
    final NamedDomainObjectContainer<ExecutionConfig> envs

    // Properties moved from primary plugin
    final String configName
//    final File _installationDirectory
//    final File _freezeDirectory


    private Project project

    NetKernelExtension(Project project, NamedDomainObjectContainer<ExecutionConfig> envs, String configName) {
        this.project = project
        this.download = new Download(project)
        this.envs = envs
        this.configName = configName
    }

    def download(Closure closure) {
        project.configure(download, closure)
    }

    def envs(Closure closure) {
        envs.configure(closure)
    }

    org.gradle.api.artifacts.Dependency dep(String name, String version = '[1.0.0,)', String group = 'urn.com.ten60.core') {
        project.dependencies.create(group: group, name: name, version: version)
    }

    void useROCRepo() {
        useRepo "http://maven.netkernelroc.org:8080/netkernel-maven"
    }

    void useLocalhostRepo() {
        useRepo "http://localhost:8080/netkernel-maven"
    }

    void useRepo(String repoURL) {
        project.repositories.maven { url repoURL }
    }

    void useMavenCentral() {
        project.repositories.mavenCentral()
    }

    void useStandardCompileDependencies() {
        project.dependencies {
            provided dep('netkernel.api')
            provided dep('netkernel.impl')
            provided dep('layer0')
            provided dep('module.standard')
            provided dep('cache.se')
            provided dep('ext.layer1', '[1.0.0,)', 'urn.org.netkernel')
        }
    }

    File getInstallationDirectory() {
        return envs[configName].directory
    }

    File getFreezeDirectory() {
        return fileSystemHelper.dirInGradleHomeDirectory('netkernel/freeze')
    }

    File getDestinationDirectory() {
        return fileSystemHelper.dirInGradleHomeDirectory('netkernel')
    }

    File getThawDirectory() {
        return fileSystemHelper.dirInGradleHomeDirectory('netkernel/thaw')
    }

    File getThawInstallationDirectory() {
        return fileSystemHelper.dirInGradleHomeDirectory('netkernel/thawInstallation')
    }

    File getFrozenArchiveFile() {
        return fileSystemHelper.dirInGradleHomeDirectory('netkernel/download/frozen.zip')
    }
}
