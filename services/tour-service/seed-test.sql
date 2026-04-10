-- Seed data for tour-service (extended, more realistic)
-- Recommended on a fresh database to avoid duplicate key conflicts.
INSERT INTO destinations (
        id,
        name,
        slug,
        country,
        region,
        description,
        created_at
    )
VALUES (
        '10000000-0000-0000-0000-000000000001',
        'Da Nang',
        'da-nang',
        'Vietnam',
        'Central Vietnam',
        'Beach city with modern bridges and nearby heritage sites',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000002',
        'Phu Quoc',
        'phu-quoc',
        'Vietnam',
        'South Vietnam',
        'Island destination known for beaches and seafood',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000003',
        'Nha Trang',
        'nha-trang',
        'Vietnam',
        'South Central Vietnam',
        'Coastal city with island tours and diving',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000004',
        'Tokyo',
        'tokyo',
        'Japan',
        'Kanto',
        'Modern city with rich food and culture',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000005',
        'Seoul',
        'seoul',
        'South Korea',
        'Seoul Capital Area',
        'Urban destination with shopping and historical palaces',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000006',
        'Bangkok',
        'bangkok',
        'Thailand',
        'Central Thailand',
        'Lively city with temples and street food',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000007',
        'Singapore',
        'singapore',
        'Singapore',
        'Singapore',
        'City-state with modern attractions and clean urban design',
        NOW()
    ),
    (
        '10000000-0000-0000-0000-000000000008',
        'Paris',
        'paris',
        'France',
        'Ile-de-France',
        'European cultural destination with museums and landmarks',
        NOW()
    );
INSERT INTO departures (id, name, slug, description, created_at)
VALUES (
        '20000000-0000-0000-0000-000000000001',
        'Ha Noi',
        'ha-noi',
        'Departure from Ha Noi',
        NOW()
    ),
    (
        '20000000-0000-0000-0000-000000000002',
        'Ho Chi Minh City',
        'ho-chi-minh-city',
        'Departure from Ho Chi Minh City',
        NOW()
    ),
    (
        '20000000-0000-0000-0000-000000000003',
        'Da Nang',
        'departure-da-nang',
        'Departure from Da Nang',
        NOW()
    );
INSERT INTO categories (id, name, slug, description, icon)
VALUES (
        '30000000-0000-0000-0000-000000000001',
        'Beach',
        'beach',
        'Beach and island focused tours',
        'umbrella'
    ),
    (
        '30000000-0000-0000-0000-000000000002',
        'Family',
        'family',
        'Family-friendly tours',
        'users'
    ),
    (
        '30000000-0000-0000-0000-000000000003',
        'Luxury',
        'luxury',
        'Premium travel package',
        'star'
    ),
    (
        '30000000-0000-0000-0000-000000000004',
        'Cultural',
        'cultural',
        'Culture and heritage focused tours',
        'landmark'
    ),
    (
        '30000000-0000-0000-0000-000000000005',
        'Adventure',
        'adventure',
        'Outdoor and activity focused tours',
        'mountain'
    ),
    (
        '30000000-0000-0000-0000-000000000006',
        'Honeymoon',
        'honeymoon',
        'Romantic tour package',
        'heart'
    );
INSERT INTO tours (
        id,
        slug,
        name,
        description,
        destination_id,
        departure_id,
        base_price,
        duration_days,
        duration_nights,
        status,
        average_rating,
        review_count,
        created_at,
        updated_at
    )
