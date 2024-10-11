DROP TABLE IF EXISTS assets CASCADE;
CREATE TABLE assets
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  asset_code VARCHAR(32) NOT NULL,
  asset_type VARCHAR(128) NOT NULL
);

DROP TABLE IF EXISTS baskets CASCADE;
CREATE TABLE baskets
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  buyer_id VARCHAR(64) NOT NULL
);

DROP TABLE IF EXISTS catalog_brands CASCADE;
CREATE TABLE catalog_brands
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  name VARCHAR(128) NOT NULL
);

DROP TABLE IF EXISTS catalog_categories CASCADE;
CREATE TABLE catalog_categories
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  name VARCHAR(128) NOT NULL
);

DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE orders
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  buyer_id VARCHAR(64) NOT NULL,
  order_date TIMESTAMP NOT NULL,
  ship_to_full_name VARCHAR(64) NOT NULL,
  ship_to_postal_code VARCHAR(16) NOT NULL,
  ship_to_todofuken VARCHAR(16) NOT NULL,
  ship_to_shikuchoson VARCHAR(32) NOT NULL,
  ship_to_azana_and_others VARCHAR(128) NOT NULL,
  consumption_tax_rate NUMERIC(18,6) NOT NULL,
  total_items_price NUMERIC(18,6) NOT NULL,
  delivery_charge NUMERIC(18,6) NOT NULL,
  consumption_tax NUMERIC(18,6) NOT NULL,
  total_price NUMERIC(18,6) NOT NULL
);

DROP TABLE IF EXISTS basket_items CASCADE;
CREATE TABLE basket_items
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  basket_id BIGINT NOT NULL,
  catalog_item_id BIGINT NOT NULL,
  unit_price NUMERIC(18,6) NOT NULL,
  quantity INT NOT NULL,
  CONSTRAINT FK_basket_items_baskets FOREIGN KEY (basket_id) REFERENCES baskets(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS catalog_items CASCADE;
CREATE TABLE catalog_items
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  name VARCHAR(512) NOT NULL,
  description VARCHAR NOT NULL,
  price NUMERIC(18,6) NOT NULL,
  product_code VARCHAR(128) NOT NULL,
  catalog_category_id BIGINT NOT NULL,
  catalog_brand_id BIGINT NOT NULL,
  row_version INT NOT NULL,
  CONSTRAINT FK_catalog_items_catalog_brands FOREIGN KEY (catalog_brand_id) REFERENCES catalog_brands(id) ON DELETE CASCADE,
  CONSTRAINT FK_catalog_items_catalog_categories FOREIGN KEY (catalog_category_id) REFERENCES catalog_categories(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS order_items CASCADE;
CREATE TABLE order_items
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  ordered_catalog_item_id BIGINT NOT NULL,
  ordered_product_name VARCHAR(512) NOT NULL,
  ordered_product_code VARCHAR(128) NOT NULL,
  unit_price NUMERIC(18,6) NOT NULL,
  quantity INT NOT NULL,
  order_id BIGINT NOT NULL,
  CONSTRAINT FK_order_items_orders FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS catalog_item_assets CASCADE;
CREATE TABLE catalog_item_assets
(
  id BIGSERIAL NOT NULL PRIMARY KEY, -- primary key column
  asset_code VARCHAR(32) NOT NULL,
  catalog_item_id BIGINT NOT NULL,
  CONSTRAINT FK_catalog_item_assets_catalog_items FOREIGN KEY (catalog_item_id) REFERENCES catalog_items(id) ON DELETE CASCADE
);