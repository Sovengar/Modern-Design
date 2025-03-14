package com.jonathan.modern_design.modulith;

import com.jonathan.modern_design.AppRunner;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

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
