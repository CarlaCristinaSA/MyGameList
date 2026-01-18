package br.com.jovirds.integrationtest.dto.wrappers.xml;

import br.com.jovirds.integrationtest.dto.GameDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class PagedModelGame implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    private List<GameDTO> content;

    public PagedModelGame() {}

    public List<GameDTO> getContent() {
        return content;
    }

    public void setContent(List<GameDTO> content) {
        this.content = content;
    }
}
