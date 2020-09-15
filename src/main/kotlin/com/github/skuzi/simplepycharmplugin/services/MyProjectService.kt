package com.github.skuzi.simplepycharmplugin.services

import com.intellij.openapi.project.Project
import com.github.skuzi.simplepycharmplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
