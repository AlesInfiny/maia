CREATE SCHEMA dressca AUTHORIZATION dressca;

-- Create the table in the specified schema
CREATE TABLE dressca.assets
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  asset_code NVARCHAR(32) NOT NULL,
  asset_type NVARCHAR(128) NOT NULL
);

-- Create the table in the specified schema
CREATE TABLE dressca.baskets
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  buyer_id NVARCHAR(64) NOT NULL
);

-- Create the table in the specified schema
CREATE TABLE dressca.catalog_brands
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  name NVARCHAR(128) NOT NULL
);

-- Create the table in the specified schema
CREATE TABLE dressca.catalog_categories
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  name NVARCHAR(128) NOT NULL
);

-- Create the table in the specified schema
CREATE TABLE dressca.orders
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  buyer_id NVARCHAR(64) NOT NULL,
  order_date TIMESTAMP NOT NULL,
  ship_to_full_name NVARCHAR(64) NOT NULL,
  ship_to_postal_code NVARCHAR(16) NOT NULL,
  ship_to_todofuken NVARCHAR(16) NOT NULL,
  ship_to_shikuchoson NVARCHAR(16) NOT NULL,
  ship_to_azana_and_others NVARCHAR(128) NOT NULL,
  consumption_tax_rate NUMERIC(18,6) NOT NULL,
  total_items_price NUMERIC(18,6) NOT NULL,
  delivery_charge NUMERIC(18,6) NOT NULL,
  consumption_tax NUMERIC(18,6) NOT NULL,
  total_price NUMERIC(18,6) NOT NULL
);

-- Create the table in the specified schema
CREATE TABLE dressca.basket_items
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  basket_id BIGINT NOT NULL,
  catalog_item_id BIGINT NOT NULL,
  unit_price NUMERIC(18,6) NOT NULL,
  quantity INT NOT NULL,
  CONSTRAINT FK_basket_items_baskets FOREIGN KEY (basket_id) REFERENCES dressca.baskets(id) ON DELETE CASCADE
);

-- Create the table in the specified schema
CREATE TABLE dressca.catalog_items
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  name NVARCHAR(512) NOT NULL,
  description NVARCHAR(MAX) NOT NULL,
  price NUMERIC(18,6) NOT NULL,
  product_code NVARCHAR(128) NOT NULL,
  catalog_category_id BIGINT NOT NULL,
  catalog_brand_id BIGINT NOT NULL,
  CONSTRAINT FK_catalog_items_catalog_brands FOREIGN KEY (catalog_brand_id) REFERENCES dressca.catalog_brands(id) ON DELETE CASCADE,
  CONSTRAINT FK_catalog_items_catalog_categories FOREIGN KEY (catalog_category_id) REFERENCES dressca.catalog_categories(id) ON DELETE CASCADE
);

-- Create the table in the specified schema
CREATE TABLE dressca.order_items
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  ordered_catalog_item_id BIGINT NOT NULL,
  ordered_product_name NVARCHAR(512) NOT NULL,
  ordered_product_code NVARCHAR(128) NOT NULL,
  unit_price NUMERIC(18,6) NOT NULL,
  quantity INT NOT NULL,
  order_id BIGINT NOT NULL,
  CONSTRAINT FK_order_items_orders FOREIGN KEY (order_id) REFERENCES dressca.orders(id) ON DELETE CASCADE
);

-- Create the table in the specified schema
CREATE TABLE dressca.catalog_item_assets
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  asset_code NVARCHAR(32) NOT NULL,
  order_item_id BIGINT NOT NULL,
  CONSTRAINT FK_order_item_assets_order_items FOREIGN KEY (order_item_id) REFERENCES dressca.order_items(id) ON DELETE CASCADE
);