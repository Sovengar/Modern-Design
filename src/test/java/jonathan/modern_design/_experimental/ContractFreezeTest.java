package jonathan.modern_design._experimental;

//public class ContractFreezeTest extends IntegrationTestConfig {
//    @Autowired
//    MockMvc mockMvc;
//    ObjectMapper jackson = new ObjectMapper();
//
//    @Value("classpath:/my-openapi.json") // extracted from deploy
//    Resource myExpectedOpenAPI;
//
//    @Test
//    void my_contract_did_not_change() throws Exception {
//        String actualOpenAPIJson = prettifyJsonString(
//                mockMvc.perform(get("/v3/api-docs"))
//                        .andReturn().getResponse().getContentAsString());
//
//        String expectedOpenAPIJson = prettifyJsonString(
//                IOUtils.toString(myExpectedOpenAPI.getInputStream())
//                        .replace(":8080", "")); // hack the extracted port
//
//
//        System.out.println("New contract: " + actualOpenAPIJson);
//        ChangedOpenApi diff = OpenApiCompare.fromContents(expectedOpenAPIJson, actualOpenAPIJson);
//
//        if (!diff.isCompatible()) {
//            String render = new MarkdownRender().render(diff);
//            System.err.println(render);
//
//            assertThat(actualOpenAPIJson)
//                    .describedAs("Exposed OpenAPI should not have changed")
//                    .isEqualTo(expectedOpenAPIJson);
//        }
//    }
//
//    private String prettifyJsonString(String rawJson) throws JsonProcessingException {
//        return StringUtils.isBlank(rawJson) ? rawJson :
//                jackson.writerWithDefaultPrettyPrinter().writeValueAsString(
//                        jackson.readValue(rawJson, Map.class));
//    }
//}

//@SpringBootTest
//@ActiveProfiles("db-mem")
//@AutoConfigureMockMvc
//public class ContactTest {
//    @Autowired
//    MockMvc mockMvc;
//    ObjectMapper jackson = new ObjectMapper();
//
//    @Value("classpath:/my-existing-openapi.json") // extracted from deploy
//    Resource myExpectedOpenAPI;
//
//    @Test
//    void my_contract_did_not_change() throws Exception {
//        String actualOpenAPIJson = prettifyJsonString(
//                mockMvc.perform(get("/v3/api-docs"))
//                        .andReturn().getResponse().getContentAsString());
//
//        String expectedOpenAPIJson = prettifyJsonString(
//                IOUtils.toString(myExpectedOpenAPI.getInputStream())
//                        .replace(":8080", "")); // hack the extracted port
//
//        System.out.println("New contract: " + actualOpenAPIJson);
//        ChangedOpenApi diff = OpenApiCompare.fromContents(expectedOpenAPIJson, actualOpenAPIJson);
//
//        if (!diff.isCompatible()) {
//            String render = new MarkdownRender().render(diff);
//            System.err.println(render);
//
//            assertThat(actualOpenAPIJson)
//                    .describedAs("Exposed OpenAPI should not have changed")
//                    .isEqualTo(expectedOpenAPIJson);
//        }
//    }
//
//    private String prettifyJsonString(String rawJson) throws JsonProcessingException {
//        return StringUtils.isBlank(rawJson) ? rawJson :
//                jackson.writerWithDefaultPrettyPrinter().writeValueAsString(
//                        jackson.readValue(rawJson, Map.class));
//    }
//}
