-- working queries...

select * from issue_mo
where month(on_sale_date) <> month(off_sale_date);



select smo.store_key as store_key,
	smo.title_key as title_key,
	year(imo.on_sale_date) as sale_year,
	month(imo.on_sale_date) as sale_month,
	sum(smo.sales) as sales_total
from issue_mo imo, sales_mo smo
where smo.issue_key = imo.issue_key
group by smo.store_key, smo.title_key, year(imo.on_sale_date), month(imo.on_sale_date);



create table issue_mo_2 (
	issue_key int not null primary key,
	on_year int not null,
	on_month int not null
);

CREATE INDEX issue_key_index USING BTREE ON issue_mo_2 (issue_key, on_year, on_month);

insert into issue_mo_2 (issue_key, on_year, on_month)
select i.issue_key, year(i.on_sale_date), month(i.on_sale_date) 
from issue_mo i;

select count(*) from issue_mo_2;

create table template_mo (
	store_key int not null, 
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	sales_total int  not null
);

CREATE INDEX template_mo_key_index USING BTREE ON template_mo (store_key, title_key, on_year, on_month);

insert into template_mo (store_key, title_key, on_year, on_month, sales_total)
select smo.store_key, smo.title_key, imo.on_year, imo.on_month, sum(smo.sales)
from issue_mo_2 imo, sales_mo smo
where smo.issue_key = imo.issue_key
group by smo.store_key, smo.title_key, imo.on_year, imo.on_month;


select distinct(store_key) from template_mo order by store_key;

select count(*) from template_mo;

-- 5
select smo.store_key as store_key,
	smo.title_key as title_key,
	imo.on_year as sale_year,
	imo.on_month as sale_month,
	smo.sales
from issue_mo_2 imo, sales_mo smo
where smo.issue_key=imo.issue_key and smo.store_key=2 and title_key=1 and on_year=2007 and on_month=8;

drop table template_td2;

drop table template_vd2;

create table template_vd2 (
	store_key int not null, 
	title_key int not null, 
	on_year int not null,
	on_month int not null,
	sales_total int
);

CREATE INDEX template_td2_key_index USING BTREE ON template_vd2 (store_key, title_key, on_year, on_month);

insert into template_vd2 (store_key, title_key, on_year, on_month, sales_total)
select store_key, title_key, CONVERT(substring(yearmonth, 1, 4), SIGNED), CONVERT(substring(yearmonth, 5, 2), SIGNED), sales_total
from template_vd;

select count(*) from template_vd2;


select vd.* 
from template_mo mo, template_vd2 vd
where mo.store_key=vd.store_key 
and mo.title_key=vd.title_key
and mo.on_year=vd.on_year
and mo.on_month=vd.on_month

select distinct(store_key) from template_mo order by store_key;

select distinct(store_key) from template_vd2 order by store_key;


-- can we average sales of the issue across all stores and apply it to a new store?

select mo.title_key as title_key, mo.on_year as on_year, mo.on_month as on_month, avg(mo.sales_total) as sales_total
from template_mo mo, template_vd2 vd
where mo.title_key=vd.title_key
and mo.on_year=vd.on_year
and mo.on_month=vd.on_month
and mo.on_year=2008 and mo.on_month=01 and mo.title_key<3;
group by title_key, on_year, on_month


select avg(sales_total) from template_mo;

desc template_mo;

select sum(c) from
(select title_key, on_year, on_month, count(store_key) as c
from template_vd2
group by title_key, on_year, on_month
having c > 0) as a;

select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month;

select count(*) from (select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month) as abc;


select mo.title_key as title_key, mo.on_year as on_year, mo.on_month as on_month, avg(mo.sales_total) as sales_total
from template_mo mo, (select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month) as vd
where mo.title_key=vd.title_key
and mo.on_year=vd.on_year
and mo.on_month=vd.on_month
and mo.on_year=2008 and mo.on_month=01 and mo.title_key<5
group by title_key, on_year, on_month
having sales_total = 0;


select count(*) from
(select mo.title_key as title_key, mo.on_year as on_year, mo.on_month as on_month, avg(mo.sales_total) as sales_total 
from template_mo mo, (select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month) as vd 
where mo.title_key=vd.title_key and mo.on_year=vd.on_year and mo.on_month=vd.on_month 
group by title_key, on_year, on_month) as a


