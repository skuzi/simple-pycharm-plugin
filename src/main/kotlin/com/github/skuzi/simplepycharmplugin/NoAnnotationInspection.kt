package com.github.skuzi.simplepycharmplugin

import com.github.skuzi.simplepycharmplugin.quickfix.AssignmentQuickFix
import com.github.skuzi.simplepycharmplugin.quickfix.ParametersListQuickFix
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.PyTupleExpression

/**
 * Inspection to detect variable declarations without type annotations
 */
class NoAnnotationInspection : PyInspection() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PyElementVisitor {
        return object : PyElementVisitor() {

            private val DESCRIPTION = "No type annotation"

            override fun visitElement(element: PsiElement) {
                super.visitElement(element)

                if (element is PyAssignmentStatement && element.annotationValue == null && isSingleVariable(element)) {
                    holder.registerProblem(element, DESCRIPTION, AssignmentQuickFix())
                    return
                }

                if (element is PyFunction) {
                    if (element.parameterList.parameters.any { it.asNamed!!.annotationValue == null }) {
                        holder.registerProblem(element.parameterList, DESCRIPTION, ParametersListQuickFix())
                    }

                    // TODO untyped function declaration

                    return
                }
            }
        }
    }

    private fun isSingleVariable(element: PyAssignmentStatement): Boolean {
        return element.rawTargets.size == 1 && element.leftHandSideExpression !is PyTupleExpression
    }
}
