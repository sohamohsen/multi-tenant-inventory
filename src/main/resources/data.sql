INSERT INTO dealers (
    id,
    tenant_id,
    name,
    email,
    subscription_type,
    create_at
)
VALUES
    (
        'f47ac10b-58cc-4372-a567-0e02b2c3d479',
        '123e4567-e89b-12d3-a456-426614174000',
        'Dealer One',
        'dealer1@test.com',
        'BASIC',
        NOW()
    ),
    (
        '9a8b7c6d-1234-4abc-9def-123456789abc',
        '123e4567-e89b-12d3-a456-426614174000',
        'Dealer Two',
        'dealer2@test.com',
        'PREMIUM',
        NOW()
    );

INSERT INTO vehicles (
    id,
    tenant_id,
    dealer_id,
    model,
    price,
    status,
    create_at
)
VALUES
    (
        'c1a7f0c2-7d3e-4c9b-9a1f-2d8f5b6e1a11',
        '123e4567-e89b-12d3-a456-426614174000',
        'f47ac10b-58cc-4372-a567-0e02b2c3d479',
        'Toyota Corolla',
        20000,
        'AVAILABLE',
        NOW()
    ),
    (
        'd2b8e1f3-8e4f-5dab-a2bf-3e9f6c7d2b22',
        '123e4567-e89b-12d3-a456-426614174000',
        '9a8b7c6d-1234-4abc-9def-123456789abc',
        'BMW X5',
        50000,
        'AVAILABLE',
        NOW()
    );