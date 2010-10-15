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
  puts "| #{headings.join(" | ")} |"
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





def print_template_vd_stats  
  puts "validation data info"
  print_heading(["Measures", "Values"])
  
  print_size("Total Records", TemplateVd.count)
  print_size("Total Stores", TemplateVd.calculate(:count, :store_key, :distinct=>true))
  print_size("Total Titles", TemplateVd.calculate(:count, :title_key, :distinct=>true))
  print_size("Total Months", TemplateVd.calculate(:count, :yearmonth, :distinct=>true))
  
  print_size("Average Records/Month", TemplateVd.count_by_sql("select avg(c) as average from (select count(*) as c from template_vd group by yearmonth) as t"))
  print_size("Average Stores/Month", TemplateVd.count_by_sql("select avg(c) from (select count(distinct(store_key)) as c from template_vd group by yearmonth) as t"))
  print_size("Average Titles/Month", TemplateVd.count_by_sql("select avg(c) from (select count(distinct(title_key)) as c from template_vd group by yearmonth) as t"))
  
end



if __FILE__ == $0
  print_template_vd_stats
  
end