select count(*) from

select * 
from (select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month) as vd
where (title_key, on_year, on_month) not in 
(select title_key, on_year, on_month from template_mo group by title_key, on_year, on_month) 

desc sales_mo;

CREATE INDEX wholesaler_key_index USING BTREE ON sales_mo (wholesaler_key);

CREATE INDEX wholesaler_key_index USING BTREE ON sales_vd (wholesaler_key);

select count(distinct(wholesaler_key)) from sales_mo;

select count(distinct(wholesaler_key)) from sales_vd;

select vd.wholesaler_key 
from (select distinct(wholesaler_key) from sales_vd) vd 
where vd.wholesaler_key not in (select distinct(wholesaler_key) from sales_mo)

select count(distinct(s.store_key)) from sales_vd s

select count(distinct(s.store_key))
from sales_vd s,
(select vd.wholesaler_key from (select distinct(wholesaler_key) from sales_vd) vd where vd.wholesaler_key not in (select distinct(wholesaler_key) from sales_mo)) d
where s.wholesaler_key = d.wholesaler_key


select count(*) from
(select t.title_key as title_key, t.on_year as on_year, t.on_month as on_month, s.wholesaler_key as wholesaler_key
from template_vd2 t, sales_vd s, (select distinct(wholesaler_key) from sales_mo) d
where t.store_key = s.store_key
and s.wholesaler_key = d.wholesaler_key
group by title_key, on_year, on_month, wholesaler_key) as abc


select t.title_key as title_key, t.on_year as on_year, t.on_month as on_month, avg(mo.sales_total) as sales_total 
from template_vd2 t, sales_vd s, (select distinct(k.wholesaler_key) from sales_mo k) d, template_mo mo, sales_mo smo
where t.store_key = s.store_key
and s.wholesaler_key = d.wholesaler_key
and mo.title_key = t.title_key
and mo.on_year = t.on_year
and mo.on_month = t.on_month
and mo.store_key = smo.store_key
and smo.wholesaler_key = s.wholesaler_key
group by s.wholesaler_key, t.title_key, t.on_year, t.on_month;

drop table template_mo;

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


select * from template_mo;

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


-- 3
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
select s.wholesaler_key, v.store_key, v.title_key, 
CONVERT(substring(v.yearmonth, 1, 4), SIGNED) as on_year , CONVERT(substring(v.yearmonth, 5, 2), SIGNED) as on_month, v.sales_total
from template_vd v, sales_vd s
where v.store_key = s.store_key
group by s.wholesaler_key, v.store_key, v.title_key, on_year, on_month;

insert into template_vd3 (wholesaler_key, store_key, title_key, on_year, on_month, sales_total)
select s.wholesaler_key, v.store_key, v.title_key, v.on_year, v.on_month, v.sales_total
from template_vd2 v, sales_vd s
where v.store_key = s.store_key
group by s.wholesaler_key, store_key, title_key, on_year, on_month;


select count(*) from template_mo;

select count(*) from template_vd;

desc template_vd

desc template_vd2

select count(*) from template_vd2;

select count(*) from template_vd3;


drop table template_vd3;

CREATE INDEX template_td_key_index_1 USING BTREE ON template_vd (store_key, title_key, yearmonth);


-- average sales by a stores wholesaler 
-- TODO re-test the assumptions again, for both approaches...

drop table IF EXISTS wholesaler_store_vd;

create table wholesaler_store_vd (
	wholesaler_key int not null, 
	store_key int not null,
	title_key int not null
);

CREATE INDEX wholesaler_store_vd_index1 USING BTREE ON wholesaler_store_vd (wholesaler_key, store_key, title_key);

CREATE INDEX wholesaler_store_key_index USING BTREE ON sales_mo (store_key, wholesaler_key);

desc sales_vd

CREATE INDEX wholesaler_store_key_index USING BTREE ON sales_vd (store_key, wholesaler_key);


select count(distinct s.wholesaler_key, v.store_key, v.title_key)
from template_vd v, sales_vd s
where v.store_key = s.store_key


insert into wholesaler_store_vd (wholesaler_key, store_key, title_key)
select distinct s.wholesaler_key, v.store_key, v.title_key
from template_vd v, sales_vd s
where v.store_key = s.store_key;


