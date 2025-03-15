package jonathan.modern_design;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

class BoundaryTest {
    @Nested
    class ArchUnitTest {
        JavaClasses classes = new ClassFileImporter().importPackages("jonathan.modern_design");
        String names = classes.stream().map(JavaClass::getSimpleName).collect(joining());

        @Test
        void testDomainWithApplication() {
            System.out.println("Studying classes: " + names);

            var sliceRule = ArchRuleDefinition.noClasses().that()
                    .resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..application..");

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
            var sliceRule = slices().matching("..modern_design.(*).*").should().notDependOnEachOther()
                    .ignoreDependency(alwaysTrue(), resideInAnyPackage(".._common..", ".._internal", ".._shared")); // allow dependencies to .events

            // progressive strangling the monolith
            EvaluationResult evaluationResult = sliceRule.evaluate(classes);
            int violations = evaluationResult.getFailureReport().getDetails().size();
            System.out.println("Number of violations: " + violations);

            assertThat(violations).isLessThan(20);
            //sliceRule.check(classes);
        }
    }

    @Nested
    class ModulithTest {
        ApplicationModules modules = ApplicationModules.of(AppRunner.class);

        @Test
        void testModuleVerification() {
            modules.forEach(System.out::println);
            modules.verify();
        }

        @SuppressWarnings("java:S2699")
        @Test
        void writeDocumentationSnippets() {
            new Documenter(modules)
                    .writeModulesAsPlantUml()
                    .writeIndividualModulesAsPlantUml();
            //new Documenter(modules).writeModuleCanvases();
            //Documenter.DiagramOptions.defaults().withStyle(Documenter.DiagramOptions.DiagramStyle.UML);
            //new Documenter(modules).writeAggregatingDocument();

        }

        //Integration tests
//    @ApplicationModuleTest
//    class OrderIntegrationTests {
//
//        // Test methods go here
//    }
    }
}