VALUES (
        '40000000-0000-0000-0000-000000000001',
        'da-nang-hoi-an-4d3n',
        'Da Nang - Hoi An Discovery 4D3N',
        'Explore beach life and heritage streets in central Vietnam.',
        '10000000-0000-0000-0000-000000000001',
        '20000000-0000-0000-0000-000000000001',
        6990000.00,
        4,
        3,
        'ACTIVE',
        4.60,
        35,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000002',
        'phu-quoc-relax-4d3n',
        'Phu Quoc Relax 4D3N',
        'Resort stay with snorkeling and sunset town visit.',
        '10000000-0000-0000-0000-000000000002',
        '20000000-0000-0000-0000-000000000002',
        7590000.00,
        4,
        3,
        'ACTIVE',
        4.50,
        27,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000003',
        'nha-trang-island-3d2n',
        'Nha Trang Island Hopping 3D2N',
        'Short beach break with island and mud bath experience.',
        '10000000-0000-0000-0000-000000000003',
        '20000000-0000-0000-0000-000000000001',
        5290000.00,
        3,
        2,
        'ACTIVE',
        4.30,
        19,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000004',
        'tokyo-fuji-5d4n',
        'Tokyo - Fuji 5D4N',
        'City highlights and Fuji day trip with shopping time.',
        '10000000-0000-0000-0000-000000000004',
        '20000000-0000-0000-0000-000000000002',
        19990000.00,
        5,
        4,
        'ACTIVE',
        4.70,
        41,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000005',
        'japan-sakura-7d6n',
        'Japan Sakura 7D6N',
        'Classic spring route with cherry blossom spots.',
        '10000000-0000-0000-0000-000000000004',
        '20000000-0000-0000-0000-000000000001',
        26990000.00,
        7,
        6,
        'ACTIVE',
        4.80,
        64,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000006',
        'seoul-nami-5d4n',
        'Seoul - Nami 5D4N',
        'Popular Korean route with city and suburb experiences.',
        '10000000-0000-0000-0000-000000000005',
        '20000000-0000-0000-0000-000000000002',
        15990000.00,
        5,
        4,
        'ACTIVE',
        4.55,
        33,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000007',
        'bangkok-pattaya-5d4n',
        'Bangkok - Pattaya 5D4N',
        'City and beach combination with optional nightlife.',
        '10000000-0000-0000-0000-000000000006',
        '20000000-0000-0000-0000-000000000003',
        12990000.00,
        5,
        4,
        'ACTIVE',
        4.40,
        28,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000008',
        'singapore-fun-4d3n',
        'Singapore Fun 4D3N',
        'Family-friendly attractions and city experiences.',
        '10000000-0000-0000-0000-000000000007',
        '20000000-0000-0000-0000-000000000001',
        18990000.00,
        4,
        3,
        'ACTIVE',
        4.65,
        22,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000009',
        'paris-museum-6d5n',
        'Paris Museum & Landmark 6D5N',
        'European classic route focused on culture and city walks.',
        '10000000-0000-0000-0000-000000000008',
        '20000000-0000-0000-0000-000000000002',
        42990000.00,
        6,
        5,
        'ACTIVE',
        4.75,
        17,
        NOW(),
        NOW()
    ),
    (
        '40000000-0000-0000-0000-000000000010',
        'da-nang-family-3d2n',
        'Da Nang Family 3D2N',
        'Easy domestic package for families with kids.',
        '10000000-0000-0000-0000-000000000001',
        '20000000-0000-0000-0000-000000000002',
        4590000.00,
        3,
        2,
        'ACTIVE',
        4.35,
        12,
        NOW(),
        NOW()
    );
