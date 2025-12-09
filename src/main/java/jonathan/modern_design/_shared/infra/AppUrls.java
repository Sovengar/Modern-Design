package jonathan.modern_design._shared.infra;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AppUrls {

    @NoArgsConstructor(access = PRIVATE)
    public static class AuthUrls {
        public static final String AUTH_MODULE_URL = "/auth";
        public static final String USER_RESOURCE_URL = "/v1/users";
        public static final String ROLE_RESOURCE_URL = "/v1/roles";
        public static final String CATALOGS_URL = "/v1/catalogs";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class BankingUrls {
        public static final String BANKING_MODULE_URL = "/banking";
        public static final String ACCOUNT_HOLDERS_RESOURCE_URL = "/v1/account-holders";
        public static final String ACCOUNTS_RESOURCE_URL = "/v1/accounts";
        public static final String TRANSACTIONS_RESOURCE_URL = "/v1/transactions";
        public static final String CATALOGS_URL = "/v1/catalogs";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class SearchUrls {
        public static final String SEARCH_MODULE_URL = "/search";
        public static final String XXX_RESOURCE_URL = "/v1/xxx";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class AmazonUrls {

        @NoArgsConstructor(access = PRIVATE)
        public static class CatalogUrls {
            public static final String CATALOG_MODULE_URL = "/catalog";
            public static final String CATALOG_RESOURCE_URL = "/v1/catalogs";
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class PaymentUrls {
            public static final String PAYMENT_MODULE_URL = "/payment";
            public static final String PAYMENTS_RESOURCE_URL = "/v1/payments";
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class SalesUrls {
            public static final String SALES_MODULE_URL = "/sales";
            public static final String SHOPPING_CART = "/v1/shopping-cart";
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class OrderUrls {
            public static final String ORDER_MODULE_URL = "/order";
            public static final String ORDERS_RESOURCE_URL = "/v1/orders";
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class ShippingUrls {
            public static final String SHIPPING_MODULE_URL = "/shipping";
            public static final String SHIPPING_RESOURCE_URL = "/v1/shipping";
        }

        @NoArgsConstructor(access = PRIVATE)
        public static class InventoryUrls {
            public static final String INVENTORY_MODULE_URL = "/inventory";
            public static final String STOCK_RESOURCE_URL = "/v1/stock";
        }
    }

}
