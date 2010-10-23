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


-- wholesaler_store_vd
drop table IF EXISTS wholesaler_store_vd;

create table wholesaler_store_vd (
	wholesaler_key int not null, 
	store_key int not null,
	title_key int not null
);

CREATE INDEX wholesaler_store_vd_index1 USING BTREE ON wholesaler_store_vd (wholesaler_key, store_key, title_key);

insert into wholesaler_store_vd (wholesaler_key, store_key, title_key)
select distinct s.wholesaler_key, v.store_key, v.title_key
from template_vd v, sales_vd s
where v.store_key = s.store_key;


-- wholesaler_store_mo
drop table IF EXISTS wholesaler_store_mo;

create table wholesaler_store_mo (
	wholesaler_key int not null, 
	store_key int not null,
	title_key int not null
);

CREATE INDEX wholesaler_store_mo_index1 USING BTREE ON wholesaler_store_mo (wholesaler_key, store_key, title_key);

insert into wholesaler_store_mo (wholesaler_key, store_key, title_key)
select distinct s.wholesaler_key, v.store_key, v.title_key
from template_mo v, sales_mo s
where v.store_key = s.store_key;

-- year_month_sales_vd
drop table IF EXISTS year_month_sales_vd;

create table year_month_sales_vd (
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

CREATE INDEX year_month_sales_vd_index1 USING BTREE ON year_month_sales_vd (on_year, on_month);

insert into year_month_sales_vd (on_year, on_month, sales_total)
select vd.on_year, vd.on_month, avg(mo.sales_total)
from (select distinct on_year, on_month from template_vd2) vd, template_mo mo
where vd.on_year = mo.on_year and vd.on_month = mo.on_month
group by vd.on_year, vd.on_month;

-- title_year_month_sales_vd
drop table IF EXISTS title_year_month_sales_vd;

create table title_year_month_sales_vd (
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

CREATE INDEX title_year_month_sales_vd_index1 USING BTREE ON title_year_month_sales_vd (title_key, on_year, on_month);

insert into title_year_month_sales_vd(title_key, on_year, on_month, sales_total)
select vd.title_key, vd.on_year, vd.on_month, avg(mo.sales_total) 
from (select distinct title_key, on_year, on_month from template_vd2) vd,
template_mo mo
where mo.title_key=vd.title_key and mo.on_year=vd.on_year and mo.on_month=vd.on_month 
group by title_key, on_year, on_month

-- template_vd2_wholesaler
drop table IF EXISTS template_vd2_wholesaler;

create table template_vd2_wholesaler (
	wholesaler_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null
);

CREATE INDEX template_vd2_wholesaler_index1 USING BTREE ON template_vd2_wholesaler (wholesaler_key, title_key, on_year, on_month);

insert into template_vd2_wholesaler(wholesaler_key, title_key, on_year, on_month)
select distinct wholesaler_key, title_key, on_year, on_month
from template_vd2 INNER JOIN wholesaler_store_vd
using (title_key, store_key);

-- template_mo_wholesaler
drop table IF EXISTS template_mo_wholesaler;

create table template_mo_wholesaler (
	wholesaler_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

CREATE INDEX template_mo_wholesaler_index1 USING BTREE ON template_mo_wholesaler (wholesaler_key, title_key, on_year, on_month);

insert into template_mo_wholesaler(wholesaler_key, title_key, on_year, on_month, sales_total)
select w.wholesaler_key, title_key, on_year, on_month, avg(sales_total)
from template_mo INNER JOIN wholesaler_store_mo w
using (title_key, store_key)
group by wholesaler_key, title_key, on_year, on_month;

-- wholesaler_title_year_month_sales_vd


drop table IF EXISTS wholesaler_title_year_month_sales_vd;

create table wholesaler_title_year_month_sales_vd (
	wholesaler_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

CREATE INDEX wholesaler_title_year_month_sales_vd_index1 USING BTREE ON wholesaler_title_year_month_sales_vd (wholesaler_key, title_key, on_year, on_month);

insert into wholesaler_title_year_month_sales_vd(wholesaler_key, title_key, on_year, on_month, sales_total)
select t.wholesaler_key, t.title_key, t.on_year, t.on_month, m.sales_total
from template_vd2_wholesaler t
INNER JOIN template_mo_wholesaler m
using (wholesaler_key, title_key, on_year, on_month);
