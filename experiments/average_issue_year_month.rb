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


def to_key(title, year, month)
  return "#{title}-#{year}-#{month}"
end

if __FILE__ == $0

  # test query
  # rs = TemplateMo.find_by_sql("select mo.title_key as title_key, mo.on_year as on_year, mo.on_month as on_month, avg(mo.sales_total) as sales_total from template_mo mo, (select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month) as vd where mo.title_key=vd.title_key and mo.on_year=vd.on_year and mo.on_month=vd.on_month and mo.on_year=2008 and mo.on_month=01 and mo.title_key<10 group by title_key, on_year, on_month")

  # actual query
  rs = TemplateMo.find_by_sql("select mo.title_key as title_key, mo.on_year as on_year, mo.on_month as on_month, avg(mo.sales_total) as sales_total from template_mo mo, (select title_key, on_year, on_month from template_vd2 group by title_key, on_year, on_month) as vd where mo.title_key=vd.title_key and mo.on_year=vd.on_year and mo.on_month=vd.on_month group by title_key, on_year, on_month")

  puts "Result set: #{rs.length}"
  
  # build a hash
  hash = {}
  rs.each do |row|
    key = to_key(row["title_key"], row["on_year"], row["on_month"])
    hash[key] = row["sales_total"]
  end
  puts "Hash set: #{hash.length}"
  
  output_lines = []
  missing_count = 0
  total = 0
  # build output
  File.open("../data/template_for_submission.csv").each_with_index do |line, i|
    line = line.strip
    if i==0
      output_lines << line
      next #skip
    end
    store, title, yearmonth = line.split(",")
    year, month = yearmonth[0,4].to_i, yearmonth[4,5].to_i
    # get average across all stores for title/year/month
    key = to_key(title, year, month)
    avg = hash[key]
    raise "could not find average for key=(#{key}) line #{(i+1)} = (#{line})" if avg.nil?
    output_lines << "#{store},#{title},#{yearmonth},#{avg}"
    puts "#{i}..." if (i%1000)==0
    total += 1
  end
  
  # write file
  f = File.open("average_issue_year_month_submission.csv", 'w')
  output_lines.each do |line|
    f.write(line + "\n")    
  end
  f.close
end
