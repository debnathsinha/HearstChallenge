
require "rubygems"
require "active_record"

class WholesalerTitleYearMonthSalesAR < ActiveRecord::Base
  set_table_name "wholesaler_title_year_month_sales_vd"
end

class WholesalerTitleYearMonthSalesAR
  def initialize
    @map = {}
  end
  
  def execute_query
    return WholesalerTitleYearMonthSalesAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0
    rs.each do |row|
      key = to_key(row["wholesaler_key"], row["title_key"], row["on_year"], row["on_month"])
      value = row["sales_total"].to_f
      @map[key] = value
    end
    puts "WholesalerTitleYearMonthSalesAR loaded #{@map.size} average sales results"
  end

  def get_sales(wholesaler, title, year, month)
    key = to_key(wholesaler, title, year, month)
    average = @map[key]
    return average
  end
  
  def to_key(wholesaler, title, year, month)
    raise "Invalid key data: wholesaler=#{wholesaler}, title=#{title}, year=#{year}, month=#{month}" if wholesaler.nil? title.nil? or year.nil? or month.nil?
    return "#{title}-#{year}-#{month}"
  end
  
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for WholesalerTitleYearMonthSalesAR..."
  o = WholesalerTitleYearMonthSalesAR.new
  o.load  
  puts "done."
end