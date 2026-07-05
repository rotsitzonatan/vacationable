CREATE SCHEMA IF NOT EXISTS vacationable;
-- Users table
CREATE TABLE vacationable.users (
                                    id SERIAL PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    first_name VARCHAR(100),
                                    last_name VARCHAR(100),
                                    phone VARCHAR(20),
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Locations/Cities table
CREATE TABLE vacationable.locations (
                                        id SERIAL PRIMARY KEY,
                                        city_name VARCHAR(100) NOT NULL UNIQUE,
                                        country VARCHAR(100) NOT NULL,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotels table
CREATE TABLE vacationable.hotels (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     description TEXT,
                                     location_id INTEGER NOT NULL REFERENCES vacationable.locations(id),
                                     address VARCHAR(255),
                                     rating DECIMAL(3, 1) DEFAULT 0,
                                     total_reviews INTEGER DEFAULT 0,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotel Amenities reference table
CREATE TABLE vacationable.amenities (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL UNIQUE,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotel-Amenities junction table
CREATE TABLE vacationable.hotel_amenities (
                                              id SERIAL PRIMARY KEY,
                                              hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id) ON DELETE CASCADE,
                                              amenity_id INTEGER NOT NULL REFERENCES vacationable.amenities(id),
                                              UNIQUE(hotel_id, amenity_id),
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rooms table (different room types)
CREATE TABLE vacationable.rooms (
                                    id SERIAL PRIMARY KEY,
                                    hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id) ON DELETE CASCADE,
                                    room_type VARCHAR(50) NOT NULL, -- Suite, Deluxe, Standard, etc.
                                    description TEXT,
                                    max_guests INTEGER NOT NULL,
                                    base_price DECIMAL(10, 2) NOT NULL,
                                    total_rooms INTEGER NOT NULL DEFAULT 1,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Room Availability (daily inventory)
CREATE TABLE vacationable.room_availability (
                                                id SERIAL PRIMARY KEY,
                                                room_id INTEGER NOT NULL REFERENCES vacationable.rooms(id) ON DELETE CASCADE,
                                                available_date DATE NOT NULL,
                                                available_count INTEGER NOT NULL,
                                                price_override DECIMAL(10, 2), -- For dynamic pricing
                                                UNIQUE(room_id, available_date),
                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reviews & Ratings
CREATE TABLE vacationable.reviews (
                                      id SERIAL PRIMARY KEY,
                                      hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id) ON DELETE CASCADE,
                                      user_id INTEGER NOT NULL REFERENCES vacationable.users(id) ON DELETE CASCADE,
                                      rating DECIMAL(3, 1) NOT NULL,
                                      title VARCHAR(255),
                                      comment TEXT,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotel Images
CREATE TABLE vacationable.hotel_images (
                                           id SERIAL PRIMARY KEY,
                                           hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id) ON DELETE CASCADE,
                                           image_url VARCHAR(500) NOT NULL,
                                           alt_text VARCHAR(255),
                                           is_primary BOOLEAN DEFAULT FALSE,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Payment Methods
CREATE TABLE vacationable.payment_methods (
                                              id SERIAL PRIMARY KEY,
                                              user_id INTEGER NOT NULL REFERENCES vacationable.users(id) ON DELETE CASCADE,
                                              card_last_four VARCHAR(4),
                                              card_type VARCHAR(20), -- VISA, MASTERCARD, etc.
                                              is_default BOOLEAN DEFAULT FALSE,
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions/Payments
CREATE TABLE vacationable.transactions (
                                           id SERIAL PRIMARY KEY,
                                           user_id INTEGER NOT NULL REFERENCES vacationable.users(id),
                                           amount DECIMAL(10, 2) NOT NULL,
                                           status VARCHAR(50) DEFAULT 'PENDING', -- COMPLETED, FAILED, REFUNDED
                                           payment_method_id INTEGER REFERENCES vacationable.payment_methods(id),
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Bookings table (updated)
CREATE TABLE vacationable.bookings (
                                       id SERIAL PRIMARY KEY,
                                       user_id INTEGER NOT NULL REFERENCES vacationable.users(id) ON DELETE CASCADE,
                                       room_id INTEGER NOT NULL REFERENCES vacationable.rooms(id),
                                       hotel_id INTEGER NOT NULL REFERENCES vacationable.hotels(id),
                                       check_in_date DATE NOT NULL,
                                       check_out_date DATE NOT NULL,
                                       number_of_guests INTEGER NOT NULL,
                                       total_price DECIMAL(10, 2) NOT NULL,
                                       status VARCHAR(50) DEFAULT 'PENDING', -- CONFIRMED, CANCELLED, COMPLETED
                                       transaction_id INTEGER REFERENCES vacationable.transactions(id),
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_hotels_location_id ON vacationable.hotels(location_id);
CREATE INDEX idx_rooms_hotel_id ON vacationable.rooms(hotel_id);
CREATE INDEX idx_room_availability_room_id ON vacationable.room_availability(room_id);
CREATE INDEX idx_room_availability_date ON vacationable.room_availability(available_date);
CREATE INDEX idx_reviews_hotel_id ON vacationable.reviews(hotel_id);
CREATE INDEX idx_reviews_user_id ON vacationable.reviews(user_id);
CREATE INDEX idx_hotel_images_hotel_id ON vacationable.hotel_images(hotel_id);
CREATE INDEX idx_bookings_user_id ON vacationable.bookings(user_id);
CREATE INDEX idx_bookings_hotel_id ON vacationable.bookings(hotel_id);
CREATE INDEX idx_bookings_room_id ON vacationable.bookings(room_id);
CREATE INDEX idx_bookings_status ON vacationable.bookings(status);
CREATE INDEX idx_transactions_user_id ON vacationable.transactions(user_id);