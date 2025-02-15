package com.jonathan.modern_design.config.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

record ErrorDetails(@JsonInclude(Include.NON_NULL) String pointer, String reason) implements Serializable {
}

