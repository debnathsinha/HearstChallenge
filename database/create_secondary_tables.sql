-- additional derived tables and views

connect hearst_challenge;

-- issue_mo_2
drop table IF EXISTS issue_mo_2;
create table issue_mo_2 (
	issue_key int not null primary key,
	on_year int not null,
	on_month int not null
);

CREATE INDEX issue_key_index USING BTREE ON issue_mo_2 (issue_key, on_year, on_month);

insert into issue_mo_2 (issue_key, on_year, on_month)
select i.issue_key, year(i.on_sale_date), month(i.on_sale_date) 
from issue_mo i;


-- template_mo
drop table IF EXISTS template_mo;
create table template_mo (
	wholesaler_key int not null, 
	store_key int not null, 
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	sales_total int  not null
);

CREATE INDEX template_mo_key_index_1 USING BTREE ON template_mo (store_key, title_key, on_year, on_month);
CREATE INDEX template_mo_key_index_2 USING BTREE ON template_mo (wholesaler_key, store_key, title_key, on_year, on_month);

insert into template_mo (wholesaler_key, store_key, title_key, on_year, on_month, sales_total)
select smo.wholesaler_key, smo.store_key, smo.title_key, imo.on_year, imo.on_month, sum(smo.sales)
from issue_mo_2 imo, sales_mo smo
where smo.issue_key = imo.issue_key
group by smo.store_key, smo.title_key, imo.on_year, imo.on_month;


-- template_td2
drop table IF EXISTS template_vd2;
create table template_vd2 (
	store_key int not null, 
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	sales_total int
);

CREATE INDEX template_td2_key_index_1 USING BTREE ON template_vd2 (store_key, title_key, on_year, on_month);

insert into template_vd2 (store_key, title_key, on_year, on_month, sales_total)
select store_key, title_key, CONVERT(substring(yearmonth, 1, 4), SIGNED), CONVERT(substring(yearmonth, 5, 2), SIGNED), sales_total
from template_vd;


-- template_vd3
drop table IF EXISTS template_vd3;

create table template_vd3 (
	wholesaler_key int, 
	store_key int not null, 
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	sales_total int
);

CREATE INDEX template_td2_key_index_1 USING BTREE ON template_vd3 (store_key, title_key, on_year, on_month);

CREATE INDEX template_td2_key_index_2 USING BTREE ON template_vd3 (wholesaler_key, store_key, title_key, on_year, on_month);

insert into template_vd3 (wholesaler_key, store_key, title_key, on_year, on_month, sales_total)
select s.wholesaler_key, v.store_key, v.title_key, v.on_year, v.on_month, v.sales_total
from template_vd2 v, sales_vd s
where v.store_key = s.store_key
group by s.wholesaler_key, store_key, title_key, on_year, on_month;

