package com.jonathan.modern_design.shared.country;

//TODO
//@RestClientTest(SwapiClient.class)
//class CountryClientTest {
//
//    @Autowired
//    private StarShipInventory swapiClient;
//
//    @Autowired
//    private MockRestServiceServer mockServer;
//
//    @Value("classpath:__files/payloads/swapi-page1.json")
//    private Resource swapiPage1;
//
//    @Value("classpath:__files/payloads/swapi-page2.json")
//    private Resource swapiPage2;
//
//    @Test
//    void should_return_the_starship_inventory() {
//        configureMockServer();
//
//        List<StarShip> starships = swapiClient.starShips();
//
//        assertThat(starships).containsExactly(
//                new StarShip("CR90 corvette", 600, new BigDecimal("3000000")),
//                new StarShip("Slave 1", 6, new BigDecimal("70000")),
//                new StarShip("Death Star", 843342, new BigDecimal("1000000000000")));
//
//    }
//
//    private void configureMockServer() {
//        this.mockServer
//                .expect(method(HttpMethod.GET))
//                .andExpect(requestTo("http://swapi/api/starships"))
//                .andRespond(withSuccess(swapiPage1, APPLICATION_JSON));
//
//        this.mockServer
//                .expect(method(HttpMethod.GET))
//                .andExpect(requestTo("http://swapi/api/starships/?page=2"))
//                .andRespond(withSuccess(swapiPage2, APPLICATION_JSON));
//    }
//}