select count(*) from wholesaler_store_vd

select count(distinct wholesaler_key) from wholesaler_store_vd

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


select count(*) from wholesaler_store_vd

select count(distinct wholesaler_key) from wholesaler_store_vd

select count(distinct store_key) from wholesaler_store_vd

select count(distinct store_key, title_key) from wholesaler_store_vd



-- 22, 67
select vd.wholesaler_key from (select distinct(wholesaler_key) from wholesaler_store_vd) vd 
where vd.wholesaler_key not in (select distinct(wholesaler_key) from wholesaler_store_mo);

select count(distinct store_key) from wholesaler_store_vd where wholesaler_key in (22, 67)

select count(distinct store_key, title_key) from wholesaler_store_vd where wholesaler_key in (22, 67)

select count(distinct store_key) from wholesaler_store_vd

select avg(a.c) from (select count(store_key) as c from wholesaler_store_vd group by wholesaler_key) a

select avg(a.c) from (select count(wholesaler_key) as c from wholesaler_store_vd group by store_key) a

select count(c) from (select count(wholesaler_key) as c from wholesaler_store_vd group by store_key having c>1) as a

select count(c) from (select count(wholesaler_key) as c from wholesaler_store_mo group by store_key having c>1) as a

select count(*) from template_vd2

select count(*)
from template_vd2 t, wholesaler_store_vd w
where t.store_key = w.store_key

desc template_vd2

select count(*) from template_vd2

select count(distinct title_key, on_year, on_month) from template_vd2

select count(distinct t.title_key, t.on_year, t.on_month, w.wholesaler_key) 
from template_vd2 t, wholesaler_store_vd w
where w.title_key = t.title_key 
and w.store_key = t.store_key

select count(*)
from 
(select distinct t.title_key, t.on_year, t.on_month, w.wholesaler_key
from template_vd2 t, wholesaler_store_vd w
where w.title_key = t.title_key 
and w.store_key = t.store_key) a_vd
NATURAL JOIN 
(select distinct t.title_key, t.on_year, t.on_month, w.wholesaler_key
from template_mo t, wholesaler_store_mo w
where w.title_key = t.title_key 
and w.store_key = t.store_key) a_mo



select vd.title_key, vd.on_year, vd.on_month, avg(mo.sales_total) 
from
(select distinct t.title_key, t.on_year, t.on_month, w.wholesaler_key
from template_vd2 t, wholesaler_store_vd w
where w.title_key = t.title_key 
and w.store_key = t.store_key) vd,
(select t.title_key, t.on_year, t.on_month, t.sales_total,  w.wholesaler_key
from template_mo t, wholesaler_store_mo w
where w.title_key = t.title_key 
and w.store_key = t.store_key) mo
where vd.wholesaler_key=mo.wholesaler_key and vd.title_key=mo.title_key and vd.on_year=mo.on_year and vd.on_month=mo.on_month
group by vd.wholesaler_key, vd.title_key, vd.on_year, vd.on_month


-- average sales for year/month 

select distinct on_year, on_month from template_vd2

select count(*) 
from
(select distinct on_year, on_month from template_vd2) vd
natural join
(select distinct on_year, on_month from template_mo) mo


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


select * from year_month_sales_vd



-- title year month sales

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

select * from title_year_month_sales_vd


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

select * from template_vd2_wholesaler




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


select * from template_mo_wholesaler



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

select * from wholesaler_title_year_month_sales_vd



-- chain investigations

CREATE INDEX template_td_key_index_2 USING BTREE ON template_vd (store_key, title_key);

CREATE INDEX sales_vd_index1 USING BTREE ON sales_vd (store_key, title_key);

CREATE INDEX chain_key_index USING BTREE ON sales_mo (chain_key);

select count(distinct chain_key) from sales_vd inner join template_vd using (store_key, title_key);

select count(distinct chain_key) from sales_vd inner join sales_mo using (chain_key);

select count(*) from (select distinct chain_key from sales_vd) vd where chain_key not in (select distinct chain_key from sales_mo); 

-- chain/title/year/month mo data

select * from (select store_key, count(distinct chain_key) as c from sales_mo group by store_key) where c>1

-- store_chain_vd
drop table if exists store_chain_vd;

