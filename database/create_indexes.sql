connect hearst_challenge;

-- sales_vd
CREATE INDEX title_key_index USING BTREE ON sales_vd (title_key);
CREATE INDEX issue_key_index USING BTREE ON sales_vd (issue_key);
CREATE INDEX wholesaler_key_index USING BTREE ON sales_vd (wholesaler_key);
CREATE INDEX wholesaler_store_key_index USING BTREE ON sales_vd (store_key, wholesaler_key);
CREATE INDEX sales_vd_index1 USING BTREE ON sales_vd (store_key, title_key);

-- issue_mo
CREATE INDEX issue_key_index USING BTREE ON issue_mo (issue_key);

-- sales_mo
CREATE INDEX title_key_index USING BTREE ON sales_mo (title_key);
CREATE INDEX issue_key_index USING BTREE ON sales_mo (issue_key);
CREATE INDEX store_key_index USING BTREE ON sales_mo (store_key);
CREATE INDEX wholesaler_key_index USING BTREE ON sales_mo (wholesaler_key);
CREATE INDEX wholesaler_store_key_index USING BTREE ON sales_mo (store_key, wholesaler_key);
CREATE INDEX chain_key_index USING BTREE ON sales_mo (chain_key);


-- template_vd
CREATE INDEX template_td_key_index_1 USING BTREE ON template_vd (store_key, title_key, yearmonth);
CREATE INDEX template_td_key_index_2 USING BTREE ON template_vd (store_key, title_key);