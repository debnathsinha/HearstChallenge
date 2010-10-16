#!/bin/sh

mysql -u root < create_tables.sql
mysql -u root < create_indexes.sql

mysql -u root < sales_vd_inserts.sql
mysql -u root < store_vd_inserts.sql
mysql -u root < template_vd_inserts.sql

mysql -u root < issue_mo_inserts.sql
mysql -u root < sales_mo_inserts.sql
mysql -u root < store_mo_inserts.sql
mysql -u root < wholesaler_mo_inserts.sql

mysql -u root < zip_plus4_data1_inserts.sql
mysql -u root < zip_plus4_data2_inserts.sql
mysql -u root < zip_plus4_data3_inserts.sql
mysql -u root < zip_plus4_data4_inserts.sql
mysql -u root < zip_plus4_data5_inserts.sql

mysql -u root < create_secondary_tables.sql
