-- Hotel ownership

ALTER TABLE vacationable.hotels
    ADD COLUMN owner_id INTEGER NOT NULL REFERENCES vacationable.users(id);

-- Catalog of grantable permissions
CREATE TABLE vacationable.permissions (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL UNIQUE
);

-- Users working at a specific hotel (owner is tracked via hotels.owner_id, not here)
CREATE TABLE vacationable.hotel_staff (
                                          id SERIAL PRIMARY KEY,
                                          hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id) ON DELETE CASCADE,
                                          user_id INTEGER NOT NULL REFERENCES vacationable.users(id) ON DELETE CASCADE,
                                          UNIQUE(hotel_id, user_id),
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Permissions granted to a given staff-at-hotel assignment
CREATE TABLE vacationable.hotel_staff_permissions (
                                                       id SERIAL PRIMARY KEY,
                                                       staff_id INTEGER NOT NULL REFERENCES vacationable.hotel_staff(id) ON DELETE CASCADE,
                                                       permission_id INTEGER NOT NULL REFERENCES vacationable.permissions(id) ON DELETE CASCADE,
                                                       UNIQUE(staff_id, permission_id)
);

-- Pending invites to become staff at a hotel
CREATE TABLE vacationable.hotel_invites (
                                            id SERIAL PRIMARY KEY,
                                            hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id) ON DELETE CASCADE,
                                            email VARCHAR(255) NOT NULL,
                                            token VARCHAR(255) NOT NULL UNIQUE,
                                            expires_at TIMESTAMP NOT NULL,
                                            accepted_at TIMESTAMP,
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for permission checks and invite lookups
CREATE INDEX idx_hotels_owner_id ON vacationable.hotels(owner_id);
CREATE INDEX idx_hotel_staff_user_id ON vacationable.hotel_staff(user_id);
CREATE INDEX idx_hotel_staff_hotel_id ON vacationable.hotel_staff(hotel_id);
CREATE INDEX idx_hotel_staff_permissions_staff_id ON vacationable.hotel_staff_permissions(staff_id);
CREATE INDEX idx_hotel_invites_token ON vacationable.hotel_invites(token);
CREATE INDEX idx_hotel_invites_hotel_id ON vacationable.hotel_invites(hotel_id);

-- Seed the permission catalog
INSERT INTO vacationable.permissions (name) VALUES
    ('EDIT_DETAILS'),
    ('MANAGE_ROOMS'),
    ('MANAGE_IMAGES'),
    ('VIEW_BOOKINGS');