INSERT INTO tour_categories (tour_id, category_id)
VALUES (
        '40000000-0000-0000-0000-000000000001',
        '30000000-0000-0000-0000-000000000001'
    ),
    (
        '40000000-0000-0000-0000-000000000001',
        '30000000-0000-0000-0000-000000000004'
    ),
    (
        '40000000-0000-0000-0000-000000000002',
        '30000000-0000-0000-0000-000000000001'
    ),
    (
        '40000000-0000-0000-0000-000000000002',
        '30000000-0000-0000-0000-000000000003'
    ),
    (
        '40000000-0000-0000-0000-000000000003',
        '30000000-0000-0000-0000-000000000001'
    ),
    (
        '40000000-0000-0000-0000-000000000003',
        '30000000-0000-0000-0000-000000000005'
    ),
    (
        '40000000-0000-0000-0000-000000000004',
        '30000000-0000-0000-0000-000000000004'
    ),
    (
        '40000000-0000-0000-0000-000000000005',
        '30000000-0000-0000-0000-000000000003'
    ),
    (
        '40000000-0000-0000-0000-000000000005',
        '30000000-0000-0000-0000-000000000006'
    ),
    (
        '40000000-0000-0000-0000-000000000006',
        '30000000-0000-0000-0000-000000000002'
    ),
    (
        '40000000-0000-0000-0000-000000000007',
        '30000000-0000-0000-0000-000000000001'
    ),
    (
        '40000000-0000-0000-0000-000000000008',
        '30000000-0000-0000-0000-000000000002'
    ),
    (
        '40000000-0000-0000-0000-000000000009',
        '30000000-0000-0000-0000-000000000004'
    ),
    (
        '40000000-0000-0000-0000-000000000010',
        '30000000-0000-0000-0000-000000000002'
    );
INSERT INTO tour_schedules (
        id,
        tour_id,
        code,
        start_date,
        end_date,
        price,
        max_participants,
        min_participants,
        status
    )
VALUES (
        '50000000-0000-0000-0000-000000000001',
        '40000000-0000-0000-0000-000000000001',
        'DN-HA-2026-05A',
        '2026-05-10',
        '2026-05-13',
        6990000.00,
        30,
        10,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000002',
        '40000000-0000-0000-0000-000000000001',
        'DN-HA-2026-06A',
        '2026-06-14',
        '2026-06-17',
        7290000.00,
        30,
        10,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000003',
        '40000000-0000-0000-0000-000000000002',
        'PQ-HCM-2026-05A',
        '2026-05-18',
        '2026-05-21',
        7590000.00,
        25,
        8,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000004',
        '40000000-0000-0000-0000-000000000002',
        'PQ-HCM-2026-07A',
        '2026-07-08',
        '2026-07-11',
        7990000.00,
        25,
        8,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000005',
        '40000000-0000-0000-0000-000000000003',
        'NT-HN-2026-05A',
        '2026-05-22',
        '2026-05-24',
        5290000.00,
        20,
        6,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000006',
        '40000000-0000-0000-0000-000000000003',
        'NT-HN-2026-06A',
        '2026-06-19',
        '2026-06-21',
        5490000.00,
        20,
        6,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000007',
        '40000000-0000-0000-0000-000000000004',
        'TK-HCM-2026-05A',
        '2026-05-12',
        '2026-05-16',
        19990000.00,
        18,
        8,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000008',
        '40000000-0000-0000-0000-000000000004',
        'TK-HCM-2026-06A',
        '2026-06-09',
        '2026-06-13',
        20990000.00,
        18,
        8,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000009',
        '40000000-0000-0000-0000-000000000005',
        'JP-SAK-2026-03A',
        '2026-03-28',
        '2026-04-03',
        26990000.00,
        20,
        8,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000010',
        '40000000-0000-0000-0000-000000000005',
        'JP-SAK-2026-04A',
        '2026-04-12',
        '2026-04-18',
        27990000.00,
        20,
        8,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000011',
        '40000000-0000-0000-0000-000000000006',
        'KR-SN-2026-05A',
        '2026-05-15',
        '2026-05-19',
        15990000.00,
        24,
        10,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000012',
        '40000000-0000-0000-0000-000000000006',
        'KR-SN-2026-06A',
        '2026-06-20',
        '2026-06-24',
        16490000.00,
        24,
        10,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000013',
        '40000000-0000-0000-0000-000000000007',
        'BK-PT-2026-05A',
        '2026-05-11',
        '2026-05-15',
        12990000.00,
        30,
        10,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000014',
        '40000000-0000-0000-0000-000000000007',
        'BK-PT-2026-07A',
        '2026-07-13',
        '2026-07-17',
        13490000.00,
        30,
        10,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000015',
        '40000000-0000-0000-0000-000000000008',
        'SG-HN-2026-05A',
        '2026-05-25',
        '2026-05-28',
        18990000.00,
        16,
        6,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000016',
        '40000000-0000-0000-0000-000000000008',
        'SG-HN-2026-06A',
        '2026-06-22',
        '2026-06-25',
        19490000.00,
        16,
        6,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000017',
        '40000000-0000-0000-0000-000000000009',
        'PR-HCM-2026-06A',
        '2026-06-05',
        '2026-06-10',
        42990000.00,
        14,
        6,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000018',
        '40000000-0000-0000-0000-000000000009',
        'PR-HCM-2026-08A',
        '2026-08-07',
        '2026-08-12',
        43990000.00,
        14,
        6,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000019',
        '40000000-0000-0000-0000-000000000010',
        'DN-FM-2026-05A',
        '2026-05-09',
        '2026-05-11',
        4590000.00,
        35,
        12,
        'OPEN'
    ),
    (
        '50000000-0000-0000-0000-000000000020',
        '40000000-0000-0000-0000-000000000010',
        'DN-FM-2026-06A',
        '2026-06-06',
        '2026-06-08',
        4790000.00,
        35,
        12,
        'OPEN'
    );
