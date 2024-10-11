INSERT INTO assets (asset_code, asset_type)
VALUES 
    ('b52dc7f712d94ca5812dd995bf926c04', 'png'),
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
    
INSERT INTO catalog_brands (id, name) 
VALUES 
    (1, '高級なブランド'),
    (2, 'カジュアルなブランド'),
    (3, 'ノーブランド');
ALTER TABLE catalog_brands ALTER COLUMN id RESTART WITH 4;

INSERT INTO catalog_categories (id, name) 
VALUES 
    (1, '服'),
    (2, 'バッグ'),
    (3, 'シューズ');
ALTER TABLE catalog_categories ALTER COLUMN id RESTART WITH 4;

INSERT INTO catalog_items (id, name, description, price, product_code, catalog_category_id, catalog_brand_id, row_version) 
VALUES 
    (1, 'クルーネック Tシャツ - ブラック', '定番の無地ロングTシャツです。', 1980, 'C000000001', 1, 3, 1),
    (2, '裏起毛 スキニーデニム', '暖かいのに着膨れしない起毛デニムです。', 4800, 'C000000002', 1, 2, 1),
    (3, 'ウールコート', 'あたたかく肌ざわりも良いウール100%のロングコートです。', 49800, 'C000000003', 1, 1, 1),
    (4, '無地 ボタンダウンシャツ', 'コットン100%の柔らかい着心地で、春先から夏、秋口まで万能に使いやすいです。', 2800, 'C000000004', 1, 2, 1),
    (5, 'レザーハンドバッグ', 'コンパクトサイズのバッグですが収納力は抜群です。', 18800, 'B000000001', 2, 3, 1),
    (6, 'ショルダーバッグ', 'エイジング加工したレザーを使用しています。', 38000, 'B000000002', 2, 2, 1),
    (7, 'トートバッグ ポーチ付き', '春の季節にぴったりのトートバッグです。インナーポーチまたは単体でも使用可能なポーチ付。', 24800, 'B000000003', 2, 3, 1),
    (8, 'ショルダーバッグ', 'さらりと気軽に纏える、キュートなミニサイズショルダー。', 2800, 'B000000004', 2, 1, 1),
    (9, 'レザー チェーンショルダーバッグ', 'エレガントな雰囲気を放つキルティングデザインです。', 258000, 'B000000005', 2, 1, 1),
    (10, 'ランニングシューズ - ブルー', '柔らかいソールは快適な履き心地で、ランニングに最適です。', 12800, 'S000000001', 3, 2, 1),
    (11, 'メダリオン ストレートチップ ドレスシューズ', 'イタリアの職人が丁寧に手作業で作り上げた一品です。', 23800, 'S000000002', 3, 1, 1);
ALTER TABLE catalog_items ALTER COLUMN id RESTART WITH 12;

INSERT INTO catalog_item_assets (id, asset_code, catalog_item_id) 
VALUES 
    (1, '45c22ba3da064391baac91341067ffe9', 1),
    (2, '4aed07c4ed5d45a5b97f11acedfbb601', 2),
    (3, '082b37439ecc44919626ba00fc60ee85', 3),
    (4, 'f5f89954281747fa878129c29e1e0f83', 4),
    (5, 'a8291ef2e8e14869a7048e272915f33c', 5),
    (6, '66237018c769478a90037bd877f5fba1', 6),
    (7, 'd136d4c81b86478990984dcafbf08244', 7),
    (8, '47183f32f6584d7fb661f9216e11318b', 8),
    (9, 'cf151206efd344e1b86854f4aa49fdef', 9),
    (10, 'ab2e78eb7fe3408aadbf1e17a9945a8c', 10),
    (11, '0e557e96bc054f10bc91c27405a83e85', 11);
ALTER TABLE catalog_item_assets ALTER COLUMN id RESTART WITH 12;
