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
group by title_key, on_year, on_month;

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

-- store_chain_vd
drop table if exists store_chain_vd;

create table store_chain_vd (
	store_key int not null,
	chain_key int not null
);

create index store_chain_vd_index1 using btree on store_chain_vd (store_key, chain_key);

insert into store_chain_vd(store_key, chain_key)
select distinct store_key, chain_key from template_vd inner join sales_vd using (store_key, title_key);

-- store_chain_mo
drop table if exists store_chain_mo;

create table store_chain_mo (
	store_key int not null,
	chain_key int not null
);

create index store_chain_mo_index1 using btree on store_chain_mo (store_key, chain_key)

insert into store_chain_mo(store_key, chain_key)
select distinct store_key, chain_key from sales_mo;

-- chain_title_year_month_sales_mo

drop table IF EXISTS chain_title_year_month_sales_mo;

create table chain_title_year_month_sales_mo (
	chain_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

CREATE INDEX chain_title_year_month_sales_mo_index1 USING BTREE ON chain_title_year_month_sales_mo (chain_key, title_key, on_year, on_month);

insert into chain_title_year_month_sales_mo(chain_key, title_key, on_year, on_month, sales_total)
select chain_key, title_key, on_year, on_month, avg(sales_total)
from template_mo t inner join store_chain_mo using (store_key)
group by chain_key, title_key, on_year, on_month;

-- chain_title_year_month_sales_td

drop table IF EXISTS chain_title_year_month_sales_td;

create table chain_title_year_month_sales_td (
	chain_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

CREATE INDEX chain_title_year_month_sales_td_index1 USING BTREE ON chain_title_year_month_sales_td (chain_key, title_key, on_year, on_month);

insert into chain_title_year_month_sales_td(chain_key, title_key, on_year, on_month, sales_total)
select distinct c.chain_key, t.title_key, t.on_year, t.on_month, mo.sales_total
from template_vd2 t, store_chain_vd c, chain_title_year_month_sales_mo mo
where c.store_key = t.store_key
and mo.title_key = t.title_key 
and mo.on_year = t.on_year
and mo.on_month = t.on_month
and mo.chain_key = c.chain_key;

-- template_storetype_mo
drop table if exists template_storetype_mo;

create table template_storetype_mo (
	store_type varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

create index template_storetype_mo_index1 using btree on template_storetype_mo (title_key, on_year, on_month)

insert into template_storetype_mo(store_type, title_key, on_year, on_month, sales_total)
select distinct or_cot_desc as store_type, title_key, on_year, on_month, avg(sales_total) as sales_total 
from template_mo, store_mo where template_mo.store_key=store_mo.store_key group by store_type, title_key, on_year, on_month;

-- template_state_mo
drop table if exists template_state_mo;

create table template_state_mo (
	state varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

create index template_state_mo_index1 using btree on template_state_mo (title_key, on_year, on_month);

insert into template_state_mo(state, title_key, on_year, on_month, sales_total)
select distinct state, title_key, on_year, on_month, avg(sales_total) as sales_total 
from template_mo, store_mo where template_mo.store_key=store_mo.store_key 
group by state, title_key, on_year, on_month;



-- template_city_mo
drop table if exists template_city_mo;

create table template_city_mo (
	city varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total float(18,9) not null default 0
);

create index template_city_mo_index1 on template_city_mo (city, title_key, on_year, on_month);

insert into template_city_mo(city, title_key, on_year, on_month, sales_total)
select distinct city, title_key, on_year, on_month, avg(sales_total)
from template_mo, store_mo where template_mo.store_key=store_mo.store_key 
group by city, title_key, on_year, on_month;


-- template_state_chain_mo
drop table if exists template_state_chain_mo;

create table template_state_chain_mo (
	state varchar(255) not null,
	chain_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

create index template_state_chain_mo_index1 on template_state_chain_mo (state, chain_key, title_key, on_year, on_month);

insert into template_state_chain_mo(state, chain_key, title_key, on_year, on_month, sales_total)
select distinct state, chain_key, title_key, on_year, on_month, avg(sales_total) as sales_total 
from template_mo, store_mo
where template_mo.store_key=store_mo.store_key 
group by state, chain_key, title_key, on_year, on_month;


-- template_state_storetype_mo
drop table if exists template_state_storetype_mo;

create table template_state_storetype_mo (
	state varchar(255) not null,
	store_type varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

create index template_state_storetype_mo_index1 on template_state_storetype_mo (state, store_type, title_key, on_year, on_month);

insert into template_state_storetype_mo(state, store_type, title_key, on_year, on_month, sales_total)
select distinct state, or_cot_desc as store_type, title_key, on_year, on_month, avg(sales_total) as sales_total 
from template_mo, store_mo
where template_mo.store_key=store_mo.store_key 
group by state, store_type, title_key, on_year, on_month;


-- template_city_chain_mo
drop table if exists template_city_chain_mo;

create table template_city_chain_mo (
	city varchar(255) not null,
	chain_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(18,9) not null default 0
);

create index template_city_chain_mo_index1 on template_city_chain_mo (city, chain_key, title_key, on_year, on_month);

insert into template_city_chain_mo(city, chain_key, title_key, on_year, on_month, sales_total)
select distinct city, chain_key, title_key, on_year, on_month, avg(sales_total)
from template_mo, store_mo
where template_mo.store_key=store_mo.store_key 
group by city, chain_key, title_key, on_year, on_month;

-- template_vd3_index_1
drop table IF EXISTS template_vd3;

create table template_vd3 (
	store_key int not null, 
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	chain_key int not null, 
	state varchar(255) not null,
	city varchar(255) not null,
	store_type varchar(255) not null,
	zip_code int not null
);

CREATE INDEX template_vd3_index_1 USING BTREE ON template_vd3 (store_key, title_key, on_year, on_month, chain_key, state, city, store_type, zip_code);

insert into template_vd3 (store_key, title_key, on_year, on_month, chain_key, state, city, store_type, zip_code)
select t.store_key, title_key, on_year, on_month, chain_key, state, city, or_cot_desc, zip_code
from template_vd2 t, store_vd s
where s.store_key=t.store_key;

-- template_city_chain_vd

drop table IF EXISTS template_city_chain_vd;

create table template_city_chain_vd (
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	chain_key int not null, 
	city varchar(255) not null,
	sales_total decimal(18,9) not null default 0
);

CREATE INDEX template_city_chain_vd_index1 ON template_city_chain_vd (title_key, on_year, on_month, chain_key, city, sales_total);

insert into template_city_chain_vd (title_key, on_year, on_month, chain_key, city, sales_total)
select distinct t.title_key, t.on_year, t.on_month, t.chain_key, t.city, c.sales_total
from template_vd3 t, template_city_chain_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month and t.chain_key=c.chain_key and t.city=c.city;


-- store_city_vd

drop table IF EXISTS store_city_vd;

create table store_city_vd (
	store_key int not null, 
	city varchar(255) not null
);

CREATE INDEX store_city_vd_index1 ON store_city_vd (store_key, city);

insert into store_city_vd(store_key, city)
select distinct store_key, city from template_vd3;



-- template_storetype_vd

drop table IF EXISTS template_storetype_vd;

create table template_storetype_vd (
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	store_type varchar(255) not null,
	sales_total decimal(18,9) not null default 0
);

CREATE INDEX template_storetype_vd_index1 ON template_storetype_vd (title_key, on_year, on_month, store_type, sales_total);

insert into template_storetype_vd (title_key, on_year, on_month, store_type, sales_total)
select distinct t.title_key, t.on_year, t.on_month, t.store_type, c.sales_total
from template_vd3 t, template_storetype_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month and t.store_type=c.store_type;

-- store_storetype_vd

drop table IF EXISTS store_storetype_vd;

create table store_storetype_vd (
	store_key int not null, 
	store_type varchar(255) not null
);

CREATE INDEX store_storetype_vd_index1 ON store_storetype_vd (store_key, store_type);

insert into store_storetype_vd(store_key, store_type)
select distinct store_key, store_type from template_vd3;

-- template_vd_store_neighbours_in_mo

drop table if exists template_vd_store_neighbours_in_mo;

create table template_vd_store_neighbours_in_mo(
	store_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	neighbour_store_key int not null 
);

CREATE INDEX template_vd_store_neighbours_in_mo_index1 ON template_vd_store_neighbours_in_mo (store_key, title_key, on_year, on_month, neighbour_store_key);

insert into template_vd_store_neighbours_in_mo(store_key, title_key, on_year, on_month, neighbour_store_key)
select distinct vd.store_key, vd.title_key, vd.on_year, vd.on_month, mo.store_key
from template_vd3 vd inner join template_mo mo
using (title_key, on_month, on_year);