INSERT INTO tour_itinerary (
        id,
        tour_id,
        day_number,
        title,
        description,
        meals,
        activities
    )
VALUES (
        '60000000-0000-0000-0000-000000000001',
        '40000000-0000-0000-0000-000000000001',
        1,
        'Arrival and city check-in',
        'Airport pickup, hotel check-in, Han River evening walk.',
        JSON_ARRAY('Dinner'),
        JSON_OBJECT('city', 'Da Nang')
    ),
    (
        '60000000-0000-0000-0000-000000000002',
        '40000000-0000-0000-0000-000000000001',
        2,
        'Ba Na Hills day trip',
        'Cable car and theme park experiences.',
        JSON_ARRAY('Breakfast', 'Lunch'),
        JSON_OBJECT('highlight', 'Golden Bridge')
    ),
    (
        '60000000-0000-0000-0000-000000000003',
        '40000000-0000-0000-0000-000000000004',
        1,
        'Tokyo central tour',
        'Visit Asakusa and Shibuya.',
        JSON_ARRAY('Dinner'),
        JSON_OBJECT('city', 'Tokyo')
    ),
    (
        '60000000-0000-0000-0000-000000000004',
        '40000000-0000-0000-0000-000000000005',
        2,
        'Sakura sightseeing',
        'Cherry blossom route and cultural spots.',
        JSON_ARRAY('Breakfast', 'Lunch'),
        JSON_OBJECT('season', 'Spring')
    ),
    (
        '60000000-0000-0000-0000-000000000005',
        '40000000-0000-0000-0000-000000000006',
        3,
        'Nami Island',
        'Scenic island tour and local food.',
        JSON_ARRAY('Breakfast', 'Lunch'),
        JSON_OBJECT('city', 'Seoul')
    ),
    (
        '60000000-0000-0000-0000-000000000006',
        '40000000-0000-0000-0000-000000000009',
        2,
        'Museum district',
        'Louvre and Seine riverside walk.',
        JSON_ARRAY('Breakfast'),
        JSON_OBJECT('city', 'Paris')
    );
