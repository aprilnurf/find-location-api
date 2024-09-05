package com.location.find.model;

import com.location.find.model.validator.NotEmptyIfString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotEmptyIfString(targetField = "type", dependentField = "location", condition = NotEmptyIfString.Condition.NOT_EMPTY)
@NotEmptyIfString(targetField = "country", dependentField = "location", condition = NotEmptyIfString.Condition.NOT_EMPTY)
@NotEmptyIfString(targetField = "latitude", dependentField = "longitude", condition = NotEmptyIfString.Condition.NOT_EMPTY)
@NotEmptyIfString(targetField = "longitude", dependentField = "latitude", condition = NotEmptyIfString.Condition.NOT_EMPTY)
@NotEmptyIfString(targetField = "location", dependentField = "latitude", condition = NotEmptyIfString.Condition.EMPTY)
@NotEmptyIfString(targetField = "location", dependentField = "longitude", condition = NotEmptyIfString.Condition.EMPTY)
public class LocationRequest {

    private LocationType type;
    private Double latitude;
    private Double longitude;
    // find by location area or search directly by long lat
    private String location;
    private Integer radius;
    private String country;
}
