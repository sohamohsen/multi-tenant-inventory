CREATE TABLE IF NOT EXISTS dealers (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(255),
    subscription_type VARCHAR(50)
    );

CREATE TABLE IF NOT EXISTS vehicles (
                                        id UUID PRIMARY KEY,
                                        model VARCHAR(255),
    status VARCHAR(50),
    price DECIMAL,
    tenant_id UUID,
    dealer_id UUID
    );