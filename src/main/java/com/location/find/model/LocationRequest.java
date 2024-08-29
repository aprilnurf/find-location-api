package com.location.find.model;

import com.location.find.model.validator.NotEmptyIfString;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotEmptyIfString(field = "latitude", dependentField = "longitude", condition = NotEmptyIfString.Condition.NOT_EMPTY, fieldType = String.class)
@NotEmptyIfString(field = "longitude", dependentField = "latitude", condition = NotEmptyIfString.Condition.NOT_EMPTY, fieldType = String.class)
@NotEmptyIfString(field = "location", dependentField = "latitude", condition = NotEmptyIfString.Condition.EMPTY, fieldType = String.class)
@NotEmptyIfString(field = "location", dependentField = "longitude", condition = NotEmptyIfString.Condition.EMPTY, fieldType = String.class)
public class LocationRequest {
    @NotNull
    private LocationType type;
    private Double latitude;
    private Double longitude;
    // find by location area or search directly by long lat
    private String location;
    private Integer radius;
}
