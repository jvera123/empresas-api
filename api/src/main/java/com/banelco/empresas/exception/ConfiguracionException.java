package com.banelco.empresas.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfiguracionException extends Exception {
    private static final long serialVersionUID = 7732832853280856836L;
    private String code;
    private String message;
}
