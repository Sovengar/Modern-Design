package com.jonathan.modern_design.modulith;

import com.jonathan.modern_design.AppRunner;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.test.ApplicationModuleTest;

class ModulithTest {
    ApplicationModules modules = ApplicationModules.of(AppRunner.class);

    @Test
    void testModuleVerification() {
        modules.verify();
    }

    @Test
    void writeDocumentationSnippets() {
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
        //new Documenter(modules).writeModuleCanvases();
        //Documenter.DiagramOptions.defaults().withStyle(Documenter.DiagramOptions.DiagramStyle.UML);
    }

    //Integration tests
//    @ApplicationModuleTest
//    class OrderIntegrationTests {
//
//        // Test methods go here
//    }
}
