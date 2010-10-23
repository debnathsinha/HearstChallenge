
require "rubygems"
require "active_record"

class YearMonthSalesAR < ActiveRecord::Base
  set_table_name "year_month_sales_vd"
end

class YearMonthSales
  
  def initialize
    @map = {}
  end
  
  def execute_query
    return YearMonthSalesAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0    
    rs.each do |row|
      key = to_key(row["on_year"], row["on_month"])
      value = row["sales_total"].to_f
      @map[key] = value
    end
    puts "YearMonthSales loaded #{@map.size} average sales results"
  end

  def get_sales(year, month)
    key = to_key(year, month)
    average = @map[key]
    return average
  end
  
  def to_key(year, month)
    raise "Invalid key data: year=#{year}, month=#{month}" if year.nil? or month.nil?
    return "#{year}-#{month}"
  end
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for YearMonthSales..."
  o = YearMonthSales.new
  o.load  
  puts "done."
end