INSERT INTO tour_inclusions (id, tour_id, type, description)
VALUES (
        '70000000-0000-0000-0000-000000000001',
        '40000000-0000-0000-0000-000000000001',
        'INCLUDED',
        '3-star/4-star hotel, transfer, selected meals'
    ),
    (
        '70000000-0000-0000-0000-000000000002',
        '40000000-0000-0000-0000-000000000002',
        'INCLUDED',
        'Resort stay, airport transfer, breakfast'
    ),
    (
        '70000000-0000-0000-0000-000000000003',
        '40000000-0000-0000-0000-000000000003',
        'INCLUDED',
        'Hotel, island boat ticket, local guide'
    ),
    (
        '70000000-0000-0000-0000-000000000004',
        '40000000-0000-0000-0000-000000000004',
        'INCLUDED',
        'Visa support, hotel, airport transfer'
    ),
    (
        '70000000-0000-0000-0000-000000000005',
        '40000000-0000-0000-0000-000000000005',
        'INCLUDED',
        'Hotel, intercity transport, breakfast'
    ),
    (
        '70000000-0000-0000-0000-000000000006',
        '40000000-0000-0000-0000-000000000006',
        'INCLUDED',
        'Hotel, transfer, city pass'
    ),
    (
        '70000000-0000-0000-0000-000000000007',
        '40000000-0000-0000-0000-000000000007',
        'INCLUDED',
        'Hotel, transfer, local guide'
    ),
    (
        '70000000-0000-0000-0000-000000000008',
        '40000000-0000-0000-0000-000000000008',
        'INCLUDED',
        'Hotel, entry tickets to selected attractions'
    ),
    (
        '70000000-0000-0000-0000-000000000009',
        '40000000-0000-0000-0000-000000000009',
        'INCLUDED',
        'Hotel, airport transfer, museum pass'
    ),
    (
        '70000000-0000-0000-0000-000000000010',
        '40000000-0000-0000-0000-000000000010',
        'INCLUDED',
        'Family hotel room, transfer, breakfast'
    );
INSERT INTO tour_optional_services (
        id,
        tour_id,
        name,
        price,
        pricing_type,
        category,
        is_available,
        is_required,
        max_quantity,
        min_quantity
    )
VALUES (
        '80000000-0000-0000-0000-000000000001',
        '40000000-0000-0000-0000-000000000001',
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
        '80000000-0000-0000-0000-000000000002',
        '40000000-0000-0000-0000-000000000002',
        'Snorkeling Combo',
        450000.00,
        'PER_PERSON',
        'ACTIVITY',
        true,
        false,
        10,
        0
    ),
    (
        '80000000-0000-0000-0000-000000000003',
        '40000000-0000-0000-0000-000000000004',
        '4G SIM Card',
        300000.00,
        'PER_PERSON',
        'UTILITY',
        true,
        false,
        30,
        0
    ),
    (
        '80000000-0000-0000-0000-000000000004',
        '40000000-0000-0000-0000-000000000005',
        'Single Room Upgrade',
        1800000.00,
        'PER_ROOM',
        'ACCOMMODATION',
        true,
        false,
        5,
        0
    ),
    (
        '80000000-0000-0000-0000-000000000005',
        '40000000-0000-0000-0000-000000000009',
        'Museum Priority Pass',
        900000.00,
        'PER_PERSON',
        'TICKET',
        true,
        false,
        20,
        0
    );
INSERT INTO tour_reviews (
        id,
        tour_id,
        customer_id,
        booking_id,
        rating,
        status,
        is_verified,
        comment
    )
VALUES (
        '90000000-0000-0000-0000-000000000001',
        '40000000-0000-0000-0000-000000000001',
        '91000000-0000-0000-0000-000000000001',
        '92000000-0000-0000-0000-000000000001',
        5,
        'APPROVED',
        true,
        'Great balance between beach and city activities.'
    ),
    (
        '90000000-0000-0000-0000-000000000002',
        '40000000-0000-0000-0000-000000000005',
        '91000000-0000-0000-0000-000000000002',
        '92000000-0000-0000-0000-000000000002',
        5,
        'APPROVED',
        true,
        'Sakura season was amazing and organization was smooth.'
    ),
    (
        '90000000-0000-0000-0000-000000000003',
        '40000000-0000-0000-0000-000000000009',
        '91000000-0000-0000-0000-000000000003',
        '92000000-0000-0000-0000-000000000003',
        4,
        'APPROVED',
        true,
        'Beautiful itinerary, but the pace was a bit tight.'
    );