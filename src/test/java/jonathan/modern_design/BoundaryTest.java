package jonathan.modern_design;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class BoundaryTest {
    JavaClasses classes = new ClassFileImporter().importPackages("jonathan.modern_design");

    @Test
    void testDomainWithApplication() {
        var sliceRule = ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..application..");

        var evaluation = sliceRule.evaluate(classes);
        evaluation.getFailureReport().getDetails().forEach(System.out::println);

        sliceRule.check(classes);
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

    @Test
    void enforceModules() {
        var sliceRule = slices().matching("..modern_design.(*)..*").should().notDependOnEachOther().ignoreDependency("..shared..", "..*.."); //Don't know if this is correct
        sliceRule.check(classes);
    }
}
