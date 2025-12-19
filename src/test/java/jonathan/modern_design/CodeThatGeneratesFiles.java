package jonathan.modern_design;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@Tag("integration")
class CodeThatGeneratesFiles {
    ApplicationModules modules = ApplicationModules.of(AppRunner.class);

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

    @SuppressWarnings("java:S2699")
    @Test
    void generateAsciidoc() {
        var canvasOptions = Documenter.CanvasOptions.defaults();

        var docOptions = Documenter.DiagramOptions.defaults()
                .withStyle(Documenter.DiagramOptions.DiagramStyle.UML);

        new Documenter(modules).writeDocumentation(docOptions, canvasOptions);
    }
}
