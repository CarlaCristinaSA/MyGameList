package br.com.jovirds.integrationtest.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperGameDTO {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private gameEmbeddedDTO embedded;

    public WrapperGameDTO() {}

    public gameEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(gameEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