create table store_chain_vd (
	store_key int not null,
	chain_key int not null
);

create index store_chain_vd_index1 using btree on store_chain_vd (store_key, chain_key)

insert into store_chain_vd(store_key, chain_key)
select distinct store_key, chain_key from template_vd inner join sales_vd using (store_key, title_key);

select count(*) from store_chain_vd;

-- store_chain_mo
drop table if exists store_chain_mo;

create table store_chain_mo (
	store_key int not null,
	chain_key int not null
);

create index store_chain_mo_index1 using btree on store_chain_mo (store_key, chain_key)

insert into store_chain_mo(store_key, chain_key)
select distinct store_key, chain_key from sales_mo;

select count(*) from store_chain_mo;

-- chain/title/year/month mo data


select chain_key, t.title_key, on_year, on_month, avg(sales_total)
from template_mo t inner join sales_mo using (store_key)
group by chain_key, title_key, on_year, on_month


-- exploring

select count(distinct city) from store_vd

select count(distinct zip_code) from store_vd

select count(distinct city) from store_mo


select count(distinct v.city) from store_vd v inner join store_mo using(city)

select count(distinct store_key) from template_vd2

select count(distinct v.store_key) from template_vd2 v inner join store_vd using(store_key)

select distinct store_key from template_vd2

CREATE INDEX store_vd_index_1 USING BTREE ON store_vd (store_key);

CREATE INDEX store_mo_index_1 USING BTREE ON store_mo (store_key);

select  t.store_key, t.title_key, t.on_year, t.on_month, s.city, s.or_cot_desc, s.zip_code, s.state
from template_vd2 t, store_vd s
where s.store_key = t.store_key



select count(*) from chain_title_year_month_sales_mo where on_year = 2009;

select * from template_mo

select c.chain_key, t.* from template_mo t, store_chain_mo c where t.store_key=c.store_key;

select * from template_mo

select * from template_mo_wholesaler

select * from wholesaler_store_mo

select count(*) from template_mo_wholesaler where on_year = 2009;

select count(*) from template_mo where on_year = 2009;

-- template_storetype_mo
drop table if exists template_storetype_mo;

