# assume the average issue/year/month sales for a given store

# process each entry in the store file and query for the average
# slow, but its an experiment to get my first submission out there

require "rubygems"
require "active_record"

# database

ActiveRecord::Base.establish_connection(
  :adapter => "mysql",
  :host => "localhost",
  :username => "root",
  :database => "hearst_challenge")

class TemplateMo < ActiveRecord::Base
  set_table_name "template_mo"
end


if __FILE__ == $0
  
  output_lines = []
  # build output
  File.open("../data/template_for_submission.csv").each_with_index do |line, i|
    line = line.strip
    if i==0
      output_lines << line
      next #skip
    end
    store, title, yearmonth = line.split(",")
    year, month = yearmonth[0,4], yearmonth[4,5]
    # get average across all stores for title/year/month
    avg = TemplateMo.average(:sales_total, :conditions=>['title_key=? and on_year=? and on_month=?',title,year,month])    
    output_lines << "#{store},#{title},#{yearmonth},#{avg}"
    puts "#{i}..." if (i%100)==0
  end
  
  # write file
  f = File.open("average_issue_year_month_submission.csv", 'w')
  output_lines.each do |line|
    f.write(line + "\n")    
  end
  f.close
end
