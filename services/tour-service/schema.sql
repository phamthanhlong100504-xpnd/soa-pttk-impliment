CREATE DATABASE IF NOT EXISTS tour_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE tour_db;

CREATE TABLE IF NOT EXISTS destinations (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL UNIQUE,
  country VARCHAR(255) NOT NULL,
  region VARCHAR(255),
  description LONGTEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departures (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL UNIQUE,
  description LONGTEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL UNIQUE,
  description LONGTEXT,
  icon VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tours (
  id VARCHAR(36) PRIMARY KEY,
  slug VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  description LONGTEXT,
  destination_id VARCHAR(36),
  departure_id VARCHAR(36),
  base_price DECIMAL(10,2),
  duration_days INT,
  duration_nights INT,
  status VARCHAR(50),
  average_rating DECIMAL(3,2),
  review_count INT DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_tours_destination FOREIGN KEY (destination_id) REFERENCES destinations(id),
  CONSTRAINT fk_tours_departure FOREIGN KEY (departure_id) REFERENCES departures(id)
);

CREATE TABLE IF NOT EXISTS tour_categories (
  tour_id VARCHAR(36) NOT NULL,
  category_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (tour_id, category_id),
  CONSTRAINT fk_tour_categories_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE,
  CONSTRAINT fk_tour_categories_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tour_schedules (
  id VARCHAR(36) PRIMARY KEY,
  tour_id VARCHAR(36) NOT NULL,
  code VARCHAR(255) NOT NULL UNIQUE,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  max_participants INT,
  min_participants INT,
  status VARCHAR(50),
  CONSTRAINT fk_tour_schedules_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tour_itinerary (
  id VARCHAR(36) PRIMARY KEY,
  tour_id VARCHAR(36) NOT NULL,
  day_number INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  description LONGTEXT,
  meals JSON,
  activities JSON,
  CONSTRAINT fk_tour_itinerary_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tour_inclusions (
  id VARCHAR(36) PRIMARY KEY,
  tour_id VARCHAR(36) NOT NULL,
  type VARCHAR(50) NOT NULL,
  description LONGTEXT,
  CONSTRAINT fk_tour_inclusions_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tour_optional_services (
  id VARCHAR(36) PRIMARY KEY,
  tour_id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  price DECIMAL(10,2),
  pricing_type VARCHAR(50),
  category VARCHAR(255),
  is_available BOOLEAN,
  is_required BOOLEAN,
  max_quantity INT,
  min_quantity INT,
  CONSTRAINT fk_tour_optional_services_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tour_reviews (
  id VARCHAR(36) PRIMARY KEY,
  tour_id VARCHAR(36) NOT NULL,
  customer_id VARCHAR(36) NOT NULL,
  booking_id VARCHAR(36) UNIQUE,
  rating INT NOT NULL,
  status VARCHAR(50),
  is_verified BOOLEAN,
  comment LONGTEXT,
  CONSTRAINT fk_tour_reviews_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);


-- Seed data for tour-service
-- Run after the schema has been created by JPA.

INSERT INTO destinations (id, name, slug, country, region, description, created_at) VALUES
('11111111-1111-1111-1111-111111111111', 'Japan', 'japan', 'Japan', 'Asia', 'Tour to Japan', NOW()),
('22222222-2222-2222-2222-222222222222', 'South Korea', 'south-korea', 'South Korea', 'Asia', 'Tour to South Korea', NOW());

INSERT INTO departures (id, name, slug, description, created_at) VALUES
('33333333-3333-3333-3333-333333333333', 'Ha Noi', 'ha-noi', 'Departure from Ha Noi', NOW()),
('44444444-4444-4444-4444-444444444444', 'Ho Chi Minh City', 'ho-chi-minh-city', 'Departure from Ho Chi Minh City', NOW());

INSERT INTO categories (id, name, slug, description, icon) VALUES
('55555555-5555-5555-5555-555555555555', 'Luxury', 'luxury', 'High-end travel package', 'star'),
('66666666-6666-6666-6666-666666666666', 'Family', 'family', 'Family-friendly package', 'users');

INSERT INTO tours (
    id, slug, name, description, destination_id, departure_id,
    base_price, duration_days, duration_nights, status,
    average_rating, review_count, created_at, updated_at
) VALUES
(
    '77777777-7777-7777-7777-777777777777',
    'japan-sakura-7d6n',
    'Japan Sakura 7D6N',
    'Experience cherry blossoms in Japan.',
    '11111111-1111-1111-1111-111111111111',
    '33333333-3333-3333-3333-333333333333',
    18990000.00,
    7,
    6,
    'ACTIVE',
    4.80,
    12,
    NOW(),
    NOW()
),
(
    '88888888-8888-8888-8888-888888888888',
    'korea-seoul-5d4n',
    'Seoul Discovery 5D4N',
    'Discover Seoul and nearby attractions.',
    '22222222-2222-2222-2222-222222222222',
    '44444444-4444-4444-4444-444444444444',
    12990000.00,
    5,
    4,
    'ACTIVE',
    4.60,
    8,
    NOW(),
    NOW()
);

INSERT INTO tour_categories (tour_id, category_id) VALUES
('77777777-7777-7777-7777-777777777777', '55555555-5555-5555-5555-555555555555'),
('77777777-7777-7777-7777-777777777777', '66666666-6666-6666-6666-666666666666'),
('88888888-8888-8888-8888-888888888888', '66666666-6666-6666-6666-666666666666');

INSERT INTO tour_schedules (
    id, tour_id, code, start_date, end_date, price, max_participants, min_participants, status
) VALUES
(
    '99999999-9999-9999-9999-999999999999',
    '77777777-7777-7777-7777-777777777777',
    'JP-SAKURA-2026-04',
    '2026-04-20',
    '2026-04-26',
    18990000.00,
    20,
    8,
    'OPEN'
),
(
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    '88888888-8888-8888-8888-888888888888',
    'KR-SEOUL-2026-05',
    '2026-05-10',
    '2026-05-14',
    12990000.00,
    25,
    10,
    'OPEN'
);

INSERT INTO tour_itinerary (id, tour_id, day_number, title, description, meals, activities) VALUES
(
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
    '77777777-7777-7777-7777-777777777777',
    1,
    'Arrival in Tokyo',
    'Arrive at the airport and transfer to the hotel.',
    JSON_ARRAY('Dinner'),
    JSON_OBJECT('city', 'Tokyo', 'notes', 'Check-in and free time')
),
(
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    '77777777-7777-7777-7777-777777777777',
    2,
    'Tokyo City Tour',
    'Visit major attractions in Tokyo.',
    JSON_ARRAY('Breakfast', 'Lunch'),
    JSON_OBJECT('places', JSON_ARRAY('Asakusa', 'Shibuya', 'Tokyo Tower'))
);

INSERT INTO tour_inclusions (id, tour_id, type, description) VALUES
('dddddddd-dddd-dddd-dddd-dddddddddddd', '77777777-7777-7777-7777-777777777777', 'INCLUDED', 'Hotel, breakfast, airport transfer'),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '77777777-7777-7777-7777-777777777777', 'EXCLUDED', 'Visa fee, personal expenses');

INSERT INTO tour_optional_services (
    id, tour_id, name, price, pricing_type, category,
    is_available, is_required, max_quantity, min_quantity
) VALUES
(
    'ffffffff-ffff-ffff-ffff-ffffffffffff',
    '77777777-7777-7777-7777-777777777777',
    'Travel Insurance',
    250000.00,
    'PER_BOOKING',
    'INSURANCE',
    true,
    false,
    1,
    0
),
(
    '12121212-1212-1212-1212-121212121212',
    '77777777-7777-7777-7777-777777777777',
    'Single Room Upgrade',
    1500000.00,
    'PER_ROOM',
    'ACCOMMODATION',
    true,
    false,
    2,
    0
);

INSERT INTO tour_reviews (
    id, tour_id, customer_id, booking_id, rating, status, is_verified, comment
) VALUES
(
    '13131313-1313-1313-1313-131313131313',
    '77777777-7777-7777-7777-777777777777',
    '14141414-1414-1414-1414-141414141414',
    '15151515-1515-1515-1515-151515151515',
    5,
    'APPROVED',
    true,
    'Great experience and well organized.'
);

