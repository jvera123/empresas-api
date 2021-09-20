package com.banelco.empresas.rest.response.empresas.v2;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RechargeConfigurationResponse {

    @JsonProperty("type")
    @JsonAlias({"type","tipo"})
    private String type;

    @JsonProperty("amounts")
    @JsonAlias({"amounts","importes"})
    private ArrayList<Double> amounts;

}
