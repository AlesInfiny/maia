INSERT INTO assets (asset_code, asset_type)
VALUES ('b52dc7f712d94ca5812dd995bf926c04', 'png'),
('80bc8e167ccb4543b2f9d51913073492', 'png'),
('05d38fad5693422c8a27dd5b14070ec8', 'png'),
('45c22ba3da064391baac91341067ffe9', 'png'),
('4aed07c4ed5d45a5b97f11acedfbb601', 'png'),
('082b37439ecc44919626ba00fc60ee85', 'png'),
('f5f89954281747fa878129c29e1e0f83', 'png'),
('a8291ef2e8e14869a7048e272915f33c', 'png'),
('66237018c769478a90037bd877f5fba1', 'png'),
('d136d4c81b86478990984dcafbf08244', 'png'),
('47183f32f6584d7fb661f9216e11318b', 'png'),
('cf151206efd344e1b86854f4aa49fdef', 'png'),
('ab2e78eb7fe3408aadbf1e17a9945a8c', 'png'),
('0e557e96bc054f10bc91c27405a83e85', 'png'),
('e622b0098808492cb883831c05486b58', 'png');

insert into catalog_brands (id,name) values (1,'高級なブランド');
insert into catalog_brands (id,name) values (2,'カジュアルなブランド');
insert into catalog_brands (id,name) values (3,'ノーブランド');

insert into catalog_categories (id,name) values (1,'服');
insert into catalog_categories (id,name) values (2,'バッグ');
insert into catalog_categories (id,name) values (3,'シューズ');
 
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (1,'クルーネック Tシャツ - ブラック','定番の無地ロングTシャツです。',1980,'C000000001',1,3);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (2,'裏起毛 スキニーデニム','暖かいのに着膨れしない起毛デニムです。',4800,'C000000002',1,2);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (3,'ウールコート','あたたかく肌ざわりも良いウール100%のロングコートです。',49800,'C000000003',1,1);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (4,'無地 ボタンダウンシャツ','コットン100%の柔らかい着心地で、春先から夏、秋口まで万能に使いやすいです。',2800,'C000000004',1,2);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (5,'レザーハンドバッグ','コンパクトサイズのバッグですが収納力は抜群です。',18800,'B000000001',2,3);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (6,'ショルダーバッグ','エイジング加工したレザーを使用しています。',38000,'B000000002',2,2);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (7,'トートバッグ ポーチ付き','春の季節にぴったりのトートバッグです。インナーポーチまたは単体でも使用可能なポーチ付。',24800,'B000000003',2,3);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (8,'ショルダーバッグ','さらりと気軽に纏える、キュートなミニサイズショルダー。',2800,'B000000004',2,1);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (9,'レザー チェーンショルダーバッグ','エレガントな雰囲気を放つキルティングデザインです。',258000,'B000000005',2,1);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (10,'ランニングシューズ - ブルー','柔らかいソールは快適な履き心地で、ランニングに最適です。',12800,'S000000001',3,2);
insert into catalog_items (id,name,description,price,product_code,catalog_category_id,catalog_brand_id) values (11,'メダリオン ストレートチップ ドレスシューズ','イタリアの職人が丁寧に手作業で作り上げた一品です。',23800,'S000000002',3,1);

insert into catalog_item_assets (id,asset_code,catalog_item_id) values (1,'45c22ba3da064391baac91341067ffe9',1);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (2,'4aed07c4ed5d45a5b97f11acedfbb601',2);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (3,'082b37439ecc44919626ba00fc60ee85',3);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (4,'f5f89954281747fa878129c29e1e0f83',4);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (5,'a8291ef2e8e14869a7048e272915f33c',5);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (6,'66237018c769478a90037bd877f5fba1',6);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (7,'d136d4c81b86478990984dcafbf08244',7);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (8,'47183f32f6584d7fb661f9216e11318b',8);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (9,'cf151206efd344e1b86854f4aa49fdef',9);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (10,'ab2e78eb7fe3408aadbf1e17a9945a8c',10);
insert into catalog_item_assets (id,asset_code,catalog_item_id) values (11,'0e557e96bc054f10bc91c27405a83e85',11);
