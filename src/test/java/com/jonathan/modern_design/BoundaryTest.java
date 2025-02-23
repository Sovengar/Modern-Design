package com.jonathan.modern_design;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class BoundaryTest {
    JavaClasses classes = new ClassFileImporter().importPackages("com.jonathan.modern_design");

    @Test
    void testDomainWithApplication() {
        ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..application..")
                //.resideInAPackage("com.jonathan.modern_design.account_module.domain").should().dependOnClassesThat().resideInAPackage("com.jonathan.modern_design.account_module.application")
                .check(classes);
    }

    @Test
    void testDomainWithInfra() {
        ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..infra..")
                .check(classes);
    }

    @Test
    void testApplicationWithInfra() {
        ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..application..").should().dependOnClassesThat().resideInAPackage("..infra..")
                .check(classes);
    }
}
