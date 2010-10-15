# basic analysis

require "rubygems"
require "active_record"

# utils 

def print_size(name, size)
  printf("| %s | %i |\n", name, size)
end

def print_number(name, size)
  printf("| %s | %.3f |\n", name, size)
end

def print_heading(headings)
  puts "|_. #{headings.join(" |_. ")} |"
end

# database

ActiveRecord::Base.establish_connection(
  :adapter => "mysql",
  :host => "localhost",
  :username => "root",
  :database => "hearst_challenge")

class TemplateVd < ActiveRecord::Base
  set_table_name "template_vd"
end

class SalesVd < ActiveRecord::Base
  set_table_name "sales_vd"
end

class SalesMo < ActiveRecord::Base
  set_table_name "sales_mo"
end


def print_template_vd_stats  
  puts "validation data info"
  print_heading(["Measures", "Values"])
  # totals
  print_size("Total Records", TemplateVd.count)
  print_size("Total Stores", TemplateVd.calculate(:count, :store_key, :distinct=>true))
  print_size("Total Titles", TemplateVd.calculate(:count, :title_key, :distinct=>true))
  print_size("Total Months", TemplateVd.calculate(:count, :yearmonth, :distinct=>true))  
  # month stats
  print_size("Average Records/Month", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from template_vd group by yearmonth) as t"))
  print_size("Average Stores/Month", TemplateVd.count_by_sql("select avg(c) from (select count(distinct(store_key)) as c from template_vd group by yearmonth) as t"))
  print_size("Average Titles/Month", TemplateVd.count_by_sql("select avg(c) from (select count(distinct(title_key)) as c from template_vd group by yearmonth) as t"))
  # store stats
  print_size("Average Records/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from template_vd group by store_key) as t"))
  print_size("Average Titles/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from template_vd group by store_key) as t"))
  # title stats
  print_size("Average Records/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from template_vd group by title_key) as t"))
  print_size("Average Stores/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from template_vd group by title_key) as t"))
  puts ""
end


def print_sales_vd_stats  
  puts "validation data info"
  print_heading(["Measures", "Values"])
  # totals
  print_size("Total Records", SalesVd.count)
  print_size("Total Wholesalers", SalesVd.calculate(:count, :wholesaler_key, :distinct=>true))
  print_size("Total Chains", SalesVd.calculate(:count, :chain_key, :distinct=>true))
  print_size("Total Stores", SalesVd.calculate(:count, :store_key, :distinct=>true))
  print_size("Total Issues", SalesVd.calculate(:count, :issue_key, :distinct=>true))
  print_size("Total Titles", SalesVd.calculate(:count, :title_key, :distinct=>true))
  
  # title stats
  print_size("Average Records/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_vd group by title_key) as t"))
  print_size("Average Issues/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_vd group by title_key) as t"))
  print_size("Average Stores/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_vd group by title_key) as t"))
  print_size("Average Chains/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_vd group by title_key) as t"))
  print_size("Average Wholesalers/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_vd group by title_key) as t"))
  # issue stats
    print_size("Average Records/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_vd group by issue_key) as t"))
  print_size("Average Wholesalers/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_vd group by issue_key) as t"))
  print_size("Average Chains/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_vd group by issue_key) as t"))
  print_size("Average Stores/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_vd group by issue_key) as t"))
  print_size("Average Titles/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_vd group by issue_key) as t"))
  # store stats
  print_size("Average Records/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_vd group by store_key) as t"))
  print_size("Average Wholesalers/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_vd group by store_key) as t"))
  print_size("Average Chains/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_vd group by store_key) as t"))
  print_size("Average Issues/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_vd group by store_key) as t"))
  print_size("Average Titles/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_vd group by store_key) as t"))  
  # chain stats
  print_size("Average Records/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_vd group by chain_key) as t"))
  print_size("Average Wholesalers/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_vd group by chain_key) as t"))
  print_size("Average Stores/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_vd group by chain_key) as t"))
  print_size("Average Issues/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_vd group by chain_key) as t"))
  print_size("Average Titles/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_vd group by chain_key) as t"))    
  # wholesaler stats
  print_size("Average Records/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_vd group by wholesaler_key) as t"))
  print_size("Average Store/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_vd group by wholesaler_key) as t"))
  print_size("Average Chains/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_vd group by wholesaler_key) as t"))
  print_size("Average Issues/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_vd group by wholesaler_key) as t"))
  print_size("Average Titles/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_vd group by wholesaler_key) as t"))   
  
  puts ""
end

def print_sales_mo_stats  
  puts "validation data info"
  print_heading(["Measures", "Values"])
  # totals
  print_size("Total Records", SalesMo.count)
  print_size("Total Wholesalers", SalesMo.calculate(:count, :wholesaler_key, :distinct=>true))
  print_size("Total Chains", SalesMo.calculate(:count, :chain_key, :distinct=>true))
  print_size("Total Stores", SalesMo.calculate(:count, :store_key, :distinct=>true))
  print_size("Total Issues", SalesMo.calculate(:count, :issue_key, :distinct=>true))
  print_size("Total Titles", SalesMo.calculate(:count, :title_key, :distinct=>true))
  
  # title stats
  print_size("Average Records/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_mo group by title_key) as t"))
  print_size("Average Issues/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_mo group by title_key) as t"))
  print_size("Average Stores/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_mo group by title_key) as t"))
  print_size("Average Chains/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_mo group by title_key) as t"))
  print_size("Average Wholesalers/Title", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_mo group by title_key) as t"))
  # issue stats
    print_size("Average Records/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_mo group by issue_key) as t"))
  print_size("Average Wholesalers/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_mo group by issue_key) as t"))
  print_size("Average Chains/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_mo group by issue_key) as t"))
  print_size("Average Stores/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_mo group by issue_key) as t"))
  print_size("Average Titles/Issue", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_mo group by issue_key) as t"))
  # store stats
  print_size("Average Records/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_mo group by store_key) as t"))
  print_size("Average Wholesalers/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_mo group by store_key) as t"))
  print_size("Average Chains/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_mo group by store_key) as t"))
  print_size("Average Issues/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_mo group by store_key) as t"))
  print_size("Average Titles/Store", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_mo group by store_key) as t"))  
  # chain stats
  print_size("Average Records/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_mo group by chain_key) as t"))
  print_size("Average Wholesalers/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(wholesaler_key)) as c from sales_mo group by chain_key) as t"))
  print_size("Average Stores/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_mo group by chain_key) as t"))
  print_size("Average Issues/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_mo group by chain_key) as t"))
  print_size("Average Titles/Chain", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_mo group by chain_key) as t"))    
  # wholesaler stats
  print_size("Average Records/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from sales_mo group by wholesaler_key) as t"))
  print_size("Average Store/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(store_key)) as c from sales_mo group by wholesaler_key) as t"))
  print_size("Average Chains/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(chain_key)) as c from sales_mo group by wholesaler_key) as t"))
  print_size("Average Issues/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(issue_key)) as c from sales_mo group by wholesaler_key) as t"))
  print_size("Average Titles/Wholesaler", TemplateVd.count_by_sql("select avg(c) as average from (select count(distinct(title_key)) as c from sales_mo group by wholesaler_key) as t"))   
  
  puts ""
end


if __FILE__ == $0
#  print_template_vd_stats
#  print_sales_vd_stats  
  print_sales_mo_stats  
  
  
end
