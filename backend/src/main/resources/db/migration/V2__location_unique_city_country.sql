-- Replace single-column uniqueness on city_name with a composite
-- uniqueness on (city_name, country), since city names are not
-- globally unique across countries (e.g. Springfield, US vs elsewhere).
ALTER TABLE vacationable.locations
    DROP CONSTRAINT locations_city_name_key;

ALTER TABLE vacationable.locations
    ADD CONSTRAINT uq_location_city_country UNIQUE (city_name, country);
