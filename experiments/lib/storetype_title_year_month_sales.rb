
require "rubygems"
require "active_record"

class StoretypeTitleYearMonthSalesAR < ActiveRecord::Base
  set_table_name "template_storetype_vd"
end

class StoretypeTitleYearMonthSales
  def initialize
    @map = {}
  end
  
  def execute_query
    return StoretypeTitleYearMonthSalesAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0
    rs.each do |row|
      key = to_key(row["store_type"], row["title_key"], row["on_year"], row["on_month"])
      value = row["sales_total"].to_f
      @map[key] = value
    end
    puts "StoretypeTitleYearMonthSales loaded #{@map.size} average sales results"
  end

  def get_sales(store_type, title, year, month)
    key = to_key(store_type, title, year, month)
    average = @map[key]
    return average
  end
  
  def to_key(store_type, title, year, month)
    raise "Invalid key data: store_type=#{store_type}, title=#{title}, year=#{year}, month=#{month}" if store_type.nil? or title.nil? or year.nil? or month.nil?
    return "#{store_type}-#{title}-#{year}-#{month}"
  end
  
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for StoretypeTitleYearMonthSales..."
  o = StoretypeTitleYearMonthSales.new
  o.load  
  puts "done."
end