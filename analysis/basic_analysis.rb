# basic analysis

require "rubygems"
require "active_record"

# utils 

def print_size(name, size)
  printf("| %s | %i |\n", name, size)
end

def print_number(size)
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
  print_size("Total records", TemplateVd.count)
  
  
end



if __FILE__ == $0
  print_template_vd_stats
  
end
