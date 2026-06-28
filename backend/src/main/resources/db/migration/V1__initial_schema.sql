  -- Users table
  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) NOT NULL UNIQUE,
      password VARCHAR(255) NOT NULL,
      first_name VARCHAR(100),
      last_name VARCHAR(100),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

  -- Hotels table
  CREATE TABLE hotels (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          location VARCHAR(255) NOT NULL,
                          price_per_night DECIMAL(10, 2) NOT NULL,
                          rating DECIMAL(3, 1),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );


-- Bookings table
  CREATE TABLE bookings (
                            id SERIAL PRIMARY KEY,
                            user_id INTEGER NOT NULL REFERENCES users(id),
                            hotel_id INTEGER NOT NULL REFERENCES hotels(id),
                            check_in_date DATE NOT NULL,
                            check_out_date DATE NOT NULL,
                            total_price DECIMAL(10, 2) NOT NULL,
                            status VARCHAR(50) DEFAULT 'PENDING',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

  CREATE INDEX idx_bookings_user_id ON bookings(user_id);
  CREATE INDEX idx_bookings_hotel_id ON bookings(hotel_id);