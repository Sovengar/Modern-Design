package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountAddressTest {

    @Test
    void should_create_account_address_from_individual_fields() {
        AccountAddress address = AccountAddress.of("123 Main St", "Springfield", "IL", "62701");
        assertThat(address.getStreet()).isEqualTo("123 Main St");
        assertThat(address.getCity()).isEqualTo("Springfield");
        assertThat(address.getState()).isEqualTo("IL");
        assertThat(address.getZipCode()).isEqualTo("62701");
    }

    @Test
    void should_create_account_address_from_single_string() {
        AccountAddress address = AccountAddress.of("123 Main St||Springfield||IL||62701");
        assertThat(address.getStreet()).isEqualTo("123 Main St");
        assertThat(address.getCity()).isEqualTo("Springfield");
        assertThat(address.getState()).isEqualTo("IL");
        assertThat(address.getZipCode()).isEqualTo("62701");
    }

    @Test
    void should_throw_exception_when_single_string_is_invalid() {
        assertThatThrownBy(() -> AccountAddress.of("123 Main St||Springfield||IL"))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    void should_return_true_for_equal_addresses() {
        AccountAddress address1 = AccountAddress.of("123 Main St", "Springfield", "IL", "62701");
        AccountAddress address2 = AccountAddress.of("123 Main St", "Springfield", "IL", "62701");
        assertThat(address1).isEqualTo(address2);
    }

    @Test
    void should_return_false_for_different_addresses() {
        AccountAddress address1 = AccountAddress.of("123 Main St", "Springfield", "IL", "62701");
        AccountAddress address2 = AccountAddress.of("456 Elm St", "Springfield", "IL", "62701");
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void should_return_correct_string_representation() {
        AccountAddress address = AccountAddress.of("123 Main St", "Springfield", "IL", "62701");
        assertThat(address.toString()).hasToString("123 Main St, Springfield, IL 62701");
    }
}
