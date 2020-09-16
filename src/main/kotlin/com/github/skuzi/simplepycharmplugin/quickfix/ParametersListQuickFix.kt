package com.github.skuzi.simplepycharmplugin.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyParameterList

/**
 * Quick fix for functions where at least one parameter is not type annotated
 */
class ParametersListQuickFix : LocalQuickFix {
    override fun getFamilyName(): String {
        return name
    }

    override fun getName(): String {
        return "Annotate function parameters"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val parameterList = descriptor.psiElement
        if (parameterList !is PyParameterList) {
            return
        }

        val generator = PyElementGenerator.getInstance(project)
        val newParameterList = generator.createParameterList(LanguageLevel.PYTHON38, "()")
        parameterList.parameters.forEach {
            newParameterList.addParameter(
                generator.createParameter(
                    it.name!!,
                    it.defaultValueText,
                    it.asNamed?.annotationValue?:"int",
                    LanguageLevel.PYTHON38
                )
            )
        }

        parameterList.replace(newParameterList)
    }
}
