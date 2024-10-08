package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class MovieResponseDTO {
    private int page;
    private List<MovieDTO> results;

    @JsonProperty("total_results")
    private int totalResults;

    @JsonProperty("total_pages")
    private int totalPages;
}