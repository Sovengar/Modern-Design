package com.jonathan.modern_design;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class BoundaryTest {
    JavaClasses classes = new ClassFileImporter().importPackages("com.jonathan");

    @Test
    void testDomainWithApplication() {
        var sliceRule = ArchRuleDefinition.noClasses().that().resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..application..");
        //.resideInAPackage("com.jonathan.modern_design.account_module.domain").should().dependOnClassesThat().resideInAPackage("com.jonathan.modern_design.account_module.application")
        //var sliceRule2 = slices().matching("..modern_design.(*)..*").should().notDependOnEachOther().ignoreDependency(resideInAnyPackage("..shared.."));
        //var sliceRule2 = slices().matching("com.jonathan.modern_design").should().notDependOnEachOther().ignoreDependency(resideInAnyPackage("..shared.."));
        sliceRule.check(classes);

        //List<String> violations = sliceRule.evaluate(classes);
        //assertThat(violations).hasSizeLessThan(30);
    }

    @Test
    void testDomainWithInfra() {
        var sliceRule = ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..infra..");

        sliceRule.check(classes);
    }

    @Test
    void testApplicationWithInfra() {
        var sliceRule = ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..application..").should().dependOnClassesThat().resideInAPackage("..infra..");

        sliceRule.check(classes);
    }
}