create table template_storetype_mo (
	store_type varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

create index template_storetype_mo_index1 using btree on template_storetype_mo (title_key, on_year, on_month)

insert into template_storetype_mo(store_type, title_key, on_year, on_month, sales_total)
select distinct or_cot_desc as store_type, title_key, on_year, on_month, avg(sales_total) as sales_total 
from template_mo, store_mo where template_mo.store_key=store_mo.store_key 
group by store_type, title_key, on_year, on_month

select * from template_storetype_mo


-- template_city_mo
drop table if exists template_city_mo;

create table template_city_mo (
	city varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(10,10) not null default 0
);

create index template_city_mo_index1 using btree on template_city_mo (title_key, on_year, on_month)

insert into template_city_mo(city, title_key, on_year, on_month, sales_total)
select distinct city, title_key, on_year, on_month, round(avg(sales_total),10) as sales_total 
from template_mo, store_mo where template_mo.store_key=store_mo.store_key 
group by city, title_key, on_year, on_month;

select * from template_city_mo

select distinct city, title_key, on_year, on_month, count(*), avg(sales_total)
from template_mo, store_mo where template_mo.store_key=store_mo.store_key 
group by city, title_key, on_year, on_month;



-- template_state_mo
drop table if exists template_state_mo;

create table template_state_mo (
	state varchar(255) not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

create index template_state_mo_index1 using btree on template_state_mo (title_key, on_year, on_month)

insert into template_state_mo(state, title_key, on_year, on_month, sales_total)
select distinct state, title_key, on_year, on_month, avg(sales_total) as sales_total 
from template_mo, store_mo where template_mo.store_key=store_mo.store_key 
group by state, title_key, on_year, on_month;


-- template_state_chain_mo
drop table if exists template_state_chain_mo;

create table template_state_chain_mo (
	state varchar(255) not null,
	chain_key int not null,
	title_key int not null,
	on_year int not null,
	on_month int not null,
	sales_total decimal(8,6) not null default 0
);

create index template_state_chain_mo_index1 on template_state_chain_mo (state, chain_key, title_key, on_year, on_month)

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

create index template_state_storetype_mo_index1 on template_state_storetype_mo (state, store_type, title_key, on_year, on_month)

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

create index template_city_chain_mo_index1 on template_city_chain_mo (city, chain_key, title_key, on_year, on_month)

insert into template_city_chain_mo(city, chain_key, title_key, on_year, on_month, sales_total)
select distinct city, chain_key, title_key, on_year, on_month, avg(sales_total)
from template_mo, store_mo
where template_mo.store_key=store_mo.store_key 
group by city, chain_key, title_key, on_year, on_month;



--
-- Variance experiments in 2009 model data

-- chain/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from chain_title_year_month_sales_mo c, template_mo t, store_chain_mo x
where c.on_year = 2009
and x.store_key=t.store_key 
and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month and c.chain_key=x.chain_key

-- wholesaler/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo_wholesaler c, template_mo t
where c.on_year = 2009
and c.title_key = t.title_key and c.on_year = t.on_year and c.on_month = t.on_month and c.wholesaler_key=t.wholesaler_key

-- year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, (select distinct on_year, on_month, avg(sales_total) as sales_total from template_mo where on_year = 2009 group by on_year, on_month) t
where c.on_year = 2009
and c.on_year = t.on_year and c.on_month = t.on_month

-- title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, (select distinct title_key, on_year, on_month, avg(sales_total) as sales_total from template_mo where on_year = 2009 group by title_key, on_year, on_month) t
where c.on_year = 2009
and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month

-- store_type/title/year/month
select count(abs(t.sales_total - c.sales_total))
from template_mo c, template_storetype_mo t, store_mo m
where c.on_year = 2009
and m.store_key=c.store_key 
and m.or_cot_desc=t.store_type and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month

-- city/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, template_city_mo t, store_mo m
where c.on_year = 2009
and m.store_key=c.store_key 
and m.city=t.city and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month;

-- state/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, template_state_mo t, store_mo m
where c.on_year = 2009
and m.store_key=c.store_key 
and m.state=t.state and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month;

-- state/chain/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, template_state_chain_mo t, store_mo m
where c.on_year = 2009
and m.store_key=c.store_key 
and m.state=t.state and m.chain_key=t.chain_key and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month;

-- state/storetype/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, template_state_storetype_mo t, store_mo m
where c.on_year = 2009
and m.store_key=c.store_key 
and m.state=t.state and m.or_cot_desc=t.store_type and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month;

-- city/chain/title/year/month
select avg(abs(t.sales_total - c.sales_total))
from template_mo c, template_city_chain_mo t, store_mo m
where c.on_year = 2009
and m.store_key=c.store_key 
and m.city=t.city and m.chain_key=t.chain_key and c.title_key=t.title_key and c.on_year=t.on_year and c.on_month=t.on_month;



-- experiment submission

-- template_vd3

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

select count(*) from template_vd3 t

select * from template_vd3 t

select distinct t.title_key, t.on_year, t.on_month, t.chain_key, t.city, c.sales_total
from template_vd3 t, template_city_chain_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month and t.chain_key=c.chain_key and t.city=c.city

select distinct t.title_key, t.on_year, t.on_month, t.city, c.sales_total
from template_vd3 t, template_city_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month and t.city=c.city

select distinct t.title_key, t.on_year, t.on_month, t.state, c.sales_total
from template_vd3 t, template_state_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month and t.state=c.state

select distinct t.title_key, t.on_year, t.on_month, t.store_type, c.sales_total
from template_vd3 t, template_storetype_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.store_type=c.store_type

select distinct on_year from template_mo

select distinct on_year from template_vd3

select distinct t.title_key, t.on_year, t.on_month, t.city, c.sales_total
from template_vd3 t, template_city_mo c
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month

select * from template_city_chain_mo


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
where t.title_key=c.title_key and t.on_year=c.on_year and t.on_month=c.on_month and t.chain_key=c.chain_key and t.city=c.city

select * from template_city_chain_vd


-- store_city_vd

drop table IF EXISTS store_city_vd;

create table store_city_vd (
	store_key int not null, 
	city varchar(255) not null
);

CREATE INDEX store_city_vd_index1 ON store_city_vd (store_key, city);

insert into store_city_vd(store_key, city)
select distinct store_key, city from template_vd3;
