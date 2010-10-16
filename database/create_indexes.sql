
-- sales_vd
CREATE INDEX title_key_index USING BTREE ON sales_vd (title_key);
CREATE INDEX issue_key_index USING BTREE ON sales_vd (issue_key);



-- sales_mo
CREATE INDEX title_key_index USING BTREE ON sales_mo (title_key);
CREATE INDEX issue_key_index USING BTREE ON sales_mo (issue_key);